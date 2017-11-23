package course.db.db_queries;

public class QueryForForums {

    static public String create() {
        return "INSERT INTO forums(owner_id, title, slug) VALUES (?, ?, ?)";
    }

    public static String findForumBySlug() {
        return "SELECT forum.title, forum.posts, forum.threads, forum.slug, user.nickname " +
                "FROM forums forum JOIN userprofiles user ON (forum.owner_id=user.id) " +
                "WHERE forum.slug = ?";
    }

    public static String incrementThreads() {
        return "";
    }

    public static String findForumIdBySlug() {
        return "SELECT id FROM forums WHERE slug = ?";
    }

    //TODO maybe subqu, denormalize
    static public String findThread(){
        return "SELECT user.nickname, forum.slug as forum_slug, thread.id, thread.slug as thread_slug," +
                "thread.created, thread.title, thread.message, thread.vote " +
                "FROM threads thread JOIN userprofiles user ON (thread.owner_id = user,id)" +
                "JOIN forums forum ON (thread.forum_id = forum.id)" +
                "WHERE forum.slug = ? ";
    }

    static public String count() {
        return "SELECT COUNT(*) FROM forums";
    }

    static public String clear() {
        return "DELETE FROM forums";
    }
}
