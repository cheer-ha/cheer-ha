package com.project.cheerha.common.util;

import java.lang.management.ManagementFactory;
import java.time.Instant;

public class InstanceUtil {

    private static final Instant INSTANCE_START_TIME = Instant.now();

    public static String getInstanceId() {
        return ManagementFactory.getRuntimeMXBean().getName();
    }

    public static Instant getInstanceStartTime() {
        return INSTANCE_START_TIME;
    }
}
