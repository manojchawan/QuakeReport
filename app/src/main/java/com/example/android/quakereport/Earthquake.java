package com.example.android.quakereport;

/**
 * Created by Manoj on 12/29/2016.
 */

public class Earthquake {
    private double mMagnitude;
    private String mLocation;
    private Long mTime;
    private String mUrl;

    public Earthquake(double Magnitude, String Location, Long Time, String url) {
        this.mMagnitude = Magnitude;
        this.mLocation = Location;
        this.mTime = Time;
        mUrl = url;
    }

    public double getmMagnitude() { return mMagnitude; }

    public String getmLocation() {
        return mLocation;
    }

    public Long getmTime() {
        return mTime;
    }

    public String getmUrl(){ return mUrl; }

}
