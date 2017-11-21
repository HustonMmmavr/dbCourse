package course.db.managers;

import course.db.dao.UserDAO;
import course.db.models.UserProfileModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;

import javax.validation.constraints.NotNull;

@Service
public class UserManager {
    private final UserDAO userDAO;

    @Autowired
    public UserManager(@NotNull UserDAO userDAO) {this.userDAO = userDAO; }

    public ResponseCodes createUser(@NotNull UserProfileModel userProfileModel) {
        try {
            userDAO.create(userProfileModel);
        }
        catch (DuplicateKeyException dupEx) {
            return ResponseCodes.CONFILICT;
        }
        return ResponseCodes.OK;
    }


}
