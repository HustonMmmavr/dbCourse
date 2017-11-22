package course.db.controllers;

import course.db.managers.ForumManager;
import course.db.managers.PostManager;
import course.db.managers.ThreadManager;
import course.db.managers.UserProfileManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.NotNull;


@RestController
public class AbstractController {
//    @Autowired
//    @NotNull
//    protected UserDAO userDAO;
//
//    @Autowired
//    @NotNull
//    protected ForumDAO forumDAO;
//
//    @Autowired
//    @NotNull
//    protected ThreadDAO threadDAO;
//
//    @Autowired
//    @NotNull
//    protected PostDAO postDAO;

    @Autowired
    @NotNull
    protected UserProfileManager userProfileManager;

    @Autowired
    @NotNull
    protected ForumManager forumManager;

    @Autowired
    @NotNull
    protected PostManager postManager;

    @Autowired
    @NotNull
    protected ThreadManager threadManager;
//    public AbstractController(@NotNull UserDAO userDAO, @NotNull ForumDAO forumDAO, @NotNull ThreadDAO threadDAO,
//                              @NotNull PostDAO postDAO) {
//        this.forumDAO = forumDAO;
//        this.threadDAO = threadDAO;
//        this.userDAO = userDAO;
//        this.postDAO = postDAO;
//    }
//
}