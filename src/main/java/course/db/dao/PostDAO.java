package course.db.dao;

import course.db.db_queries.QueryForForums;
import course.db.db_queries.QueryForPost;
import course.db.db_queries.QueryForThread;
import course.db.db_queries.QueryForUserProfile;
import course.db.models.*;
import jdk.nashorn.internal.scripts.JD;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.persistence.criteria.CriteriaBuilder;
import javax.validation.constraints.NotNull;

@Repository
public class PostDAO extends AbstractDAO {
    @NotNull
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public PostDAO(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public PostModel findById(Integer id) {
        return new PostModel();
    }

    public PostModel updatePost(PostModel newModel) {
        return new PostModel();
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
                    threadModel = jdbcTemplate.queryForObject(QueryForThread.findById(), new
                            Object[] {postModel.getThread()}, _getThreadModel);
                } else if (arg.equals("forum")) {
                    forumModel = jdbcTemplate.queryForObject(QueryForForums.findForumBySlug(),
                            new Object[] {postModel.getForum()}, _getForumModel);
                }
            }
        }
        return new PostDetailsModel(userProfileModel, postModel, threadModel, forumModel);
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
