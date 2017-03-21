package it.globrutto.popularmovies.model;

/**
 * Created by giuseppelobrutto on 09/03/17.
 */

public class Review {
    private String id;
    private String author;
    private String content;
    private String url;

    public void setId(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getAuthor() {
        return author;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getContent() {
        return content;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getUrl() {
        return url;
    }
}
