package course.db.dao;

import course.db.db_queries.QueryForForums;
import course.db.db_queries.QueryForPost;
import course.db.db_queries.QueryForThread;
import course.db.db_queries.QueryForUserProfile;
import course.db.models.*;
import jdk.nashorn.internal.scripts.JD;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataRetrievalFailureException;
import org.springframework.dao.RecoverableDataAccessException;
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
        list.add(threadModel.getId());
        if (since != null) {
            list.add(since);
        }
        if (limit != null) {
            list.add(limit);
        }
        if (sort == null) {
            return jdbcTemplate.query(QueryForPost.postsFlat(limit, since, desc), list.toArray(), _getPostModel);
        }
        switch (sort) {
            case "flat" :
                return jdbcTemplate.query(QueryForPost.postsFlat(limit, since, desc), list.toArray(), _getPostModel);
            case "tree" :
                return jdbcTemplate.query(QueryForPost.postsTree(limit,since,desc), list.toArray(), _getPostModel);
            case "parent_tree" :
                return jdbcTemplate.query(QueryForPost.postsParentTree(limit, since, desc), list.toArray(), _getPostModel);
            default:
                break;
        }
        return jdbcTemplate.query(QueryForPost.postsFlat(limit, since, desc), list.toArray(), _getPostModel);
    }

    public void createByThreadIdOrSlug(List<PostModel> postModelList, ThreadModel threadModel) {
        Integer threadId;
        if (threadModel.getId() != null)
            threadId = threadModel.getId();
        else
            threadId = jdbcTemplate.queryForObject(QueryForThread.findThreadIdBySlug(), new Object[] {threadModel.getSlug()}, Integer.class);

        Integer forumId = jdbcTemplate.queryForObject(QueryForThread.findForum(), new Object[] {threadId}, Integer.class);
        Timestamp created = new Timestamp(System.currentTimeMillis());
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        dateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
        try (Connection conn = jdbcTemplate.getDataSource().getConnection()){
            conn.setAutoCommit(false);
            try (PreparedStatement createPost = conn.prepareStatement(QueryForPost.createPost(), Statement.NO_GENERATED_KEYS);)  {
//                createPost = conn.prepareStatement(QueryForPost.createPost(), Statement.NO_GENERATED_KEYS);
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
//                createPost.c
            }
            catch (Exception ex) {
                conn.rollback();
//                conn.setAutoCommit(true);
                throw new DataRetrievalFailureException(ex.getLocalizedMessage());
            }
            finally {
                conn.setAutoCommit(true);
            }
        }
        catch (SQLException ex) {
            int c = 1;
            throw new DataRetrievalFailureException(ex.getLocalizedMessage());
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

    public PostModel updatePost(PostModel old) {
        PostModel postModel = jdbcTemplate.queryForObject(QueryForPost.getById(), new Object[] {old.getId()}, _getPostModel);
//        final PostModel post = findById(id);
        String message = old.getMessage();
        StringBuilder builder = new StringBuilder("UPDATE posts SET message = ?");
        if (!message.equals(postModel.getMessage())) {
            builder.append(", is_edited = TRUE");
            postModel.setEdited(true);
            postModel.setMessage(message);
        }
        builder.append(" WHERE id = ?");
        jdbcTemplate.update(builder.toString(), message, old.getId());
        return postModel;
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
