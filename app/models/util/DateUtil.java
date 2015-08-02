package models.util;

import models.Global;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import play.Logger;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by n.diazgranados on 26/07/2015.
 */
public class DateUtil {

    public static final String ISO8601_DATE_FORMAT = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'";
    public static DateFormat dateFormat;

    static {
        DateUtil.dateFormat = new SimpleDateFormat(Global.DEFAULT_DATE_FORMAT);
        dateFormat.setTimeZone(Global.DEFAULT_TIME_ZONE);
    }

    //------------------------------------------------- USING ISO8601

    /**
     * Transform ISO 8601 string to Calendar.
     */
    public static Date buildISO8601Date(final String iso8601string) {
        //http://joda-time.sourceforge.net/timezones.html
        DateTimeFormatter iso = DateTimeFormat.forPattern(Global.DEFAULT_DATE_FORMAT);
        DateTimeZone timeZone = DateTimeZone.forID("America/Bogota");
        DateTime dateTime = iso.parseDateTime(iso8601string).withZone(timeZone);
        Date date = dateTime.toDate();
        Logger.info("DateUtil.buildISO8601Date(): " + dateFormat.format(date));
        return date;
    }

    //------------------------------------------------- USING MILLISECONDS

    /**
     * Create a date at beginning of day.
     */
    public static Date buildDateStartOfDay(String millSecondStringDate) {
        Date date = new Date(Long.valueOf(millSecondStringDate));
        Calendar cal = Calendar.getInstance();
        cal.setTimeZone(Global.DEFAULT_TIME_ZONE);
        cal.setTime(date);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        date.setTime(cal.getTimeInMillis());
        Logger.info("DateUtil.buildDateStartOfDay(): " + dateFormat.format(date));
        return date;
    }

    /**
     * Create a date at the end of the day.
     */
    public static Date buildDateEndOfDay(String millSecondStringDate) {
        Date date = new Date(Long.valueOf(millSecondStringDate));
        Calendar cal = Calendar.getInstance();
        cal.setTimeZone(Global.DEFAULT_TIME_ZONE);
        cal.setTime(date);
        cal.set(Calendar.HOUR_OF_DAY, 23);
        cal.set(Calendar.MINUTE, 59);
        cal.set(Calendar.SECOND, 59);
        date.setTime(cal.getTimeInMillis());
        Logger.info("DateUtil.buildDateEndOfDay(): " + dateFormat.format(date));
        return date;
    }

    /**
     * Create a date.
     */
    public static Date buildDate(String millSecondStringDate) {
        Date date = new Date(Long.valueOf(millSecondStringDate));
        Logger.info("DateUtil.buildDate(): " + date.toString());
        return date;
    }


    public static Date addMillisecond(Date date, int milliseconds) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.setTimeZone(Global.DEFAULT_TIME_ZONE);
        cal.add(Calendar.MILLISECOND, milliseconds);
        date.setTime(cal.getTimeInMillis());
        Logger.info("DateUtil.addMinute(): " + dateFormat.format(date));
        return date;
    }

    public static Date addSecond(Date date, int seconds) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.setTimeZone(Global.DEFAULT_TIME_ZONE);
        cal.add(Calendar.SECOND, seconds);
        date.setTime(cal.getTimeInMillis());
        Logger.info("DateUtil.addMinute(): " + dateFormat.format(date));
        return date;
    }

    public static Date addMinute(Date date, int minute) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.setTimeZone(Global.DEFAULT_TIME_ZONE);
        cal.add(Calendar.MINUTE, minute);
        date.setTime(cal.getTimeInMillis());
        Logger.info("DateUtil.addMinute(): " + dateFormat.format(date));
        return date;
    }

    public static Date addHour(Date date, int hours) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.setTimeZone(Global.DEFAULT_TIME_ZONE);
        cal.add(Calendar.HOUR_OF_DAY, hours);
        date.setTime(cal.getTimeInMillis());
        Logger.info("DateUtil.addHour(): " + dateFormat.format(date));
        return date;
    }

    public static Date addDays(Date date, int days) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.setTimeZone(Global.DEFAULT_TIME_ZONE);
        cal.add(Calendar.DAY_OF_MONTH, days);
        date.setTime(cal.getTimeInMillis());
        Logger.info("DateUtil.addDays(): " + dateFormat.format(date));
        return date;
    }

    public static Date addSpecificTime(Date date,int which, int valueToChange, int howMany) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.setTimeZone(Global.DEFAULT_TIME_ZONE);
        cal.add(valueToChange, howMany);
        date.setTime(cal.getTimeInMillis());
        Logger.info("DateUtil.addDays(): " + dateFormat.format(date));
        return date;
    }
}
