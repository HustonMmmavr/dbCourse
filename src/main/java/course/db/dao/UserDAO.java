package course.db.dao;

import course.db.db_queries.QueryForForums;
import course.db.db_queries.QueryForUserProfile;
import course.db.models.UserProfileModel;
import course.db.views.UserProfileView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.validation.constraints.NotNull;

@Repository
public class UserDAO extends AbstractDAO {
    @NotNull
    @Autowired
    JdbcTemplate jdbcTemplate;

    @Autowired
    public UserDAO(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void clear() {

    }

    public void create(@NotNull UserProfileModel userProfileModel) {
        jdbcTemplate.update(QueryForUserProfile.create(), userProfileModel.getAbout(), userProfileModel.getEmail(),
                            userProfileModel.getFullname(), userProfileModel.getNickname());
    }


}
