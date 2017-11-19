package course.db.controllers;

import course.db.dao.ForumDAO;
import course.db.dao.PostDAO;
import course.db.dao.ThreadDAO;
import course.db.dao.UserDAO;
import course.db.views.PostDetailsView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.staticmock.MockStaticEntityMethods;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.NotNull;


@RestController
public class AbstractController {
    @Autowired
    @NotNull
    protected UserDAO userDAO;

    @Autowired
    @NotNull
    protected ForumDAO forumDAO;

    @Autowired
    @NotNull
    protected ThreadDAO threadDAO;

    @Autowired
    @NotNull
    protected PostDAO postDAO;

//    public AbstractController(@NotNull UserDAO userDAO, @NotNull ForumDAO forumDAO, @NotNull ThreadDAO threadDAO,
//                              @NotNull PostDAO postDAO) {
//        this.forumDAO = forumDAO;
//        this.threadDAO = threadDAO;
//        this.userDAO = userDAO;
//        this.postDAO = postDAO;
//    }
//
}