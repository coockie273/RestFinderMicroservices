package app.users.telegramUser;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;


@Entity
@Table(name = "telegram_user")
public class TelegramUser {

    private @Id Long id;
    private String username;

    public TelegramUser() {
    }

    public TelegramUser(Long telegram_id, String username) {
        this.id = telegram_id;
        this.username = username;
    }

    public Long getTelegram_id() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public void setTelegram_id(Long telegram_id) {
        this.id = telegram_id;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
