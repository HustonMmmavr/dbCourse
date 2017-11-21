package course.db.dao;

import course.db.db_queries.QueryForForums;
import course.db.models.ForumModel;
import course.db.views.ForumView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jmx.export.annotation.ManagedNotification;
import org.springframework.stereotype.Repository;

import javax.validation.constraints.NotNull;

@Repository
public class ForumDAO extends AbstractDAO {
    @NotNull
    private final JdbcTemplate jdbcTemplate;

    public ForumDAO(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public void create(@NotNull ForumModel view) {
        String getUserId = "Select userid from user where username=?";
        int userId = jdbcTemplate.queryForObject(getUserId, new Object[] {view.getUser()}, Integer.class);
        String createForum = "Insert into forum (values(?,?,?)";
        jdbcTemplate.update(createForum, userId, view.getTitle(), view.getSlug());
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
