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

public class CollectorData {
	private final static String TAG = "CollectorData";
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

	public SQLiteDatabase getmSQLiteDatabase() {
		return mSQLiteDatabase;
	}

	public CollectorData(Context context, String dataFileName, int MODE){
		this.mContext = context;
		this.dataFileName = dataFileName;
		this.dataIOmode = MODE;
		Log.e(TAG, "CollectorData create succeeded");
	}

	public void saveSharedPreferencesData(HashMap<String, Object> dataMap, String[] items){
		mSharedPreferences = mContext.getSharedPreferences(dataFileName, dataIOmode);
		mSPeditor = mSharedPreferences.edit();
		for(int i=0; i<items.length; i++){
			mSPeditor.putString(items[i], dataMap.get(items[i]).toString());
		}
		mSPeditor.commit();
		Log.e(TAG, "saveSharedPreferencesData succeeded");
	}

	public HashMap<String, Object> getSharedPreferencesData(String[] items){
		dataMap = new HashMap<String, Object>();
		for(int i=0; i<items.length; i++){
			String sTemp = mSharedPreferences.getString(items[i], null);
			dataMap.put(items[i], sTemp);
		}
		Log.e(TAG, "getSharedPreferencesData succeeded");
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
		Log.e(TAG, "saveIOdata succeeded");
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
		Log.e(TAG, "loadIOdata succeeded");
		return content.toString();
	}

	public void createDatabases(int version){
		mDatabaseHelper = new DatabaseHelper(mContext, dataFileName, null, version);
		mSQLiteDatabase = mDatabaseHelper.getWritableDatabase();
		mSQLiteDatabase.close();
		Log.e(TAG, "createDatabases succeeded");
	}

	public void dbExecSQL(String sql){
		mSQLiteDatabase = mDatabaseHelper.getWritableDatabase();
		mSQLiteDatabase.execSQL(sql);
		mSQLiteDatabase.close();
		Log.e(TAG, "dbExecSQL succeeded");
	}

	public ArrayList<HashMap<String,Object>> dbRawQuery(String sql, String items[]){
		mSQLiteDatabase = mDatabaseHelper.getWritableDatabase();
		Cursor cursor = mSQLiteDatabase.rawQuery(sql, null);
		mSQLiteDatabase.close();
		Log.e(TAG, "dbRawQuery succeeded");
		return outCursor(mSQLiteDatabase, cursor, items);
	}

	public long addDatabasesData(String tableName, ContentValues values) {
		mSQLiteDatabase = mDatabaseHelper.getWritableDatabase();
		changeItemNum = mSQLiteDatabase.insert(tableName, null, values);
		mSQLiteDatabase.close();
		Log.e(TAG, "addDatabasesData succeeded");
		return changeItemNum;
	}

	public long updataDatabasesData(String tableName, ContentValues values, String where) {
		mSQLiteDatabase = mDatabaseHelper.getWritableDatabase();
		changeItemNum = mSQLiteDatabase.update(tableName, values, where, null);
		mSQLiteDatabase.close();
		Log.e(TAG, "updataDatabasesData succeeded");
		return changeItemNum;
	}

	public long deleteDatabasesData(String tableName, String where) {
		mSQLiteDatabase = mDatabaseHelper.getWritableDatabase();
		changeItemNum = mSQLiteDatabase.delete(tableName, where, null);
		mSQLiteDatabase.close();
		Log.e(TAG, "deleteDatabasesData succeeded");
		return changeItemNum;
	}

	public ArrayList<HashMap<String,Object>> queryDatabasesData(String tableName, String where, String items[]) {
		mSQLiteDatabase = mDatabaseHelper.getWritableDatabase();
		Cursor cursor = mSQLiteDatabase.query(tableName, items, where, null, null, null, null);
		Log.e(TAG, "queryDatabasesData succeeded");
		return outCursor(mSQLiteDatabase, cursor, items);
	}

	private ArrayList<HashMap<String,Object>> outCursor(SQLiteDatabase sqliteDatabase, Cursor cursor, String items[]) {
		queryMap = new ArrayList<HashMap<String,Object>>();
		if (cursor.moveToFirst()) {
			do {
				dataMap = new HashMap<String, Object>();
				for(int i=0;i<items.length;i++){
					dataMap.put(items[i], cursor.getString(cursor.getColumnIndex(items[i])).toString());
				}
				queryMap.add(dataMap);
			} while (cursor.moveToNext());
		}
		cursor.close();
		sqliteDatabase.close();
		return queryMap;
	}

	public void replaceDatabasesData() {
		mSQLiteDatabase = mDatabaseHelper.getWritableDatabase();
		mSQLiteDatabase.beginTransaction();
		try {
			/* 数据库事务模板 */
			mSQLiteDatabase.setTransactionSuccessful();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			mSQLiteDatabase.endTransaction();
		}
		mSQLiteDatabase.close();
		Log.e(TAG, "replaceDatabasesData succeeded");
	}

}

class DatabaseHelper extends SQLiteOpenHelper {
	private final static String TAG = "DatabaseHelper";

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
		Log.e(TAG, "onCreate succeeded");
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		switch (oldVersion) {
		case 1:
			db.execSQL(CREATE_CATEGORY);

		default:
		}
		Log.e(TAG, "onUpgrade succeeded");
	}
}
