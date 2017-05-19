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

	public static final String TAG = "DatabaseHandler.java";

	private static final int DATABASE_VERSION = 4;

	protected static final String DATABASE_NAME = "NinjaDatabase2";

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

		sql += "CREATE TABLE " + tableName;
		sql += " ( ";
		sql += fieldObjectId + " INTEGER PRIMARY KEY AUTOINCREMENT, ";
		sql += fieldObjectName + " TEXT ";
		sql += " ) ";

		db.execSQL(sql);

	}

	/*
	 * quando o banco de dados � atualizado, isso far� com que ele mostre o que foi criado e vai atualizar.
	 */
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

		String sql = "DROP TABLE IF EXISTS " + tableName;
		db.execSQL(sql);

		onCreate(db);
	}

	public boolean create(MyObject myObj) {

		boolean createSuccessful = false;

		if (!checkIfExists(myObj.objectName)) {

			SQLiteDatabase db = this.getWritableDatabase();

			ContentValues values = new ContentValues();
			values.put(fieldObjectName, myObj.objectName);
			createSuccessful = db.insert(tableName, null, values) > 0;

			db.close();

			if (createSuccessful) {
				Log.e(TAG, myObj.objectName + " created.");
			}
		}

		return createSuccessful;
	}

	public boolean checkIfExists(String objectName) {

		boolean recordExists = false;

		SQLiteDatabase db = this.getWritableDatabase();
		Cursor cursor = db.rawQuery("SELECT " + fieldObjectId + " FROM "
				+ tableName + " WHERE " + fieldObjectName + " = '" + objectName
				+ "'", null);

		if (cursor != null) {

			if (cursor.getCount() > 0) {
				recordExists = true;
			}
		}

		cursor.close();
		db.close();

		return recordExists;
	}

	public List<MyObject> read(String searchTerm) {

		List<MyObject> recordsList = new ArrayList<MyObject>();

		// seleciona a query
		String sql = "";
		sql += "SELECT * FROM " + tableName;
		sql += " WHERE " + fieldObjectName + " LIKE '%" + searchTerm + "%'";
		sql += " ORDER BY " + fieldObjectId + " DESC";
		sql += " LIMIT 0,5";

		SQLiteDatabase db = this.getWritableDatabase();

		// executa a query
		Cursor cursor = db.rawQuery(sql, null);

		// percorre todas as listas e add
		if (cursor.moveToFirst()) {
			do {

				// int productId =
				// Integer.parseInt(cursor.getString(cursor.getColumnIndex(fieldProductId)));
				String objectName = cursor.getString(cursor
						.getColumnIndex(fieldObjectName));
				MyObject myObject = new MyObject(objectName);

				// add para a lista
				recordsList.add(myObject);

			} while (cursor.moveToNext());
		}

		cursor.close();
		db.close();

		// retorna � lista de registros
		return recordsList;
	}

}
