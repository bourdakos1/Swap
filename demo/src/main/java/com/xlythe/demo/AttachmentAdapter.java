package com.xlythe.demo;

import android.content.Context;
import android.database.Cursor;
import android.provider.MediaStore;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.io.File;

public class AttachmentAdapter extends RecyclerView.Adapter<AttachmentAdapter.ViewHolder> {

    private Context mContext;
    private Cursor mCursor;

    public AttachmentAdapter(Context context, Cursor cursor) {
        mContext = context;
        mCursor = cursor;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView mImage;
        private Context mContext;

        public ViewHolder(View view, Context context) {
            super(view);
            mContext = context;
            mImage = (ImageView) view.findViewById(R.id.image);
        }

        public void setCursor(Cursor cursor){
            String path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Thumbnails.DATA));
            Glide.with(mContext)
                    .load(new File(path))
                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                    .dontAnimate()
                    .placeholder(R.color.loading)
                    .into(mImage);
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View layout = LayoutInflater.from(mContext).inflate(R.layout.attach_icon, parent, false);
        return new ViewHolder(layout, mContext);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        mCursor.moveToPosition(position);
        holder.setCursor(mCursor);
    }

    @Override
    public int getItemCount() {
        return mCursor.getCount();
    }
}
