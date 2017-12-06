package course.db.dao;

import course.db.models.ForumModel;
import course.db.models.PostModel;
import course.db.models.ThreadModel;
import course.db.models.UserProfileModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import javax.validation.constraints.NotNull;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.TimeZone;

@Repository
public class AbstractDAO {
    public void clear(){}
    public Integer count() {return 0;}

    //(Integer votes, Integer id, String title, String author, String message, String created, String forum, Str
    protected RowMapper<ThreadModel> _getThreadModel = (rs, rowNum) -> {
        final Timestamp timestamp = rs.getTimestamp("created");
        final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        dateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
        return new ThreadModel(
            rs.getInt("votes"), rs.getInt("id"), rs.getString("title"), rs.getString("nickname"),
            rs.getString("message"), dateFormat.format(timestamp.getTime()), null ,rs.getString("thread_slug")
        );
    };

    protected RowMapper<ForumModel> _getForumModel = (rs, rowNum) -> new ForumModel(
            rs.getString("title"), rs.getString("nickname"), rs.getString("slug"), rs.getInt("posts"),
            rs.getInt("threads"));


    protected RowMapper<PostModel> _getPostModel = (rs, rowNum) -> new PostModel(

    );

    protected RowMapper<UserProfileModel> _getUserModel = (rs, rowNum) ->
            new UserProfileModel(rs.getString("nickname"), rs.getString("fullname"),
                    rs.getString("about"), rs.getString("email"));


}
