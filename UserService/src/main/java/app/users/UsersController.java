package app.users;

import app.users.telegramUser.TelegramUser;
import app.users.telegramUser.TelegramUserRepository;
import app.users.userRestaraunt.UserRestaurant;
import app.users.userRestaraunt.UserRestaurantRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;


@RestController
public class UsersController {

    @Autowired
    TelegramUserRepository telegramUserRepository;

    @Autowired
    UserRestaurantRepository userRestaurantRepository;

    @PostMapping("/register")
    ResponseEntity<Map<String, Integer>> register(@RequestBody TelegramUser telegramUser) {
        TelegramUser user = telegramUserRepository.save(telegramUser);
        Map<String, Integer> responseBody;
        if (user != null) responseBody = Map.of("code", 1);
        else responseBody = Map.of("code", 0, "type", 0);
        return ResponseEntity.status(HttpStatus.OK).body(responseBody);
    }

    @PostMapping("/addToFavoriteList")
    ResponseEntity<Map<String, Integer>> addToFavoriteList(@RequestBody UserRestaurant userRestaurant) {
        //TODO: добавить синхронизацию с сервисом рестораны для проверки синхронизации записей.
        UserRestaurant record = userRestaurantRepository.save(userRestaurant);
        Map<String, Integer> responseBody;

        // Проверка на уникальность записи
        if (record != null) responseBody = Map.of("code", 1);
        else responseBody = Map.of("code", 0, "type", 0);

        return ResponseEntity.status(HttpStatus.OK).body(responseBody);
    }

    @PostMapping("/removeFromFavoriteList")
    ResponseEntity<Map<String, Integer>> removeFromFavoriteList(@RequestBody UserRestaurant userRestaraunt) {
        Map<String, Integer> responseBody;
        try {
            // Проверка на уникальность записи
            userRestaurantRepository.deleteEntry(userRestaraunt.getUser_id(), userRestaraunt.getRestaurant());
            responseBody = Map.of("code", 1);
        } catch (NullPointerException e1) {
            responseBody = Map.of("code", 0, "type", 1);
        }
        return ResponseEntity.status(HttpStatus.OK).body(responseBody);
    }

    @PostMapping("/showFavoriteList")
    ResponseEntity<Map<String, Object>> showFromFavoriteList(@RequestBody Map<String, Long> requestBody) {
        Long user_id = requestBody.get("user_id");
        Map<String, Object> responseBody;
        List<String> restaurants = userRestaurantRepository.showRestaurants(user_id);
        if (restaurants != null) {
            if (restaurants.size() == 0) {
                responseBody = Map.of("code", 0, "type", 1);
            } else {
                String body = IntStream.range(0, restaurants.size())
                        .mapToObj(i -> (i + 1) + ". " + restaurants.get(i))
                        .collect(Collectors.joining(System.lineSeparator()));
                responseBody = Map.of("code", 1, "body", body);
            }
        } else {
            responseBody = Map.of("code", 0, "type", 0);
        }

        return ResponseEntity.status(HttpStatus.OK).body(responseBody);
    }


}
