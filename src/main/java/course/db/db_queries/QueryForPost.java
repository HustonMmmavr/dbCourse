package course.db.db_queries;

public class QueryForPost {

    // not correct
//    static public String getById() {
//        return "SELECT * FROM posts where id = ?";
//    }

    // join
    static public String getById() {
        return "SELECT _user.nickname, ";
    }
    static public String count() {
        return "SELECT COUNT(*) FROM posts";
    }

    static public String clear() {
        return "DELETE FROM posts";
    }
}
