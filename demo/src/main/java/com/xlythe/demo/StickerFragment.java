package com.xlythe.demo;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class StickerFragment extends Fragment {
    private static final String TAG = StickerFragment.class.getSimpleName();
    private static final boolean DEBUG = false;

    public static StickerFragment newInstance() {
        StickerFragment fragment = new StickerFragment();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_sticker, container, false);
        RecyclerView gridView = rootView.findViewById(R.id.content);
        gridView.setAdapter(new StickerAdapter(getContext()));
        gridView.setLayoutManager(new GridAutofitLayoutManager(getContext()));
        return rootView;
    }

    private static class GridAutofitLayoutManager extends GridLayoutManager {
        private int mColumnWidth;
        private boolean mColumnWidthChanged = true;

        public GridAutofitLayoutManager(Context context) {
            // Initially set spanCount to 1, will be changed automatically later.
            super(context, 1);
        }

        private void setColumnWidth(int newColumnWidth) {
            log("setColumnWidth newColumnWidth=%s", newColumnWidth);
            if (newColumnWidth > 0 && newColumnWidth != mColumnWidth) {
                mColumnWidth = newColumnWidth;
                mColumnWidthChanged = true;
            }
        }

        private void measureChildren(RecyclerView.Recycler recycler) {
            if (getChildCount() == 0) {
                //Scrap measure one child
                View scrap = recycler.getViewForPosition(0);
                addView(scrap);
                measureChildWithMargins(scrap, 0, 0);

                /*
                 * We make some assumptions in this code based on every child
                 * view being the same size (i.e. a uniform grid). This allows
                 * us to compute the following values up front because they
                 * won't change.
                 */
                setColumnWidth(getDecoratedMeasuredWidth(scrap));

                detachAndScrapView(scrap, recycler);
            }

            if (mColumnWidthChanged && mColumnWidth > 0) {
                int totalSpace;
                if (getOrientation() == RecyclerView.VERTICAL) {
                    totalSpace = getWidth() - getPaddingRight() - getPaddingLeft();
                } else {
                    totalSpace = getHeight() - getPaddingTop() - getPaddingBottom();
                }
                int spanCount = Math.max(1, totalSpace / mColumnWidth);
                log("totalSpace=%s, spanCount=%s", totalSpace, spanCount);
                setSpanCount(spanCount);
                mColumnWidthChanged = false;
            }
        }

        @Override
        public void onLayoutChildren(RecyclerView.Recycler recycler, RecyclerView.State state) {
            measureChildren(recycler);
            super.onLayoutChildren(recycler, state);
        }
    }

    private static void log(String msg, Object... args) {
        if (DEBUG) {
            Log.d(TAG, String.format(msg, args));
        }
    }
}
