package REST_API_TakeHome;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

/** Helper class to achieve some behind the scenes tasks **/
/** Starting with converting time **/
public class Utilities {
    public static String getCurrentDate() {
        Date d = new Date();
        return getISOStringForDate(d);
    }
    public static String getISOStringForDate(Date date) {
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.US);
        dateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
        return dateFormat.format(date);
    }

    /** Using this to retrieve JSON types **/
    public static String getJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}