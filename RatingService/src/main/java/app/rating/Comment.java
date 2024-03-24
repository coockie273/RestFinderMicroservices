package app.rating;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "comment")
public class Comment {
    @Id
    private int id;
    private String username;
    private String restaurant;
    private String content;
    private int rating;

    public Comment() {
    }

    public Comment(String username, String restaurant, String content, int rating) {
        this.username = username;
        this.restaurant = restaurant;
        this.content = content;
        this.rating = rating;
    }

    public String getUsername() {
        return this.username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getRestaurant() {
        return this.restaurant;
    }

    public void setRestaurant(String restaurant) {
        this.restaurant = restaurant;
    }

    public String getContent() {
        return this.content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getRating() {
        return this.rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }
}