package top.infra.jackson2;

import static org.joda.time.format.DateTimeFormat.forPattern;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.Minutes;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.util.TimeZone;

/**
 * Created by zhanghaolun on 16/6/20.
 */
public abstract class DefaultTimeZone {

    public static final String CRON_EVERY_30_SECONDS = "0/30 * * * * ?";

    public static final DateTimeZone DEFAULT_DATE_TIME_ZONE = DateTimeZone.getDefault();
    public static final TimeZone DEFAULT_TIME_ZONE = DEFAULT_DATE_TIME_ZONE.toTimeZone();
    public static final DateTime EPOCH = new DateTime(0L, DEFAULT_DATE_TIME_ZONE);

    /**
     * see: http://stackoverflow.com/questions/15245307/java-simpledateformat-timezone-offset-with-minute-separated-by-colon
     * You can get the timezone offset formatted like +08:00
     * with the SimpleDateFormat in Java 7 (yyyy-MM-dd'T'HH:mm:ss.SSSXXX)
     * with the Joda's DateTimeFormat (yyyy-MM-dd'T'HH:mm:ss.SSSZZ)
     */
    public static final String PATTERN_JAVA_ISO8601 = "yyyy-MM-dd'T'HH:mm:ss.SSSXXX";
    public static final String PATTERN_JODA_ISO8601 = "yyyy-MM-dd'T'HH:mm:ss.SSSZZ";

    public static final DateTimeFormatter yyyyMMdd_HHmmss;
    public static final DateTimeFormatter ISO8601;

    static {
        yyyyMMdd_HHmmss = forPattern("yyyyMMdd-HH:mm:ss").withZone(DEFAULT_DATE_TIME_ZONE);
        ISO8601 = DateTimeFormat.forPattern(PATTERN_JODA_ISO8601).withZone(DEFAULT_DATE_TIME_ZONE);
    }

    private DefaultTimeZone() {
    }

    public static DateTime now() {
        return DateTime.now(DEFAULT_DATE_TIME_ZONE);
    }

    public static Integer minutesSinceEpoch(final DateTime dateTime) {
        return Minutes.minutesBetween(EPOCH, dateTime).getMinutes();
    }
}
