package com.example.android.books_android;

import static android.R.attr.author;

/**
 * Created by hp on 8/12/2017.
 */

public class Books {

    private String mTitle;
    private String[] mAuthor;
    private String mDescription;
    private String mLink;

    public Books(String title, String[] author, String description, String link){

        this.mTitle = title;
        this.mAuthor = author;
        this.mDescription = description;
        this.mLink = link;
    }

    public String getTitle(){
        return mTitle;
    }

    public String[] getAuthor(){
        return mAuthor;
    }

    public String generateStringOfAuthor() {
        String s = "";
        for(int i=0; i<mAuthor.length; i++) {
            if(i == mAuthor.length-1)
                s += mAuthor[i];
            else
                s += mAuthor[i] + ", ";
        }
        return s;
    }

    public String getDescription(){
        return mDescription;
    }

    public String getLink(){
        return mLink;
    }
}
