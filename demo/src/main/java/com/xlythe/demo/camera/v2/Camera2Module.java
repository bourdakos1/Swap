package com.xlythe.demo.camera.v2;

import android.annotation.TargetApi;

import com.xlythe.demo.camera.CameraView;

@TargetApi(21)
public class Camera2Module extends Camera2VideoModule {
    public Camera2Module(CameraView cameraView) {
        super(cameraView);
    }
}
