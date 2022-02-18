package pl.adamsiedlecki.otm.tools.uuid;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.UUID;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class UuidTool {

    public static String getRandom() {
        return UUID.randomUUID().toString();
    }
}
