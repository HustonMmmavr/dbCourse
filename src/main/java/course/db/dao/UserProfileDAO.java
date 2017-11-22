package course.db.dao;

import course.db.db_queries.QueryForForums;
import course.db.db_queries.QueryForUserProfile;
import course.db.models.UserProfileModel;
import course.db.views.UserProfileView;
import org.apache.catalina.mapper.Mapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

@Repository
public class UserProfileDAO extends AbstractDAO {
    @NotNull
    @Autowired
    JdbcTemplate jdbcTemplate;

    RowMapper<UserProfileModel> getUserView = (rs, rowNum) ->
            new UserProfileModel(rs.getString("nickname"), rs.getString("fullname"),
                    rs.getString("about"), rs.getString("email"));

    @Autowired
    public UserProfileDAO(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public void create(@NotNull UserProfileModel userProfileModel) {
        jdbcTemplate.update(QueryForUserProfile.create(), userProfileModel.getAbout(), userProfileModel.getEmail(),
                            userProfileModel.getFullname(), userProfileModel.getNickname());
    }

    public UserProfileModel getUserByNick(String nickname) {
        return jdbcTemplate.queryForObject(QueryForUserProfile.getUserByNickOrEmail(),
                new Object[] {nickname, null}, getUserView);
    }

    public Integer change(UserProfileModel userProfileModel) {
        final StringBuilder sql = new StringBuilder("UPDATE userprofiles SET");
        final List<Object> args = new ArrayList<>();
        String about = userProfileModel.getAbout();
        String fullname = userProfileModel.getFullname();
        String email = userProfileModel.getEmail();

        if (about != null) {
            sql.append(" about = ?,");
            args.add(about);
        }
        if (email != null) {
            sql.append(" email = ?,");
            args.add(email);
        }
        if (fullname != null) {
            sql.append(" fullname = ?,");
            args.add(fullname);
        }
        if (!args.isEmpty()) {
            sql.delete(sql.length() - 1, sql.length());
            sql.append(" WHERE nickname = ?");
            args.add(userProfileModel.getNickname());
            return jdbcTemplate.update(sql.toString(), args.toArray());
        }
        return 0;
    }

    public List<UserProfileModel> getUsersByNickOrEmail(String nickname, String email) {
        return jdbcTemplate.query(QueryForUserProfile.getUserByNickOrEmail(),
                new Object[] {nickname, email}, getUserView);
    }

    @Override
    public Integer count() {
        return jdbcTemplate.queryForObject(QueryForUserProfile.count(), Integer.class);
    }

    @Override
    public void clear() {
        jdbcTemplate.execute(QueryForUserProfile.clear());
    }

}
