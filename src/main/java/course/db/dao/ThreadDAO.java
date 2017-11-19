package course.db.dao;

import jdk.nashorn.internal.scripts.JD;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.validation.constraints.NotNull;

@Repository
public class ThreadDAO extends AbstractDAO {
    @NotNull
    JdbcTemplate jdbcTemplate;

    @Autowired
    public ThreadDAO(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void clear(){
        final String clearQuery = "DELETE "
    }
}
