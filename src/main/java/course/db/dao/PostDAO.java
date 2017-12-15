package course.db.dao;

import com.sun.org.apache.xpath.internal.operations.Bool;
import course.db.db_queries.QueryForForums;
import course.db.db_queries.QueryForPost;
import course.db.db_queries.QueryForThread;
import course.db.db_queries.QueryForUserProfile;
import course.db.models.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataRetrievalFailureException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import javax.validation.constraints.NotNull;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.TimeZone;

@Repository
public class PostDAO extends AbstractDAO {
    @NotNull
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public PostDAO(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public PostModel findById(Integer id) {
        PostModel postModel = jdbcTemplate.queryForObject(QueryForPost.getById(), new Object[] {id}, _getPostModel);
        return postModel;
    }

    private void fillStatment(PreparedStatement statement, Integer id, Integer a_id, Integer t_id, Timestamp created,
                              Integer f_id, PostModel model) throws SQLException {
        statement.setInt(1, id);  // id
        statement.setInt(2, model.getParent());  // parent_id
        statement.setInt(3, a_id); //author_id
        statement.setTimestamp(4, created); // crecreated
        statement.setInt(5, f_id);  // forumId
        statement.setString(6, model.getMessage()); // message
        statement.setInt(7, t_id);
        if (model.getParent() == 0) {
            statement.setInt(8, id);    // id_of_root
            statement.setArray(9, null);    // arr
            statement.setInt(10, id);   // arr
        }
        else {
            Array pathArray = jdbcTemplate.queryForObject(QueryForPost.getPath(), new Object[] {model.getParent()}, Array.class);
            statement.setInt(8, ((Integer[])pathArray.getArray())[0]); // id
            statement.setArray(9, pathArray); //arr
            statement.setInt(10, id); // arr
        }
    }

    public List<PostModel> findSorted(ThreadModel threadModel, Integer limit, Integer since, String sort, Boolean desc) {
        List<Object> list = new ArrayList<>();
        StringBuilder builder = new StringBuilder();

        list.add(threadModel.getId());
        if (since != null) {
            list.add(since);
        }
        if (limit != null) {
            list.add(limit);
        }

        if (sort == null)
            sort = "flat";

        String sortOrder;
        String signSort;
        if (desc == Boolean.TRUE) {
            sortOrder = " DESC ";
            signSort = " < ";
        }
        else {
            sortOrder = " ASC ";
            signSort = " > ";
        }

        switch (sort) {
            case "flat" :
                builder.append(QueryForPost.flatOrTreeposts());
                if (since != null) {
                    builder.append(" AND id ");
                    builder.append(signSort + "? ");
                }
                builder.append(" ORDER BY id ");
                builder.append(sortOrder);
                if (limit != null) {
                    builder.append(" LIMIT ?");
                }
                break;
            case "tree" :
                builder.append(QueryForPost.flatOrTreeposts());
                if (since != null) {
                    builder.append(" AND path_to_post");
                    builder.append(signSort);
                    builder.append("(SELECT path_to_post FROM posts WHERE id = ?)");
                }
                builder.append(" ORDER BY path_to_post ");
                builder.append(sortOrder);
                if (limit != null) {
                    builder.append(" LIMIT ?");
                }
                break;
            case "parent_tree" :
                builder.append(QueryForPost.findPosts());
                builder.append("WHERE id_of_root IN (SELECT id FROM posts WHERE thread_id = ? AND parent_id = 0 ");
                if (since != null) {
                    builder.append(" AND path_to_post");
                    builder.append(signSort);
                    builder.append("(SELECT path_to_post FROM posts WHERE id = ?) ");
                }
                builder.append(" ORDER BY id ");
                builder.append(sortOrder);
                if (limit != null) {
                    builder.append(" LIMIT ?");
                }
                builder.append(")");
                builder.append("ORDER BY path_to_post ");
                builder.append(sortOrder);
                break;
            default:
                break;
        }
        return jdbcTemplate.query(builder.toString(), list.toArray(), _getPostModel);
    }

    public void createByThreadIdOrSlug(List<PostModel> postModelList, ThreadModel threadModel) throws DataAccessException {
        Integer threadId;
        if (threadModel.getId() != null)
            threadId = threadModel.getId();
        else
            threadId = jdbcTemplate.queryForObject(QueryForThread.findThreadIdBySlug(), new Object[] {threadModel.getSlug()}, Integer.class);

        Integer forumId = jdbcTemplate.queryForObject(QueryForThread.findForum(), new Object[] {threadId}, Integer.class);
        Timestamp created = new Timestamp(System.currentTimeMillis());
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        dateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
        Connection conn = null;
        PreparedStatement createPost = null;
        try {
            conn = jdbcTemplate.getDataSource().getConnection();
            conn.setAutoCommit(false);
            try {
                createPost = conn.prepareStatement(QueryForPost.createPost());
                for (PostModel postModel : postModelList) {
                    Integer userId = jdbcTemplate.queryForObject(QueryForUserProfile.getIdByNick(), new Object[]{postModel.getAuthor()}, Integer.class);
                    Integer postId = jdbcTemplate.queryForObject(QueryForPost.getId(), Integer.class);
                    fillStatment(createPost, postId, userId, threadId, created, forumId, postModel);

                    createPost.addBatch();
                    postModel.setCreated(dateFormat.format(created));
                    postModel.setId(postId);
                }
                createPost.executeBatch();
                conn.commit();
            }
            catch (Exception ex) {
                conn.rollback();
                throw new DataRetrievalFailureException(ex.getLocalizedMessage());
            }
            finally {
                if (createPost != null)
                    createPost.close();
                conn.setAutoCommit(true);
            }
        }
        catch (SQLException ex) {
            throw new DataRetrievalFailureException(ex.getLocalizedMessage());
        }
        finally {
            try {
                if (conn != null)
                    conn.close();
            }
            catch (Exception e)
            {
                throw new DataRetrievalFailureException(e.getLocalizedMessage());
            }
        }
        jdbcTemplate.update(QueryForThread.updatePostCount(), new Object[] { postModelList.size(), forumId});
    }

    public PostDetailsModel getDetails(Integer id, String[] args) {
        PostModel postModel = jdbcTemplate.queryForObject(QueryForPost.getById(), new Object[] {id}, _getPostModel);
        UserProfileModel userProfileModel = null;
        ThreadModel threadModel = null;
        ForumModel forumModel = null;

        if (args != null) {
            for (String arg : args) {
                if (arg.equals("user")) {
                    userProfileModel = jdbcTemplate.queryForObject(QueryForUserProfile.getUserByNickOrEmail(),
                            new Object[] {postModel.getAuthor(), null}, _getUserModel);
                } else if (arg.equals("thread")) {
                    threadModel = jdbcTemplate.queryForObject(QueryForThread.findThreadById(), new
                            Object[] {postModel.getThread()}, _getThreadModel);
                } else if (arg.equals("forum")) {
                    forumModel = jdbcTemplate.queryForObject(QueryForForums.findForumBySlug(),
                            new Object[] {postModel.getForum()}, _getForumModel);
                }
            }
        }
        return new PostDetailsModel(userProfileModel, postModel, threadModel, forumModel);
    }

    public PostModel updatePost(PostModel newPostModel) {
        PostModel oldPostModel = jdbcTemplate.queryForObject(QueryForPost.getById(), new Object[] {newPostModel.getId()}, _getPostModel);
        StringBuilder builder = new StringBuilder("UPDATE posts SET message = ?");
        if (!(newPostModel.getMessage()).equals(oldPostModel.getMessage())) {
            builder.append(", is_edited = TRUE");
            oldPostModel.setEdited(true);
            oldPostModel.setMessage(newPostModel.getMessage());
        }
        builder.append(" WHERE id = ?");
        jdbcTemplate.update(builder.toString(), newPostModel.getMessage(), newPostModel.getId());
        return oldPostModel;
    }

    @Override
    public Integer count() {
        return jdbcTemplate.queryForObject(QueryForPost.count(), Integer.class);
    }

    @Override
    public void clear() {
        jdbcTemplate.execute(QueryForPost.clear());
    }
}
