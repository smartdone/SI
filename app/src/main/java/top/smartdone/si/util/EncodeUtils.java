package top.smartdone.si.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by smartdone on 2017/12/5.
 */

public class EncodeUtils {
    public static String unicodeToString(String str) {

        Pattern pattern = Pattern.compile("(\\\\u(\\p{XDigit}{4}))");
        Matcher matcher = pattern.matcher(str);
        char ch;
        while (matcher.find()) {
            ch = (char) Integer.parseInt(matcher.group(2), 16);
            str = str.replace(matcher.group(1), ch+"" );
        }

        return str;

    }
}
