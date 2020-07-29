package com.listen.utils;

/**
 * @Author: Listen
 * @Date: 2020/7/25
 */
public class MoneyUtils {
    public static String formatMoney(Double money) {
        StringBuilder sb = new StringBuilder(String.valueOf(money));
        if (money != 0){
            for (int i = sb.length(); i > 0; i = i - 3) {
                if (i < sb.indexOf(".")) {
                    sb.insert(i, ",");
                }
            }
        }
        return sb.toString();
    }
}
