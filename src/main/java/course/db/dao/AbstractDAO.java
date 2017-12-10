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
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.TimeZone;

@Repository
public class AbstractDAO {
    public void clear(){}
    public Integer count() {return 0;}

    private String getDateFormat(Timestamp tp) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        dateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
        return dateFormat.format(tp.getTime());
    }

    protected RowMapper<ThreadModel> _getThreadModel = (rs, rowNum) -> {
        String date = getDateFormat(rs.getTimestamp("created"));
        return new ThreadModel(
            rs.getInt("votes"), rs.getInt("id"), rs.getString("title"), rs.getString("nickname"),
            rs.getString("message"), date, rs.getString("forum_slug") ,rs.getString("thread_slug")
        );
    };

    protected RowMapper<ForumModel> _getForumModel = (rs, rowNum) -> new ForumModel(
            rs.getString("title"), rs.getString("nickname"), rs.getString("slug"), rs.getInt("posts"),
            rs.getInt("threads"));


    protected RowMapper<PostModel> _getPostModel = (rs, rowNum) -> {
        String date = getDateFormat(rs.getTimestamp("created"));
        return new PostModel(rs.getInt("id"), rs.getInt("parent"), rs.getString("nickname"), rs.getString("message"),
                rs.getBoolean("is_edited"), rs.getString("slug"),
                rs.getInt("thread_id"), date);
    };

    protected RowMapper<UserProfileModel> _getUserModel = (rs, rowNum) ->
            new UserProfileModel(rs.getString("nickname"), rs.getString("fullname"),
                    rs.getString("about"), rs.getString("email"));
}
