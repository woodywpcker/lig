package com.example.camera;

import java.io.IOException;
import java.util.Iterator;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.ParseException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.os.Bundle;

public class ShowActivity extends Activity {
	
	JSONArray jsonObjNvrArray;
	String urlStr = "";//��������ͷ��Ϣhttp://192.168.3.136/readDevTreePerm1.do?orgId=2&userId=1
	String camList = "";//����ͷ�б��ַ���
	String server = "";//�������ַ���
	String login = "" ;//��¼��Ϣ�ַ���
	String[] nvrArray ;//nvr�洢����
	String[] camArray ;//�洢����ͷ����
	Bundle camData = new Bundle(); //�����Ը�bundle����
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.listview);
	
		Intent intent = getIntent();
		Bundle data = intent.getExtras();
		
		server = data.getString("server");//���MainActivity�����ķ�������ַ
		login = data.getString("login");//���MainActivity�����ĵ�¼��Ϣ
		
		try {
			JSONObject jsonObj_login = new JSONObject(login);
			JSONObject jsonObj_Object = jsonObj_login.getJSONObject("object");
			JSONObject jsonObj_User = jsonObj_Object.getJSONObject("user");
			
			String orgId = jsonObj_User.getString("orgId");
			String role = jsonObj_User.getString("role");
			
			urlStr = "http://" + server + "/readDevTreePerm1.do?" + "orgId=" + orgId + "&" + "role=" + role;
				    
			} catch (JSONException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		HttpGet httpRequest = new HttpGet(urlStr);
		HttpClient httpClient = new DefaultHttpClient();
		HttpResponse httpResponse = null;

		try {
			httpResponse = httpClient.execute(httpRequest);
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		if (httpResponse.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
			try {
				camList = EntityUtils.toString(httpResponse.getEntity());
				
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		//����nvr��json
		try {
			JSONObject jsonObj = new JSONObject(camList); 
			boolean bool = jsonObj.getBoolean("ok");
			String message = jsonObj.getString("message");
			JSONObject secondObj = jsonObj.getJSONObject("object");

			jsonObjNvrArray = secondObj.getJSONArray("children");// ��ȡnvr������
			nvrArray = new String [jsonObjNvrArray.length()];//Ϊnv�洢�������ռ�
		
			for (int j = 0; j < jsonObjNvrArray.length(); j++) {

				JSONObject jsonObjNvr = ((JSONObject) jsonObjNvrArray.opt(j));
				nvrArray[j] = jsonObjNvr.getString("name");

			}
		} catch (JSONException e) {
			System.out.println("Json parse error");
			e.printStackTrace();
		}
		
		ListView list = (ListView) findViewById(R.id.list);
		//�������װΪArraAdapter
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,R.layout.array_item,nvrArray);
		//ΪListView����Adapter
		list.setAdapter(adapter);	
		list.setOnItemClickListener(new OnItemClickListener()
		{

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub
				
				JSONArray tem_jsonCamArray = null;
				try {
					tem_jsonCamArray = jsonObjNvrArray.getJSONObject(arg2).getJSONArray("children");
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				String s_tem_jsonCamArray = tem_jsonCamArray.toString();	
				camData.putString("s_tem_jsonCamArray",s_tem_jsonCamArray);	
				
				Intent intent = new Intent(ShowActivity.this,ShowCamActivity.class);
				intent.putExtras(camData);
				startActivity(intent);
			
			}
			
		}
		);
	}
}
	/*
	 * ��������ͷ��Ϣ��JSON
	 */
	/*
	private void parseCamListJson(String strResult) {
		
		
	try {
			JSONObject jsonObj = new JSONObject(strResult); // //
			boolean bool = jsonObj.getBoolean("ok");
			String message = jsonObj.getString("message");
			JSONObject secondObj = jsonObj.getJSONObject("object");

			JSONArray jsonObjNvrArray = secondObj.getJSONArray("children");// ��ȡnvr������
		
			for (int j = 0,c=0; j < jsonObjNvrArray.length(); j++) {

				JSONArray jsonObjCamArray = jsonObjNvrArray.getJSONObject(j)
						.getJSONArray("children"); // ��ȡ��һ��nvr���飬0��1��

				for (int i = 0; i < jsonObjCamArray.length(); i++,c++) {

					
					JSONObject jsonObjCam = ((JSONObject) jsonObjCamArray.opt(i));

					String devId = jsonObjCam.getString("devId");
					camArray[c] = devId; 
					System.out.println(camArray[c]);
					System.out.println(devId);

				}
			}
		
	} catch (JSONException e) {
			System.out.println("Json parse error");
			e.printStackTrace();
		}
		

	}
	*/


