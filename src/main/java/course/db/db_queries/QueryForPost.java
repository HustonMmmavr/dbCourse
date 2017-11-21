package course.db.db_queries;

public class QueryForPost {
    static public String count() {
        return "SELECT COUNT(*) FROM posts";
    }

    static public String clear() {
        return "DELETE FROM posts";
    }
}
