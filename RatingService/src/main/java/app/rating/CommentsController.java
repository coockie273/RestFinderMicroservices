package app.rating;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import redis.clients.jedis.JedisPooled;

@RestController
public class CommentsController {
    @Autowired
    CommentRepository commentRepository;

    JedisPooled jedis = new JedisPooled("redis", 6379);

    public CommentsController() {
    }

    @PostMapping({"/showComments"})
    ResponseEntity<Map<String, Object>> showComments(@RequestBody Map<String, String> requestBody) {
        String restaurant = requestBody.get("restaurant");
        List<String> restaurants = this.commentRepository.showComments(restaurant);
        Map responseBody;
        if (!restaurants.equals(null)) {
            if (restaurants.size() == 0) {
                responseBody = Map.of("code", 0, "type", 1);
            } else {
                String body = (String)IntStream.range(0, restaurants.size()).mapToObj((i) -> {
                    return i + 1 + ". " + (String)restaurants.get(i);
                }).collect(Collectors.joining(System.lineSeparator()));
                responseBody = Map.of("code", 1, "body", body);
            }
        } else {
            responseBody = Map.of("code", 0, "type", 0);
        }

        return ResponseEntity.status(HttpStatus.OK).body(responseBody);
    }

    float getCachedRating(String restaurant) {
        int ttl = 60;
        String raw = jedis.get(restaurant);
        if (raw != null) {
            return Float.parseFloat(raw);
        }
        Float rating = this.commentRepository.showRating(restaurant);
        jedis.setex(restaurant, ttl, rating.toString());
        return rating;
    }

    @PostMapping({"/showRating"})
    ResponseEntity<Map<String, Object>> showRating(@RequestBody Map<String, String> requestBody) {
        String restaurant = (String)requestBody.get("restaurant");
        Map responseBody;

        try {
            float rating = getCachedRating(restaurant);
            responseBody = Map.of("code", 1, "body", rating);
        } catch (NullPointerException n) {
            responseBody = Map.of("code", 0, "type", 1);
        }

        return ResponseEntity.status(HttpStatus.OK).body(responseBody);
    }

    String getCachedTop() {
        int ttl = 600;
        String raw = jedis.get("top");
        if (raw != null) {
            return raw;
        }
        List<String> top = this.commentRepository.top();
        String body = IntStream.range(0, top.size()).mapToObj((i) -> i + 1 + ". " + top.get(i))
                .collect(Collectors.joining(System.lineSeparator()));
        jedis.setex("top", ttl, body);
        return body;
    }

    @PostMapping({"/top"})
    ResponseEntity<Map<String, Object>> top() {
        Map responseBody;
        try {
            String body = getCachedTop();
            responseBody = Map.of("code", 1, "body", body);
        } catch (NullPointerException var4) {
            responseBody = Map.of("code", 0, "type", 1);
        }

        return ResponseEntity.status(HttpStatus.OK).body(responseBody);
    }

    @PostMapping({"/myComments"})
    ResponseEntity<Map<String, Object>> myComments(@RequestBody Map<String, String> requestBody) {
        String username = (String)requestBody.get("username");
        List<String> comments = this.commentRepository.myComments(username);
        Map responseBody;
        if (!comments.equals((Object)null)) {
            if (comments.size() == 0) {
                responseBody = Map.of("code", 0, "type", 1);
            } else {
                String body = (String)IntStream.range(0, comments.size()).mapToObj((i) -> {
                    return i + 1 + ". " + (String)comments.get(i);
                }).collect(Collectors.joining(System.lineSeparator()));
                responseBody = Map.of("code", 1, "body", body);
            }
        } else {
            responseBody = Map.of("code", 0, "type", 0);
        }

        return ResponseEntity.status(HttpStatus.OK).body(responseBody);
    }
}
