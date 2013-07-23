package com.example.camera;

import java.io.IOException;
import java.io.Serializable;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.ParseException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.StrictMode;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

public class MainActivity extends Activity {

	Handler handler;
	SharedPreferences preferences;
	SharedPreferences.Editor editor;
	String s_userName;
	String s_password;
	String s_login;
	String result;
		
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()
        .detectDiskReads()
        .detectDiskWrites()
        .detectNetwork()   // or .detectAll() for all detectable problems
        .penaltyLog()
        .build());
        StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder()
        .detectLeakedSqlLiteObjects()
        .detectLeakedClosableObjects()
        .penaltyLog()
        .penaltyDeath()
        .build());
		
		
		preferences = getSharedPreferences("crazyit", MODE_PRIVATE);
		editor = preferences.edit();

		Button bn = (Button) findViewById(R.id.login);// 登录button

		final Bundle data = new Bundle(); // 创建以个bundle对象

		bn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				
				
				EditText user = (EditText) findViewById(R.id.user);
				EditText passwd = (EditText) findViewById(R.id.passwd);
				EditText server = (EditText) findViewById(R.id.server);

				String s_server = server.getText().toString();
				//s_server = s_server + ":8080/monitor2";
				
				data.putString("server", s_server);// 把服务器地址放入data，传到下个activity

				// 把服务器地址存储在SharedPreferences
				editor.putString("all_server", s_server);

				s_userName = user.getText().toString();
				s_password = passwd.getText().toString();
				s_login = "http://" + s_server + "/login.do?" + "username="
						+ s_userName + "&password=" + s_password; // 登录请求login.do，返回一个登录信息的json
				result = "";// 用于存储从中心读取的登录信息
				
				// 请求中心获取有关登录信息
				HttpGet httpRequest = new HttpGet(s_login);
				HttpClient httpClient = new DefaultHttpClient();
				HttpResponse httpResponse = null;

				try {
					httpResponse = httpClient.execute(httpRequest);
					
				} catch (ClientProtocolException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}

				if (httpResponse.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
					try {
						result = EntityUtils.toString(httpResponse.getEntity());// 存储从中心读取的数据
						data.putString("login", result);
						editor.putString("all_login", result);
						editor.commit();
						
						Intent intent = new Intent(MainActivity.this,ShowActivity.class);
						intent.putExtras(data);
						startActivity(intent);

					} catch (ParseException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}

			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

}
