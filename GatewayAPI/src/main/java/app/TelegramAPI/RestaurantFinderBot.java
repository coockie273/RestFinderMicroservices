package app.TelegramAPI;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.Arrays;
import java.util.Collections;
import java.util.Map;


@Component
public class RestaurantFinderBot extends TelegramLongPollingBot {

    @Value("${SERVICE_USERS_HOST}")
    private String UserAPI;

    @Value("${SERVICE_RESTAURANTS_HOST}")
    private String RestaurantAPI;

    @Value("${SERVICE_RATING_HOST}")
    private String RatingAPI;

    public RestaurantFinderBot(@Value("${BOT_TOKEN}") String botToken) {
        super(botToken);
    }

    @Override
    public String getBotUsername() {
        return "restaurant_finder_bot";
    }

    private void sendMessage(Long chatId, String text) {
        var chatIdStr = String.valueOf(chatId);
        var sendMessage = new SendMessage(chatIdStr, text);
        try {
            execute(sendMessage);
        } catch (TelegramApiException e) {
            System.out.println("Sending message error");
        }
    }


    @Override
    public void onUpdateReceived(Update update) {
        if (!update.hasMessage() || !update.getMessage().hasText()) {
            return;
        }
        var messages = update.getMessage().getText().split(" ", 2);
        var chatId = update.getMessage().getChatId();
        String uname = update.getMessage().getChat().getUserName();
        switch (messages[0]) {
            case "/register" -> register(chatId, uname);
            case "/add_to_favorite" -> addToFavorite(chatId, messages);
            case "/remove_from_favorite" -> removeFromFavorite(chatId, messages);
            case "/show_favorite" -> showFavorite(chatId);
            case "/show_restaurants" -> showRestaurants(chatId);
            case "/random_restaurant" -> randomRestaurant(chatId);
            case "/filter_restaurants" -> filterRestaurants(Arrays.copyOfRange(messages, 1, messages.length));
            case "/show_comments" -> showComments(chatId, messages);
            case "/rating" -> rating(chatId, messages);
            case "/top" -> top(chatId);
            case "/add_comment" -> addComment(chatId, uname, Arrays.copyOfRange(messages, 1, messages.length));
            case "/delete_comment" -> deleteComment(chatId, uname, messages[1]);
            case "/my_comments" -> myComments(chatId, uname);
            default -> wrongRequest(chatId);
        }
    }

    public void wrongRequest(Long chatId) {
        sendMessage(chatId, "Неверный запрос");
    }

    private ResponseEntity<Map> sendPostRequest(String url, Map body) {

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));

        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(body, headers);

        return new RestTemplate().postForEntity(url, entity, Map.class);
    }

    ///////////////////////////////////////////     User service       /////////////////////////////////////////////////

    public void register(Long chatId, String username) {

        String url = UserAPI + "/register";

        ResponseEntity<Map> response = sendPostRequest(url, Map.of("id", chatId, "username", username));

        if (response.getStatusCode() == HttpStatus.OK) {
            Map<String, Integer> responseBody = response.getBody();
            if (responseBody.get("code") == 1) {
                sendMessage(chatId, "Добро пожаловать " + username + "!");
            } else {
                // Обработка типов ошибок
                switch (responseBody.get("type")) {
                    default -> sendMessage(chatId, "Неизвестная ошибка");
                }
            }
        } else {
            sendMessage(chatId, "Сервер не отвечает");
        }

    }

    public void addToFavorite(Long chatId, String[] message) {

        if (message.length < 2) {
            sendMessage(chatId, "Введите название ресторана");
            return;
        }

        String restaurant = message[1];

        String url = UserAPI + "/addToFavoriteList";

        ResponseEntity<Map> response = sendPostRequest(url, Map.of("user_id", chatId, "restaurant", restaurant));

        if (response.getStatusCode() == HttpStatus.OK) {
            Map<String, Integer> responseBody = response.getBody();
            if (responseBody.get("code") == 1) {
                sendMessage(chatId, "Ресторан " + restaurant + " успешно добавлен в список избранных ресторанов.");
            } else {
                // Обработка типов ошибок
                switch (responseBody.get("type")) {
                    default -> sendMessage(chatId, "Неизвестная ошибка");
                }
            }
        } else {
            sendMessage(chatId, "Сервер не отвечает");
        }
    }

    public void removeFromFavorite(Long chatId, String[] message) {

        if (message.length < 2) {
            sendMessage(chatId, "Введите название ресторана");
            return;
        }

        String restaurant = message[1];

        String url = UserAPI + "/removeFromFavoriteList";

        ResponseEntity<Map> response = sendPostRequest(url, Map.of("user_id", chatId, "restaurant", restaurant));

        if (response.getStatusCode() == HttpStatus.OK) {
            Map<String, Integer> responseBody = response.getBody();
            if (responseBody.get("code") == 1) {
                sendMessage(chatId, "Ресторан " + restaurant + " успешно удален из списка избранных ресторанов");
            } else {
                // Обработка типов ошибок
                switch (responseBody.get("type")) {
                    case 1 -> sendMessage(chatId, "Ресторан " + restaurant + "не найден в списке избранных ресторанов");
                    default -> sendMessage(chatId, "Неизвестная ошибка");
                }
            }
        } else {
            sendMessage(chatId, "Сервер не отвечает");
        }
    }

    public void showFavorite(Long chatId) {
        String url = UserAPI + "/showFavoriteList";

        ResponseEntity<Map> response = sendPostRequest(url, Map.of("user_id", chatId));

        if (response.getStatusCode() == HttpStatus.OK) {
            Map<String, Object> responseBody = response.getBody();
            if ((Integer)responseBody.get("code") == 1) {
                sendMessage(chatId, (String)responseBody.get("body"));
            } else {
                // Обработка типов ошибок
                switch ((Integer)responseBody.get("type")) {
                    case 1 -> sendMessage(chatId, "Список избранных ресторанов пуст");
                    default -> sendMessage(chatId, "Неизвестная ошибка");
                }
            }
        } else {
            sendMessage(chatId, "Сервер не отвечает");
        }
    }

    ///////////////////////////////////////////     Restaurant service       ///////////////////////////////////////////

    public void showRestaurants(Long chatId) {
        String url = RestaurantAPI + "/showRestaurants";

        ResponseEntity<Map> response = sendPostRequest(url, null);

        if (response.getStatusCode() == HttpStatus.OK) {
            Map<String, Object> responseBody = response.getBody();
            if ((Integer)responseBody.get("code") == 1) {
                sendMessage(chatId, (String)responseBody.get("body"));
            } else {
                // Обработка типов ошибок
                switch ((Integer)responseBody.get("type")) {
                    case 1 -> sendMessage(chatId, "Список ресторанов пуст");
                    default -> sendMessage(chatId, "Неизвестная ошибка");
                }
            }
        } else {
            sendMessage(chatId, "Сервер не отвечает");
        }
    }

    public void randomRestaurant(Long chatId) {

        String url = RestaurantAPI + "/randomRestaurant";

        ResponseEntity<Map> response = sendPostRequest(url, null);

        if (response.getStatusCode() == HttpStatus.OK) {
            Map<String, Object> responseBody = response.getBody();
            if ((Integer)responseBody.get("code") == 1) {
                sendMessage(chatId, (String)responseBody.get("body"));
            } else {
                // Обработка типов ошибок
                switch ((Integer)responseBody.get("type")) {
                    default -> sendMessage(chatId, "Неизвестная ошибка");
                }
            }
        } else {
            sendMessage(chatId, "Сервер не отвечает");
        }
    }

    public void filterRestaurants(String[] filters) {
        // Send message to RestaurantService with filters
        // Пока впадлу
        return;
    }

    ///////////////////////////////////////////     Rating service       ///////////////////////////////////////////

    public void showComments(Long chatId,String[] message) {

        if (message.length < 2) {
            sendMessage(chatId, "Введите название ресторана");
            return;
        }

        String restaurant = message[1];

        String url = RatingAPI + "/showComments";

        ResponseEntity<Map> response = sendPostRequest(url, Map.of("restaurant", restaurant));

        if (response.getStatusCode() == HttpStatus.OK) {
            Map<String, Object> responseBody = response.getBody();
            if ((Integer)responseBody.get("code") == 1) {
                sendMessage(chatId, (String)responseBody.get("body"));
            } else {
                // Обработка типов ошибок
                switch ((Integer)responseBody.get("type")) {
                    case 1 -> sendMessage(chatId, "Список отзывов пуст");
                    default -> sendMessage(chatId, "Неизвестная ошибка");
                }
            }
        } else {
            sendMessage(chatId, "Сервер не отвечает");
        }
    }

    public void rating(Long chatId, String[] message) {

        if (message.length < 2) {
            sendMessage(chatId, "Введите название ресторана");
            return;
        }

        String restaurant = message[1];

        String url = RatingAPI + "/showRating";

        ResponseEntity<Map> response = sendPostRequest(url, Map.of("restaurant", restaurant));

        if (response.getStatusCode() == HttpStatus.OK) {
            Map<String, Object> responseBody = response.getBody();
            if ((Integer)responseBody.get("code") == 1) {
                sendMessage(chatId, "Средняя оценка ресторана: " + responseBody.get("body"));
            } else {
                // Обработка типов ошибок
                switch ((Integer)responseBody.get("type")) {
                    case 1 -> sendMessage(chatId, "Список оценок для этого ресторана пуст");
                    default -> sendMessage(chatId, "Неизвестная ошибка");
                }
            }
        } else {
            sendMessage(chatId, "Сервер не отвечает");
        }
    }

    public void top (Long chatId) {
        String url = RatingAPI + "/top";

        ResponseEntity<Map> response = sendPostRequest(url, null);

        if (response.getStatusCode() == HttpStatus.OK) {
            Map<String, Object> responseBody = response.getBody();
            if ((Integer)responseBody.get("code") == 1) {
                sendMessage(chatId, (String)responseBody.get("body"));
            } else {
                // Обработка типов ошибок
                switch ((Integer)responseBody.get("type")) {
                    default -> sendMessage(chatId, "Неизвестная ошибка");
                }
            }
        } else {
            sendMessage(chatId, "Сервер не отвечает");
        }
    }

    public void addComment (Long chatId, String uname, String[] comment) {
        // Send message to RatingService with uname and comment
        return;
    }

    public void deleteComment (Long chatId, String uname, String restaurant) {
        // Send message to RatingService with uname and comment
        return;
    }

    public void myComments(Long chatId, String uname) {
        String url = RatingAPI + "/myComments";

        ResponseEntity<Map> response = sendPostRequest(url, Map.of("username", uname));

        if (response.getStatusCode() == HttpStatus.OK) {
            Map<String, Object> responseBody = response.getBody();
            if ((Integer)responseBody.get("code") == 1) {
                sendMessage(chatId, (String)responseBody.get("body"));
            } else {
                // Обработка типов ошибок
                switch ((Integer)responseBody.get("type")) {
                    case 1 -> sendMessage(chatId, "Вы еще не писали отзывы");
                    default -> sendMessage(chatId, "Неизвестная ошибка");
                }
            }
        } else {
            sendMessage(chatId, "Сервер не отвечает");
        }
    }

}
