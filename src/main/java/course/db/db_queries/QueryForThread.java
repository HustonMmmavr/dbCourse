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
                "VALUES(?, ?, ?,  NOW(), ?, ?, ?) RETURNING id";
    }

    static private String findThread() {
        return "SELECT _user.nickname, thread.created, forum.slug AS forum_slug, thread.id, thread.message, thread.slug AS thread_slug, " +
                "thread.title, thread.votes " +
                "FROM threads thread JOIN userprofiles _user ON (thread.author_id = _user.id)" +
                "  JOIN forums forum ON (thread.forum_id = forum.id) ";
    }

    static public String findThreadById() {
        return findThread() + "  WHERE thread.id = ?";
    }

    static public String findThreadBySlug() {
        return findThread() + "  WHERE thread.slug = ?::CITEXT";
    }

//    static public String findThreadById() {
//        return "SELECT _user.nickname, thread.created, forum.slug AS forum_slug, thread.id, thread.message, thread.slug AS thread_slug, " +
//                "thread.title, thread.votes " +
//                "FROM threads thread JOIN userprofiles _user ON (thread.author_id = _user.id)" +
//                "  JOIN forums forum ON (thread.forum_id = forum.id) " +
//                "  WHERE thread.id = ?";
//
//    }

//    static public String findThreadBySlug() {
//        return "SELECT _user.nickname, thread.created, forum.slug AS forum_slug, thread.id, thread.message, thread.slug AS thread_slug, " +
//                "thread.title, thread.votes " +
//                "FROM threads thread JOIN userprofiles _user ON (thread.author_id = _user.id)" +
//                "  JOIN forums forum ON (thread.forum_id = forum.id) " +
//                "  WHERE thread.slug = ?::CITEXT";
//    }

    // not correct
//    static public String findById() {
//        return "SELECT * FROM threads WHERE id = ?";
//    }
//
//    static public String findBySlug() {
//        return "SELECT * FROM threads WHERE slug = ?::CITEXT";
//    }
//
//    static public String findBySlugOrId() {return "";}

    static public String createWithDate() {
        return "INSERT INTO threads (author_id, forum_id, title, created, message, votes, slug)" +
                "VALUES(?, ?, ?, ?::TIMESTAMPTZ, ?, ?, ?) RETURNING id";
    }


    static public String count() {
        return "SELECT COUNT(*) FROM threads";
    }

    static public String clear() {
        return "DELETE FROM threads";
    }
}
