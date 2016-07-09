package com.xlythe.demo;

import android.content.pm.PackageManager;
import android.graphics.PorterDuff;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;

import com.xlythe.swap.SwapEditText;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private SwapEditText mEditText;
    private ImageView mGalleryAttachments;
    private ImageView mCameraAttachments;
    private ImageView mStickerAttachments;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mEditText = (SwapEditText) findViewById(R.id.edit_text);
        mGalleryAttachments = (ImageView) findViewById(R.id.gallery);
        mCameraAttachments = (ImageView) findViewById(R.id.camera);
        mStickerAttachments = (ImageView) findViewById(R.id.sticker);

        // Clear button selection when keyboard is dismissed
        mEditText.setOnFragmentHiddenListener(new SwapEditText.OnFragmentHiddenListener() {
            @Override
            public void onFragmentHidden() {
                clearAttachmentSelection();
            }
        });

        mEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        // Hide the camera fragment if the device has no camera
        mCameraAttachments.setVisibility(getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA_ANY) ? View.VISIBLE : View.GONE);

        // Set up recycler view
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.list);
        final LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(layoutManager);

        // Fill list view with dummy messages
        ArrayList<Message> messages = new ArrayList<>();
        for (int i = 0; i< 30; i++) {
            messages.add(new Message());
        }

        MessageAdapter adapter = new MessageAdapter(this, messages);
        recyclerView.setAdapter(adapter);

        // Dismiss keyboard when scrolling
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState > 0) {
                    mEditText.hideKeyboard();
                }
            }
        });
    }

    public void onButtonClicked(View view) {
        clearAttachmentSelection();
        ((ImageView) view).setColorFilter(getResources().getColor(R.color.colorPrimary), PorterDuff.Mode.SRC_ATOP);

        view.setEnabled(false);
        switch (view.getId()) {
            case R.id.gallery:
                mEditText.showFragment(GalleryFragment.newInstance());
                break;
            case R.id.camera:
                mEditText.showFragment(CameraFragment.newInstance());
                break;
            case R.id.sticker:
                mEditText.showFragment(StickerFragment.newInstance());
                break;
        }
    }

    private void clearAttachmentSelection() {
        mGalleryAttachments.clearColorFilter();
        mCameraAttachments.clearColorFilter();
        mStickerAttachments.clearColorFilter();
        int color = getResources().getColor(R.color.button);
        mGalleryAttachments.setColorFilter(color, PorterDuff.Mode.SRC_ATOP);
        mCameraAttachments.setColorFilter(color, PorterDuff.Mode.SRC_ATOP);
        mStickerAttachments.setColorFilter(color, PorterDuff.Mode.SRC_ATOP);

        mGalleryAttachments.setEnabled(true);
        mCameraAttachments.setEnabled(true);
        mStickerAttachments.setEnabled(true);
    }

    @Override
    public void onBackPressed() {
        if (mEditText.getFragmentVisibility()) {
            mEditText.hideKeyboard();
        } else {
            super.onBackPressed();
        }
    }
}
