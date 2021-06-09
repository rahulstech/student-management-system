package rahulstech.javafx.studentmanagementsystem.util;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Helpers {

    public static boolean isEmptyString(String string) {
        return null == string || "".equals(string);
    }

    public static boolean isEmptyArray(String[] array) {
        return null == array || 0 == array.length;
    }

    public static String arrayToString(String[] array, String joiner) {
        if (null == array || 0 == array.length) return null;
        joiner = isEmptyString(joiner) ? "," : joiner;
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < array.length; i++) {
            if (i > 0)
                builder.append(",");
            builder.append(array[i]);
        }
        return builder.toString();
    }

    public static String[] filterMatched(String[] src, String pattern) {
        if (null == src || 0 == src.length) {
            throw new IllegalArgumentException("empty src");
        }
        if (isEmptyString(pattern)) {
            throw new IllegalArgumentException("empty pattern");
        }
        Pattern _pattern = Pattern.compile(pattern);
        List<String> out = new ArrayList<>();
        for (String s : src) {
            Matcher matcher = _pattern.matcher(s);
            if (matcher.find()) {
                out.add(s);
            }
        }
        return out.toArray(new String[0]);
    }

    public static String[] removeMatched(String[] src, String pattern) {
        if (null == src || 0 == src.length) {
            throw new IllegalArgumentException("empty src");
        }
        if (isEmptyString(pattern)) {
            throw new IllegalArgumentException("empty pattern");
        }
        Pattern _pattern = Pattern.compile(pattern);
        List<String> out = new ArrayList<>();
        for (String s : src) {
            Matcher matcher = _pattern.matcher(s);
            if (matcher.results().count() == 0) {
                out.add(s);
            }
        }
        return out.toArray(new String[0]);
    }

    public static String[] replaceEachMatched(String[] src, String pattern, String with) {
        if (null == src || 0 == src.length) {
            throw new IllegalArgumentException("empty src");
        }
        if (isEmptyString(pattern)) {
            throw new IllegalArgumentException("empty pattern");
        }
        if (null == with) {
            throw new NullPointerException("null with");
        }
        Pattern _pattern = Pattern.compile(pattern);
        List<String> out = new ArrayList<>();
        for (String s : src) {
            Matcher matcher = _pattern.matcher(s);
            if (matcher.find()) {
                String replaced = matcher.replaceAll(with);
                out.add(replaced);
            }
            else {
                out.add(s);
            }
        }
        return out.toArray(new String[0]);
    }
}
