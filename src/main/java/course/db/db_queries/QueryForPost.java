package course.db.db_queries;

public class QueryForPost {

    // not correct
//    static public String getById() {
//        return "SELECT * FROM posts where id = ?";
//    }
    static public String createPost() {
        return "INSERT INTO posts (author_id, created, forum_id, id, message, parent, thread_id, path, root_id) " +
                "VALUES(?, ?, ?, ?, ?, ?, ?, array_append(?, ?), ?)";
    }
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
