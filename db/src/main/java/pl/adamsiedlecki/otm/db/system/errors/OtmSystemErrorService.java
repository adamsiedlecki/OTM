package pl.adamsiedlecki.otm.db.system.errors;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class OtmSystemErrorService {

    private final OtmSystemErrorRepo otmSystemErrorRepo;

    public <S extends OtmSystemError> S save(S entity) {
        return otmSystemErrorRepo.save(entity);
    }

    public Page<OtmSystemError> findAll(Pageable pageable) {
        return otmSystemErrorRepo.findAll(pageable);
    }
}
