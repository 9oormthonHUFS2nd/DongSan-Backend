package com.dongsan.common.format;

import java.time.LocalDateTime;

public class TimeFormat {

    public static final int SEC = 60;
    public static final int MIN = 60;
    public static final int HOUR = 24;
    public static final int DAY = 7;
    public static final int WEEK = 4;
    public static final int MONTH = 12;

    public static String formatTimeString(LocalDateTime createdAt) {
        LocalDateTime now = LocalDateTime.now();
        long time = java.time.Duration.between(createdAt, now).getSeconds();
        String msg;
        if (time < SEC) {
            msg = "방금 전";
        } else if ((time /= SEC) < MIN) {
            msg = time + "분 전";
        } else if ((time /= MIN) < HOUR) {
            msg = (time) + "시간 전";
        } else if ((time /= HOUR) < DAY) {
            msg = (time) + "일 전";
        } else if ((time /= DAY) < WEEK) {
            msg = (time) + "주 전";
        } else if ((time /= WEEK) < MONTH) {
            msg = (time) + "달 전";
        } else {
            time /= MONTH;
            msg = (time) + "년 전";
        }
        return msg;
    }
}
