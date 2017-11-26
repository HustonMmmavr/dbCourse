package course.db.dao;

import com.sun.org.apache.regexp.internal.RE;
import course.db.db_queries.QueryForForums;
import course.db.db_queries.QueryForThread;
import course.db.db_queries.QueryForUserProfile;
import course.db.managers.ResponseCodes;
import course.db.models.ForumModel;
import course.db.models.ThreadModel;
import course.db.models.UserProfileModel;
import course.db.views.ForumView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jmx.export.annotation.ManagedNotification;
import org.springframework.stereotype.Repository;

import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;


@Repository
public class ForumDAO extends AbstractDAO {
    @NotNull
    private final JdbcTemplate jdbcTemplate;
    public ForumDAO(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public void create(@NotNull ForumModel forumModel) {
        int userId = jdbcTemplate.queryForObject(QueryForUserProfile.getIdByNick(), new Object[]
                {forumModel.getUser()}, Integer.class);
        jdbcTemplate.update(QueryForForums.create(), userId, forumModel.getTitle(), forumModel.getSlug());
    }

    public ForumModel getForumBySlug(String slug) {
        return jdbcTemplate.queryForObject(QueryForForums.findForumBySlug(), new Object[] {slug}, _getForumModel);
    }

    // TODO tr
    public List<UserProfileModel> getUsers(String slug, Integer limit, String since, Boolean desc) {
        StringBuilder builder = new StringBuilder(QueryForForums.findUsers());
        int forumId = jdbcTemplate.queryForObject(QueryForForums.findForumIdBySlug(), new Object[] {slug}, Integer.class);
        List<Object> list = new ArrayList<>();
        list.add(forumId);

        if (since != null) {
            builder.append("AND _user.nickname" + (desc == Boolean.TRUE  ? "< ? " : "> ?"));
            list.add(since);
        }

        builder.append("ORDER by _user.nickname" + (desc == Boolean.TRUE  ? "DESC" : ""));

        if (limit != null) {
            builder.append("LIMIT ?");
            list.add(limit);
        }
        return jdbcTemplate.query(QueryForForums.findUsers(), list.toArray(),
                _getUserModel);
    }

    public List<ThreadModel> getThreads(String slug, Integer limit, String since, Boolean desc) {
        int forumId = jdbcTemplate.queryForObject(QueryForForums.findForumIdBySlug(), new Object[] {slug}, Integer.class);
        return jdbcTemplate.query(QueryForForums.findThreads(), new Object[] {forumId},
                _getThreadModel);

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
