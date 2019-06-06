package com.common.util;

import java.util.UUID;

public class IdGenerateHelper {
    public static String nextId() {
        return UUID.randomUUID().toString();
    }
}
