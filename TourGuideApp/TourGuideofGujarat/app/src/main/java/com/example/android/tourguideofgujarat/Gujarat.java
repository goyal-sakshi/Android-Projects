package com.example.android.tourguideofgujarat;

/**
 * Created by hp on 8/5/2017.
 */

public class Gujarat {

    private String mHeading;
    private String mDetails;
    private String mLocation;
    private int mImageResourseId = NO_IMAGE_RESOURSE;
    private static final int NO_IMAGE_RESOURSE = -1;

    public Gujarat(String heading, String details, String location){
        mHeading = heading;
        mDetails = details;
        mLocation = location;
    }

    public Gujarat(String heading, String details, int imageResourseId, String location){
        mHeading = heading;
        mDetails = details;
        mImageResourseId = imageResourseId;
        mLocation = location;
    }

    public String getHeading() {
        return mHeading;
    }

    public String getDeatails() {
        return mDetails;
    }

    public boolean hasImage() {
        return mImageResourseId != NO_IMAGE_RESOURSE;
    }

    public int getImageResourseId() {
        return mImageResourseId;
    }
    public String getLocation() {
        return mLocation;
    }
}
