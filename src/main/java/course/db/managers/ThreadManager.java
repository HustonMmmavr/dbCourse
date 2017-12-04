package course.db.managers;

import course.db.dao.ThreadDAO;
import course.db.models.ThreadModel;
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

    public StatusManagerRequest findThreadBySlugOrId(ThreadModel threadModel) {
        try {
            ThreadModel model = threadDAO.findBySlugOrId(threadModel);
            threadModel.copy(model);
        }
        catch (EmptyResultDataAccessException eRx) {
            return new StatusManagerRequest(ManagerResponseCodes.NO_RESULT, eRx);
        }
        catch (DataAccessException dAx) {
            return new StatusManagerRequest(ManagerResponseCodes.DB_ERROR, dAx);
        }
        return new StatusManagerRequest(ManagerResponseCodes.OK);
    }

    public StatusManagerRequest updateThread(ThreadModel threadModel) {
        try {
            threadDAO.updateThread(threadModel);
            ThreadModel newThreadModel = threadDAO.findBySlugOrId(threadModel);
            threadModel.copy(newThreadModel);
        }
        catch (DuplicateKeyException dAx) {
            return new StatusManagerRequest(ManagerResponseCodes.CONFILICT, dAx);
        }
        catch (EmptyResultDataAccessException eRx) {
            return new StatusManagerRequest(ManagerResponseCodes.NO_RESULT, eRx);
        }
        catch (DataAccessException dAx) {
            return new StatusManagerRequest(ManagerResponseCodes.DB_ERROR, dAx);
        }
        return new StatusManagerRequest(ManagerResponseCodes.OK);
    }

    public StatusManagerRequest findThreadBySlug(ThreadModel threadModel) {
        try {
            ThreadModel existingThread = threadDAO.findThread(threadModel);
            threadModel.copy(existingThread);
        }
        catch (EmptyResultDataAccessException eRx) {
            return new StatusManagerRequest(ManagerResponseCodes.NO_RESULT, eRx);
        }
        catch (DataAccessException dAx) {
            return new StatusManagerRequest(ManagerResponseCodes.DB_ERROR, dAx);
        }
        return new StatusManagerRequest(ManagerResponseCodes.OK);
    }

    public StatusManagerRequest createThread (ThreadModel threadModel) {
        try {
            ThreadModel createdThread =  threadDAO.createThread(threadModel);
            threadModel.copy(createdThread);
            //TODO fill data from
        }
        catch (EmptyResultDataAccessException eRx) {
            return new StatusManagerRequest(ManagerResponseCodes.NO_RESULT, eRx);
        }
        catch (DuplicateKeyException dAx) {
            return new StatusManagerRequest(ManagerResponseCodes.CONFILICT, dAx);
        }
        catch (DataAccessException dAx) {
            return new StatusManagerRequest(ManagerResponseCodes.DB_ERROR, dAx);
        }
        return new StatusManagerRequest(ManagerResponseCodes.OK);
    }

    public StatusManagerRequest statusClear() {
        try {
            threadDAO.clear();
        }
        catch (DataAccessException dAx) {
            return new StatusManagerRequest(ManagerResponseCodes.DB_ERROR, dAx);
        }
        return new StatusManagerRequest(ManagerResponseCodes.OK);
    }

    public Integer statusCount() {
        return threadDAO.count();
    }
}

//            threadModel.setAuthor(createdThread.getAuthor());
//            threadModel.setCreated(createdThread.getCreated());
//            threadModel.setId(createdThread.getId());
//            threadModel.setForum(createdThread.getForum());
//            threadModel.setSlug(createdThread.getSlug());
//            threadModel.setMessage(createdThread.getMessage());
//            threadModel.setTitle(createdThread.getTitle());
//            threadModel.setVotes(createdThread.getVotes());
//           threadModel;

//    public ManagerResponseCodes create (ThreadModel threadModel) {
//        try {
//           ThreadModel createdThread =  threadDAO.create(threadModel);
////           threadModel;
//            //TODO fill data from
//        }
//        catch (EmptyResultDataAccessException ex) {
//            return ManagerResponseCodes.NO_RESULT;
//        }
//        catch (DuplicateKeyException dex) {
//            return ManagerResponseCodes.CONFILICT;
//        }
//        catch (DataAccessException d) {
//            return ManagerResponseCodes.DB_ERROR;
//        }
//        return ManagerResponseCodes.OK;
//    }




//        try {

//        }
//        catch (DataAccessException dAx) {
//            return ManagerResponseCodes.DB_ERROR;
//        }
//        return ManagerResponseCodes.OK;