package course.db.managers;

import course.db.dao.UserProfileDAO;
import course.db.models.UserProfileModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;

import javax.validation.constraints.NotNull;

@Service
public class UserManager {
    private final UserProfileDAO userProfileDAO;

    @Autowired
    public UserManager(@NotNull UserProfileDAO userProfileDAO) {this.userProfileDAO = userProfileDAO; }

    public ResponseCodes createUser(@NotNull UserProfileModel userProfileModel) {
        try {
            userProfileDAO.create(userProfileModel);
        }
        catch (DuplicateKeyException dupEx) {
            return ResponseCodes.CONFILICT;
        }
        return ResponseCodes.OK;
    }

    public ResponseCodes statusClear(Integer count) {
        try {
            count = userProfileDAO.count();
            userProfileDAO.clear();
        }
        catch (DataAccessException dAx) {
            return ResponseCodes.DB_ERROR;
        }
        return ResponseCodes.OK;
    }


}
