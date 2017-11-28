package course.db.db_queries;

import com.sun.xml.internal.ws.server.ServerRtException;

public class QueryForThread {

//    static public String create() {
//        return "INSERT INTO threads (author_id, forum_id, title, created, message, vote, slug)" +
//                "VALUES(?, ?, ?, CASE WHEN ? IS NULL THEN NOW() ELSE ? END"
//    }

    // TODO maybe function in db
    static public String createNoDate() {
        return "INSERT INTO threads (author_id, forum_id, title, created, message, votes, slug)" +
                "VALUES(?, ?, ?,  NOW(), ?, ?, ?)";
    }

    // not correct
    static public String findById() {
        return "SELECT * FROM threads WHERE id = ?";
    }

    static public String createWithDate() {
        return "INSERT INTO threads (author_id, forum_id, title, created, message, votes, slug)" +
                "VALUES(?, ?, ?, ?, ?, ?, ?)";
    }


    static public String count() {
        return "SELECT COUNT(*) FROM threads";
    }

    static public String clear() {
        return "DELETE FROM threads";
    }
}
