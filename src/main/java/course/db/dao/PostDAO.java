package course.db.dao;

import course.db.db_queries.QueryForPost;
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

    @Override
    public Integer count() {
        return jdbcTemplate.queryForObject(QueryForPost.count(), Integer.class);
    }

    @Override
    public void clear() {
        jdbcTemplate.execute(QueryForPost.clear());
    }
}
