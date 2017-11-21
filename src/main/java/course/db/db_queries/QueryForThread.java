package course.db.db_queries;

public class QueryForThread {
    static public String count() {
        return "SELECT COUNT(*) FROM threads";
    }

    static public String clear() {
        return "DELETE FROM threads";
    }
}
