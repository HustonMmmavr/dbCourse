package course.db.managers;

import course.db.dao.PostDAO;
import course.db.models.PostDetailsModel;
import course.db.models.PostModel;
import course.db.models.ThreadModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import javax.validation.constraints.NotNull;
import java.util.List;

@Service
public class PostManager {
    private final PostDAO postDAO;

    @Autowired
    public PostManager(@NotNull PostDAO postDAO) {this.postDAO = postDAO;}

    public StatusManagerRequest findById(Integer id, PostModel postModel) {
        try {
            PostModel model = postDAO.findById(id);
            postModel.copy(model);
        } catch (EmptyResultDataAccessException eRx) {
            return new StatusManagerRequest(ManagerResponseCodes.NO_RESULT, eRx);
        }
        catch (DataAccessException dAx) {
            return new StatusManagerRequest(ManagerResponseCodes.DB_ERROR, dAx);
        }
        return new StatusManagerRequest(ManagerResponseCodes.OK);
        //        catch (DuplicateKeyException ex) {
//            return new StatusManagerRequest(ManagerResponseCodes.CONFILICT, ex);
//        }
    }

    public StatusManagerRequest create(List<PostModel> postModelList, ThreadModel threadModel) {
        try {
            if (threadModel.getId() != null) {
//                postDAO.createByForumId(postModelList, threadModel);
            }
            else {
//                postDAO.createByForumSlug(postModelList, threadModel);
            }
        }
        catch (DuplicateKeyException dKx) {
            return new StatusManagerRequest(ManagerResponseCodes.CONFILICT, dKx);
        }
        catch (EmptyResultDataAccessException eRx) {
            return new StatusManagerRequest(ManagerResponseCodes.NO_RESULT, eRx);
        }
        catch (DataAccessException dAx) {
            return new StatusManagerRequest(ManagerResponseCodes.DB_ERROR, dAx);
        }
        return new StatusManagerRequest(ManagerResponseCodes.OK);
    }
//    public StatusManagerRequest findParent(Pos)

    public StatusManagerRequest updatePost(PostModel postModel) {
        try {
            PostModel model = postDAO.updatePost(postModel);
            postModel.copy(model);
        }
        catch (DuplicateKeyException dKx) {
            return new StatusManagerRequest(ManagerResponseCodes.CONFILICT, dKx);
        }
        catch (EmptyResultDataAccessException eRx) {
            return new StatusManagerRequest(ManagerResponseCodes.NO_RESULT, eRx);
        }
        catch (DataAccessException dAx) {
            return new StatusManagerRequest(ManagerResponseCodes.DB_ERROR, dAx);
        }
        return new StatusManagerRequest(ManagerResponseCodes.OK);
    }

    public StatusManagerRequest findPostDetailsById(Integer id, String[] args, PostDetailsModel postDetailsModel) {
        try {
            PostDetailsModel model = postDAO.getDetails(id, args);
            postDetailsModel.copy(model);
        }
        catch (EmptyResultDataAccessException eRx) {
            return new StatusManagerRequest(ManagerResponseCodes.NO_RESULT, eRx);
        }
        catch (DataAccessException dAx) {
            return new StatusManagerRequest(ManagerResponseCodes.DB_ERROR, dAx);
        }
        return new StatusManagerRequest(ManagerResponseCodes.OK);
    }

    public StatusManagerRequest statusClear() {
        try {
            postDAO.clear();
        }
        catch (DataAccessException dAx) {
            return new StatusManagerRequest(ManagerResponseCodes.DB_ERROR, dAx);
        }
        return new StatusManagerRequest(ManagerResponseCodes.OK);
    }

    public Integer statusCount() {
        return postDAO.count();
    }
}


//    public StatusManagerRequest findPostDetailsById(PostModel postModel) {
//        try {
//            PostModel model = postDAO.findById(postModel.getId());
//            postModel.copy(model);
//        }
//        catch (EmptyResultDataAccessException eRx) {
//            return new StatusManagerRequest(ManagerResponseCodes.NO_RESULT, eRx);
//        }
//        catch (DataAccessException dAx) {
//            return new StatusManagerRequest(ManagerResponseCodes.DB_ERROR, dAx);
//        }
//        return new StatusManagerRequest(ManagerResponseCodes.OK);
//    }


//            model.setId(postModel.getId());

//            model.setId(postModel.getId());

//        }
//        catch (DataAccessException dAx) {
//            return ManagerResponseCodes.DB_ERROR;
//        }
//        return ManagerResponseCodes.OK;