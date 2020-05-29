package com.example.anikrakib.covid_19bd;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.RemoteViews;

import com.android.volley.Request;
import com.android.volley.Request.Method;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;

/**
 * Implementation of App Widget functionality.
 */
public class AppWidget extends AppWidgetProvider {

    static void updateAppWidget(final Context context, final AppWidgetManager appWidgetManager,
                                final int appWidgetId) {

        //Updating views of widget through RemoteViews
        final RemoteViews[] views = {new RemoteViews(context.getPackageName(), R.layout.app_widget)};

        // Instantiate the RequestQueue.
        RequestQueue queue = Volley.newRequestQueue(context);
        String url ="https://corona-bd.herokuapp.com/stats";
        String url2 ="https://disease.sh/v2/countries/bangladesh";
        //String url ="https://coronabdapi.herokuapp.com/api/?format=json";

        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        //Converting response to UTF-8
                        try {
                            response = new String(response.getBytes("ISO-8859-1"), "UTF-8");

                            //Create a JSONObject from the JSON_RESPONSE string
                            JSONObject baseJsonResponse = new JSONObject(response);
                            //JSONObject data = baseJsonResponse.getJSONObject("data");

                            JSONObject jsonObject = new JSONObject(response.toString());
                            JSONObject death = jsonObject.getJSONObject("death");
                            JSONObject cases = jsonObject.getJSONObject("positive");
                            JSONObject recover = jsonObject.getJSONObject("recovered");
                            JSONObject test = jsonObject.getJSONObject("test");


                            //set the views to be updated on the widget
                            views[0] = setViewGroupCases(views[0], cases);
                            views[0] = setViewGroupDeath(views[0], death);
                            views[0] = setViewGroupRecover(views[0], recover);
                            views[0] = setViewGroupTest(views[0], test);
                            views[0] = setViewGroupLastUpdate(views[0], jsonObject);

                            //Add tap-to-update option
                            Intent intentSync = new Intent(context, AppWidget.class);
                            intentSync.setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE);
                            intentSync.putExtra( AppWidgetManager.EXTRA_APPWIDGET_IDS, new int[] { appWidgetId } );
                            PendingIntent pendingSync = PendingIntent.getBroadcast(context,0, intentSync, PendingIntent.FLAG_UPDATE_CURRENT);
                            views[0].setOnClickPendingIntent(R.id.updateView, pendingSync);

                            //Notify the widget to get updated
                            appWidgetManager.updateAppWidget(appWidgetId, views[0]);

                        } catch (UnsupportedEncodingException e) {
                            e.printStackTrace();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //Add tap-to-update option when get errors
                Intent intentSync = new Intent(context, AppWidget.class);
                intentSync.setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE);
                intentSync.putExtra( AppWidgetManager.EXTRA_APPWIDGET_IDS, new int[] { appWidgetId } );
                PendingIntent pendingSync = PendingIntent.getBroadcast(context,0, intentSync, PendingIntent.FLAG_UPDATE_CURRENT);
                views[0].setOnClickPendingIntent(R.id.updateView, pendingSync);
                appWidgetManager.updateAppWidget(appWidgetId, views[0]);
                Log.e("Volley Error:", String.valueOf(error));
            }
        });

        StringRequest stringRequest1 = new StringRequest(Request.Method.GET, url2,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        //Converting response to UTF-8
                        try {
                            response = new String(response.getBytes("ISO-8859-1"), "UTF-8");


                            JSONObject jsonObject = new JSONObject(response.toString());

                            //set the views to be updated on the widget
                            views[0] = setViewGroupActive(views[0], jsonObject);

                            //Add tap-to-update option
                            Intent intentSync = new Intent(context, AppWidget.class);
                            intentSync.setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE);
                            intentSync.putExtra( AppWidgetManager.EXTRA_APPWIDGET_IDS, new int[] { appWidgetId } );
                            PendingIntent pendingSync = PendingIntent.getBroadcast(context,0, intentSync, PendingIntent.FLAG_UPDATE_CURRENT);
                            views[0].setOnClickPendingIntent(R.id.updateView, pendingSync);

                            //Notify the widget to get updated
                            appWidgetManager.updateAppWidget(appWidgetId, views[0]);

                        } catch (UnsupportedEncodingException e) {
                            e.printStackTrace();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //Add tap-to-update option when get errors
                Intent intentSync = new Intent(context, AppWidget.class);
                intentSync.setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE);
                intentSync.putExtra( AppWidgetManager.EXTRA_APPWIDGET_IDS, new int[] { appWidgetId } );
                PendingIntent pendingSync = PendingIntent.getBroadcast(context,0, intentSync, PendingIntent.FLAG_UPDATE_CURRENT);
                views[0].setOnClickPendingIntent(R.id.updateView, pendingSync);
                appWidgetManager.updateAppWidget(appWidgetId, views[0]);
                Log.e("Volley Error:", String.valueOf(error));
            }
        });

        // Add the request to the RequestQueue.
        queue.add(stringRequest);
        queue.add(stringRequest1);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        super.onUpdate(context, appWidgetManager, appWidgetIds);
        final RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.app_widget);

//        if(isNetworkConnected(context))
//            //Make the progressBar visible at the beginning
//            remoteViews.setViewVisibility(R.id.loadingIndicator, View.VISIBLE);
//        else
//            //Set Tap To Refresh to No Internet... if no internet available
//            remoteViews.setTextViewText(R.id.tapToRefresh, "No Internet");

        appWidgetManager.updateAppWidget(appWidgetIds, remoteViews);

        //If multiple widgets are active, then update them all
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId);
        }
    }

    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }
    static RemoteViews setViewGroupCases(RemoteViews views, JSONObject data){
        try {
            views.setTextViewText(R.id.todayPositiveBD, data.getString("last24"));
            views.setTextViewText(R.id.confirmedTv, data.getString("total"));


//            views.setTextViewText(R.id.new_infected, "নতুনঃ "+data.getString("new_infected"));
//            views.setTextViewText(R.id.total_infected, data.getString("total_infected"));
//            views.setTextViewText(R.id.new_cured, "নতুনঃ "+data.getString("new_cured"));
//            views.setTextViewText(R.id.total_cured, data.getString("total_cured"));
//            views.setTextViewText(R.id.new_death, "নতুনঃ "+data.getString("new_death"));
//            views.setTextViewText(R.id.total_death, data.getString("total_death"));
//            views.setTextViewText(R.id.new_test, "নতুনঃ "+data.getString("new_test"));
//            views.setTextViewText(R.id.total_test, data.getString("total_test"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return views;
    }
    static RemoteViews setViewGroupDeath(RemoteViews views, JSONObject data){
        try {
            views.setTextViewText(R.id.todayDeathBD, data.getString("last24"));
            views.setTextViewText(R.id.deceasedTv, data.getString("total"));

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return views;
    }
    static RemoteViews setViewGroupRecover(RemoteViews views, JSONObject data){
        try {
            views.setTextViewText(R.id.todayRecoverBd, data.getString("last24"));
            views.setTextViewText(R.id.recoveredTv, data.getString("total"));

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return views;
    }
    static RemoteViews setViewGroupTest(RemoteViews views, JSONObject data){
        try {

            views.setTextViewText(R.id.todayTestBD,data.getString("last24"));
            views.setTextViewText(R.id.activeTv, data.getString("total_test"));

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return views;
    }
    static RemoteViews setViewGroupActive(RemoteViews views, JSONObject data){
        try {

            views.setTextViewText(R.id.activeTv,data.getString("active"));

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return views;
    }
    static RemoteViews setViewGroupLastUpdate(RemoteViews views, JSONObject data){
        try {

            views.setTextViewText(R.id.lastUpdatedTv,data.getString("updated_on"));

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return views;
    }
}

