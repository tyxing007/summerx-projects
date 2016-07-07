package io.summerx.framework.database;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * SQL帮助类
 */
public class SQLHelper {

    public final static Pattern ORDER_PATTERN = Pattern.compile("order\\s*by[\\w|\\W|\\s|\\S]*", Pattern.CASE_INSENSITIVE);

    /**
     * select count(*)
     */
    public static String count(String sql) {
        // 去掉排序
        String sqlToUse = removeOrders(sql);
        // count
        return "select count(*) from (" + sql + ")";
    }

    /**
     * 去掉排序
     */
    public static String removeOrders(String sql) {
        Matcher m = ORDER_PATTERN.matcher(sql);
        StringBuffer sb = new StringBuffer();
        while (m.find()) {
            m.appendReplacement(sb, "");
        }
        m.appendTail(sb);
        return sb.toString();
    }
}
