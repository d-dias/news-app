package com.example.dilkidias.newsapp;

import android.graphics.Bitmap;

/**
 * Created by DILKI DIAS on 11-Jun-17.
 */

public class News {
    private String mNewsTitle;
    private String mTags;
    private String mUrl;
    private Bitmap mImageBitmap;
    private int mRatings;
    private String mPublisher;
    private String mPublishedDate;

    public News(String newsTitle, String newsTags, String url,
                Bitmap imageBitmap, int ratings, String publisher, String publishedDate) {
        mNewsTitle = newsTitle;
        mTags = newsTags;
        mUrl = url;
        mImageBitmap = imageBitmap;
        mRatings = ratings;
        mPublisher = publisher;
        mPublishedDate = publishedDate;
    }

    public String getNewsTitle() {
        return mNewsTitle;
    }

    public String getTags() {
        return mTags;
    }

    public String getUrl() {
        return mUrl;
    }

    public Bitmap getImageBitmap() {
        return mImageBitmap;
    }

    public int getRatings() {
        return mRatings;
    }

    public String getPublisher() {
        return mPublisher;
    }

    public String getPublishedDate() {
        return mPublishedDate;
    }
}
