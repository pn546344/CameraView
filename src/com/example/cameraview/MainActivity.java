package com.example.cameraview;

import java.io.IOException;

import android.app.Activity;
import android.hardware.Camera;
import android.hardware.Camera.AutoFocusCallback;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SurfaceHolder;
import android.view.SurfaceHolder.Callback;
import android.view.SurfaceView;
import android.view.Window;
import android.view.WindowManager;


public class MainActivity extends Activity implements Callback, AutoFocusCallback {

	//�ŧi�Ѽ�
	SurfaceHolder surfaceholder;
	SurfaceView surfaceview;
	Camera camera;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //����e�����ù�
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(
        		WindowManager.LayoutParams.FLAG_FULLSCREEN, 
        		WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);
        //�ϥ�surfaceholder����surfaceview
        surfaceview = (SurfaceView)findViewById(R.id.surfaceView1);
        surfaceholder = surfaceview.getHolder();
        surfaceholder.addCallback(this);
        
        
    }
	@Override
	public void surfaceChanged(SurfaceHolder arg0, int arg1, int arg2, int arg3) {
		// surfaceholder���ܮ�
		camera.startPreview();								//�ҥά۾��^���e��
		camera.autoFocus(this);								//�۰ʹ�J(�u�|��J�@��)
		
	}
	@Override
	public void surfaceCreated(SurfaceHolder arg0) {
		// surfaceholder�Ыخ�
		try {
			camera = Camera.open();							//�}�ҷӬ۾�
			camera.setPreviewDisplay(surfaceholder);		//�]�w�۾����W����ܦ�m
			camera.setDisplayOrientation(90); 				//�]�w�^�����e�����ਤ��
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		
		
	}
	@Override
	public void surfaceDestroyed(SurfaceHolder arg0) {
		// surfaceholder�M�P��
		camera.stopPreview();								//����۾��^���e��
		camera.release();									//����۾��귽
		
	}
	@Override
	public void onAutoFocus(boolean arg0, Camera arg1) {
		// TODO Auto-generated method stub
		
	}



}
