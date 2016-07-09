package com.xlythe.demo;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

public class StickerAdapter extends RecyclerView.Adapter<StickerAdapter.ViewHolder> {
    private static final Sticker[] STICKERS = {
            new Sticker(R.drawable.thumb_affection),
            new Sticker(R.drawable.thumb_approval),
            new Sticker(R.drawable.thumb_confused),
            new Sticker(R.drawable.thumb_crying),
            new Sticker(R.drawable.thumb_depressed),
            new Sticker(R.drawable.thumb_elated),
            new Sticker(R.drawable.thumb_embarrassed),
            new Sticker(R.drawable.thumb_excited),
            new Sticker(R.drawable.thumb_failure),
            new Sticker(R.drawable.thumb_killme),
            new Sticker(R.drawable.thumb_lazyass),
            new Sticker(R.drawable.thumb_love),
            new Sticker(R.drawable.thumb_nicksface),
            new Sticker(R.drawable.thumb_noproblem),
            new Sticker(R.drawable.thumb_pat),
            new Sticker(R.drawable.thumb_pets),
            new Sticker(R.drawable.thumb_pout),
            new Sticker(R.drawable.thumb_praiseme),
            new Sticker(R.drawable.thumb_puppydog),
            new Sticker(R.drawable.thumb_seansface),
            new Sticker(R.drawable.thumb_shocked),
            new Sticker(R.drawable.thumb_shy),
            new Sticker(R.drawable.thumb_stressed),
            new Sticker(R.drawable.thumb_suspicious),
            new Sticker(R.drawable.thumb_thinking),
            new Sticker(R.drawable.thumb_unmotivated)
    };

    private Context mContext;

    public StickerAdapter(Context context) {
        mContext = context;
    }

    @Override
    public StickerAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View layout = LayoutInflater.from(mContext).inflate(R.layout.grid_item_sticker, parent, false);
        return new ViewHolder(layout);
    }

    @Override
    public void onBindViewHolder(StickerAdapter.ViewHolder holder, final int position) {
        holder.setSticker(STICKERS[position]);
    }

    @Override
    public int getItemCount() {
        return STICKERS.length;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final ImageView mImage;

        public ViewHolder(View view) {
            super(view);
            mImage = (ImageView) view.findViewById(R.id.image);
        }

        void setSticker(Sticker sticker) {
            mImage.setImageResource(sticker.getThumbnail());
        }
    }
}
