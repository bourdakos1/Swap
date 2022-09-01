package com.xlythe.demo;

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.Cursor;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.xlythe.view.camera.Image;

import java.io.File;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class AttachmentAdapter extends RecyclerView.Adapter<AttachmentAdapter.ViewHolder> {

    private final Context mContext;
    private final Cursor mCursor;

    public AttachmentAdapter(Context context, Cursor cursor) {
        mContext = context;
        mCursor = cursor;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final ImageView mImage;
        private final Context mContext;

        public ViewHolder(View view, Context context) {
            super(view);
            mContext = context;
            mImage = view.findViewById(R.id.image);
        }

        @SuppressLint("RestrictedApi")
        public void setCursor(Cursor cursor){
            String path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Thumbnails.DATA));
            Image.with(mContext)
                    .load(new File(path))
                    .into(mImage);
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View layout = LayoutInflater.from(mContext).inflate(R.layout.attach_icon, parent, false);
        return new ViewHolder(layout, mContext);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        mCursor.moveToPosition(position);
        holder.setCursor(mCursor);
    }

    @Override
    public int getItemCount() {
        return mCursor.getCount();
    }
}
