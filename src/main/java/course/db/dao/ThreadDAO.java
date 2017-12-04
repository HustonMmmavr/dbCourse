package course.db.dao;

import course.db.db_queries.QueryForForums;
import course.db.db_queries.QueryForThread;
import course.db.db_queries.QueryForUserProfile;
import course.db.models.ThreadModel;
import course.db.views.ThreadView;
import jdk.nashorn.internal.scripts.JD;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import javax.persistence.criteria.CriteriaBuilder;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

@Repository
public class ThreadDAO extends AbstractDAO {
    @NotNull
    JdbcTemplate jdbcTemplate;

    @Autowired
    public ThreadDAO(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    // TODO transcaction
    public ThreadModel createThread(ThreadModel threadModel) {
        Integer userId = jdbcTemplate.queryForObject(QueryForUserProfile.getIdByNick(), new Object[] {threadModel.getAuthor()},
                Integer.class);
        Integer forumId = jdbcTemplate.queryForObject(QueryForForums.findForumIdBySlug(), new Object[] {threadModel.getSlug()},
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
        int res = jdbcTemplate.update(QueryForForums.incThreadCount(), new Object[] {forumId});

        return jdbcTemplate.queryForObject(QueryForForums.findThreads(), new Object[] {threadModel.getSlug()}, _getThreadModel);
    }

    public void updateThread(ThreadModel threadModel) {
        StringBuilder builder = new StringBuilder("UPDATE threads SET");
        List<Object> args = new ArrayList<>();
        if (threadModel.getMessage() != null) {
            builder.append(" message = ?,");
            args.add(threadModel.getMessage());
        }

        if (threadModel.getTitle() != null) {
            builder.append(" title = ?,");
            args.add(threadModel.getTitle());
        }

        if (!args.isEmpty()) {
            builder.delete(builder.length() - 1, builder.length());
            if (threadModel.getId() != null) {
                builder.append(" WHERE id = ?");
                args.add(threadModel.getId());
            }
            else {
                builder.append(" WHERE slug = ?");
                args.add(threadModel.getTitle());
            }
            jdbcTemplate.update(builder.toString(), args.toArray());
        }
    }

    public ThreadModel findBySlugOrId(ThreadModel threadModel) {
        return jdbcTemplate.queryForObject(QueryForThread.findBySlugOrId(), new Object[]
                {threadModel.getId(), threadModel.getTitle()}, _getThreadModel);
    }

    public ThreadModel findThread(ThreadModel threadModel) {
        return jdbcTemplate.queryForObject(QueryForForums.findThreads(), new Object[] {threadModel.getSlug()}, _getThreadModel);
    }

    @Override
    public Integer count() {
        return jdbcTemplate.queryForObject(QueryForThread.count(), Integer.class);
    }

    @Override
    public void clear(){
        jdbcTemplate.execute(QueryForThread.clear());
    }
}


//    public ThreadModel createByForum(ThreadModel threadModel) {
//        Integer userId = jdbcTemplate.queryForObject(QueryForUserProfile.getIdByNick(), new Object[] {threadModel.getAuthor()},
//                                                            Integer.class);
//        Integer forumId = jdbcTemplate.queryForObject(QueryForForums.findForumBySlug(), new Object[] {threadModel.getSlug()},
//                Integer.class);
//        String created = threadModel.getCreated();
//        if (created == null) {
//            jdbcTemplate.update(QueryForThread.createNoDate(), new Object[] {
//                    userId, forumId, threadModel.getTitle(), threadModel.getMessage(), 0, threadModel.getSlug()});
//        }
//        else
//        {
//            jdbcTemplate.update(QueryForThread.createWithDate(), new Object[]{
//                    userId, forumId, threadModel.getTitle(), threadModel.getCreated(), threadModel.getMessage(), 0, threadModel.getSlug()});
//        }
//        return jdbcTemplate.queryForObject(QueryForThread.findThread(), new Object[] {threadModel.getSlug(), threadModel.getId()}, _getThread);
//    }
