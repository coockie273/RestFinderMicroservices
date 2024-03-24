package app.users.userRestaraunt;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRestaurantRepository extends JpaRepository<UserRestaurant, Integer> {

    @Transactional
    @Modifying
    @Query("DELETE FROM UserRestaurant ur WHERE ur.user_id = :user_id AND ur.restaurant = :restaurant")
    void deleteEntry(@Param("user_id") Long user_id, @Param("restaurant") String restaurant);

    @Query("SELECT ur.restaurant FROM UserRestaurant ur WHERE ur.user_id = :user_id")
    List<String> showRestaurants(@Param("user_id") Long user_id);
}
