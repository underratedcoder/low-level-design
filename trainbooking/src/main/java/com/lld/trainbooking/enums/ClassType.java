package com.lld.trainbooking.enums;

/**
 * Enum for different travel classes
 */
public enum ClassType {
    SLEEPER("SL"),
    AC_3_TIER("3A"),
    AC_2_TIER("2A"),
    AC_1_TIER("1A"),
    GENERAL("GN");

    private final String code;

    ClassType(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }
}