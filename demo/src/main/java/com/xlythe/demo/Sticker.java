package com.xlythe.demo;

public class Sticker {
    private final int mThumbnailResId;

    public Sticker(int thumbnailResId) {
        mThumbnailResId = thumbnailResId;
    }

    public int getThumbnail() {
        return mThumbnailResId;
    }
}
