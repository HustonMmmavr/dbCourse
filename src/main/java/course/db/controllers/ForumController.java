package course.db.controllers;

import course.db.models.ForumModel;
import course.db.views.AbstractView;
import course.db.views.ErrorView;
import course.db.views.ForumView;
import course.db.views.ThreadView;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path="/forum")
public class ForumController extends AbstractController {
    @RequestMapping(path="/create", method= RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE,
    produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<AbstractView> createForum(@RequestBody ForumView forumView) {
        try {
            forumDAO.create(new ForumModel(forumView));
        }
        catch (DuplicateKeyException ex) {

        }
        return  ResponseEntity.status(HttpStatus.OK).body(new ErrorView(""));
    }

    @RequestMapping(path="/{slug}/details", method= RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<AbstractView> getBranchDetails(@PathVariable(value="slug") String slug) {
        return  ResponseEntity.status(HttpStatus.OK).body(new ErrorView(""));
    }

    @RequestMapping(path="/{slug}/create", method= RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<AbstractView> createBranch(@PathVariable(value="slug") String slug, @RequestBody ThreadView threadView) {
        return  ResponseEntity.status(HttpStatus.OK).body(new ErrorView(""));
    }

    @RequestMapping(path="/{slug}/users", method= RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<AbstractView> getUsers(@PathVariable(value="slug") String slug,
                                                 @RequestParam(value="limit",required = false) Integer limit,
                                                 @RequestParam(value="since",required = false) String since,
                                                 @RequestParam(value="desc",required = false) Boolean desc
                                                 ) {

        return  ResponseEntity.status(HttpStatus.OK).body(new ErrorView(""));
    }

    @RequestMapping(path="/{slug}/threads", method= RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<AbstractView> getThreads(@PathVariable(value="slug") String slug,
                                                   @RequestParam(value="limit",required = false) Integer limit,
                                                   @RequestParam(value="since",required = false) String since,
                                                   @RequestParam(value="desc",required = false) Boolean desc
                                                ) {

        return  ResponseEntity.status(HttpStatus.OK).body(new ErrorView(""));
    }
}
