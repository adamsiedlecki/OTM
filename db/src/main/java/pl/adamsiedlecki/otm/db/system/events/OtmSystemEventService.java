package pl.adamsiedlecki.otm.db.system.events;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class OtmSystemEventService {

    private final OtmSystemEventRepo otmSystemEventRepo;

    public OtmSystemEvent save(OtmSystemEvent entity) {
        return otmSystemEventRepo.save(entity);
    }

    public Page<OtmSystemEvent> findAll(Pageable pageable) {
        return otmSystemEventRepo.findAll(pageable);
    }
}
