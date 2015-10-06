package com.bunk3r.popularmovies.fragments;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.support.v4.app.Fragment;

import com.bunk3r.popularmovies.services.MoviesDbService;

public class BaseFragment extends Fragment {

    public MoviesDbService mService;

    private final ServiceConnection mConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName className, IBinder service) {
            MoviesDbService.DestinyServiceBinder binder = (MoviesDbService.DestinyServiceBinder) service;
            mService = binder.getInstance();
            serviceConnected();
        }

        @Override
        public void onServiceDisconnected(ComponentName arg0) {
            mService = null;
        }
    };

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        Intent intent = new Intent(activity, MoviesDbService.class);
        activity.bindService(intent, mConnection, Context.BIND_AUTO_CREATE);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        if (mService != null) {
            serviceDisconnected();
            getActivity().unbindService(mConnection);
        }
    }

    public void serviceConnected() {
        // STUB
    }

    public void serviceDisconnected() {
        // STUB
    }

}