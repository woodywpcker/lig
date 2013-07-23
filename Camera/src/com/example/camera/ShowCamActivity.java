package com.example.camera;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class ShowCamActivity extends Activity {
	
	String[] camArray = null;//存储摄像头数组
	JSONArray jsonCamArray = null;//
	Bundle jsonObj_camData = new Bundle();//用于存储传送数据
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.listview);
		
	    Intent get_intent = getIntent();
		Bundle camData = get_intent.getExtras();
		String tem_camArray = camData.getString("s_tem_jsonCamArray");
		
		try {
			jsonCamArray = new JSONArray(tem_camArray);
			camArray = new String[jsonCamArray.length()];
			
			for(int i = 0;i<jsonCamArray.length();i++){
				

				JSONObject jsonObjCam = ((JSONObject) jsonCamArray.opt(i));

				String name = jsonObjCam.getString("name");
				camArray[i] = name; 	
			}
			
			
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		ListView list = (ListView) findViewById(R.id.list);
		//将数组包装为ArraAdapter
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,R.layout.array_item,camArray);
		//为ListView设置Adapter
		list.setAdapter(adapter);
		list.setOnItemClickListener(new OnItemClickListener(){

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub
				
			JSONObject jsonObjCam = (JSONObject) jsonCamArray.opt(arg2);
			String s_jsonObjCam = jsonObjCam.toString();
			jsonObj_camData.putString("jsonObjCam",s_jsonObjCam);
			
			Intent intent = new Intent(ShowCamActivity.this,PlayActivity.class);
			intent.putExtras(jsonObj_camData);
			startActivity(intent);
			}
			
		});
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		//getMenuInflater().inflate(R.menu.show_cam, menu);
		return true;
	}

}
