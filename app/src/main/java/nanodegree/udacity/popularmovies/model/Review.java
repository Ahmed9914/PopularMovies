package nanodegree.udacity.popularmovies.model;

/**
 * Created by Ahmd on 12/03/2018.
 */

public class Review {
    private String author;
    private String content;

    public Review() {}

    public Review(String mAuthor, String mContent) {
        author = mAuthor;
        content = mContent;

    }

    public String getContent() {
        return content;
    }

    public String getAuthor() {

        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
