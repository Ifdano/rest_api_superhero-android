//Класс с данные по таблицами базы данных

package com.example.myapplication_superhero.data;

import android.provider.BaseColumns;

public final class SuperheroContract {

	//класс с данными таблицы базы данных
	public static final class SuperheroEntry implements BaseColumns{
		//название таблицы
		public static final String TABLE_NAME = "superheroes";
		
		//названия столбцов
		public static final String _ID = BaseColumns._ID;
		public static final String COLUMN_ID = "id_main";
		public static final String COLUMN_CHARACTER = "character";
		public static final String COLUMN_INTELLIGENCE = "intelligence";
		public static final String COLUMN_POWER = "power";
		public static final String COLUMN_SPEED = "speed";
		public static final String COLUMN_NAME = "name";
		public static final String COLUMN_RACE = "species";
		public static final String COLUMN_GENDER = "gender";
		public static final String COLUMN_IMAGE_URL = "avatar_url";
		public static final String COLUMN_HEIGHT = "height";
		public static final String COLUMN_WEIGHT = "weight";
		public static final String COLUMN_OCCUPATION = "occupation";
		public static final String COLUMN_APPEARANCE = "appearance";
		public static final String COLUMN_PUBLISHER = "publisher";
	}
}