package course.db.controllers;

import course.db.managers.ResponseCodes;
import course.db.models.PostModel;
import course.db.views.AbstractView;
import course.db.views.ErrorView;
import course.db.views.PostDetailsView;
import course.db.views.PostView;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path="/post")
public class PostController extends AbstractController {
    @RequestMapping(path="/{id}/details", method= RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<AbstractView> getDetails(@PathVariable(value="id") String stringId,
                                                   @RequestParam(value="related", required = false) String[] related) {
        PostModel postModel = new PostModel();
        postModel.setId(new Integer(stringId));
        ResponseCodes responseCode = postManager.findPostById(postModel);//(forumModel);
        switch(responseCode) {
            case OK:
                return new ResponseEntity<>(postModel.toView(), null, HttpStatus.CREATED); //
            case NO_RESULT:
                return new ResponseEntity<>(new ErrorView("No such foru"), null, HttpStatus.NOT_FOUND);
            default:
                return new ResponseEntity<>(new ErrorView("Error db"), null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @RequestMapping(path="/{id}/details", method= RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<AbstractView> setDetails(@PathVariable(value="id") String id, @RequestBody PostView postDetailsView) {
        PostModel postModel = new PostModel(postDetailsView);
        postModel.setId(new Integer(id));
        ResponseCodes responseCode = postManager.updatePost(postModel);//(forumModel);
        switch(responseCode) {
            case OK:
                return new ResponseEntity<>(postModel.toView(), null, HttpStatus.CREATED); //
            case NO_RESULT:
                return new ResponseEntity<>(new ErrorView("No such foru"), null, HttpStatus.NOT_FOUND);
            case CONFILICT:
                return new ResponseEntity<>(new ErrorView("conflict"), null, HttpStatus.CONFLICT);
            default:
                return new ResponseEntity<>(new ErrorView("Error db"), null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
