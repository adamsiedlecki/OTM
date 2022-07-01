package pl.adamsiedlecki.otm.utils;

import org.springframework.stereotype.Component;
import pl.adamsiedlecki.odg.model.PresentableOnChart;

@Component
public class Converter {

    public PresentableOnChart convert(pl.adamsiedlecki.otm.db.PresentableOnChart presentableDb) {
        return new PresentableOnChart()
                .groupName(presentableDb.getGroupName())
                .time(presentableDb.getTime())
                .value(presentableDb.getValue());
    }
}
