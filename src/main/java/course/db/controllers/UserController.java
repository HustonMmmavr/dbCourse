package course.db.controllers;

import com.sun.org.apache.regexp.internal.RE;
import course.db.managers.ResponseCodes;
import course.db.models.UserProfileModel;
import course.db.views.AbstractView;
import course.db.views.ErrorView;
import course.db.views.UserProfileView;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping(path="/user")
public class UserController extends AbstractController {
    @RequestMapping(path="/{nickname}/create", method= RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> createUser(@RequestBody UserProfileView userProfileView,
                                                     @PathVariable(value="nickname") String nickname) {
        userProfileView.setNickname(nickname);
        ResponseCodes responseCode = userProfileManager.createUser(new UserProfileModel(userProfileView));

        switch (responseCode) {
            case OK:
                return new ResponseEntity<>(userProfileView, null, HttpStatus.CREATED);
            case CONFILICT:
                List<UserProfileView> users = new ArrayList<>();
                ResponseCodes responseCode1 = userProfileManager.getUsersByNickOrEmail(userProfileView.getNickname(),
                        userProfileView.getEmail(), users);
                if (responseCode1 == ResponseCodes.DB_ERROR)
                    return new ResponseEntity<>(new ErrorView("ErrorDB"), null, HttpStatus.INTERNAL_SERVER_ERROR);
                return new ResponseEntity<>(users, null, HttpStatus.CONFLICT);
            default:
                return new ResponseEntity<>(new ErrorView("ErrorDB"), null, HttpStatus.INTERNAL_SERVER_ERROR);
        }

//        return new ResponseEntity<>(new ErrorView("f"), null, HttpStatus.OK);

    }

    @RequestMapping(path="/{nickname}/profile", method= RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<AbstractView> getProfile(@PathVariable(value = "nickname") String nickname) {
        UserProfileView userProfileView = new UserProfileView();
        ResponseCodes responseCode = userProfileManager.getUserByNick(nickname, userProfileView);

        switch (responseCode) {
            case OK:
                return new ResponseEntity<>(userProfileView, null, HttpStatus.OK);
            case NO_RESULT:
                return new ResponseEntity<>(new ErrorView("No such user"), null, HttpStatus.NOT_FOUND);
            default:
                return new ResponseEntity<>(new ErrorView("ErrorDB"), null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @RequestMapping(path="/{nickname}/profile", method= RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<AbstractView> setProfile(@RequestBody UserProfileView userProfileView,
                                                   @PathVariable(value = "nickname") String nickname) {
        userProfileView.setNickname(nickname);
        ResponseCodes responseCode = userProfileManager.changeUser(userProfileView);

        switch (responseCode) {
            case OK:
                return new ResponseEntity<>(userProfileView, null, HttpStatus.OK);
            case NO_RESULT:
                return new ResponseEntity<>(new ErrorView("No such user"), null, HttpStatus.NOT_FOUND);
            case CONFILICT:
                return new ResponseEntity<>(new ErrorView("New data conflicts with existing user"), null, HttpStatus.CONFLICT);
            default:
                return new ResponseEntity<>(new ErrorView("ErrorDB"), null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
