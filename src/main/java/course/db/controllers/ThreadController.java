package course.db.controllers;

import course.db.managers.ResponseCodes;
import course.db.models.ThreadModel;
import course.db.views.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;

@RestController
@RequestMapping(path="/thread")
public class ThreadController extends AbstractController {
    @RequestMapping(path="/{slug_or_id}/create", method= RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<AbstractView> createPosts(@RequestBody ArrayList<PostView> posts,
                                                     @PathVariable(value = "slug_or_id") String slug_or_id) {
        return new ResponseEntity<>(new ErrorView("f"), null, HttpStatus.OK);

    }

    private void checkAndSetSlugOrId(String slug_or_id, ThreadModel threadModel) {
        try {
            int id = new Integer(slug_or_id);
            threadModel.setId(id);
        } catch (NumberFormatException e) {
            threadModel.setSlug(slug_or_id);
        }
    }

    @RequestMapping(path="/{slug_or_id}/details", method= RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<AbstractView> getDetails(@PathVariable(value = "slug_or_id") String slug_or_id) {

        final ThreadModel threadModel = new ThreadModel();
        checkAndSetSlugOrId(slug_or_id, threadModel);
        ResponseCodes responseCode = threadManager.findThreadBySlugOrId(threadModel);

        switch (responseCode) {
            case OK:
                return new ResponseEntity<>(threadModel.toView(), null, HttpStatus.OK); //
            case NO_RESULT:
                return new ResponseEntity<>(new ErrorView("No such thread"), null, HttpStatus.NOT_FOUND); //
            default:
                return new ResponseEntity<>(new ErrorView("errorDb"), null, HttpStatus.NOT_FOUND); //
        }
    }

    @RequestMapping(path="/{slug_or_id}/details", method= RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<AbstractView> setDetails(@RequestBody ThreadView threadView,
                                                   @PathVariable(value = "slug_or_id") String slug_or_id) {
        final ThreadModel threadModel = new ThreadModel();
        checkAndSetSlugOrId(slug_or_id, threadModel);

        ResponseCodes responseCode = threadManager.updateThread(threadModel);
        switch (responseCode) {
            case OK:
                return new ResponseEntity<>(threadModel.toView(), null, HttpStatus.OK); //
            case NO_RESULT:
                return new ResponseEntity<>(new ErrorView("No such thread"), null, HttpStatus.NOT_FOUND); //
            case CONFILICT:
                return new ResponseEntity<>(new ErrorView("Conflict"), null, HttpStatus.CONFLICT); //
            default:
                return new ResponseEntity<>(new ErrorView("errorDb"), null, HttpStatus.NOT_FOUND); //
        }

    }

    @RequestMapping(path="/{slug_or_id}/posts", method= RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<AbstractView> getPosts(@PathVariable(value = "slug_or_id") String slug_or_id,
                                                 @RequestParam(value="limit",required = false) Integer limit,
                                                 @RequestParam(value="since",required = false) Integer since,
                                                 @RequestParam(value="sort",required = false) String sort,
                                                 @RequestParam(value="desc",required = false) Boolean desc) {
        return new ResponseEntity<>(new ErrorView("f"), null, HttpStatus.OK);

    }

    
    @RequestMapping(path="/{slug_or_id}/vote", method= RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<AbstractView> voteThread(@RequestBody VoteView voteView) {
        return new ResponseEntity(new ErrorView(""), null, HttpStatus.NOT_FOUND);
    }
}
