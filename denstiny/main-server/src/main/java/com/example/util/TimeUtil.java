package com.example.util;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.Map;

public class TimeUtil {
    // 요일을 한국어로 변환하는 맵
    private static final Map<DayOfWeek, String> dayOfWeekMap = new HashMap<>();

    // static 초기화 블록
    static {
        dayOfWeekMap.put(DayOfWeek.MONDAY, "월");
        dayOfWeekMap.put(DayOfWeek.TUESDAY, "화");
        dayOfWeekMap.put(DayOfWeek.WEDNESDAY, "수");
        dayOfWeekMap.put(DayOfWeek.THURSDAY, "목");
        dayOfWeekMap.put(DayOfWeek.FRIDAY, "금");
        dayOfWeekMap.put(DayOfWeek.SATURDAY, "토");
        dayOfWeekMap.put(DayOfWeek.SUNDAY, "일");
    }
    public static LocalTime getCurrentTime() {
        return LocalTime.now();
    }
    public static String getCurrentDayOfWeek() {
        LocalDateTime now = LocalDateTime.now();
        DayOfWeek dayOfWeek = now.getDayOfWeek();
        return dayOfWeekMap.get(dayOfWeek);
    }
}