package pl.adamsiedlecki.otm.db;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public interface PresentableOnChart {

    LocalDateTime getTime();

    BigDecimal getValue();

    String getGroupName();
}
