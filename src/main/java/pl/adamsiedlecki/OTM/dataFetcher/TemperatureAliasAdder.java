package pl.adamsiedlecki.OTM.dataFetcher;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import pl.adamsiedlecki.OTM.db.location.Location;
import pl.adamsiedlecki.OTM.db.location.LocationService;
import pl.adamsiedlecki.OTM.db.tempData.TemperatureData;
import pl.adamsiedlecki.OTM.db.tempDataAlias.TempDataAlias;
import pl.adamsiedlecki.OTM.db.tempDataAlias.TempDataAliasService;

import java.util.List;

@Component
class TemperatureAliasAdder {

    private final TempDataAliasService aliasService;
    private final LocationService locationService;

    @Autowired
    public TemperatureAliasAdder(TempDataAliasService aliasService, LocationService locationService) {
        this.aliasService = aliasService;
        this.locationService = locationService;
    }

    public void add(TemperatureData tempData){
        List<TempDataAlias> aliases = aliasService.findAll();
        for(TempDataAlias alias : aliases){
            if (tempData.getTransmitterName().trim().equals(alias.getOriginalName())) {
                tempData.setTransmitterName(tempData.getTransmitterName() + " " + alias.getAliasName());

                Location location = locationService.getOrSave("" + alias.getLatitude(), "" + alias.getLongitude());
                tempData.setLocation(location);


            }
        }
    }
}
