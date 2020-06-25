//Класс для обновление данных выбранного персонажа по id

package com.example.myapplication_superhero;

import android.os.Bundle;

import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;

import android.view.View;
import android.view.View.OnTouchListener;
import android.view.MotionEvent;

import android.widget.Button;
import android.widget.ImageView;
import android.widget.EditText;
import android.widget.Toast;

import android.content.Intent;
import android.content.ContentValues;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.squareup.picasso.Picasso;
import com.squareup.picasso.Callback;

import com.example.myapplication_superhero.data.SuperheroContract.SuperheroEntry;
import com.example.myapplication_superhero.data.SuperheroDbHelper;

//диалоговые окна
import com.example.myapplication_superhero.dialogs.UpdateDialog;
import com.example.myapplication_superhero.dialogs.BackDialog;
import com.example.myapplication_superhero.dialogs.BackMainDialog;

public class UpdateData extends FragmentActivity implements OnTouchListener{
	/*для получения/передачи ID персонажа из базы данных, которого мы выбрали,
	  чтобы знать данные какого персонажа нужно вывести*/
	public static final String KEY_ID = "key_id";
	
	//компоненты
	private Button buttonUpdate;
	private Button buttonBack;
	private Button buttonMain;
	
	private ImageView avatarImage;
	
	private EditText editCharacter;
	private EditText editIntelligence;
	private EditText editPower;
	private EditText editSpeed;
	private EditText editName;
	private EditText editRace;
	private EditText editGender;
	private EditText editImageUrl;
	private EditText editHeight;
	private EditText editWeight;
	private EditText editOccupation;
	private EditText editApperance;
	private EditText editPublisher;
	
	private Intent intent;
	
	private SuperheroDbHelper DbHelper;
	
	//индекс персонажа, данные которого мы хотим изменить
	private int currentId;

	protected void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_update);
		
		init();
		//получение индекса персонажа для удаления
		getCharacterId();
	}
	
	//начальная инициализация
	public void init(){
		//находим компоненты
		buttonUpdate = (Button)findViewById(R.id.buttonUpdate);
		buttonBack = (Button)findViewById(R.id.buttonBack);
		buttonMain = (Button)findViewById(R.id.buttonMain);
		
		avatarImage = (ImageView)findViewById(R.id.avatarImage);
		
		editCharacter = (EditText)findViewById(R.id.editCharacter);
		editIntelligence = (EditText)findViewById(R.id.editIntelligence);
		editPower = (EditText)findViewById(R.id.editPower);
		editSpeed = (EditText)findViewById(R.id.editSpeed);
		editName = (EditText)findViewById(R.id.editName);
		editRace = (EditText)findViewById(R.id.editRace);
		editGender = (EditText)findViewById(R.id.editGender);
		editImageUrl = (EditText)findViewById(R.id.editImageUrl);
		editHeight = (EditText)findViewById(R.id.editHeight);
		editWeight = (EditText)findViewById(R.id.editWeight);
		editOccupation = (EditText)findViewById(R.id.editOccupation);
		editApperance = (EditText)findViewById(R.id.editApperance);
		editPublisher = (EditText)findViewById(R.id.editPublisher);
		
		//устанавливаем слушателей
		buttonUpdate.setOnTouchListener(this);
		buttonBack.setOnTouchListener(this);
		buttonMain.setOnTouchListener(this);
		
		//создаем базу данных
		DbHelper = new SuperheroDbHelper(this);
	}
	
	//получаем id персонажа, который мы передали в это окно
	public void getCharacterId(){
		intent = getIntent();
		
		currentId = intent.getIntExtra(KEY_ID, -1);
		
		if(currentId > 0){
			displayDatabaseInfo();
		}else{
			Toast.makeText(
					getApplicationContext(),
					"Ошибка получения ID!",
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
				SuperheroEntry.COLUMN_IMAGE_URL,
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
			int apperanceIndex = cursor.getColumnIndex(SuperheroEntry.COLUMN_APPEARANCE);
			int publisherIndex = cursor.getColumnIndex(SuperheroEntry.COLUMN_PUBLISHER);
			int imageUrlIndex = cursor.getColumnIndex(SuperheroEntry.COLUMN_IMAGE_URL);
			
			while(cursor.moveToNext()){
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
				String currentAppearance = cursor.getString(apperanceIndex);
				String currentPublisher = cursor.getString(publisherIndex);
				String currentImageUrl = cursor.getString(imageUrlIndex);
				
				//устанавливаем изображение
				Callback callback = new Callback(){
					public void onSuccess(){}
					
					public void onError(){}
				};
				
				Picasso
					.with(getApplicationContext())
					.load(currentImageUrl)
					.placeholder(R.drawable.loadingimage)
					.error(R.drawable.errorimage)
					.into(avatarImage);
				
				//устанавливаем данные в поля, чтобы потом их изменять
				editCharacter.setText(currentCharacter);
				editIntelligence.setText("" + currentIntelligence);
				editPower.setText("" + currentPower);
				editSpeed.setText("" + currentSpeed);
				editName.setText(currentName);
				editRace.setText(currentSpecies);
				editGender.setText(currentGender);
				editHeight.setText(currentHeight);
				editWeight.setText(currentWeight);
				editOccupation.setText(currentOccupation);
				editApperance.setText(currentAppearance);
				editPublisher.setText(currentPublisher);
				editImageUrl.setText(currentImageUrl);
			}
			
		}catch(Exception ex){
			
		}finally{
			db.close();
			cursor.close();
		}
	}
	
	//обновление данных персонажа в базе данных
	public void updateCharacter(){
		//берем базу данных для записи
		SQLiteDatabase db = DbHelper.getWritableDatabase();
		ContentValues values = new ContentValues();
		
		//получаем данные из полей
		String tempImageUrl = editImageUrl.getText().toString().trim();
		
		String tempCharacter = editCharacter.getText().toString().trim();
		
		/*если строка пустая, то будет ошибка при переводе в число
		поэтому заранее проверяем, чтобы поле не было пустым*/
		int tempIntelligence = 0;
		String tempIntelligenceElse = editIntelligence.getText().toString().trim();
		if(tempIntelligenceElse.length() > 0)
			tempIntelligence = Integer.parseInt(tempIntelligenceElse);
		
		int tempPower = 0;
		String tempPowerElse = editPower.getText().toString().trim();
		if(tempPowerElse.length() > 0)
			tempPower = Integer.parseInt(tempPowerElse);
		
		int tempSpeed = 0;
		String tempSpeedElse = editSpeed.getText().toString().trim();
		if(tempSpeedElse.length() > 0)
			tempSpeed = Integer.parseInt(tempSpeedElse);
		
		String tempName = editName.getText().toString().trim();
		String tempRace = editRace.getText().toString().trim();
		String tempGender = editGender.getText().toString().trim();
		String tempHeight = editHeight.getText().toString().trim();
		String tempWeight = editWeight.getText().toString().trim();
		String tempOccupation = editOccupation.getText().toString().trim();
		String tempAppearance = editApperance.getText().toString().trim();
		String tempPublisher = editPublisher.getText().toString().trim();
		
		/*если все данные полей заполнены, то обновляем данные
		  в противном случае сообщаем, что нужно заполнить все поля*/
		if(tempImageUrl.length() > 0 && tempCharacter.length() > 0 && tempIntelligenceElse.length() > 0 && 
	       tempPowerElse.length() > 0 && tempSpeedElse.length() > 0 && tempName.length() > 0 && 
		   tempRace.length() > 0 && tempGender.length() > 0 && tempHeight.length() > 0 &&
		   tempWeight.length() > 0 && tempOccupation.length() > 0 && tempAppearance.length() > 0 &&
		   tempPublisher.length() > 0){
			
			//устанавливаем обновленные данные
			values.put(SuperheroEntry.COLUMN_CHARACTER, tempCharacter);
			values.put(SuperheroEntry.COLUMN_INTELLIGENCE, tempIntelligence);
			values.put(SuperheroEntry.COLUMN_POWER, tempPower);
			values.put(SuperheroEntry.COLUMN_SPEED, tempSpeed);
			values.put(SuperheroEntry.COLUMN_NAME, tempName);
			values.put(SuperheroEntry.COLUMN_RACE, tempRace);
			values.put(SuperheroEntry.COLUMN_GENDER, tempGender);
			values.put(SuperheroEntry.COLUMN_HEIGHT, tempHeight);
			values.put(SuperheroEntry.COLUMN_WEIGHT, tempWeight);
			values.put(SuperheroEntry.COLUMN_OCCUPATION, tempOccupation);
			values.put(SuperheroEntry.COLUMN_APPEARANCE, tempAppearance);
			values.put(SuperheroEntry.COLUMN_PUBLISHER, tempPublisher);
			values.put(SuperheroEntry.COLUMN_IMAGE_URL, tempImageUrl);
			
			//далем запрос: UPDATE TABLE_NAME SET values WHERE ID = currentId
			db.update(
					SuperheroEntry.TABLE_NAME,
					values,
					SuperheroEntry._ID + "=?",
					new String[]{"" + currentId}
				);
			
			Toast.makeText(
					getApplicationContext(),
					"Обновлено!",
					Toast.LENGTH_LONG
				).show();
			
			//после обновления - возвращаемся на предыдущий экран
			intent = new Intent(this, CurrentData.class);
			
			//не забыв передать id персонажа, которого мы обновили
			intent.putExtra(KEY_ID, currentId);
			startActivity(intent);
			
		}else{
			Toast.makeText(
					getApplicationContext(),
					"Заполните все поля!",
					Toast.LENGTH_LONG
				).show();
		}
		
	}
	
	/*метод, который вызывается после подтверждения [в диалоговом окне]
	  для возврата на предыдущий экран*/
	public void setBack(){
		intent = new Intent(this, CurrentData.class);
		
		//не забываем передать id персонажа
		intent.putExtra(KEY_ID, currentId);
		
		startActivity(intent);
	}
	
	/*метод, который вызывается после подтверждения [в диалоговом окне]
	  для возврата на главный экран*/
	public void setBackMain(){
		intent = new Intent(this, Main.class);
		startActivity(intent);
	}
	
	public boolean onTouch(View view, MotionEvent event){
		if(event.getAction() == MotionEvent.ACTION_DOWN){
			//кнопка для обновления данных
			if(view == buttonUpdate){
				//диалоговое окно для подтверждения обновления
				FragmentManager manager = getSupportFragmentManager();
				UpdateDialog dialog = new UpdateDialog();
				dialog.show(manager, "update_dialog");
			}
			
			//кнопка возврата на предыдущее окно
			if(view == buttonBack){
				//диалоговое окно для подтверждения возврата
				FragmentManager manager = getSupportFragmentManager();
				BackDialog dialog = new BackDialog();
				dialog.show(manager, "back_dialog");
			}
			
			//кнопка возврата на главный экран
			if(view == buttonMain){
				//диалоговое окно для подтверждения возврата
				FragmentManager manager = getSupportFragmentManager();
				BackMainDialog dialog = new BackMainDialog();
				dialog.show(manager, "back_main_dialog");
			}
		}
		
		return false;
	}
}