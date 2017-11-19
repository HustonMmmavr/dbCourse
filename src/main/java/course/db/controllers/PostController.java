package course.db.controllers;

import course.db.views.AbstractView;
import course.db.views.ErrorView;
import course.db.views.PostDetailsView;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path="/post")
public class PostController extends AbstractController {
    @RequestMapping(path="/{id}/details", method= RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<AbstractView> getDetails(@PathVariable(value="id") String stringId) {
        final int id;
        try {
            id = new Integer(stringId);
        } catch (NumberFormatException ex) {
            return new ResponseEntity<>(new ErrorView("Invalid id"), null, HttpStatus.BAD_REQUEST);
        }
        return  new ResponseEntity<ErrorView>(HttpStatus.OK,null, new PostDetailsView());
    }

    @RequestMapping(path="/{id}/details", method= RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<AbstractView> setDetails(@PathVariable(value="id") String id, @RequestBody PostDetailsView postDetailsView) {
        return  ResponseEntity.status(HttpStatus.OK).body(new ErrorView(""));
    }
}
