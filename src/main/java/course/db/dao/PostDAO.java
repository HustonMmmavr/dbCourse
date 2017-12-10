package course.db.dao;

import course.db.db_queries.QueryForForums;
import course.db.db_queries.QueryForPost;
import course.db.db_queries.QueryForThread;
import course.db.db_queries.QueryForUserProfile;
import course.db.models.*;
import jdk.nashorn.internal.scripts.JD;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataRetrievalFailureException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.persistence.criteria.CriteriaBuilder;
import javax.validation.constraints.NotNull;
import java.sql.*;
import java.text.SimpleDateFormat;
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
//        try (Connection connection = jdbcTemplate.getDataSource().getConnection()) {
//            connection.setAutoCommit(false);
//            try (PreparedStatement postsPrepared = connection.prepareStatement(QueryForPost.createPost(), Statement.NO_GENERATED_KEYS);
////                 PreparedStatement userForumsPrepared = connection.prepareStatement(QueryForPost.insertIntoForumUsers(), Statement.NO_GENERATED_KEYS)) {
//                for (PostModel post : postModelList) {
//                    final Integer userId = jdbcTemplate.queryForObject(QueryForUserProfile.getIdByNick(), Integer.class, post.getAuthor());
//                    final Integer postId = jdbcTemplate.queryForObject("SELECT nextval('posts_id_seq')", Integer.class);
//                    postsPrepared.setInt(1, userId);
//                    postsPrepared.setTimestamp(2, created);
//                    postsPrepared.setInt(3, forumId);
//                    postsPrepared.setInt(4, postId);
//                    postsPrepared.setString(5, post.getMessage());
//                    postsPrepared.setInt(6, post.getParent());
//                    postsPrepared.setInt(7, threadId);
//                    postsPrepared.setInt(9, postId);
//                    if (post.getParent() == 0) {
//                        postsPrepared.setArray(8, null);
//                        postsPrepared.setInt(10, postId);
//                    } else {
//                        final Array path = jdbcTemplate.queryForObject("SELECT path FROM posts WHERE id = ?", Array.class, post.getParent());
//                        postsPrepared.setArray(8, path);
//                        postsPrepared.setInt(10, ((Integer[]) path.getArray())[0]);
//                    }
//                    postsPrepared.addBatch();
////                    userForumsPrepared.setInt(1, userId);
////                    userForumsPrepared.setInt(2, forumId);
////                    userForumsPrepared.addBatch();
//                    post.setCreated(dateFormat.format(created));
//                    post.setId(postId);
//                }
////                postsPrepared.executeBatch();
////                userForumsPrepared.executeBatch();
//                connection.commit();
//            } catch (SQLException ex) {
////                connection.rollback();
//                throw new DataRetrievalFailureException(null);
//            } finally {
////                connection.setAutoCommit(true);
//            }
//        } catch (SQLException ex) {
//            throw new DataRetrievalFailureException(null);
//        }
//        jdbcTemplate.update(QueryForThread.updateForumsPostsCount(), postModelList.size(), forumId);


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
