package com.example.android.sunshine.utilities;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.support.v4.content.ContextCompat;
import android.util.Log;

import com.example.android.sunshine.DetailActivity;
import com.example.android.sunshine.R;
import com.example.android.sunshine.data.SunshinePreferences;
import com.example.android.sunshine.data.WeatherContract;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.wearable.Asset;
import com.google.android.gms.wearable.DataApi;
import com.google.android.gms.wearable.PutDataMapRequest;
import com.google.android.gms.wearable.PutDataRequest;
import com.google.android.gms.wearable.Wearable;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class NotificationUtils {

    /*
     * The columns of data that we are interested in displaying within our notification to let
     * the user know there is new weather data available.
     */
    public static final String[] WEATHER_NOTIFICATION_PROJECTION = {
            WeatherContract.WeatherEntry.COLUMN_WEATHER_ID,
            WeatherContract.WeatherEntry.COLUMN_MAX_TEMP,
            WeatherContract.WeatherEntry.COLUMN_MIN_TEMP,
    };

    /*
     * We store the indices of the values in the array of Strings above to more quickly be able
     * to access the data from our query. If the order of the Strings above changes, these
     * indices must be adjusted to match the order of the Strings.
     */
    public static final int INDEX_WEATHER_ID = 0;
    public static final int INDEX_MAX_TEMP = 1;
    public static final int INDEX_MIN_TEMP = 2;

    /*
     * This notification ID can be used to access our notification after we've displayed it. This
     * can be handy when we need to cancel the notification, or perhaps update it. This number is
     * arbitrary and can be set to whatever you like. 3004 is in no way significant.
     */
    private static final int WEATHER_NOTIFICATION_ID = 3004;

    /**
     * Constructs and displays a notification for the newly updated weather for today.
     *
     * @param context Context used to query our ContentProvider and use various Utility methods
     */
    public static void notifyUserOfNewWeather(Context context) {

        /* Build the URI for today's weather in order to show up to date data in notification */
        Uri todaysWeatherUri = WeatherContract.WeatherEntry
                .buildWeatherUriWithDate(SunshineDateUtils.normalizeDate(System.currentTimeMillis()));

        /*
         * The MAIN_FORECAST_PROJECTION array passed in as the second parameter is defined in our WeatherContract
         * class and is used to limit the columns returned in our cursor.
         */
        Cursor todayWeatherCursor = context.getContentResolver().query(
                todaysWeatherUri,
                WEATHER_NOTIFICATION_PROJECTION,
                null,
                null,
                null);

        /*
         * If todayWeatherCursor is empty, moveToFirst will return false. If our cursor is not
         * empty, we want to show the notification.
         */
        if (todayWeatherCursor.moveToFirst()) {

            /* Weather ID as returned by API, used to identify the icon to be used */
            int weatherId = todayWeatherCursor.getInt(INDEX_WEATHER_ID);
            double high = todayWeatherCursor.getDouble(INDEX_MAX_TEMP);
            double low = todayWeatherCursor.getDouble(INDEX_MIN_TEMP);

            Resources resources = context.getResources();
            int largeArtResourceId = SunshineWeatherUtils
                    .getLargeArtResourceIdForWeatherCondition(weatherId);

            Bitmap largeIcon = BitmapFactory.decodeResource(
                    resources,
                    largeArtResourceId);

            String notificationTitle = context.getString(R.string.app_name);

            String notificationText = getNotificationText(context, weatherId, high, low);

            /* getSmallArtResourceIdForWeatherCondition returns the proper art to show given an ID */
            int smallArtResourceId = SunshineWeatherUtils
                    .getSmallArtResourceIdForWeatherCondition(weatherId);

            /*
             * NotificationCompat Builder is a very convenient way to build backward-compatible
             * notifications. In order to use it, we provide a context and specify a color for the
             * notification, a couple of different icons, the title for the notification, and
             * finally the text of the notification, which in our case in a summary of today's
             * forecast.
             */
            NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(context)
                    .setColor(ContextCompat.getColor(context,R.color.colorPrimary))
                    .setSmallIcon(smallArtResourceId)
                    .setLargeIcon(largeIcon)
                    .setContentTitle(notificationTitle)
                    .setContentText(notificationText)
                    .setAutoCancel(true);

            /*
             * This Intent will be triggered when the user clicks the notification. In our case,
             * we want to open Sunshine to the DetailActivity to display the newly updated weather.
             */
            Intent detailIntentForToday = new Intent(context, DetailActivity.class);
            detailIntentForToday.setData(todaysWeatherUri);

            TaskStackBuilder taskStackBuilder = TaskStackBuilder.create(context);
            taskStackBuilder.addNextIntentWithParentStack(detailIntentForToday);
            PendingIntent resultPendingIntent = taskStackBuilder
                    .getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);

            notificationBuilder.setContentIntent(resultPendingIntent);

            NotificationManager notificationManager = (NotificationManager)
                    context.getSystemService(Context.NOTIFICATION_SERVICE);

            /* WEATHER_NOTIFICATION_ID allows you to update or cancel the notification later on */
            notificationManager.notify(WEATHER_NOTIFICATION_ID, notificationBuilder.build());

            notifyWear(context, BitmapFactory.decodeResource(resources, smallArtResourceId),high,low);
            /*
             * Since we just showed a notification, save the current time. That way, we can check
             * next time the weather is refreshed if we should show another notification.
             */
            SunshinePreferences.saveLastNotificationTime(context, System.currentTimeMillis());
        }

        /* Always close your cursor when you're done with it to avoid wasting resources. */
        todayWeatherCursor.close();
    }

    private static void notifyWear(Context context, Bitmap bitmap, double high, double low) {
        final GoogleApiClient mGoogleApiClient;



        final String WEATHER_PATH = "/weather";
        final String HIGH_TEMP_KEY= "high_temp_key";
        final String LOW_TEMP_KEY = "low_temp_key";
        final String ICON_KEY = "icon_key";

        mGoogleApiClient = new GoogleApiClient.Builder(context)
                .addApi(Wearable.API)
                .addConnectionCallbacks(new GoogleApiClient.ConnectionCallbacks() {
                    @Override
                    public void onConnected(@Nullable Bundle bundle) {
                        Log.d("WearApp","connect");
                    }

                    @Override
                    public void onConnectionSuspended(int i) {

                    }
                }).build();

        mGoogleApiClient.connect();

        Asset assetWeather = createAssetFromBitmap(Bitmap.createScaledBitmap(bitmap,52,52,true));

        PutDataMapRequest putDataMapRequest = PutDataMapRequest.create(WEATHER_PATH);
        putDataMapRequest.setUrgent();
        putDataMapRequest.getDataMap().putString(HIGH_TEMP_KEY, SunshineWeatherUtils.formatTemperature(context, high));
        Log.d("high temp",SunshineWeatherUtils.formatTemperature(context,high));
        putDataMapRequest.getDataMap().putString(LOW_TEMP_KEY, SunshineWeatherUtils.formatTemperature(context, low));
        putDataMapRequest.getDataMap().putAsset(ICON_KEY, assetWeather);
        putDataMapRequest.getDataMap().putLong("timestamp", System.currentTimeMillis());

        PutDataRequest request = putDataMapRequest.asPutDataRequest();
        Wearable.DataApi.putDataItem(mGoogleApiClient, request).setResultCallback(new ResultCallback<DataApi.DataItemResult>() {
            @Override
            public void onResult(DataApi.DataItemResult dataItemResult) {
                if (dataItemResult.getStatus().isSuccess()) {
                    Log.e("Watch Log", "Successfully send weather info");
                } else {
                    Log.e("Watch Log", "Failed to send weather info ");
                }
            }
        });
    }

    private static Asset createAssetFromBitmap(Bitmap bitmap) {
        ByteArrayOutputStream byteStream = null;
        try {
            byteStream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteStream);
            return Asset.createFromBytes(byteStream.toByteArray());
        } finally {
            if (null != byteStream) {
                try {
                    byteStream.close();
                } catch (IOException e) {
                    // ignore
                }
            }
        }
    }

    /**
     * Constructs and returns the summary of a particular day's forecast using various utility
     * methods and resources for formatting. This method is only used to create the text for the
     * notification that appears when the weather is refreshed.
     * <p>
     * The String returned from this method will look something like this:
     * <p>
     * Forecast: Sunny - High: 14°C Low 7°C
     *
     * @param context   Used to access utility methods and resources
     * @param weatherId ID as determined by Open Weather Map
     * @param high      High temperature (either celsius or fahrenheit depending on preferences)
     * @param low       Low temperature (either celsius or fahrenheit depending on preferences)
     * @return Summary of a particular day's forecast
     */
    private static String getNotificationText(Context context, int weatherId, double high, double low) {

        /*
         * Short description of the weather, as provided by the API.
         * e.g "clear" vs "sky is clear".
         */
        String shortDescription = SunshineWeatherUtils
                .getStringForWeatherCondition(context, weatherId);

        String notificationFormat = context.getString(R.string.format_notification);

        /* Using String's format method, we create the forecast summary */
        String notificationText = String.format(notificationFormat,
                shortDescription,
                SunshineWeatherUtils.formatTemperature(context, high),
                SunshineWeatherUtils.formatTemperature(context, low));

        return notificationText;
    }
}
