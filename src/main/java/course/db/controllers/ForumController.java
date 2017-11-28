package course.db.controllers;

import course.db.managers.ResponseCodes;
import course.db.managers.ThreadManager;
import course.db.models.ForumModel;
import course.db.models.ThreadModel;
import course.db.views.*;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping(path="/forum")
public class ForumController extends AbstractController {
    @RequestMapping(path="/create", method= RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE,
    produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<AbstractView> createForum(@RequestBody ForumView forumView) {
        ResponseCodes responseCode = forumManager.create(new ForumModel(forumView));
        switch(responseCode) {
            case OK:
                // TODO read from db //maybe
                forumView.setPosts(0);
                forumView.setThreads(0);
                return new ResponseEntity<>(forumView, null, HttpStatus.CREATED); //
            case NO_RESULT:
                return new ResponseEntity<>(new ErrorView("No such user"), null, HttpStatus.NOT_FOUND);
            case CONFILICT:
                ForumModel existingForum = new ForumModel();
                existingForum.setSlug(forumView.getSlug());
                ResponseCodes responseCode1 = forumManager.findForum(existingForum);
                if (responseCode1 == ResponseCodes.DB_ERROR)
                    return new ResponseEntity<>(new ErrorView("Error db"), null, HttpStatus.INTERNAL_SERVER_ERROR);
                return new ResponseEntity<AbstractView>(existingForum.toView(), null, HttpStatus.CONFLICT);
            default:
                return new ResponseEntity<>(new ErrorView("Error db"), null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @RequestMapping(path="/{slug}/details", method= RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<AbstractView> getBranchDetails(@PathVariable(value="slug") String slug) {
        ForumModel forumModel = new ForumModel();
        forumModel.setSlug(slug);
        ResponseCodes responseCode = forumManager.findForum(forumModel);
        switch(responseCode) {
            case OK:
                return new ResponseEntity<>(forumModel.toView(), null, HttpStatus.CREATED); //
            case NO_RESULT:
                return new ResponseEntity<>(new ErrorView("No such foru"), null, HttpStatus.NOT_FOUND);
            default:
                return new ResponseEntity<>(new ErrorView("Error db"), null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @RequestMapping(path="/{slug}/create", method= RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<AbstractView> createBranch(@PathVariable(value="slug") String slug, @RequestBody ThreadView threadView) {
        threadView.setSlug(slug);
        ThreadModel threadModel = new ThreadModel(threadView);
        ResponseCodes responseCode = threadManager.createThread(threadModel);
        switch(responseCode) {
            case OK:
                return new ResponseEntity<>(threadModel.toView(), null, HttpStatus.CREATED); //
            case NO_RESULT:
                return new ResponseEntity<>(new ErrorView("No such user"), null, HttpStatus.NOT_FOUND);
            case CONFILICT:
                ThreadModel existingThread = new ThreadModel();
                existingThread.setSlug(threadView.getSlug());
                ResponseCodes responseCode1 = threadManager.findThreadBySlug(existingThread);
                if (responseCode1 == ResponseCodes.DB_ERROR)
                    return new ResponseEntity<>(new ErrorView("Error db"), null, HttpStatus.INTERNAL_SERVER_ERROR);
                return new ResponseEntity<AbstractView>(existingThread.toView(), null, HttpStatus.CONFLICT);
            default:
                return new ResponseEntity<>(new ErrorView("Error db"), null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @RequestMapping(path="/{slug}/threads", method= RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> getThreads(@PathVariable(value="slug") String slug,
                                                 @RequestParam(value="limit",required = false) Integer limit,
                                                 @RequestParam(value="since",required = false) String since,
                                                 @RequestParam(value="desc",required = false) Boolean desc
                                                 ) {
        ForumModel forumModel = new ForumModel();
        forumModel.setSlug(slug);
        ResponseCodes responseCode = forumManager.findForum(forumModel);
        switch (responseCode) {
            case NO_RESULT:
                return new ResponseEntity<>(new ErrorView("No such user"), null, HttpStatus.NOT_FOUND);
            case DB_ERROR:
                return new ResponseEntity<>(new ErrorView("Error db"), null, HttpStatus.INTERNAL_SERVER_ERROR);
            default:
                break;
        }

        List<ThreadView> threadViewList = new ArrayList<>();
        ResponseCodes responseCode1 = forumManager.findThreads(forumModel, limit, since, desc, threadViewList);
        switch (responseCode1) {
            case NO_RESULT:
                return new ResponseEntity<>(new ErrorView("No such user"), null, HttpStatus.NOT_FOUND);
            case DB_ERROR:
                return new ResponseEntity<>(new ErrorView("Error db"), null, HttpStatus.INTERNAL_SERVER_ERROR);
            default:
                break;
        }
        return new ResponseEntity<>(threadViewList, null, HttpStatus.INTERNAL_SERVER_ERROR);

    }

    @RequestMapping(path="/{slug}/users", method= RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> getUsers(@PathVariable(value="slug") String slug,
                                                   @RequestParam(value="limit",required = false) Integer limit,
                                                   @RequestParam(value="since",required = false) String since,
                                                   @RequestParam(value="desc",required = false) Boolean desc
                                                ) {
        ForumModel forumModel = new ForumModel();
        forumModel.setSlug(slug);
        ResponseCodes responseCode = forumManager.findForum(forumModel);
        switch (responseCode) {
            case NO_RESULT:
                return new ResponseEntity<>(new ErrorView("No such user"), null, HttpStatus.NOT_FOUND);
            case DB_ERROR:
                return new ResponseEntity<>(new ErrorView("Error db"), null, HttpStatus.INTERNAL_SERVER_ERROR);
            default:
                break;
        }

        // TODO make table users forums
        // time
        List<UserProfileView> userProfileList = new ArrayList<>();
        ResponseCodes responseCode1 = forumManager.findUsers(forumModel, limit, since, desc, userProfileList);
        switch (responseCode1) {
            case OK:
                return new ResponseEntity<>(userProfileList, null, HttpStatus.OK);
            case NO_RESULT:
                return new ResponseEntity<>(new ErrorView("No such user"), null, HttpStatus.NOT_FOUND);
            default:
                return new ResponseEntity<>(new ErrorView("Error db"), null, HttpStatus.INTERNAL_SERVER_ERROR);
        }

//        return  ResponseEntity.status(HttpStatus.OK).body(new ErrorView(""));
    }
}
