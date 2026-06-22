package com.bage.study.algorithm.tutorial;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;

/**
 *
 */
public class Main {

    public static void main(String[] args) {
         Scanner in = new Scanner(System.in);
            int count = in.nextInt();
            // 注意 hasNext 和 hasNextLine 的区别
            TreeMap<String, Integer> total = new TreeMap<>();
            Map<String, Integer> max = new HashMap<>();
            Map<String, Integer> min = new HashMap<>();
            while (count -- >= 0) { // 注意 while 处理多个 case
                String input = in.nextLine();
                if (input == null || input.trim().length() == 0) {
                    continue;
                }
                String[] split = input.split(",");
                Date date = new Date(Long.parseLong(split[0]));
                int value = Integer.parseInt(split[1]);

                //  求和
//            String keyOrigin= date.getYear() + "-" + date.getMonth() + "-" + date.getDate() + " " + date.getHours() + ":" + date.getMinutes();
                String key = new Date(date.getYear(), date.getMonth(), date.getDate(),
                        date.getHours(), date.getMinutes()).getTime() + "";
                total.put(key, total.getOrDefault(key, 0) + value);

                // 最值
                max.put(key, Math.max(max.getOrDefault(key, Integer.MIN_VALUE), value));
                min.put(key, Math.min(min.getOrDefault(key, Integer.MAX_VALUE), value));
            }


            Map.Entry<String, Integer> firstEntry = total.firstEntry();
            Map.Entry<String, Integer> lastEntry = total.lastEntry();

        LocalDateTime start = LocalDateTime.ofEpochSecond(Long.parseLong(
                    firstEntry.getKey()) / 1000, 0, java.time.ZoneOffset.ofHours(8));
        LocalDateTime end = LocalDateTime.ofEpochSecond(Long.parseLong(
        lastEntry.getKey()) / 1000, 0, java.time.ZoneOffset.ofHours(8));

            long sub = Duration.between(start, end).toMinutes();
            for (int i = 0; i < sub-1 ; i++) {
                LocalDateTime localDateTime = start.plusMinutes(i + 1);
                String key = new Date(localDateTime.getYear(), localDateTime.getMonthValue(), localDateTime.getDayOfMonth(),
                        localDateTime.getHour(), localDateTime.getMinute()).getTime() + "";
                if (!total.containsKey(key)) {
                    total.put(key, 0);
                    max.put(key, 0);
                    min.put(key, 0);
                }
            }

            System.out.println(total.size());
            for (String key : total.keySet()) {
                System.out.println(key + "," + max.get(key) + "," + min.get(
                        key) + "," + total.get(key));
            }
        }

}
