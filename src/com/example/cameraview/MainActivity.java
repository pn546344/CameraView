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

	//�ŧi�Ѽ�
	SurfaceHolder surfaceholder;
	SurfaceView surfaceview;
	Camera camera;
	LocationManager locationManager;
	TextView tLongitude,tLatitude;			//�g��,�n��
	String best;
	ImageView img;
	float currentDegree = 0f;
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
        
        //���������TextView
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
        
        SensorManager sm = (SensorManager)getSystemService(SENSOR_SERVICE);		//�ǷP���޲z��
        
        /* 
        	���USensor Listener (registerListener)
        	�Ĥ@�ӰѼ�: Sensor.TYPE_ORIENTATION(��V�ǷP��);
        	�ĤG�ӰѼ�: 
        		SENSOR_DELAY_FASTEST(0�@����);
        		SENSOR_DELAY_GAME(20,000�@����)
        		SENSOR_DELAY_UI(60,000�@����))
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

	@Override
	public void onLocationChanged(Location arg0) {
		// ���m�o�ͧ��ܮ�
		showLocation(arg0);
	}

	@Override
	public void onProviderDisabled(String arg0) {
		// ��������m��������
		
	}

	@Override
	public void onProviderEnabled(String arg0) {
		// ��ҥΦ�m��������
		
	}

	@Override
	public void onStatusChanged(String arg0, int arg1, Bundle arg2) {
		// ��w�쪬�A���ܮ�
		
	}

	@Override
	public void onAccuracyChanged(Sensor arg0, int arg1) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onSensorChanged(SensorEvent arg0) {
		// �ǷP�����i�s����(��V����)
		if (arg0.sensor.getType() == Sensor.TYPE_ORIENTATION) {
			   float degree = arg0.values[0];
			   
			   /*
			   RotateAnimation���O�G�����ܤưʵe��
			    
			   �Ѽƻ���:
			   fromDegrees�G���઺�}�l���סC
			   toDegrees�G���઺�������סC
			   pivotXType�GX�b�����Y�Ҧ��A�i�H���Ȭ�ABSOLUTE�BRELATIVE_TO_SELF�BRELATIVE_TO_PARENT�C
			   pivotXValue�GX���Ъ����Y�ȡC
			   pivotYType�GY�b�����Y�Ҧ��A�i�H���Ȭ�ABSOLUTE�BRELATIVE_TO_SELF�BRELATIVE_TO_PARENT�C
			   pivotYValue�GY���Ъ����Y�ȡC
			   */
			   RotateAnimation ra = new RotateAnimation(
			     currentDegree, // �ʵe�_�l�ɪ��󪺨���
			     -degree,       // �ʵe�����ɪ�����઺����(�i�j��360��)-��ܰf�ɰw����,+��ܶ��ɰw����
			     Animation.RELATIVE_TO_SELF, 0.5f, //�ʵe�۹�󪫥�X�y�Ъ��}�l��m, �q0%~100%������, 50%������X��V���ФW�����I��m
			     Animation.RELATIVE_TO_SELF, 0.5f); //�ʵe�۹�󪫥�Y�y�Ъ��}�l��m, �q0%~100%������, 50%������Y��V���ФW�����I��m

			   ra.setDuration(200); // ����L�{����ɶ�
			   ra.setRepeatCount(-1); // �ʵe���Ʀ��� (-1 ��ܤ@������)
			   img.startAnimation(ra); // ù�L�Ϥ��ϥα���ʵe
			   currentDegree = -degree; // �O�s����᪺�׼�, currentDegree�O�@�Ӧb�����w�q��float�����ܶq
			  }
		
	}



}
