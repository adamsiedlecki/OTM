package pl.adamsiedlecki.OTM.db.messengerRecipient;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MessengerRecipientService {

    private final MessengerRecipientRepo messengerRecipientRepo;

    @Autowired
    public MessengerRecipientService(MessengerRecipientRepo messengerRecipientRepo) {
        this.messengerRecipientRepo = messengerRecipientRepo;
    }

    public List<MessengerRecipient> findAll() {
        return messengerRecipientRepo.findAll();
    }
}
