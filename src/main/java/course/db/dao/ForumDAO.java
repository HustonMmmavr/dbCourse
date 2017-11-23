package course.db.dao;

import com.sun.org.apache.regexp.internal.RE;
import course.db.db_queries.QueryForForums;
import course.db.db_queries.QueryForThread;
import course.db.db_queries.QueryForUserProfile;
import course.db.managers.ResponseCodes;
import course.db.models.ForumModel;
import course.db.models.ThreadModel;
import course.db.views.ForumView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jmx.export.annotation.ManagedNotification;
import org.springframework.stereotype.Repository;

import javax.validation.constraints.NotNull;

@Repository
public class ForumDAO extends AbstractDAO {
    @NotNull
    private final JdbcTemplate jdbcTemplate;

    private RowMapper<ForumModel> _getForumBySlug = (rs, rowNum) -> new ForumModel(
            rs.getString("title"), rs.getString("nickname"), rs.getString("slug"), rs.getInt("posts"),
            rs.getInt("threads"));


    public ForumDAO(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }



    public void create(@NotNull ForumModel forumModel) {
        int userId = jdbcTemplate.queryForObject(QueryForUserProfile.getIdByNick(), new Object[]
                {forumModel.getUser()}, Integer.class);
        jdbcTemplate.update(QueryForForums.create(), userId, forumModel.getTitle(), forumModel.getSlug());
    }

    public ThreadModel createThread(ThreadModel threadModel) {
        Integer userId = jdbcTemplate.queryForObject(QueryForUserProfile.getIdByNick(), new Object[] {threadModel.getAuthor()},
                Integer.class);
        Integer forumId = jdbcTemplate.queryForObject(QueryForForums.findForumBySlug(), new Object[] {threadModel.getSlug()},
                Integer.class);
        String created = threadModel.getCreated();
        if (created == null) {
            jdbcTemplate.update(QueryForThread.createNoDate(), new Object[] {
                    userId, forumId, threadModel.getTitle(), threadModel.getMessage(), 0, threadModel.getSlug()});
        }
        else
        {
            jdbcTemplate.update(QueryForThread.createWithDate(), new Object[]{
                    userId, forumId, threadModel.getTitle(), threadModel.getCreated(), threadModel.getMessage(), 0, threadModel.getSlug()});
        }
        return jdbcTemplate.queryForObject(QueryForForums.findThread(), new Object[] {threadModel.getSlug()}, _getThread);
    }

    public ForumModel getForumBySlug(String slug) {
        return jdbcTemplate.queryForObject(QueryForForums.findForumBySlug(), new Object[] {slug}, _getForumBySlug);
    }

    public void getThreads() {

    }

    public void getUsers() {

    }

    @Override
    public Integer count(){
        return jdbcTemplate.queryForObject(QueryForForums.count(), Integer.class);
    }

    @Override
    public void clear() {
        jdbcTemplate.execute(QueryForForums.clear());
    }
}
