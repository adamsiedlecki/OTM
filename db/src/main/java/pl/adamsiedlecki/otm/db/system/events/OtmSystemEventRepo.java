package pl.adamsiedlecki.otm.db.system.events;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OtmSystemEventRepo extends JpaRepository<OtmSystemEvent, Long> {

}
