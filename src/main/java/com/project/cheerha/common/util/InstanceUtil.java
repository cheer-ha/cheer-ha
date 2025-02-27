package com.project.cheerha.common.util;

import java.lang.management.ManagementFactory;

public class InstanceUtil {

    public static String getInstanceId() {
        return ManagementFactory.getRuntimeMXBean().getName();
    }
}
