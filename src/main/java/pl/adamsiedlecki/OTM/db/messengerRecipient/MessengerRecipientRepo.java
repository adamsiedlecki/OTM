package pl.adamsiedlecki.OTM.db.messengerRecipient;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MessengerRecipientRepo extends JpaRepository<MessengerRecipient, Long> {
}
