package com.example.cameraview;

import java.io.IOException;

import android.app.Activity;
import android.hardware.Camera;
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
import android.widget.TextView;
import android.widget.Toast;


public class MainActivity extends Activity implements Callback, AutoFocusCallback, LocationListener {

	//�ŧi�Ѽ�
	SurfaceHolder surfaceholder;
	SurfaceView surfaceview;
	Camera camera;
	LocationManager locationManager;
	TextView tLongitude,tLatitude;			//�g�� , �n��
	String best;
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
        if(best != null)
        {
        	Location loc = locationManager.getLastKnownLocation(best);
        	showLocation(loc); 		//��ܸg�n��
        }
        	
        
    }
    
    @Override
    protected void onResume() {
    	if(best != null){
    		locationManager.requestLocationUpdates(best, 100, 1, this);
    	}
    	super.onResume();
    }
	private void showLocation(Location loc) {
		if(loc != null)
		{
			double latitude = loc.getLatitude();
			double longitude = loc.getLongitude();
			tLatitude.setText(latitude+"");
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
	public void onLocationChanged(Location location) {
		// ���m�o�ͧ��ܮ�
		showLocation(location);
	}

	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {
		// ��w�쪬�A���ܮ�
		
	}

	@Override
	public void onProviderEnabled(String provider) {
		// ��_�Φ�m��������
		
	}

	@Override
	public void onProviderDisabled(String provider) {
		// ��������m��������
		
	}



}
