package app.restaurants;

import app.restaurants.restaurant.RestaurantRepository;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import redis.clients.jedis.JedisPooled;

@RestController
public class RestaurantsController {
    @Autowired
    RestaurantRepository restaurantRepository;

    String jedisHost;

    JedisPooled jedis = new JedisPooled("redis", 6379);

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

    Object[] cachedMinMax(){
        int ttl = 600;
        ObjectMapper objectMapper = new ObjectMapper();
        String raw = jedis.get("min_max");
        if (raw != null) {
            try {
                return objectMapper.readValue(raw, Object[].class);
            } catch (JsonProcessingException e) {
                return null;
            }
        }
        Object[] minMax = (Object[])this.restaurantRepository.minMax().get(0);
        try {
            jedis.setex("min_max", ttl, objectMapper.writeValueAsString(minMax));
            return minMax;
        } catch (JsonProcessingException e) {
            return null;
        }
    }

    String getCachedRestaurantInfo(int index) {
        int ttl = 600;

        String raw = jedis.get(index);
        if (raw != null) {
            return raw;
        }
        String restInfo = this.restaurantRepository.randomRestaurant(index);
        jedis.setex(index.toString(), ttl, restInfo);
        return restInfo;
    }

    @PostMapping({"/randomRestaurant"})
    ResponseEntity<Map<String, Object>> randomRestaurant() {
        Object[] minMax = cachedMinMax();
        int randomIndex = (new Random()).nextInt((Integer)minMax[1] - (Integer)minMax[0] + 1) + (Integer)minMax[0];
        String restaurant = getCachedRestaurantInfo(randomIndex);
        Map responseBody;
        if (restaurant != null) {
            responseBody = Map.of("code", 1, "body", restaurant);
        } else {
            responseBody = Map.of("code", 0, "type", 0);
        }

        return ResponseEntity.status(HttpStatus.OK).body(responseBody);
    }
}
