package course.db.managers;

import course.db.dao.PostDAO;
import course.db.dao.ThreadDAO;
import course.db.views.ThreadView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import javax.validation.constraints.NotNull;

@Service
public class ThreadManager {
    private final ThreadDAO threadDAO;

    @Autowired
    public ThreadManager(@NotNull ThreadDAO threadDAO) {this.threadDAO = threadDAO;}

    public ResponseCodes statusClear(Integer count) {
        try {
            count = threadDAO.count();
            threadDAO.clear();
        }
        catch (DataAccessException dAx) {
            return ResponseCodes.DB_ERROR;
        }
        return ResponseCodes.OK;
    }
}
