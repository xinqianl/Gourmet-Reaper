package com.edu.cmu.gourmetreaper.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import com.facebook.AccessTokenTracker;
import com.facebook.ProfileTracker;

/**
 * Created by ali on 8/3/15.
 */
public class FacebookService extends Service {
    private AccessTokenTracker aTT;
    private ProfileTracker pT;

    public FacebookService(AccessTokenTracker mTokenTracker, ProfileTracker mProfileTracker) {
        aTT = mTokenTracker;
        pT = mProfileTracker;
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        aTT.startTracking();
        pT.startTracking();
        // We want this service to continue running until it is explicitly
        // stopped, so return sticky.

        return START_STICKY;

    }

    @Override
    public void onDestroy() {
        aTT.stopTracking();
        aTT.stopTracking();
    }


}
