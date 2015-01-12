package org.creek.openhab.androidclient.util;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 
 * @author Andrey Pereverzin
 */
public final class Util {
    private static final String LOCATION_DATE_TIME_FORMAT_DEF = "HH:mm dd/MM/yyyy";
    private static final java.text.DateFormat LOCATION_DATE_TIME_FORMAT = new SimpleDateFormat(LOCATION_DATE_TIME_FORMAT_DEF);
    private static final MathContext DOUBLES_CONTEXT = new MathContext(2, RoundingMode.HALF_UP);

    private Util() {
        //
    }
    
    public static String formatDouble(double val) {
        try {
            return new BigDecimal(val).setScale(2, RoundingMode.HALF_UP).toPlainString();
        } catch (NumberFormatException ex) {
            return Double.toString(val);
        }
    }
    
    public static String[] concatArrays(String[] array1, String[] array2) {
        String[] newArray = new String[array1.length + array2.length];
        for (int i = 0; i < array1.length; i++) {
            newArray[i] = array1[i];
        }
        int n = array1.length;
        for (int i = n; i < n + array2.length; i++) {
            newArray[i] = array2[i - n];
        }
        return newArray;
    }
    
    public static boolean isStringNotEmpty(String str) {
        return str != null && !"".equals(str);
    }
    
    public static String formatLocationTime(long locationTime) {
        return LOCATION_DATE_TIME_FORMAT.format(new Date(locationTime));
    }
}
