import java.util.ArrayList;
import java.util.List;

public class Writer extends User{
    private List<Article> articles;

    public Writer(String username, String password) {
        super(username, password);
        articles = new ArrayList<>();
    }

    @Override
    public UserType getUserType() {
        return UserType.WRITER;
    }

    public void createArticle(String title, String content) {
        articles.add(new Article(title, content));
    }

    public Article sendArticle(String title) {
        return articles.stream().filter(article1 -> article1.getTitle().equals(title)).findFirst().orElse(null);
    }
}
