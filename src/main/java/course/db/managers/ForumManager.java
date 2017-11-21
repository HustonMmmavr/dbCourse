package course.db.managers;

import com.sun.org.apache.regexp.internal.RE;
import course.db.dao.ForumDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import javax.validation.constraints.NotNull;

@Service
public class ForumManager {
    private final ForumDAO forumDAO;

    @Autowired
    public ForumManager(@NotNull ForumDAO forumDAO) {this.forumDAO = forumDAO;}

    public ResponseCodes statusClear(Integer count) {
        try {
            count = forumDAO.count();
            forumDAO.clear();
        }
        catch (DataAccessException dAx) {
            return ResponseCodes.DB_ERROR;
        }
        return ResponseCodes.OK;
    }

}
