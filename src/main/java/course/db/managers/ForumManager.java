package course.db.managers;

import com.sun.org.apache.regexp.internal.RE;
import course.db.dao.ForumDAO;
import course.db.models.ForumModel;
import course.db.models.ThreadModel;
import course.db.views.ForumView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import javax.validation.constraints.NotNull;
import java.util.List;

@Service
public class ForumManager {
    private final ForumDAO forumDAO;

    @Autowired
    public ForumManager(@NotNull ForumDAO forumDAO) {this.forumDAO = forumDAO;}

    public ResponseCodes create(ForumModel forumModel) {//, List<ForumView> forumViewList) {
        try {
            forumDAO.create(forumModel);
        }
        catch (EmptyResultDataAccessException ex) {
            return ResponseCodes.NO_RESULT;
        }
        catch(DuplicateKeyException dupE) {
            return ResponseCodes.CONFILICT;
        }
        catch (DataAccessException daEx) {
            return ResponseCodes.DB_ERROR;
        }
        return ResponseCodes.OK;
    }

//    public ResponseCodes createThread (ThreadModel threadModel) {
//        try {
//           ThreadModel createdThread =  threadDAOd.createThread(threadModel);
//
//           threadModel.setAuthor(createdThread.getAuthor());
//           threadModel.setCreated(createdThread.getCreated());
//           threadModel.setId(createdThread.getId());
//           threadModel.setForum(createdThread.getForum());
//           threadModel.setSlug(createdThread.getSlug());
//           threadModel.setMessage(createdThread.getMessage());
//           threadModel.setTitle(createdThread.getTitle());
//           threadModel.setVotes(createdThread.getVotes());
////           threadModel;
//            //TODO fill data from
//        }
//        catch (EmptyResultDataAccessException ex) {
//            return ResponseCodes.NO_RESULT;
//        }
//        catch (DuplicateKeyException dex) {
//            return ResponseCodes.CONFILICT;
//        }
//        catch (DataAccessException d) {
//            System.out.print(d.getMessage());
//            return ResponseCodes.DB_ERROR;
//        }
//        return ResponseCodes.OK;
//    }


    public ResponseCodes findForum(ForumModel forumModel) {//, List<ForumView> forumViewList) {
        try {
            ForumModel forum = forumDAO.getForumBySlug(forumModel.getSlug());
            forumModel.setPosts(forum.getPosts());
            forumModel.setThreads(forum.getThreads());
            forumModel.setTitle(forum.getTitle());
            forumModel.setUser(forum.getUser());
        }
        catch (EmptyResultDataAccessException e) {
            return ResponseCodes.NO_RESULT;
        }
        catch (DataAccessException daEx) {
            return ResponseCodes.DB_ERROR;
        }
        return ResponseCodes.OK;
    }

    public ResponseCodes statusClear() {
        try {
            forumDAO.clear();
        }
        catch (DataAccessException dAx) {
            return ResponseCodes.DB_ERROR;
        }
        return ResponseCodes.OK;
    }

    public Integer statusCount() {
//        try {
            return forumDAO.count();
//        }
//        catch (DataAccessException dAx) {
//            return ResponseCodes.DB_ERROR;
//        }
//        return ResponseCodes.OK;
    }
}
