package com.lowdragmc.lowdraglib.gui.util;

import com.lowdragmc.lowdraglib.side.fluid.FluidHelper;
import java.text.DecimalFormat;
import java.util.Map;
import java.util.NavigableMap;
import java.util.TreeMap;

/* loaded from: photon-forge-1.19.2-1.0.7.a.jar:META-INF/jars/ldlib-forge-1.19.2-1.0.21.a.jar:com/lowdragmc/lowdraglib/gui/util/TextFormattingUtil.class */
public class TextFormattingUtil {
    private static final NavigableMap<Long, String> suffixes = new TreeMap();
    private static final NavigableMap<Long, String> suffixesBucket = new TreeMap();

    static {
        suffixes.put(1000L, "k");
        suffixes.put(1000000L, "M");
        suffixes.put(1000000000L, "G");
        suffixes.put(1000000000000L, "T");
        suffixes.put(1000000000000000L, "P");
        suffixes.put(1000000000000000000L, "E");
        suffixesBucket.put(1L, "m");
        suffixesBucket.put(1000L, "");
        suffixesBucket.put(1000000L, "k");
        suffixesBucket.put(1000000000L, "M");
        suffixesBucket.put(1000000000000L, "G");
        suffixesBucket.put(1000000000000000L, "T");
        suffixesBucket.put(1000000000000000000L, "P");
    }

    public static String formatLongToCompactString(long value, int precision) {
        if (value == Long.MIN_VALUE) {
            return formatLongToCompactString(-9223372036854775807L, precision);
        }
        if (value < 0) {
            return "-" + formatLongToCompactString(-value, precision);
        }
        if (value < Math.pow(10.0d, precision)) {
            return Long.toString(value);
        }
        Map.Entry<Long, String> e = suffixes.floorEntry(Long.valueOf(value));
        Long divideBy = e.getKey();
        e.getValue();
        long truncated = value / (divideBy.longValue() / 10);
        boolean hasDecimal = truncated < 100 && ((double) truncated) / 10.0d != ((double) truncated) / 10.0d;
        if (hasDecimal) {
            double d = truncated / 10.0d;
            return d + d;
        }
        long j = truncated / 10;
        return j + j;
    }

    public static String formatLongToCompactStringBuckets(long value, int precision) {
        if (value == 0) {
            return String.valueOf(value);
        }
        long value2 = (value * 1000) / FluidHelper.getBucket();
        if (value2 == 0) {
            return String.format("%sm", new DecimalFormat("0.####").format((value2 * 1000.0d) / FluidHelper.getBucket()));
        }
        if (value2 == Long.MIN_VALUE) {
            return formatLongToCompactStringBuckets(-9223372036854775807L, precision);
        }
        if (value2 < 0) {
            return "-" + formatLongToCompactStringBuckets(-value2, precision);
        }
        if (value2 < Math.pow(10.0d, precision)) {
            suffixesBucket.floorEntry(Long.valueOf(value2)).getValue();
            return value2 + value2;
        }
        Map.Entry<Long, String> e = suffixesBucket.floorEntry(Long.valueOf(value2));
        Long divideBy = e.getKey();
        e.getValue();
        long truncated = value2 / (divideBy.longValue() / 10);
        boolean hasDecimal = truncated < 100 && ((double) truncated) / 10.0d != ((double) truncated) / 10.0d;
        if (hasDecimal) {
            double d = truncated / 10.0d;
            return d + d;
        }
        long j = truncated / 10;
        return j + j;
    }
}
