package com.example.cameraview;

import java.io.IOException;

import android.app.Activity;
import android.hardware.Camera;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.hardware.Camera.AutoFocusCallback;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SurfaceHolder;
import android.view.SurfaceHolder.Callback;
import android.view.SurfaceView;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


public class MainActivity extends Activity implements Callback, AutoFocusCallback, LocationListener, SensorEventListener {

	//宣告參數
	SurfaceHolder surfaceholder;
	SurfaceView surfaceview;
	Camera camera;
	LocationManager locationManager;
	TextView tLongitude,tLatitude;			//經度,緯度
	String best;
	ImageView img;
	float currentDegree = 0f;
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
        
        //抓取對應的TextView
        tLongitude = (TextView)findViewById(R.id.textView2);
        tLatitude  = (TextView)findViewById(R.id.textView4);
        
        locationManager = (LocationManager)getSystemService(LOCATION_SERVICE);
        Criteria criteria = new Criteria();
        criteria.setAccuracy(Criteria.ACCURACY_COARSE);
        
        best = locationManager.getBestProvider(criteria, true);
        if(best != null){
        	Location loc = locationManager.getLastKnownLocation(best);
        	showLocation(loc);
        }
        
        img = (ImageView)findViewById(R.id.imageView1);
        
        SensorManager sm = (SensorManager)getSystemService(SENSOR_SERVICE);		//傳感器管理器
        
        /* 
        	註冊Sensor Listener (registerListener)
        	第一個參數: Sensor.TYPE_ORIENTATION(方向傳感器);
        	第二個參數: 
        		SENSOR_DELAY_FASTEST(0毫秒延遲);
        		SENSOR_DELAY_GAME(20,000毫秒延遲)
        		SENSOR_DELAY_UI(60,000毫秒延遲))
      */
        sm.registerListener(
        		this, 
        		sm.getDefaultSensor(Sensor.TYPE_ORIENTATION),
        		SensorManager.SENSOR_DELAY_FASTEST);
    }
	
    @Override
    protected void onResume() {
    	if(best != null){
    		locationManager.requestLocationUpdates(best, 100, 1, this);
    	}
    	super.onResume();
    }
    
    
    private void showLocation(Location loc) {
    	if(loc != null){
    		double latiude = loc.getLatitude();
    		double longitude = loc.getLongitude();
    		tLatitude.setText(latiude+"");
    		tLongitude.setText(longitude+"");
    		Toast.makeText(this, "is Update", Toast.LENGTH_SHORT).show();
    	}
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

	@Override
	public void onLocationChanged(Location arg0) {
		// 當位置發生改變時
		showLocation(arg0);
	}

	@Override
	public void onProviderDisabled(String arg0) {
		// 當關閉位置供應器時
		
	}

	@Override
	public void onProviderEnabled(String arg0) {
		// 當啟用位置供應器時
		
	}

	@Override
	public void onStatusChanged(String arg0, int arg1, Bundle arg2) {
		// 當定位狀態改變時
		
	}

	@Override
	public void onAccuracyChanged(Sensor arg0, int arg1) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onSensorChanged(SensorEvent arg0) {
		// 傳感器報告新的值(方向改變)
		if (arg0.sensor.getType() == Sensor.TYPE_ORIENTATION) {
			   float degree = arg0.values[0];
			   
			   /*
			   RotateAnimation類別：旋轉變化動畫類
			    
			   參數說明:
			   fromDegrees：旋轉的開始角度。
			   toDegrees：旋轉的結束角度。
			   pivotXType：X軸的伸縮模式，可以取值為ABSOLUTE、RELATIVE_TO_SELF、RELATIVE_TO_PARENT。
			   pivotXValue：X坐標的伸縮值。
			   pivotYType：Y軸的伸縮模式，可以取值為ABSOLUTE、RELATIVE_TO_SELF、RELATIVE_TO_PARENT。
			   pivotYValue：Y坐標的伸縮值。
			   */
			   RotateAnimation ra = new RotateAnimation(
			     currentDegree, // 動畫起始時物件的角度
			     -degree,       // 動畫結束時物件旋轉的角度(可大於360度)-表示逆時針旋轉,+表示順時針旋轉
			     Animation.RELATIVE_TO_SELF, 0.5f, //動畫相對於物件的X座標的開始位置, 從0%~100%中取值, 50%為物件的X方向坐標上的中點位置
			     Animation.RELATIVE_TO_SELF, 0.5f); //動畫相對於物件的Y座標的開始位置, 從0%~100%中取值, 50%為物件的Y方向坐標上的中點位置

			   ra.setDuration(200); // 旋轉過程持續時間
			   ra.setRepeatCount(-1); // 動畫重複次數 (-1 表示一直重複)
			   img.startAnimation(ra); // 羅盤圖片使用旋轉動畫
			   currentDegree = -degree; // 保存旋轉後的度數, currentDegree是一個在類中定義的float類型變量
			  }
		
	}



}
