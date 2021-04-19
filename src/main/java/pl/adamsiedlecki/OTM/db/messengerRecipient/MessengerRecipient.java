package pl.adamsiedlecki.OTM.db.messengerRecipient;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class MessengerRecipient {

    @Id
    @GeneratedValue
    private Long id;
    private String recipientId;

    public MessengerRecipient() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getRecipientId() {
        return recipientId;
    }

    public void setRecipientId(String recipientId) {
        this.recipientId = recipientId;
    }
}
