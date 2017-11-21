package course.db.controllers;

import course.db.views.AbstractView;
import course.db.views.ErrorView;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping(path="/service")
public class ServiceController extends AbstractController {
    @RequestMapping(path="/clear", method= RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<AbstractView> clearService() {
        return new ResponseEntity<>(new ErrorView("f"), null, HttpStatus.OK);

    }

    @RequestMapping(path="/status", method= RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<AbstractView> getStatus() {
        return new ResponseEntity<>(new ErrorView("f"), null, HttpStatus.OK);
    }
}
