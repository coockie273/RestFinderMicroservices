package app.rating;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CommentsController {
    @Autowired
    CommentRepository commentRepository;

    public CommentsController() {
    }

    @PostMapping({"/showComments"})
    ResponseEntity<Map<String, Object>> showComments(@RequestBody Map<String, String> requestBody) {
        String restaurant = (String)requestBody.get("restaurant");
        List<String> restaurants = this.commentRepository.showComments(restaurant);
        Map responseBody;
        if (!restaurants.equals((Object)null)) {
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

    @PostMapping({"/showRating"})
    ResponseEntity<Map<String, Object>> showRating(@RequestBody Map<String, String> requestBody) {
        String restaurant = (String)requestBody.get("restaurant");
        float rating = this.commentRepository.showRating(restaurant);

        Map responseBody;
        try {
            responseBody = Map.of("code", 0, "body", rating);
        } catch (NullPointerException var6) {
            responseBody = Map.of("code", 0, "type", 1);
        }

        return ResponseEntity.status(HttpStatus.OK).body(responseBody);
    }

    @PostMapping({"/top"})
    ResponseEntity<Map<String, Object>> top() {
        List<String> top = this.commentRepository.top();

        Map responseBody;
        try {
            String body = (String)IntStream.range(0, top.size()).mapToObj((i) -> {
                return i + 1 + ". " + (String)top.get(i);
            }).collect(Collectors.joining(System.lineSeparator()));
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