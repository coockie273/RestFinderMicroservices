package app.rating;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Integer> {
    @Query(
            value = "SELECT concat('Пользователь: ', c.username, '\n','Отзыв: ', c.content, '\n','Оценка: ', c.rating) FROM comment c WHERE c.restaurant = :restaurant",
            nativeQuery = true
    )
    List<String> showComments(@Param("restaurant") String restaurant);

    @Query("SELECT AVG(c.rating) FROM Comment c WHERE c.restaurant = :restaurant ")
    float showRating(@Param("restaurant") String restaurant);

    @Query(
            value = "SELECT CONCAT(res, ' - ', rat) FROM( SELECT c.restaurant as res, ROUND(AVG(rating), 2) as rat FROM comment c GROUP BY c.restaurant ORDER BY rat DESC LIMIT 5) as query",
            nativeQuery = true
    )
    List<String> top();

    @Query(
            value = "SELECT concat('Ресторан: ', c.restaurant, '\n','Отзыв: ', c.content, '\n','Оценка: ', c.rating) FROM comment c WHERE c.username = :username",
            nativeQuery = true
    )
    List<String> myComments(@Param("username") String username);
}
