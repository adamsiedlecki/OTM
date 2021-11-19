package pl.adamsiedlecki.otm.db.tempDataAlias;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TempDataAliasRepo extends JpaRepository<TempDataAlias, Long> {


    Optional<TempDataAlias> findByOriginalName(String originalName);
}
