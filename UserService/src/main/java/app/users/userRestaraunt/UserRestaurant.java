package app.users.userRestaraunt;


import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.Table;

import java.io.Serializable;
import java.util.Objects;


@Entity
@IdClass(UserRestaurantId.class)
public class UserRestaurant {

    private @Id Long user_id;
    private @Id String restaurant;

    public UserRestaurant() {
    }

    public UserRestaurant(Long user_id, String restaurant) {
        this.user_id = user_id;
        this.restaurant = restaurant;
    }

    public Long getUser_id() {
        return user_id;
    }

    public String getRestaurant() {
        return restaurant;
    }

    public void setUser_id(Long user_id) {
        this.user_id = user_id;
    }

    public void setRestaurant(String restaurant) {
        this.restaurant = restaurant;
    }
}

class UserRestaurantId implements Serializable {
    private Long user_id;
    private String restaurant;

    public UserRestaurantId() {
    }

    public UserRestaurantId(Long user_id, String restaurant) {
        this.user_id = user_id;
        this.restaurant = restaurant;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserRestaurantId that = (UserRestaurantId) o;
        return Objects.equals(user_id, that.user_id) && Objects.equals(restaurant, that.restaurant);
    }

    @Override
    public int hashCode() {
        return Objects.hash(user_id, restaurant);
    }

}
