package com.example.autocompletetextviewdb;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DatabaseHandler extends SQLiteOpenHelper {

	private static final int DATABASE_VERSION = 4;

	protected static final String DATABASE_NAME = "DATABASE_NAME";

	public String tableName = "locations";
	public String fieldObjectId = "id";
	public String fieldObjectName = "name";

	// construtor
	public DatabaseHandler(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {

		String sql = "";
		String sql_index = "";

		sql += "CREATE TABLE IF NOT EXISTS " + tableName;
		sql += " ( ";
		sql += fieldObjectId + " INTEGER PRIMARY KEY AUTOINCREMENT, ";
		sql += fieldObjectName + " TEXT ";
		sql += " ) ";

		sql_index += "CREATE INDEX IF NOT EXISTS objName ON "+ tableName + "(" + fieldObjectName+")";

		db.execSQL(sql);
		db.execSQL(sql_index);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

		String sql = "DROP TABLE IF EXISTS " + tableName;
		db.execSQL(sql);

		onCreate(db);
	}

	public void insert(List<String> list) {
		SQLiteDatabase db = getWritableDatabase();
		db.beginTransaction();

		for (String string : list) {
			ContentValues values = new ContentValues();
			values.put(fieldObjectName, string);
			db.insert(tableName, null, values);
		}
		db.setTransactionSuccessful();
		db.endTransaction();
	}

	public List<MyObject> read(String searchTerm) {

		List<MyObject> recordsList = new ArrayList<MyObject>();

		String sql = "";

		sql += "SELECT * FROM " + tableName;
		sql += " WHERE " + fieldObjectName + " LIKE '" + searchTerm + "%'";
		sql += " ORDER BY " + fieldObjectId + " DESC";
		sql += " LIMIT 0,5";

		SQLiteDatabase db = this.getReadableDatabase();

		Cursor cursor = db.rawQuery(sql, null);

		if (cursor.moveToFirst()) {
			do {
				// int productId =
				// Integer.parseInt(cursor.getString(cursor.getColumnIndex(fieldProductId)));
				String objectName = cursor.getString(cursor
						.getColumnIndex(fieldObjectName));
				MyObject myObject = new MyObject(objectName);

				recordsList.add(myObject);

			} while (cursor.moveToNext());
		}

		cursor.close();
		db.close();

		return recordsList;
	}
}
