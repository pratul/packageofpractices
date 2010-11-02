/**
 * Copyright 2010 (c) Pratul Kalia
 */


package net.agropedia.pop;

import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.provider.BaseColumns;
import android.util.Log;

public class DBManager {

	private static final String DATABASE_NAME = "pop.db";
	private static final int DATABASE_VERSION = 1;

	private static final String CREATE_TABLE_PRACTICES = "create table practices (" +
	BaseColumns._ID + " integer primary key autoincrement, " +
	"nid integer not null, "  +
	"title text not null, " +
	"created integer, " +
	"changed integer, " +
	"teaser TEXT, " +
	"body TEXT, " +
	"f_introduction TEXT, " +
	"f_climatic TEXT, " +
	"f_soil TEXT, " +
	"f_varieties TEXT, " +
	"f_cropping TEXT, " +
	"f_field_prep TEXT, " +
	"f_seed_sowing TEXT, " +
	"f_nutrient TEXT, " +
	"f_water TEXT, " +
	"f_weed TEXT, " +
	"f_disease TEXT, " +
	"f_insect TEXT, " +
	"f_harvesting TEXT, " +
	"f_yield TEXT" +
	")";

	public SQLiteDatabase db;
	private final Context ctx;
	private final DBHelper dbHelper;


	public DBManager(Context _context) {
		ctx = _context;
		dbHelper = new DBHelper(ctx, DATABASE_NAME, null, DATABASE_VERSION);

	}

	public DBManager open() throws SQLException {
		try {
			db = dbHelper.getWritableDatabase();
			Log.w("POP", "Got writable database.");
		}
		catch (SQLiteException e) {
			db = dbHelper.getReadableDatabase();
			Log.w("POP", "Got readable database.");
		}

		return this;
	}

	public void close() {
		db.close();
	}


	private static class DBHelper extends SQLiteOpenHelper {

		public DBHelper(Context context, String name, CursorFactory factory, int version) {
			super(context, name, factory, version);
		}

		// Called when no database exists on the disk.
		@Override
		public void onCreate(SQLiteDatabase db) {
			Log.w("POP", "Going to create first time database.");
			db.execSQL(CREATE_TABLE_PRACTICES);
			Log.w("POP", "First time database creation done.");
		}

		// Called when database version on disk mismatches version in the code.
		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			Log.w("POP", "Uprading database file from " + oldVersion + " to " + newVersion);

		}


	}

}