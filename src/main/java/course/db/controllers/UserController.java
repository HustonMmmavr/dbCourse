package course.db.controllers;

import course.db.views.AbstractView;
import course.db.views.UserProfileView;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path="/user")
public class UserController extends AbstractController {
    @RequestMapping(path="/{nickname}/create", method= RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<AbstractView> createThread(@RequestBody UserProfileView userProfileView,
                                                     @PathVariable(value="nickname") String nickname) {

    }

    @RequestMapping(path="/{nickname}/profile", method= RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<AbstractView> getProfile(@PathVariable(value = "nickname") String nickname) {

    }

    @RequestMapping(path="/{nickname}/profile", method= RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<AbstractView> setProfile(@RequestBody UserProfileView userProfileView,
                                                   @PathVariable(value = "nickname") String nickname) {

    }

}
