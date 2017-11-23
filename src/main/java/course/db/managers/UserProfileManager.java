package course.db.managers;

import com.sun.org.apache.regexp.internal.RE;
import course.db.dao.UserProfileDAO;
import course.db.models.UserProfileModel;
import course.db.views.UserProfileView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import javax.validation.constraints.NotNull;
import java.util.List;

@Service
public class UserProfileManager {
    private final UserProfileDAO userProfileDAO;

    @Autowired
    public UserProfileManager(@NotNull UserProfileDAO userProfileDAO) {this.userProfileDAO = userProfileDAO; }

    public ResponseCodes createUser(@NotNull UserProfileModel userProfileModel) {
        try {
            userProfileDAO.create(userProfileModel);
        }
        catch (DuplicateKeyException dupEx) {
            return ResponseCodes.CONFILICT;
        }
        return ResponseCodes.OK;
    }

    public ResponseCodes getUserByNick(String nickname, UserProfileView user) {
        try {
            UserProfileModel userModel = userProfileDAO.getUserByNick(nickname);
            user.setNickname(userModel.getNickname());
            user.setAbout(userModel.getAbout());
            user.setEmail(userModel.getEmail());
            user.setFullname(userModel.getFullname());
        }
        catch (EmptyResultDataAccessException ex) {
            return ResponseCodes.NO_RESULT;
        }
        catch (DataAccessException daEx) {
            return ResponseCodes.DB_ERROR;
        }
        return ResponseCodes.OK;
    }

    public ResponseCodes getUsersByNickOrEmail(String nickname, String email, List<UserProfileView> users) {
        try {
            List<UserProfileModel> userProfileModels = userProfileDAO.getUsersByNickOrEmail(nickname, email);
            for (UserProfileModel userProfileModel : userProfileModels) {
                users.add(userProfileModel.toView());
            }
        }
        catch (DataAccessException daEx) {
            return ResponseCodes.DB_ERROR;
        }
        return ResponseCodes.OK;
    }

    public ResponseCodes changeUser(UserProfileView userProfileView) {
        try {
            int res = userProfileDAO.change(new UserProfileModel(userProfileView));
            if (res == 0)
                return ResponseCodes.NO_RESULT;
        }
        catch (DuplicateKeyException duEx){
            return ResponseCodes.CONFILICT;
        }
        catch (DataAccessException daEx) {
            return ResponseCodes.DB_ERROR;
        }
        return ResponseCodes.OK;
    }

    public ResponseCodes statusClear() {
        try {
            userProfileDAO.clear();
        }
        catch (DataAccessException dAx) {
            return ResponseCodes.DB_ERROR;
        }
        return ResponseCodes.OK;
    }

    public Integer statusCount() {
//        try {
            return userProfileDAO.count();
//        }
//        catch (DataAccessException dAx) {
//            return ResponseCodes.DB_ERROR;
//        }
//        return ResponseCodes.OK;
    }

}
