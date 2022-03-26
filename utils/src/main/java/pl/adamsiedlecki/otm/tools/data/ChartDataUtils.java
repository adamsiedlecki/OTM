package pl.adamsiedlecki.otm.tools.data;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import pl.adamsiedlecki.otm.db.PresentableOnChart;

import java.math.BigDecimal;
import java.util.List;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ChartDataUtils {

    public static boolean isAnyBelowZero(final List<? extends PresentableOnChart> data) {
        return data.stream().anyMatch(td -> td.getValue() != null && td.getValue().compareTo(BigDecimal.ZERO) < 0);
    }
}
