package com.webserver;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Logger {
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS");

    /**
     * Structure the request log entry with standard fields.
     */
    public static void logRequest(String clientIp, String method, String resource, long durationMs, int statusCode) {
        String timestamp = LocalDateTime.now().format(formatter);
        String threadName = Thread.currentThread().getName();
        System.out.printf("[%s] [%s] %s | %s %s | Status: %d | Time: %d ms%n",
                timestamp, threadName, clientIp, method, resource, statusCode, durationMs);
    }

    public static void info(String message) {
        String timestamp = LocalDateTime.now().format(formatter);
        String threadName = Thread.currentThread().getName();
        System.out.printf("[%s] [%s] INFO: %s%n", timestamp, threadName, message);
    }

    public static void error(String message) {
        String timestamp = LocalDateTime.now().format(formatter);
        String threadName = Thread.currentThread().getName();
        System.err.printf("[%s] [%s] ERROR: %s%n", timestamp, threadName, message);
    }
}
