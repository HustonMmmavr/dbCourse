package course.db.controllers;

import course.db.views.AbstractView;
import course.db.views.ErrorView;
import course.db.views.VoteView;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path="/thread")
public class ThreadController extends AbstractController {
//    @RequestMapping(path="/{slug_or_id}/create", method= RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE,
//        produces = MediaType.APPLICATION_JSON_VALUE)
//    public ResponseEntity<AbstractView> createThread() {
//
//    }
//
//    @RequestMapping(path="/{slug_or_id}/details", method= RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
//    public ResponseEntity<AbstractView> getDetails() {
//
//    }
//
//    @RequestMapping(path="/{slug_or_id}/details", method= RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE,
//            produces = MediaType.APPLICATION_JSON_VALUE)
//    public ResponseEntity<AbstractView> setDetails(@RequestBody ThreadDetailsView threadDetailsView) {
//
//    }
//
//    @RequestMapping(path="/{slug_or_id}/posts", method= RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
//    public ResponseEntity<AbstractView> getPosts() {
//
//    }

    
    @RequestMapping(path="/{slug_or_id}/vote", method= RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<AbstractView> voteThread(@RequestBody VoteView voteView) {
        return new ResponseEntity(new ErrorView(""), null, HttpStatus.NOT_FOUND);
    }
}
