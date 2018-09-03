package com.xlythe.demo;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.io.File;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class CameraFragment extends com.xlythe.fragment.camera.CameraFragment {
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_camera, container, false);
    }

    @Override
    public void onImageCaptured(File file) {

    }

    @Override
    public void onVideoCaptured(File file) {

    }

    public static CameraFragment newInstance() {
        return new CameraFragment();
    }
}
