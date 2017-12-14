package course.db.db_queries;

public class QueryForPost {

    static public String createPost() {
        return "INSERT INTO posts (id, parent_id, author_id, created, forum_id, message, thread_id, id_of_root, path_to_post) " +
                "VALUES(?, ?, ?, ?::TIMESTAMPTZ, ?, ?, ?, ?, array_append(?, ?))";
    }

    public static String getId() {
        return "SELECT nextval('posts_id_seq')";
    }

    static public String getPath() {
        return "SELECT path_to_post FROM posts WHERE id = ?";
    }

    static public String getById() {
        return "SELECT created, id, is_edited, message, parent_id, thread_id, forum_slug, autor_name " +
                "FROM posts WHERE id = ? ";
    };

    static public String oldgetById() {
        return "SELECT post.created, post.id, post.is_edited, post.message, post.parent_id, post.thread_id, forum.slug, _user.nickname " +
                "FROM posts post JOIN userprofiles _user ON (_user.id = post.author_id) JOIN forums forum ON (forum.id = post.forum_id) " +
                "WHERE post.id = ? ";
    };

    static private String findPosts() {
        return "SELECT created, forum_slug, id, is_edited, message, parent_id, thread_id, author_name FROM posts " +
            "WHERE thread_id = ?";
    }

    public static String postsFlat(final Integer limit, final Integer since, final Boolean desc ) {
        StringBuilder builder = new StringBuilder();
        builder.append("SELECT u.nickname, p.created, f.slug, p.id, p.is_edited, p.message, p.parent_id, p.thread_id ");
        builder.append("FROM userprofiles u JOIN posts p ON (u.id = p.author_id) ");
        builder.append("JOIN forums f ON (f.id = p.forum_id) ");
        builder.append("WHERE p.thread_id = ? ");
        String order = (desc == Boolean.TRUE ? " DESC " : " ASC ");
        String sign = (desc == Boolean.TRUE ? " < " : " > ");
        if (since != null) {
            builder.append(" AND p.id").append(sign).append("? ");
        }
        builder.append("ORDER BY p.id ").append(order);
        if (limit != null) {
            builder.append("LIMIT ?");
        }
        return builder.toString();
    }

    public static String postsTree(final Integer limit, final Integer since, final Boolean desc ) {
        StringBuilder builder = new StringBuilder();
        builder.append("SELECT u.nickname, p.created, f.slug, p.id, p.is_edited, p.message, p.parent_id, p.thread_id ");
        builder.append("FROM userprofiles u JOIN posts p ON (u.id = p.author_id) ");
        builder.append("JOIN forums f ON (f.id = p.forum_id) ");
        builder.append("WHERE p.thread_id = ? ");
        String order = (desc == Boolean.TRUE ? " DESC " : " ASC ");
        String sign = (desc == Boolean.TRUE ? " < " : " > ");
        if (since != null) {
            builder.append(" AND p.path_to_post ").append(sign).append("(SELECT path_to_post FROM posts WHERE id = ?) ");
        }
        builder.append("ORDER BY p.path_to_post ").append(order);
        if (limit != null) {
            builder.append("LIMIT ?");
        }
        return builder.toString();
    }

    public static String postsParentTree(final Integer limit, final Integer since, final Boolean desc ) {
        String order = (desc == Boolean.TRUE ? " DESC " : " ASC ");
        String sign = (desc == Boolean.TRUE ? " < " : " > ");
        StringBuilder builder = new StringBuilder();
        builder.append("SELECT u.nickname, p.created, f.slug, p.id, p.is_edited, p.message, p.parent_id, p.thread_id ");
        builder.append("FROM userprofiles u JOIN posts p ON (u.id = p.author_id) ");
        builder.append("JOIN forums f ON (f.id = p.forum_id) ");
        builder.append("WHERE p.id_of_root IN (SELECT id FROM posts WHERE thread_id = ? AND parent_id = 0 ");
        if (since != null) {
            builder.append(" AND path_to_post ").append(sign).append("(SELECT path_to_post FROM posts WHERE id = ?) ");
        }
        builder.append("ORDER BY id ").append(order);
        if (limit != null) {
            builder.append(" LIMIT ?");
        }
        builder.append(") ");
        builder.append("ORDER BY p.path_to_post ").append(order);
        return builder.toString();
    }


    static public String count() {
        return "SELECT COUNT(*) FROM posts";
    }

    static public String clear() {
        return "DELETE FROM posts";
    }
}
