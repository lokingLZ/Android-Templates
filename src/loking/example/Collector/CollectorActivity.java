package loking.example.Collector;

import loking.example.androidtemplates.R;
import java.util.ArrayList;
import java.util.List;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.view.WindowManager;

public class CollectorActivity extends Activity {
	private static String TAG = "CollectorActivity";
	@Override/* 创建活动 */
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Log.e(TAG, getClass().getSimpleName()+" onCreate");
		ActivityCollector.addActivity(this);
	}
	
	@Override/* 销毁活动 */
	protected void onDestroy() {
		super.onDestroy();
		Log.e(TAG, getClass().getSimpleName()+" onDestroy");
		ActivityCollector.removeActivity(this);
	}
	
	@Override/* 重启活动 */
	protected void onRestart() {
		super.onRestart();
		Log.e(TAG, getClass().getSimpleName()+" onRestart");
	}
	
	@Override/* 创建菜单栏 */
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		Log.e(TAG, getClass().getSimpleName()+" onCreateOptionsMenu");
		return true;
	}
	
	@Override/* 设置菜单栏 */
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.add_item:
			Log.e(TAG, getClass().getSimpleName()+" You clicked Add");
			break;
		case R.id.remove_item:
			Log.e(TAG, getClass().getSimpleName()+" You clicked Remove");
			break;
		default:
		}
		return true;
	}
	
	@Override/* 活动摧毁时保存临时数据 */
	protected void onSaveInstanceState(Bundle outState) {
		// TODO Auto-generated method stub
		super.onSaveInstanceState(outState);
		Log.e(TAG, getClass().getSimpleName()+" onSaveInstanceState");
	}
	
	@Override/* 返回数据时启动活动 */
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
	}
	
	/* 设置状态栏和标题栏 */
	protected void setWindow(Boolean setTitle, Boolean setStatusBar){
		if(setTitle)	requestWindowFeature(Window.FEATURE_NO_TITLE);
		if(setStatusBar)	getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
	}
	
	/* 获取临时数据 */
	protected void getInstanceState(Bundle outState){
		if(outState != null){
			
		}
	}
	
	/* 请求返回值的跳转 */
	protected void startActivityResult(Class<?> activity, int requestCode){
		Intent intent = new Intent(getApplicationContext(), activity);
		startActivityForResult(intent, requestCode);
	}
	
	/* 返回返回值 */
	protected void backActivityResult(Bundle resultDate, int resultCode){
		Intent intent = new Intent();
		intent.putExtras(resultDate);
		setResult(resultCode, intent);
	}
}

class ActivityCollector {

	public static List<Activity> activities = new ArrayList<Activity>();

	public static void addActivity(Activity activity) {
		activities.add(activity);
	}

	public static void removeActivity(Activity activity) {
		activities.remove(activity);
	}

	public static void finishAll() {
		for (Activity activity : activities) {
			if (!activity.isFinishing()) {
				activity.finish();
			}
		}
	}
}

