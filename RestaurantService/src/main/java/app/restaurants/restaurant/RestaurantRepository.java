package app.restaurants.restaurant;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface RestaurantRepository extends JpaRepository<Restaurant, Integer> {
    @Query(
            value = "SELECT concat(r.name,': ', r.description, '\nKeywords: ', string_agg(rk.keyword, ', ')) as keywords FROM restaurant r INNER JOIN restaurant_keyword rk ON r.id = rk.restaurant_id GROUP BY r.id",
            nativeQuery = true
    )
    List<String> showRestaurants();

    @Query("SELECT MIN(r.id), MAX(r.id) FROM Restaurant r")
    List<Object> minMax();

    @Query(
            value = "SELECT concat(r.name,': ', r.description, '\nKeywords: ', string_agg(rk.keyword, ', ')) as keywords  FROM restaurant r INNER JOIN restaurant_keyword rk ON r.id = rk.restaurant_id GROUP BY r.id HAVING id >= :id ORDER BY id LIMIT 1",
            nativeQuery = true
    )
    String randomRestaurant(@Param("id") int id);
}
