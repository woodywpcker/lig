package com.example.camera;

import java.io.IOException;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import android.net.Uri;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.Menu;


public class PlayActivity extends Activity {
	
	SharedPreferences preferences;
	String userId;
	String rtspRequest;
	String rtspResult;
	String mesRtsp;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//setContentView(R.layout.activity_play);
		
		preferences = getSharedPreferences("crazyit",MODE_PRIVATE);
		String all_server = preferences.getString("all_server","127.0.0.1");
		String all_login = preferences.getString("all_login", null);
		
		try {
			JSONObject jsonObj_login = new JSONObject(all_login);
			JSONObject jsonObj_Object = jsonObj_login.getJSONObject("object");
			JSONObject jsonObj_User = jsonObj_Object.getJSONObject("user");
			
			userId = jsonObj_User.getString("userId");
			System.out.println("445566"+userId);
				
		} catch (JSONException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		Intent get_intent = getIntent();
		Bundle camData = get_intent.getExtras();
		String tem_jsonCam = camData.getString("jsonObjCam");
		
		try {
			JSONObject jsonObjCam = new JSONObject(tem_jsonCam);
			
			String secondType = jsonObjCam.getString("secondType");
			String devId = jsonObjCam.getString("devId");
			String uuid = jsonObjCam.getString("uuid");
			String vip = jsonObjCam.getString("vip");
			String port = jsonObjCam.getString("vport");
			String nvr = jsonObjCam.getString("vjson");
			
			JSONObject jsonObjNvr = new JSONObject(nvr);
			String nvrName = jsonObjNvr.getString("username");
			String nvrPassWord = jsonObjNvr.getString("password");
		
			
			rtspRequest = "http://" + all_server + "/rtsp.do?" + "secondType=" + secondType + "&uuid=" + uuid + "&ip=" + vip + "&port=" + port + "&nvrName=" +
			nvrName + "&nvrPassWord=" + nvrPassWord + "&devId=" + devId + "&userId=" + userId;
			
			//System.out.println("0000"+rtspRequest);
			
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		HttpGet httpRequest = new HttpGet(rtspRequest);
		HttpClient httpClient = new DefaultHttpClient();
		HttpResponse httpResponse = null;
		
		try {
			httpResponse = httpClient.execute(httpRequest);
			
			if(httpResponse.getStatusLine().getStatusCode() == HttpStatus.SC_OK){
				
				rtspResult = EntityUtils.toString(httpResponse.getEntity());
				System.out.println("11111"+rtspResult);
			}
			
			JSONObject jsonobjRtsp = new JSONObject(rtspResult);
			mesRtsp = jsonobjRtsp.getString("message");
			System.out.println("22222"+mesRtsp);
				
			    //Intent it = new Intent(Intent.ACTION_VIEW); 
                //Intent it = new Intent("com.cooliris.media.MovieView");  
                // it.setAction(Intent.ACTION_VIEW);
                //it.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                //Uri data = Uri.parse(mesRtsp);
                //it.setDataAndType(data, "video/mp4");
               // startActivity(it);
		

			
			
			
			
			
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.play, menu);
		return true;
	}

}
