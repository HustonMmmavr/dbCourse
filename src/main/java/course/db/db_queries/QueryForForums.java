package course.db.db_queries;

public class QueryForForums {
    static public String count() {
        return "SELECT COUNT(*) FROM forums";
    }

    static public String clear() {
        return "DELETE FROM forums";
    }
}
