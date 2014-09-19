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

	//宣告參數
	SurfaceHolder surfaceholder;
	SurfaceView surfaceview;
	Camera camera;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //控制畫面全螢幕
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(
        		WindowManager.LayoutParams.FLAG_FULLSCREEN, 
        		WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);
        //使用surfaceholder控制surfaceview
        surfaceview = (SurfaceView)findViewById(R.id.surfaceView1);
        surfaceholder = surfaceview.getHolder();
        surfaceholder.addCallback(this);
        
        
    }
	@Override
	public void surfaceChanged(SurfaceHolder arg0, int arg1, int arg2, int arg3) {
		// surfaceholder改變時
		camera.startPreview();								//啟用相機擷取畫面
		camera.autoFocus(this);								//自動對焦(只會對焦一次)
		
	}
	@Override
	public void surfaceCreated(SurfaceHolder arg0) {
		// surfaceholder創建時
		try {
			camera = Camera.open();							//開啟照相機
			camera.setPreviewDisplay(surfaceholder);		//設定相機視頻的顯示位置
			camera.setDisplayOrientation(90); 				//設定擷取的畫面旋轉角度
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		
		
	}
	@Override
	public void surfaceDestroyed(SurfaceHolder arg0) {
		// surfaceholder撤銷時
		camera.stopPreview();								//停止相機擷取畫面
		camera.release();									//釋放相機資源
		
	}
	@Override
	public void onAutoFocus(boolean arg0, Camera arg1) {
		// TODO Auto-generated method stub
		
	}



}
