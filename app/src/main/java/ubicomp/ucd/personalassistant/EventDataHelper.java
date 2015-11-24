package ubicomp.ucd.personalassistant;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.CalendarContract;

import java.util.Calendar;

/**
 * Created by specter on 10/24/15.
 */
public class EventDataHelper {


    public static final String[] INSTANCE_PROJECTION = new String[]{
            CalendarContract.Instances.EVENT_ID,
            CalendarContract.Instances.BEGIN,
            CalendarContract.Instances.TITLE,
            CalendarContract.Instances.OWNER_ACCOUNT,
            CalendarContract.Instances.START_DAY,
            CalendarContract.Instances.END_DAY,
            CalendarContract.Instances.DESCRIPTION,
            CalendarContract.Instances.ALL_DAY,
            CalendarContract.Instances.END,
            CalendarContract.Instances.EVENT_COLOR,
            CalendarContract.Instances.EVENT_LOCATION,
            CalendarContract.Instances.HAS_ALARM


    };

    private static final int PROJECTION_ID_INDEX = 0;
    private static final int PROJECTION_BEGIN_INDEX = 1;
    private static final int PROJECTION_TITLE_INDEX = 2;
    private static final int PROJECTION_OWNER_INDEX = 3;
    private static final int PROJECTION_SART_DAY_INDEX = 4;
    private static final int PROJECTION_END_DAY_INDEX = 5;
    private static final int PROJECTION_DESC_INDEX = 6;
    private static final int PROJECTION_ALL_DAY_INDEX = 7;
    private static final int PROJECTION_END_INDEX = 8;
    private static final int PROJECTION_COLOR_INDEX = 9;
    private static final int PROJECTION_LOCATION_INDEX = 10;
    private static final int PROJECTION_HAS_ALARM_INDEX = 11;
    private static Calendar nowTime;


    public static EventResultDataModel getEventData(Context context) {

        nowTime = Calendar.getInstance();
        Calendar beginTime = nowTime;
        beginTime.set(Calendar.HOUR_OF_DAY, 0);
        beginTime.set(Calendar.MINUTE, 0);
        beginTime.set(Calendar.SECOND, 0);
        Long startOfDayMillis = beginTime.getTimeInMillis();

        Calendar endTime = nowTime;
        endTime.set(Calendar.HOUR_OF_DAY, 23);
        endTime.set(Calendar.MINUTE, 59);
        endTime.set(Calendar.SECOND, 59);
        Long endOfDayMillis = endTime.getTimeInMillis();

        Cursor cursor = null;
        ContentResolver cr =context.getContentResolver();
        Uri.Builder builder = CalendarContract.Instances.CONTENT_URI.buildUpon();
        ContentUris.appendId(builder, startOfDayMillis);
        ContentUris.appendId(builder, endOfDayMillis);

        cursor = cr.query(builder.build(),
                INSTANCE_PROJECTION,
                null,
                null, null);


        long eventBeginTime = 0;
        long eventEndTime = 0;
        EventResultDataModel eventResult = new EventResultDataModel();
        while (cursor.moveToNext()) {
            String title;
            String location;
            int eventColor;
            int allday;
            int hasAlarm;
            String description;


            eventBeginTime = cursor.getLong(PROJECTION_BEGIN_INDEX);
            eventEndTime = cursor.getLong(PROJECTION_END_INDEX);
            title = cursor.getString(PROJECTION_TITLE_INDEX);
            location = cursor.getString(PROJECTION_LOCATION_INDEX);
            eventColor = cursor.getInt(PROJECTION_COLOR_INDEX);
            allday = cursor.getInt(PROJECTION_ALL_DAY_INDEX);
            hasAlarm = cursor.getInt(PROJECTION_HAS_ALARM_INDEX);
            description = cursor.getString(PROJECTION_DESC_INDEX);
            long currenttime = Calendar.getInstance().getTimeInMillis();
            if (currenttime >= eventBeginTime && currenttime <= eventEndTime) {
                eventResult.setEventContent("Currently, you have event:\n" + title + "\n and location of event is " + location);
                eventResult.setHasEventNow(true);
                eventResult.setEventTitle(title);
                eventResult.setEventLocation(location);
            } else {
                eventResult.setHasEventNow(false);
                eventResult.setEventContent("Currently, there's no event.");
            }
        }
        return eventResult;


    }

}
