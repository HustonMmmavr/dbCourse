package course.db.controllers;

import course.db.views.AbstractView;
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

    }

    @RequestMapping(path="/status", method= RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<AbstractView> getStatus() {

    }
}
