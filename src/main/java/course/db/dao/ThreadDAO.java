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

@Repository
public class ThreadDAO extends AbstractDAO {
    @NotNull
    JdbcTemplate jdbcTemplate;

//    private RowMapper<ThreadModel> _getThread = (rs, rowNum) -> new ThreadModel(
//    );

    @Autowired
    public ThreadDAO(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
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

    @Override
    public Integer count() {
        return jdbcTemplate.queryForObject(QueryForThread.count(), Integer.class);
    }

    @Override
    public void clear(){
        jdbcTemplate.execute(QueryForThread.clear());
    }
}
