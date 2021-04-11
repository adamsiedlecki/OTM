package pl.adamsiedlecki.OTM.db.tempDataAlias;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TempDataAliasRepo extends JpaRepository<TempDataAlias, Long> {


}
