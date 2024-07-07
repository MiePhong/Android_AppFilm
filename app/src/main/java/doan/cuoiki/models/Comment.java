package doan.cuoiki.models;
public class Comment {
    private String avatarUrl;
    private String username;
    private String content;
    private String date;
    private String id;



    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Comment(String avatarUrl, String username, String content, String date, String id) {
        this.avatarUrl = avatarUrl;
        this.username = username;
        this.content = content;
        this.date = date;
        this.id = id;
    }

    public String getAvatarUrl() {
        return avatarUrl;
    }

    public void setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
