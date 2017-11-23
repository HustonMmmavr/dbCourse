package course.db.managers;

import com.sun.org.apache.regexp.internal.RE;
import course.db.dao.PostDAO;
import course.db.views.PostView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import javax.validation.constraints.NotNull;

@Service
public class PostManager {
    private final PostDAO postDAO;

    @Autowired
    public PostManager(@NotNull PostDAO postDAO) {this.postDAO = postDAO;}

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
