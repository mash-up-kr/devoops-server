package com.devoops.exception.message;

import java.util.Arrays;
import java.util.stream.Collectors;

public class ErrorMessageResolver {

    private static final String NOTIFICATION_PREFIX = ":rotating_light:  [**Error 발생!**]\n";
    private static final String STACK_TRACE_AFFIX = "\n```\n";
    private static final String DISCORD_LINE_SEPARATOR = "\n";
    private static final int STACK_TRACE_LENGTH = 10;

    public static String resolve(Throwable throwable) {
        String errorMessage = throwable.toString();
        String stackTrace = getStackTraceAsString(throwable);

        return NOTIFICATION_PREFIX
                + errorMessage
                + STACK_TRACE_AFFIX
                + stackTrace
                + STACK_TRACE_AFFIX;
    }

    private static String getStackTraceAsString(Throwable throwable) {
        return Arrays.stream(throwable.getStackTrace())
                .map(StackTraceElement::toString)
                .limit(STACK_TRACE_LENGTH)
                .collect(Collectors.joining(DISCORD_LINE_SEPARATOR));
    }
}
