package com.example.chatgrount.MainActyvity.Model;

import android.app.Application;
import android.content.Context;

import java.text.SimpleDateFormat;
import java.util.concurrent.TimeUnit;

public class GetTimeAgo extends Application {

    private static final int SECOND_MILLIS = 1000;
    private static final int MINUTE_MILLIS = 60 * SECOND_MILLIS;
    private static final int HOUR_MILLIS = 60 * MINUTE_MILLIS;
    private static final int DAY_MILLIS = 24 * HOUR_MILLIS;


    public static String GetTimeAgo(long time, Context ctx) {
        if (time < 1000000000000L) {
            // if timestamp given in seconds, convert to millis
            time *= 1000;
        }

        long now = System.currentTimeMillis();
        if (time > now || time <= 0) {
            return null;
        }

        // TODO: localize
        final long diff = (now - time);
      String t =  String.format("Hoạt động %d phút trước",
                TimeUnit.MILLISECONDS.toMinutes(diff),
                TimeUnit.MILLISECONDS.toSeconds(diff) -
                        TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(diff))
        );
        String t2 =  String.format("Hoạt động %d giờ trước",
                TimeUnit.MILLISECONDS.toHours(diff),
                TimeUnit.MILLISECONDS.toMinutes(diff),
                TimeUnit.MILLISECONDS.toSeconds(diff) -
                        TimeUnit.HOURS.toSeconds(TimeUnit.MINUTES.toSeconds(TimeUnit.MICROSECONDS.toMinutes(diff)))
        );

        if (diff < MINUTE_MILLIS) {
            return "Đang hoạt động";
        } else if (diff < 2 * MINUTE_MILLIS) {
            return t;
        } else if (diff < 50 * MINUTE_MILLIS) {
            return t;
        } else if (diff < 90 * MINUTE_MILLIS) {
            return t;
        } else if (diff < 24 * HOUR_MILLIS) {
            return t2;
        } else if (diff < 48 * HOUR_MILLIS) {
            return "Hoạt động 1 ngày trước";
        } else {
            return "Hoạt động "+ diff / DAY_MILLIS + " ngày trước";
        }
    }
}
