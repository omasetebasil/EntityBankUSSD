package com.entitybank.digital.ussd.util;

import java.time.LocalTime;

public class GreetingUtil {

    public static String greeting() {
        LocalTime now = LocalTime.now();

        if (now.isBefore(LocalTime.NOON)) {
            return "morning";
        } else if (now.isBefore(LocalTime.of(17, 0))) {
            return "afternoon";
        } else {
            return "evening";
        }
    }
}
