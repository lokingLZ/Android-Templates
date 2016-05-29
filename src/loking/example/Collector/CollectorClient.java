package loking.example.Collector;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.net.HttpURLConnection;
import java.net.URL;
import org.json.JSONArray;
import org.json.JSONObject;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import android.util.Log;

public class CollectorClient {
	private static String TAG = "CollectorClient";
	private HttpURLConnection mHttpURLConnection = null;
	private URL mURL = null;
	private DataOutputStream out;
	private InputStreamReader in;
	
	public CollectorClient(URL url){
		this.mURL = url;
	}
	
	private void initConnection() {
		mHttpURLConnection.setConnectTimeout(8000);//设置连接超时
		mHttpURLConnection.setReadTimeout(8000);//设置从服务器读取数据
		mHttpURLConnection.setDoInput(true);//设置允许输入
		mHttpURLConnection.setDoOutput(true);//设置允许输出
		mHttpURLConnection.setUseCaches(false);//设置不允许使用缓存
	}
	
	public void sendRequestWithHttpGETURLConnection() {
		new Thread(new Runnable() {// 开启线程来发起网络请求
			@Override
			public void run() {
				try {
					mHttpURLConnection = (HttpURLConnection) mURL.openConnection();
					mHttpURLConnection.setRequestMethod("GET");
					initConnection();
					if(mHttpURLConnection.getResponseCode()==HttpURLConnection.HTTP_OK)
					{
						Log.e(TAG, "HttpURLConnection successed");
						in = new InputStreamReader(mHttpURLConnection.getInputStream(), "utf-8");
						BufferedReader reader = new BufferedReader(in);
						StringBuilder response = new StringBuilder();
						String line;
						while ((line = reader.readLine()) != null) {
							response.append(line);
						}
					}
				} catch (Exception e) {
					e.printStackTrace();
				} finally {
					if (mHttpURLConnection != null) {
						mHttpURLConnection.disconnect();
					}
				}
			}
		}).start();
	}
	
	public void sendRequestWithHttpPOSTURLConnection(final String postStr) {
		new Thread(new Runnable() {// 开启线程来发起网络请求
			@Override
			public void run() {
				try {
					mHttpURLConnection = (HttpURLConnection) mURL.openConnection();
					mHttpURLConnection.setRequestMethod("POST");
					out = new DataOutputStream(mHttpURLConnection.getOutputStream());
					out.writeBytes(postStr);
					initConnection();
					if(mHttpURLConnection.getResponseCode()==HttpURLConnection.HTTP_OK)
					{
						Log.e(TAG, "HttpURLConnection successed");
						in = new InputStreamReader(mHttpURLConnection.getInputStream(), "utf-8");
						BufferedReader reader = new BufferedReader(in);
						StringBuilder response = new StringBuilder();
						String line;
						while ((line = reader.readLine()) != null) {
							response.append(line);
						}
					}
				} catch (Exception e) {
					e.printStackTrace();
				} finally {
					if (mHttpURLConnection != null) {
						mHttpURLConnection.disconnect();
					}
				}
			}
		}).start();
	}
	
	public void parseXMLWithPull(String xmlData) {
		try {
			XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
			XmlPullParser xmlPullParser = factory.newPullParser();
			xmlPullParser.setInput(new StringReader(xmlData));
			int eventType = xmlPullParser.getEventType();
			String id = "";
			String name = "";
			String version = "";
			while (eventType != XmlPullParser.END_DOCUMENT) {
				String nodeName = xmlPullParser.getName();
				switch (eventType) {
				// 开始解析某个结点
				case XmlPullParser.START_TAG: {
					if ("id".equals(nodeName)) {
						id = xmlPullParser.nextText();
					} else if ("name".equals(nodeName)) {
						name = xmlPullParser.nextText();
					} else if ("version".equals(nodeName)) {
						version = xmlPullParser.nextText();
					}
					break;
				}
				// 完成解析某个结点
				case XmlPullParser.END_TAG: {
					if ("app".equals(nodeName)) {
						Log.d("MainActivity", "id is " + id);
						Log.d("MainActivity", "name is " + name);
						Log.d("MainActivity", "version is " + version);
					}
					break;
				}
				default:
					break;
				}
				eventType = xmlPullParser.next();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void parseJSONWithJSONObject(String jsonData) {
		try {
			JSONArray jsonArray = new JSONArray(jsonData);
			for (int i = 0; i < jsonArray.length(); i++) {
				JSONObject jsonObject = jsonArray.getJSONObject(i);
				String id = jsonObject.getString("id");
				String name = jsonObject.getString("name");
				String version = jsonObject.getString("version");
				Log.d("MainActivity", "id is " + id);
				Log.d("MainActivity", "name is " + name);
				Log.d("MainActivity", "version is " + version);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
