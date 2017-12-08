package course.db.dao;

import course.db.db_queries.QueryForForums;
import course.db.db_queries.QueryForUserProfile;
import course.db.models.ForumModel;
import course.db.models.ThreadModel;
import course.db.models.UserProfileModel;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.validation.constraints.NotNull;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.TimeZone;


@Repository
public class ForumDAO extends AbstractDAO {
    @NotNull
    private final JdbcTemplate jdbcTemplate;
    public ForumDAO(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public ForumModel create(@NotNull ForumModel forumModel) {
        int userId = jdbcTemplate.queryForObject(QueryForUserProfile.getIdByNick(), new Object[]
                {forumModel.getUser()}, Integer.class);
        jdbcTemplate.update(QueryForForums.create(), userId, forumModel.getTitle(), forumModel.getSlug());
//        return jdbcTemplate.queryForObject(QueryForForums.create(), new Object[] {userId, forumModel.getTitle(), forumModel.getSlug()}, _getForumModel);
        return null;
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
        list.add(forumId);
        list.add(forumId);

        if (since != null) {
            builder.append(" AND _user.nickname " + (desc == Boolean.TRUE  ? "< ? " : "> ? "));
            list.add(since);
        }

        builder.append("ORDER by _user.nickname " + (desc == Boolean.TRUE  ? "DESC " : " "));

        if (limit != null) {
            builder.append("LIMIT ? ");
            list.add(limit);
        }
        return jdbcTemplate.query(builder.toString(), list.toArray(),
                _getUserModel);
    }

    public List<ThreadModel> getThreads(String slug, Integer limit, String since, Boolean desc) {
        int forumId = jdbcTemplate.queryForObject(QueryForForums.findForumIdBySlug(), new Object[] {slug}, Integer.class);
        StringBuilder builder = new StringBuilder(QueryForForums.findThreadsById());
        List<Object> list = new ArrayList<>();
        list.add(forumId);

        //since=" 2017-11-27 22:54:55.047629+03";
        if (since != null) {
            builder.append(" AND thread.created " + (desc == Boolean.TRUE  ? " <= ? " : " >= ? "));
//                    "< to_timestamp(?," +
//                    " 'yyyy-MM-dd\'T\'HH:mm:ss.SSS\'Z\'') " : "> to_timestamp(?, 'yyyy-MM-dd\'T\'HH:mm:ss.SSS\'Z\'') "));
            //final Timestamp timestamp = new Timestamp(since);
            final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
            dateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
           // list.add(dateFormat.get);
        }

        builder.append("ORDER by thread.created " + (desc == Boolean.TRUE  ? "DESC " : " "));

        if (limit != null) {
            builder.append("LIMIT ? ");
            list.add(limit);
        }

        List<ThreadModel> models = jdbcTemplate.query(builder.toString(), list.toArray(new Object[list.size()]),
                _getThreadModel);

        for (ThreadModel model : models) {
            model.setForum(slug);
        }
        return models;
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
