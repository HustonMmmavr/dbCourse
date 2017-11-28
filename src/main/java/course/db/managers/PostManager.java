package course.db.managers;

import com.sun.org.apache.regexp.internal.RE;
import course.db.dao.PostDAO;
import course.db.models.PostDetailsModel;
import course.db.models.PostModel;
import course.db.views.PostView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import javax.validation.constraints.NotNull;

@Service
public class PostManager {
    private final PostDAO postDAO;

    @Autowired
    public PostManager(@NotNull PostDAO postDAO) {this.postDAO = postDAO;}

//    public ResponseCodes findPostById(Integer id, PostModel model) {
//        return ResponseCodes.OK;
//    }

    public ResponseCodes updatePost(PostModel postModel) {
        try {
            PostModel model = postDAO.updatePost(postModel);
            postModel.copy(model);
        }
        catch (DuplicateKeyException ex) {
            return ResponseCodes.CONFILICT;
        }
        catch (EmptyResultDataAccessException dAx) {
            return ResponseCodes.NO_RESULT;
        }
        catch (DataAccessException dAx) {
            return ResponseCodes.DB_ERROR;
        }
        return ResponseCodes.OK;
    }

    public ResponseCodes findPostDetailsById(Integer id, String[] args, PostDetailsModel postDetailsModel) {
        try {
            PostDetailsModel model = postDAO.getDetails(id, args);
//            model.setId(postModel.getId());
            postDetailsModel.copy(model);
        }
        catch (EmptyResultDataAccessException dAx) {
            return ResponseCodes.NO_RESULT;
        }
        catch (DataAccessException dAx) {
            return ResponseCodes.DB_ERROR;
        }
        return ResponseCodes.OK;
    }

    public ResponseCodes findPostDetailsById(PostModel postModel) {
        try {
            PostModel model = postDAO.findById(postModel.getId());
//            model.setId(postModel.getId());
            postModel.copy(model);
        }
        catch (EmptyResultDataAccessException dAx) {
            return ResponseCodes.NO_RESULT;
        }
        catch (DataAccessException dAx) {
            return ResponseCodes.DB_ERROR;
        }
        return ResponseCodes.OK;
    }

    public ResponseCodes statusClear() {
        try {
//            count = postDAO.count();
            postDAO.clear();
        }
        catch (DataAccessException dAx) {
            return ResponseCodes.DB_ERROR;
        }
        return ResponseCodes.OK;
    }

    public Integer statusCount() {
//        try {
            return postDAO.count();
//        }
//        catch (DataAccessException dAx) {
//            return ResponseCodes.DB_ERROR;
//        }
//        return ResponseCodes.OK;
    }
}
