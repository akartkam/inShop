package com.akartkam.inShop.domain;

import java.util.UUID;

public class GeneratorId {
    public static UUID createId() {
        UUID uuid = java.util.UUID.randomUUID();
        return uuid;
    }
}
