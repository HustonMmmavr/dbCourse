package course.db.dao;

import course.db.models.ThreadModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import javax.validation.constraints.NotNull;

@Repository
public class AbstractDAO {
    public void clear(){}
    public Integer count() {return 0;}

    //(Integer votes, Integer id, String title, String author, String message, String created, String forum, Str
    protected RowMapper<ThreadModel> _getThread = (rs, rowNum) -> new ThreadModel(
            rs.getInt("votes"), rs.getInt("id"), rs.getString("title"), rs.getString("nickname"),
            rs.getString("message"), rs.getString("created"), rs.getString("forum_slug"),rs.getString("thread_slug")
    );

}
