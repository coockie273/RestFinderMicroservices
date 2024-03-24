package app.users.telegramUser;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface TelegramUserRepository extends JpaRepository<TelegramUser, Integer> {


}
