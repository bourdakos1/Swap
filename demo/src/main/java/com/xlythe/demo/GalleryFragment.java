package com.xlythe.demo;

import android.Manifest;
import android.content.CursorLoader;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import static com.xlythe.demo.PermissionUtils.hasPermissions;

public class GalleryFragment extends Fragment {

    private static final String[] REQUIRED_PERMISSIONS;

    static {
        if (Build.VERSION.SDK_INT >= 33) {
            REQUIRED_PERMISSIONS = new String[] {
                    Manifest.permission.READ_MEDIA_IMAGES,
                    Manifest.permission.READ_MEDIA_VIDEO,
            };
        } else {
            REQUIRED_PERMISSIONS = new String[] {
                    Manifest.permission.READ_EXTERNAL_STORAGE
            };
        }
    }

    private static final int REQUEST_CODE_REQUIRED_PERMISSIONS = 2;

    public static GalleryFragment newInstance() {
        return new GalleryFragment();
    }

    private Cursor mCursor;
    private AttachmentAdapter mAdapter;
    private RecyclerView mAttachments;
    private View mPermissionPrompt;

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == REQUEST_CODE_REQUIRED_PERMISSIONS) {
            if (hasPermissions(getContext(), REQUIRED_PERMISSIONS)) {
                showGallery();
            } else {
                showPermissionPrompt();
            }
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_gallery, container, false);

        mAttachments = rootView.findViewById(R.id.attachments);
        mAttachments.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        mAttachments.addItemDecoration(new GalleryItemDecoration(getResources().getDrawable(R.drawable.divider_attach)));

        mPermissionPrompt = rootView.findViewById(R.id.layout_permissions);
        mPermissionPrompt.findViewById(R.id.request_permissions).setOnClickListener(v -> requestPermissions(REQUIRED_PERMISSIONS, REQUEST_CODE_REQUIRED_PERMISSIONS));

        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (hasPermissions(getContext(), REQUIRED_PERMISSIONS)) {
            showGallery();
        } else {
            showPermissionPrompt();
        }
    }

    private void showGallery() {
        mAttachments.setVisibility(View.VISIBLE);
        mPermissionPrompt.setVisibility(View.GONE);

        mCursor = createCursor();
        mAdapter = new AttachmentAdapter(getContext(), mCursor);
        mAttachments.setAdapter(mAdapter);
    }

    private void showPermissionPrompt() {
        mAttachments.setVisibility(View.GONE);
        mPermissionPrompt.setVisibility(View.VISIBLE);
    }

    private Cursor createCursor() {
        String[] projection = {
                MediaStore.Files.FileColumns._ID,
                MediaStore.Files.FileColumns.DATA,
                MediaStore.Files.FileColumns.MEDIA_TYPE,
        };

        // Return only video and image metadata.
        String selection = MediaStore.Files.FileColumns.MEDIA_TYPE + "="
                + MediaStore.Files.FileColumns.MEDIA_TYPE_IMAGE
                + " OR "
                + MediaStore.Files.FileColumns.MEDIA_TYPE + "="
                + MediaStore.Files.FileColumns.MEDIA_TYPE_VIDEO;

        Uri queryUri = MediaStore.Files.getContentUri("external");

        CursorLoader cursorLoader = new CursorLoader(
                getContext(),
                queryUri,
                projection,
                selection,
                null,
                MediaStore.Files.FileColumns.DATE_ADDED + " DESC"
        );

        return cursorLoader.loadInBackground();
    }
}