package course.db.db_queries;

public class QueryForForums {

    static public String create() {
        return "INSERT INTO forums(owner_id, title, slug) VALUES (?, ?, ?)";
    }

    public static String findForumBySlug() {
        return "SELECT forum.title, forum.posts, forum.threads, forum.slug, _user.nickname " +
                "FROM forums forum JOIN userprofiles _user ON (forum.owner_id=_user.id) " +
                "WHERE forum.slug = ?";
    }

//    public static String findForumIdBySlug() {
//        return "SELECT forum.title, forum.posts, forum.threads, forum.slug, _user.nickname " +
//                "FROM forums forum JOIN userprofiles _user ON (forum.owner_id=_user.id) " +
//                "WHERE forum.slug = ?";
//    }

    public static String incrementThreads() {
        return "";
    }

    public static String findForumIdBySlug() {
        return "SELECT id FROM forums WHERE slug = ?";
    }

    //TODO maybe subqu, denormalize
    static public String findThread(){
        return "SELECT _user.nickname, forum.slug as forum_slug, thread.id, thread.slug as thread_slug," +
                "thread.created, thread.title, thread.message, thread.votes " +
                "FROM threads thread JOIN userprofiles _user ON (thread.author_id = _user.id)" +
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