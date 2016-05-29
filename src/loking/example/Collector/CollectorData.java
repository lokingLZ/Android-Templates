package loking.example.Collector;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.HashMap;

import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.Toast;

public class CollectorData {
	private static String TAG = "CollectorData";
	private SharedPreferences mSharedPreferences;
	private SharedPreferences.Editor mSPeditor;
	private DatabaseHelper mDatabaseHelper;
	private SQLiteDatabase mSQLiteDatabase;
	private HashMap<String, Object> dataMap;
	private int dataIOmode = 0;
	private long changeItemNum;
	private String dataFileName = "data", line = "";;
	private Context mContext;
	private FileOutputStream out = null;
	private BufferedWriter writer = null;
	private FileInputStream in = null;
	private BufferedReader reader = null;
	private ArrayList<HashMap<String, Object>> queryMap;
	
	public CollectorData(Context context, String dataFileName, int MODE){
		this.mContext = context;
		this.dataFileName = dataFileName;
		this.dataIOmode = MODE;
		Log.e(TAG, getClass().getSimpleName()+" CollectorData");
	}
	
	public void saveSharedPreferencesData(HashMap<String, Object> dataMap, String[] items){
		mSharedPreferences = mContext.getSharedPreferences(dataFileName, dataIOmode);
		mSPeditor = mSharedPreferences.edit();
		for(int i=0; i<items.length; i++){
			mSPeditor.putString(items[i], dataMap.get(items[i]).toString());
		}
		mSPeditor.commit();
		Log.e(TAG, getClass().getSimpleName()+" saveSharedPreferencesData");
	}
	
	public HashMap<String, Object> getSharedPreferencesData(String[] items){
		dataMap = new HashMap<String, Object>();
		for(int i=0; i<items.length; i++){
			String sTemp = mSharedPreferences.getString(items[i], null);
			dataMap.put(items[i], sTemp);
		}
		Log.e(TAG, getClass().getSimpleName()+" getSharedPreferencesData");
		return dataMap;
	}
	
	public void saveIOdata(Object inputText) {
		try {
			out = mContext.openFileOutput(dataFileName, dataIOmode);
			writer = new BufferedWriter(new OutputStreamWriter(out));
			writer.write(inputText.toString());
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (writer != null) {
					writer.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		Log.e(TAG, getClass().getSimpleName()+" saveIOdata");
	}
	
	public String loadIOdata() {
		StringBuilder content = new StringBuilder();
		try {
			in = mContext.openFileInput("data");
			reader = new BufferedReader(new InputStreamReader(in));
			while ((line = reader.readLine()) != null) {
				content.append(line);
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (reader != null) {
				try {
					reader.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		Log.e(TAG, getClass().getSimpleName()+" loadIOdata");
		return content.toString();
	}
	
	protected void createDatabases(int version, String create, String upgrade){
		mDatabaseHelper = new DatabaseHelper(mContext, dataFileName, null, version);
		mSQLiteDatabase = mDatabaseHelper.getWritableDatabase();
		Log.e(TAG, getClass().getSimpleName()+" createDatabases");
	}
	
	protected void dbExecSQL(String sql){
		mSQLiteDatabase.execSQL(sql);
	}
	
	protected ArrayList<HashMap<String,Object>> dbRawQuery(String sql, String items[]){
		Cursor cursor = mSQLiteDatabase.rawQuery(sql, null);
		return outCursor(cursor, items);
	}
	
	protected long addDatabasesData(String tableName, ContentValues values) {
		changeItemNum = mSQLiteDatabase.insert(tableName, null, values);
		return changeItemNum;
	}

	protected long updataDatabasesData(String tableName, ContentValues values, String where, String items[]) {
		changeItemNum = mSQLiteDatabase.update(tableName, values, where, items);
		return changeItemNum;
	}
	
	protected long deleteDatabasesData(String tableName, String where, String items[]) {
		changeItemNum = mSQLiteDatabase.delete(tableName, where, items);
		return changeItemNum;
	}
	
	protected ArrayList<HashMap<String,Object>> queryDatabasesData(String items[]) {
			Cursor cursor = mSQLiteDatabase.query("Book", null, null, null, null, null,
					null);
			return outCursor(cursor, items);
	}

	private ArrayList<HashMap<String,Object>> outCursor(Cursor cursor, String items[]) {
		queryMap = new ArrayList<HashMap<String,Object>>();
		if (cursor.moveToFirst()) {
			dataMap = new HashMap<String, Object>();
			do {
				for(int i=0;i<items.length;i++){
					dataMap.put(items[i], cursor.getString(cursor.getColumnIndex(items[i])).toString());
				}
				queryMap.add(dataMap);
			} while (cursor.moveToNext());
		}
		cursor.close();
		return queryMap;
	}
	
	protected void replaceDatabasesData() {
		mSQLiteDatabase.beginTransaction();
		try {
			mSQLiteDatabase.delete("Book", null, null);
//			if (true) {
//				throw new NullPointerException();
//			}
			ContentValues values = new ContentValues();
			values.put("name", "Game of Thrones");
			values.put("author", "George Martin");
			values.put("pages", 720);
			values.put("price", 20.85);
			mSQLiteDatabase.insert("Book", null, values);
			mSQLiteDatabase.setTransactionSuccessful();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			mSQLiteDatabase.endTransaction();
		}
	}

}

class DatabaseHelper extends SQLiteOpenHelper {

	public static final String CREATE_BOOK = "create table Book ("
			+ "id integer primary key autoincrement, "
			+ "author text, "
			+ "price real, "
			+ "pages integer, "
			+ "name text)";

	public static final String CREATE_CATEGORY = "create table Category ("
			+ "id integer primary key autoincrement, "
			+ "category_name text, "
			+ "category_code integer)";

	private Context mContext;

	public DatabaseHelper(Context context, String name, CursorFactory factory, int version) {
		super(context, name, factory, version);
		mContext = context;
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL(CREATE_BOOK);
		db.execSQL(CREATE_CATEGORY);
		Toast.makeText(mContext, "Create succeeded", Toast.LENGTH_SHORT).show();
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		switch (oldVersion) {
		case 1:
			db.execSQL(CREATE_CATEGORY);
			
		default:
		}
	}

}