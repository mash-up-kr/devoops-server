package com.devoops.util;

import java.util.List;
import java.util.Map;

public class SummaryFormatter {
    public static String formatWithNumbering(List<Map.Entry<String, String>> summaries) {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < summaries.size(); i++) {
            Map.Entry<String, String> entry = summaries.get(i);
            builder.append(i + 1)
                .append(". ")
                .append(entry.getKey())  // 제목
                .append("\n")
                .append(entry.getValue()) // 내용
                .append("\n\n");
        }
        return builder.toString().trim(); // 마지막 공백 제거
    }
}
