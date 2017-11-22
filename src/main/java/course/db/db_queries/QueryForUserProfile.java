package course.db.db_queries;

public class QueryForUserProfile {
    static public String create() {
        return "INSERT INTO userprofiles (about, email, fullname, nickname) VALUES(?, ?, ?, ?)";
    }

    static public String getUserByNickOrEmail() {
        return "SELECT * FROM userprofiles WHERE nickname=? OR email =?";
    }

    static public String count() {
        return "SELECT COUNT(*) FROM userprofiles";
    }

    static public String clear() {
        return "DELETE FROM userprofiles";
    }
}
