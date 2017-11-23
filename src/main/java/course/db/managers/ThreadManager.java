package course.db.managers;

import com.sun.istack.internal.Nullable;
import com.sun.org.apache.regexp.internal.RE;
import course.db.dao.PostDAO;
import course.db.dao.ThreadDAO;
import course.db.models.ThreadModel;
import course.db.views.ThreadView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import javax.validation.constraints.NotNull;

@Service
public class ThreadManager {
    private final ThreadDAO threadDAO;

    @Autowired
    public ThreadManager(@NotNull ThreadDAO threadDAO) {this.threadDAO = threadDAO;}

//    public ResponseCodes create (ThreadModel threadModel) {
//        try {
//           ThreadModel createdThread =  threadDAO.create(threadModel);
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
//            return ResponseCodes.DB_ERROR;
//        }
//        return ResponseCodes.OK;
//    }

    public ResponseCodes findThread(ThreadModel threadModel) {
        try {
            ThreadModel existingThread = threadDAO.findThread(threadModel);
            threadModel.copy(existingThread);
        }
        catch (EmptyResultDataAccessException em) {
            return ResponseCodes.NO_RESULT;
        }
        catch (DataAccessException d) {
            return ResponseCodes.DB_ERROR;
        }
        return ResponseCodes.OK;
    }

    public ResponseCodes createThread (ThreadModel threadModel) {
        try {
            ThreadModel createdThread =  threadDAO.createThread(threadModel);

            threadModel.setAuthor(createdThread.getAuthor());
            threadModel.setCreated(createdThread.getCreated());
            threadModel.setId(createdThread.getId());
            threadModel.setForum(createdThread.getForum());
            threadModel.setSlug(createdThread.getSlug());
            threadModel.setMessage(createdThread.getMessage());
            threadModel.setTitle(createdThread.getTitle());
            threadModel.setVotes(createdThread.getVotes());
//           threadModel;
            //TODO fill data from
        }
        catch (EmptyResultDataAccessException ex) {
            return ResponseCodes.NO_RESULT;
        }
        catch (DuplicateKeyException dex) {
            return ResponseCodes.CONFILICT;
        }
        catch (DataAccessException d) {
            System.out.print(d.getMessage());
            return ResponseCodes.DB_ERROR;
        }
        return ResponseCodes.OK;
    }

    public ResponseCodes statusClear() {
        try {
            threadDAO.clear();
        }
        catch (DataAccessException dAx) {
            return ResponseCodes.DB_ERROR;
        }
        return ResponseCodes.OK;
    }

    public Integer statusCount() {
//        try {
            return threadDAO.count();
//        }
//        catch (DataAccessException dAx) {
//            return ResponseCodes.DB_ERROR;
//        }
//        return ResponseCodes.OK;
    }
}
