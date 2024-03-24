package app.restaurants;

import app.restaurants.restaurant.RestaurantRepository;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RestaurantsController {
    @Autowired
    RestaurantRepository restaurantRepository;

    public RestaurantsController() {
    }

    @PostMapping({"/showRestaurants"})
    ResponseEntity<Map<String, Object>> showRestaurants() {
        List<String> restaurants = this.restaurantRepository.showRestaurants();
        Map responseBody;
        if (restaurants != null) {
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

    @PostMapping({"/randomRestaurant"})
    ResponseEntity<Map<String, Object>> randomRestaurant() {
        Object[] minMax = (Object[])this.restaurantRepository.minMax().get(0);
        int randomIndex = (new Random()).nextInt((Integer)minMax[1] - (Integer)minMax[0] + 1) + (Integer)minMax[0];
        String restaurant = this.restaurantRepository.randomRestaurant(randomIndex);
        Map responseBody;
        if (restaurant != null) {
            responseBody = Map.of("code", 1, "body", restaurant);
        } else {
            responseBody = Map.of("code", 0, "type", 0);
        }

        return ResponseEntity.status(HttpStatus.OK).body(responseBody);
    }
}