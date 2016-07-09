package com.xlythe.demo;

import android.content.Context;
import android.net.Uri;

public class Sticker {
    private final int mThumbnailResId;

    public Sticker(int thumbnailResId) {
        mThumbnailResId = thumbnailResId;
    }

    public int getThumbnail() {
        return mThumbnailResId;
    }
}
