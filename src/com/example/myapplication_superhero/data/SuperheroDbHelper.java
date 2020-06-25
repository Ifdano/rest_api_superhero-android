//����� ��� �������� ���� ������

package com.example.myapplication_superhero.data;

import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import android.content.Context;

import com.example.myapplication_superhero.data.SuperheroContract.SuperheroEntry;

public class SuperheroDbHelper extends SQLiteOpenHelper{
	//�������� ���� ������
	public static final String DATABASE_NAME = "superheroes.db";
	//������
	public static final int DATABASE_VERSION = 1;
	
	//������ �� �������� ������� � ���� ������
	public static final String SQL_TABLE = "CREATE TABLE " + 
			SuperheroEntry.TABLE_NAME + "(" +
			SuperheroEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
			SuperheroEntry.COLUMN_ID + " INTEGER NOT NULL, " +
			SuperheroEntry.COLUMN_CHARACTER + " TEXT NOT NULL, " +
			SuperheroEntry.COLUMN_INTELLIGENCE + " INTEGER NOT NULL, " +
			SuperheroEntry.COLUMN_POWER + " INTEGER NOT NULL, " +
			SuperheroEntry.COLUMN_SPEED + " INTEGER NOT NULL, " +
			SuperheroEntry.COLUMN_NAME + " TEXT NOT NULL, " +
			SuperheroEntry.COLUMN_RACE + " TEXT NOT NULL, " +
			SuperheroEntry.COLUMN_GENDER + " TEXT NOT NULL, " +
			SuperheroEntry.COLUMN_HEIGHT + " TEXT NOT NULL, " +
			SuperheroEntry.COLUMN_WEIGHT + " TEXT NOT NULL, " +
			SuperheroEntry.COLUMN_OCCUPATION + " TEXT NOT NULL, " +
			SuperheroEntry.COLUMN_APPEARANCE + " TEXT NOT NULL, " +
			SuperheroEntry.COLUMN_PUBLISHER + " TEXT NOT NULL, " +
			SuperheroEntry.COLUMN_IMAGE_URL + " TEXT NOT NULL);";
			;
	
	public SuperheroDbHelper(Context context){
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}
	
	@Override
	public void onCreate(SQLiteDatabase db){
		//������� �������
		db.execSQL(SQL_TABLE);
	}

	//���������� ���� ������
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion){
		//������� �������
		db.execSQL("DROP TABLE IF EXISTS " + SuperheroEntry.TABLE_NAME + ";");
		//������� �����
		onCreate(db);
	}
}