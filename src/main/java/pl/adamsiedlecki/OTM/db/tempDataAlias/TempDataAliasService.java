package pl.adamsiedlecki.OTM.db.tempDataAlias;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TempDataAliasService {

    private TempDataAliasRepo tempDataAliasRepo;

    @Autowired
    public TempDataAliasService(TempDataAliasRepo tempDataAliasRepo) {
        this.tempDataAliasRepo = tempDataAliasRepo;
    }

    public List<TempDataAlias> findAll() {
        return tempDataAliasRepo.findAll();
    }

    public <S extends TempDataAlias> S save(S s) {
        return tempDataAliasRepo.save(s);
    }

    public Optional<TempDataAlias> findById(Long aLong) {
        return tempDataAliasRepo.findById(aLong);
    }

    public boolean existsById(Long aLong) {
        return tempDataAliasRepo.existsById(aLong);
    }

    public long count() {
        return tempDataAliasRepo.count();
    }

    public void deleteById(Long aLong) {
        tempDataAliasRepo.deleteById(aLong);
    }

    public void delete(TempDataAlias tempDataAlias) {
        tempDataAliasRepo.delete(tempDataAlias);
    }

    public void deleteAll(Iterable<? extends TempDataAlias> iterable) {
        tempDataAliasRepo.deleteAll(iterable);
    }

    public void deleteAll() {
        tempDataAliasRepo.deleteAll();
    }
}
