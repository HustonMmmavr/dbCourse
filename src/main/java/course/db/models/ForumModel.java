package course.db.models;

import course.db.views.ForumView;

public class ForumModel {
    private String title;
    private String user;
    private String slug;
    private Integer posts;
    private Integer threads;

    public ForumModel() {}

    public ForumModel(String title, String user,String slug, Integer posts, Integer threads) {
        this.posts = posts;
        this.slug = slug;
        this.threads = threads;
        this.user = user;
        this.title = title;
    }

    public ForumModel(ForumView forumView) {
        this.posts = forumView.getPosts();
        this.slug = forumView.getSlug();
        this.threads = forumView.getThreads();
        this.user = forumView.getUser();
        this.title = forumView.getTitle();
    }

    public ForumView toForumView(){
        final ForumView forumView = new ForumView(title,user, slug, posts, threads);
        return forumView;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getSlug() {
        return slug;
    }

    public void setSlug(String slug) {
        this.slug = slug;
    }

    public Integer getPosts() {
        return posts;
    }

    public void setPosts(Integer posts) {
        this.posts = posts;
    }

    public Integer getThreads() {
        return threads;
    }

    public void setThreads(Integer threads) {
        this.threads = threads;
    }
}
