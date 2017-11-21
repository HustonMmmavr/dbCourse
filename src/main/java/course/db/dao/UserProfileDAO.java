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
public class UserProfileDAO extends AbstractDAO {
    @NotNull
    @Autowired
    JdbcTemplate jdbcTemplate;

    @Autowired
    public UserProfileDAO(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public void create(@NotNull UserProfileModel userProfileModel) {
        jdbcTemplate.update(QueryForUserProfile.create(), userProfileModel.getAbout(), userProfileModel.getEmail(),
                            userProfileModel.getFullname(), userProfileModel.getNickname());
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
