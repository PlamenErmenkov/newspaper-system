public class Article {
    private String title;
    private String content;
    private boolean isApproved;

    public Article(String title, String content) {
        this.title = title;
        this.content = content;
        isApproved = false;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public boolean isApproved() {
        return isApproved;
    }

    public void setApproved(boolean approved) {
        isApproved = approved;
    }
}
