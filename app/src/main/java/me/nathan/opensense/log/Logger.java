package me.nathan.opensense.log;

import android.content.Context;
import android.widget.Toast;

public class Logger {

    public static void consoleDebug(String message) {
        System.out.println("opensense.debug: " + message);
    }

    public static void consoleInfo(String message) {
        System.out.println("opensense.info: " + message);
    }

    public static void appMessage(Context context, String message) {
        Toast.makeText(context.getApplicationContext(), message, Toast.LENGTH_SHORT).show();
    }
}
