package pl.adamsiedlecki.OTM.tools;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import pl.adamsiedlecki.OTM.db.tempData.TemperatureData;
import pl.adamsiedlecki.OTM.db.tempDataAlias.TempDataAlias;
import pl.adamsiedlecki.OTM.db.tempDataAlias.TempDataAliasService;

import java.util.List;

@Component
public class TemperatureAliasAdder {

    private final TempDataAliasService aliasService;

    @Autowired
    public TemperatureAliasAdder(TempDataAliasService aliasService) {
        this.aliasService = aliasService;
    }

    public void add(TemperatureData tempData){
        List<TempDataAlias> aliases = aliasService.findAll();
        for(TempDataAlias alias : aliases){
            if (tempData.getTransmitterName().trim().equals(alias.getOriginalName())) {
                tempData.setTransmitterName(tempData.getTransmitterName() + " " + alias.getAliasName());
                tempData.setLatitude(alias.getLatitude());
                tempData.setLongitude(alias.getLongitude());
            }
        }
    }
}
