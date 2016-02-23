package com.edu.cmu.gourmetreaper.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import com.edu.cmu.gourmetreaper.util.DBConnector;
import com.facebook.AccessTokenTracker;
import com.facebook.ProfileTracker;

/**
 * Created by ali on 8/3/15.
 */
public class DBService extends Service {
    private DBConnector dbConnector;

    public DBService(DBConnector dbConnector) {
        this.dbConnector = dbConnector;
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        dbConnector.open();
        // We want this service to continue running until it is explicitly
        // stopped, so return sticky.

        return START_STICKY;

    }

    @Override
    public void onDestroy() {
        dbConnector.close();
    }


}
