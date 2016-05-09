package com.maming.common.util;

import java.math.BigDecimal;

public class Converter {

    public static String toString2(Object o) {
        try {
            if (o == null) {
                return "";
            } else if (o instanceof String) {
                return (String) o;
            } else if (o instanceof Integer) {
                return String.valueOf((Integer) o);
            } else if (o instanceof Long) {
                return String.valueOf((Long) o);
            } else if (o instanceof Short) {
                return String.valueOf((Short) o);
            } else if (o instanceof BigDecimal) {
                return String.valueOf(((BigDecimal) o).intValue());
            } else if (o instanceof Double) {
                return String.valueOf(((Double) o).intValue());
            } else if (o instanceof Float) {
                return String.valueOf(((Float) o).intValue());
            }
            return o.toString();
        } catch (Exception ex) {
            return "";
        }
    }

    public static Integer converterToInteger(Object obj) {
        return converterToInteger(obj, 0);
    }

    public static Integer converterToInteger(Object o, int defaultValue) {
        try {
            if (o == null) {
                return defaultValue;
            } else if (o instanceof String) {
                return Integer.parseInt((String) o);
            } else if (o instanceof Integer) {
                return ((Integer) o).intValue();
            } else if (o instanceof Long) {
                return ((Long) o).intValue();
            } else if (o instanceof Short) {
                return ((Short) o).intValue();
            } else if (o instanceof BigDecimal) {
                return ((BigDecimal) o).intValue();
            } else if (o instanceof Double) {
                return ((Double) o).intValue();
            } else if (o instanceof Float) {
                return ((Float) o).intValue();
            } else if (o instanceof Boolean) {
                return Boolean.TRUE.equals(o) ? 1 : 0;
            }
            return Integer.parseInt(o.toString());
        } catch (Exception ex) {
            return defaultValue;
        }
    }

    public void test1() {
        System.out.println(toString2(8));
        System.out.println(toString2(new Float(8.8)));
        System.out.println(toString2(new Boolean(true)));
    }

    public void test2() {
        System.out.println(converterToInteger(new Float("100.98")));
        System.out.println(converterToInteger(new Boolean(false)));
    }

    public static void main(String[] args) {
        System.out.println(new BigDecimal(8.8));
        Converter test = new Converter();
        test.test1();
        test.test2();
    }
}
