package utils;

import java.time.LocalTime;

public class Utils {

    // ✅ Check if current system time is between given hours (24-hr format)
    public static boolean isWithinTime(int startHour, int endHour) {
        LocalTime now = LocalTime.now();
        return !now.isBefore(LocalTime.of(startHour, 0)) && now.isBefore(LocalTime.of(endHour, 0));
    }

    // ✅ Validate if a username has exactly 10 characters and no special characters
    public static boolean isValidUsername(String username) {
        return username.matches("[a-zA-Z0-9]{10}");
    }

    // ✅ Check if input contains any invalid characters
    public static boolean containsInvalidChars(String input, String invalidChars) {
        for (char c : invalidChars.toCharArray()) {
            if (input.indexOf(c) != -1) return true;
        }
        return false;
    }

}
