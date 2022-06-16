package pl.adamsiedlecki.otm.db.system.errors;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OtmSystemErrorRepo extends JpaRepository<OtmSystemError, Long> {

}
