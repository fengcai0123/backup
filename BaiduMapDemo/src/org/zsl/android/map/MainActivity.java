package org.zsl.android.map;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.model.LatLng;


public class MainActivity extends Activity {
	public MapView mapView = null;
	public BaiduMap baiduMap = null;

	// ��λ�������
	public LocationClient locationClient = null;
	//�Զ���ͼ��
	BitmapDescriptor mCurrentMarker = null;
	boolean isFirstLoc = true;// �Ƿ��״ζ�λ

	public BDLocationListener myListener = new BDLocationListener() {
		@Override
		public void onReceiveLocation(BDLocation location) {
			// map view ���ٺ��ڴ����½��յ�λ��
			if (location == null || mapView == null)
				return;
			
			MyLocationData locData = new MyLocationData.Builder()
					.accuracy(location.getRadius())
					// �˴����ÿ����߻�ȡ���ķ�����Ϣ��˳ʱ��0-360
					.direction(100).latitude(location.getLatitude())
					.longitude(location.getLongitude()).build();
			baiduMap.setMyLocationData(locData);	//���ö�λ����
			
			
			if (isFirstLoc) {
				isFirstLoc = false;
				
				
				LatLng ll = new LatLng(location.getLatitude(),
						location.getLongitude());
				MapStatusUpdate u = MapStatusUpdateFactory.newLatLngZoom(ll, 16);	//���õ�ͼ���ĵ��Լ����ż���
//				MapStatusUpdate u = MapStatusUpdateFactory.newLatLng(ll);
				baiduMap.animateMapStatus(u);
			}
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// ��ʹ��SDK�����֮ǰ��ʼ��context��Ϣ������ApplicationContext
		// ע��÷���Ҫ��setContentView����֮ǰʵ��
		SDKInitializer.initialize(getApplicationContext());
		setContentView(R.layout.main_activity);
		
		mapView = (MapView) this.findViewById(R.id.mapView); // ��ȡ��ͼ�ؼ�����
		baiduMap = mapView.getMap();
		//������λͼ��
		baiduMap.setMyLocationEnabled(true);
		
		locationClient = new LocationClient(getApplicationContext()); // ʵ����LocationClient��
		locationClient.registerLocationListener(myListener); // ע���������
		this.setLocationOption();	//���ö�λ����
		locationClient.start(); // ��ʼ��λ
		// baiduMap.setMapType(BaiduMap.MAP_TYPE_NORMAL); // ����Ϊһ���ͼ

		// baiduMap.setMapType(BaiduMap.MAP_TYPE_SATELLITE); //����Ϊ���ǵ�ͼ
		// baiduMap.setTrafficEnabled(true); //������ͨͼ

	}

	// ����״̬ʵ�ֵ�ͼ�������ڹ���
	@Override
	protected void onDestroy() {
		//�˳�ʱ���ٶ�λ
		locationClient.stop();
		baiduMap.setMyLocationEnabled(false);
		// TODO Auto-generated method stub
		super.onDestroy();
		mapView.onDestroy();
		mapView = null;
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		mapView.onResume();
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		mapView.onPause();
	}

	/**
	 * ���ö�λ����
	 */
	private void setLocationOption() {
		LocationClientOption option = new LocationClientOption();
		option.setOpenGps(true); // ��GPS
		option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy);// ���ö�λģʽ
		option.setCoorType("bd09ll"); // ���صĶ�λ����ǰٶȾ�γ��,Ĭ��ֵgcj02
		option.setScanSpan(5000); // ���÷���λ����ļ��ʱ��Ϊ5000ms
		option.setIsNeedAddress(true); // ���صĶ�λ���������ַ��Ϣ
		option.setNeedDeviceDirect(true); // ���صĶ�λ��������ֻ���ͷ�ķ���
		
		locationClient.setLocOption(option);
	}

}
