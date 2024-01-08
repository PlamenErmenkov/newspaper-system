public class Editor extends User {
    public Editor(String username, String password) {
        super(username, password);
    }

    @Override
    public UserType getUserType() {
        return UserType.EDITOR;
    }

    public void editArticle(Article article, String content) {
        article.setContent(content);
    }

    public String displayArticle(Article article) {
        return article.getTitle() + "\n" + article.getContent();
    }

    public void approveArticle(Article article) {
        article.setApproved(true);
    }
}
