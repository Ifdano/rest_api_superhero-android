//Класс для отображения данных конкретного персонажа, по id

package com.example.myapplication_superhero;

import android.os.Bundle;

import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;

import android.view.View;
import android.view.MotionEvent;
import android.view.View.OnTouchListener;

//компоненты окна
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

//для базы данных
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import android.content.Intent;

//загрузка изображений с Picasso
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Callback;

import com.example.myapplication_superhero.data.SuperheroContract.SuperheroEntry;
import com.example.myapplication_superhero.data.SuperheroDbHelper;

//диалоговое окно для удаления персонажа
import com.example.myapplication_superhero.dialogs.DeleteDialogCurrent;

public class CurrentData extends FragmentActivity implements OnTouchListener{
	/*для получения/передачи ID персонажа из базы данных, которого мы выбрали,
	  чтобы знать данные какого персонажа нужно вывести*/
	public static final String KEY_ID = "key_id";
	
	//компоненты
	private Button buttonUpdate;
	private Button buttonDelete;
	private Button buttonBack;
	
	private ImageView avatarImage;
	
	private TextView textCharacter;
	private TextView textIntelligence;
	private TextView textPower;
	private TextView textSpeed;
	private TextView textName;
	private TextView textRace;
	private TextView textGender;
	private TextView textHeight;
	private TextView textWeight;
	private TextView textOccupation;
	private TextView textApperance;
	private TextView textPublisher;
	
	private Intent intent;
	
	//индекс персонажа, который мы выбрали для удаления
	private int currentId;
	
	//база данных
	private SuperheroDbHelper DbHelper;
	
	protected void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_current);
		
		init();
		//получение индекса персонажа для удаления
		getCharacterId();
	}
	
	//начальная инициализация
	public void init(){
		//находим компоненты
		buttonUpdate = (Button)findViewById(R.id.buttonUpdate);
		buttonDelete = (Button)findViewById(R.id.buttonDelete);
		buttonBack = (Button)findViewById(R.id.buttonBack);
		
		avatarImage = (ImageView)findViewById(R.id.avatarImage);
		
		textCharacter = (TextView)findViewById(R.id.textCharacter);
		textIntelligence = (TextView)findViewById(R.id.textIntelligence);
		textPower = (TextView)findViewById(R.id.textPower);
		textSpeed = (TextView)findViewById(R.id.textSpeed);
		textName = (TextView)findViewById(R.id.textName);
		textRace = (TextView)findViewById(R.id.textRace);
		textGender = (TextView)findViewById(R.id.textGender);
		textHeight = (TextView)findViewById(R.id.textHeight);
		textWeight = (TextView)findViewById(R.id.textWeight);
		textOccupation = (TextView)findViewById(R.id.textOccupation);
		textApperance = (TextView)findViewById(R.id.textApperance);
		textPublisher = (TextView)findViewById(R.id.textPublisher);
		
		//устанавливаем слушателей
		buttonUpdate.setOnTouchListener(this);
		buttonDelete.setOnTouchListener(this);
		buttonBack.setOnTouchListener(this);
		
		//создаем базу данных
		DbHelper = new SuperheroDbHelper(this);
	}
	
	//получаем id персонажа, который мы передали в это окно
	public void getCharacterId(){
		intent = getIntent();
		
		//получаем id
		currentId = intent.getIntExtra(KEY_ID, -1);
		
		/*если мы получили id, то вызываем метод для вывода данных из
		 базы данных на экран*/
		if(currentId > 0){
			displayDatabaseInfo();
		}else{
			//если id не получено - сообщаем об этом
			Toast.makeText(
					getApplicationContext(),
					"Ошибка получения ID",
					Toast.LENGTH_LONG
				).show();
		}
	}
	
	//отображение данных персонажа из базы данных
	public void displayDatabaseInfo(){
		SQLiteDatabase db = DbHelper.getReadableDatabase();
		
		String[] projection = {
				SuperheroEntry.COLUMN_CHARACTER,
				SuperheroEntry.COLUMN_INTELLIGENCE,
				SuperheroEntry.COLUMN_POWER,
				SuperheroEntry.COLUMN_SPEED,
				SuperheroEntry.COLUMN_NAME,
				SuperheroEntry.COLUMN_RACE,
				SuperheroEntry.COLUMN_GENDER,
				SuperheroEntry.COLUMN_HEIGHT,
				SuperheroEntry.COLUMN_WEIGHT,
				SuperheroEntry.COLUMN_OCCUPATION,
				SuperheroEntry.COLUMN_APPEARANCE,
				SuperheroEntry.COLUMN_PUBLISHER,
				SuperheroEntry.COLUMN_IMAGE_URL
		};
		
		/*выборка из базы данных
		  в нашем случае, мы находим данные персонажа по id*/
		String selection = SuperheroEntry._ID + "=?";
		String[] selectionArgs = {"" + currentId};
		
		Cursor cursor = db.query(
				SuperheroEntry.TABLE_NAME,
				projection,
				selection,
				selectionArgs,
				null,
				null,
				null
			);
		
		try{
			
			int characterIndex = cursor.getColumnIndex(SuperheroEntry.COLUMN_CHARACTER);
			int intelligenceIndex = cursor.getColumnIndex(SuperheroEntry.COLUMN_INTELLIGENCE);
			int powerIndex = cursor.getColumnIndex(SuperheroEntry.COLUMN_POWER);
			int speedIndex = cursor.getColumnIndex(SuperheroEntry.COLUMN_SPEED);
			int nameIndex = cursor.getColumnIndex(SuperheroEntry.COLUMN_NAME);
			int speciesIndex = cursor.getColumnIndex(SuperheroEntry.COLUMN_RACE);
			int genderIndex = cursor.getColumnIndex(SuperheroEntry.COLUMN_GENDER);
			int heightIndex = cursor.getColumnIndex(SuperheroEntry.COLUMN_HEIGHT);
			int weightIndex = cursor.getColumnIndex(SuperheroEntry.COLUMN_WEIGHT);	
			int occupationIndex = cursor.getColumnIndex(SuperheroEntry.COLUMN_OCCUPATION);
			int appearanceIndex = cursor.getColumnIndex(SuperheroEntry.COLUMN_APPEARANCE);
			int publisherIndex = cursor.getColumnIndex(SuperheroEntry.COLUMN_PUBLISHER);
			int imageUrlIndex = cursor.getColumnIndex(SuperheroEntry.COLUMN_IMAGE_URL);
			
			while(cursor.moveToNext()){
				//получаем все данные из базы данных
				String currentCharacter = cursor.getString(characterIndex);
				int currentIntelligence = cursor.getInt(intelligenceIndex);
				int currentPower = cursor.getInt(powerIndex);
				int currentSpeed = cursor.getInt(speedIndex);
				String currentName = cursor.getString(nameIndex);
				String currentSpecies = cursor.getString(speciesIndex);
				String currentGender = cursor.getString(genderIndex);
				String currentHeight = cursor.getString(heightIndex);
				String currentWeight = cursor.getString(weightIndex);
				String currentOccupation = cursor.getString(occupationIndex);
				String currentAppearance = cursor.getString(appearanceIndex);
				String currentPublisher = cursor.getString(publisherIndex);
				String currentImageUrl = cursor.getString(imageUrlIndex);
				
				//устанавливаем изображение
				Callback callback = new Callback(){
					public void onSuccess(){}
					
					public void onError(){}
				};
				
				//загружаем изображение по ссылке
				Picasso
					.with(getApplicationContext())
					.load(currentImageUrl)
					.placeholder(R.drawable.loadingimage)
					.error(R.drawable.errorimage)
					.into(avatarImage);
				
				//устанавливаем данные в поля
				textCharacter.setText(currentCharacter);
				textIntelligence.setText("" + currentIntelligence);
				textPower.setText("" + currentPower);
				textSpeed.setText("" + currentSpeed);
				textName.setText(currentName);
				textRace.setText(currentSpecies);
				textGender.setText(currentGender);
				textHeight.setText(currentHeight);
				textWeight.setText(currentWeight);
				textOccupation.setText(currentOccupation);
				textApperance.setText(currentAppearance);
				textPublisher.setText(currentPublisher);
			}
			
		}catch(Exception ex){
			
		}finally{
			db.close();
			cursor.close();
		}
	}
	
	//удаление персонажа
	public void deleteCharacter(){
		SQLiteDatabase db = DbHelper.getWritableDatabase();
		
		//удаляем по id персонажа
		db.delete(
				SuperheroEntry.TABLE_NAME,
				SuperheroEntry._ID + "=?",
				new String[]{"" + currentId}
			);
		
		Toast.makeText(
					getApplicationContext(),
					"УДАЛЕНО!",
					Toast.LENGTH_LONG
				).show();
		
		//после удаления - возвращаемся в главное окно
		intent = new Intent(this, Main.class);
		startActivity(intent);
	}
	
	public boolean onTouch(View view, MotionEvent event){
		
		if(event.getAction() == MotionEvent.ACTION_DOWN){
			//кнопка обновления данных персонажа
			if(view == buttonUpdate){
				//переходим в окно UpdateData
				intent = new Intent(this, UpdateData.class);
				
				//передаем id персонажа, данные которого мы хотим обновить
				intent.putExtra(KEY_ID, currentId);
				
				startActivity(intent);
			}
			
			//кнопка удаления персонажа
			if(view == buttonDelete){
				//диалоговое окно для подтверждения удаления
				FragmentManager manager = getSupportFragmentManager();
				DeleteDialogCurrent dialog = new DeleteDialogCurrent();
				dialog.show(manager, "delete_dialog_current");
			}
			
			//кнопка возвращения на главный экран
			if(view == buttonBack){
				intent = new Intent(this, Main.class);
				startActivity(intent);
			}
		}
		
		return false;
	}
}