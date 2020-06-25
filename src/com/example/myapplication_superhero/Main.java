//Главное окно со списком персонажей

package com.example.myapplication_superhero;

import android.os.Bundle;

//компоненты окна
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.EditText;

//для работы с обычным списком
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;

//обработка касаний
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.MotionEvent;

//переход на другое окно
import android.content.Intent;
//для изменения и удаления данных из базы данных
import android.content.ContentValues;

//для работы с базой данных
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;

import android.util.Log;

//подписчики
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

//для многопоточности
import io.reactivex.schedulers.Schedulers;
import io.reactivex.android.schedulers.AndroidSchedulers;

import io.reactivex.annotations.NonNull;

import java.util.ArrayList;

//для кастомного адаптера списка
import com.example.myapplication_superhero.customAdapter.CustomAdapter;
import com.example.myapplication_superhero.customAdapter.Character;

//для базы данных
import com.example.myapplication_superhero.data.SuperheroContract.SuperheroEntry;
import com.example.myapplication_superhero.data.SuperheroDbHelper;

//диалоговое окно для удаления
import com.example.myapplication_superhero.dialogs.DeleteDialog;
import com.example.myapplication_superhero.dialogs.DeleteAllDialog;

//для получения данных по api
import com.example.myapplication_superhero.characterApi.Characters;
import com.example.myapplication_superhero.characterApi.SuperheroService;
import com.example.myapplication_superhero.characterApi.Results;

//основной класс, реализующий интерфейсы обработки касаний экрана, списка и выпадающего списка 
public class Main extends FragmentActivity implements OnTouchListener, OnItemClickListener, OnItemLongClickListener{
	/*для передачи ID элемента из базы данных в другое окно,
	 чтобы мы знали данные какого персонажа нужно будет вывести на экран*/ 
	public static final String KEY_ID = "key_id";

	//кнопки полного очищения данных и удаления
	private Button buttonGetData;
	private Button buttonClearData;
	private Button buttonDeleteData;
	
	//поле для ввода имени персонажа
	private EditText editName;
	
	//список с персонажами
	private ListView listCharacters;
	//массив с данными персонажей
	private ArrayList<Character> characters;
	//кастомный адаптер
	private CustomAdapter customAdapter;
	//для хранения id элементов
	private ArrayList<Integer> charactersId;
	
	private Intent intent;
	
	//база данных
	private SuperheroDbHelper DbHelper;
	
	//id персонажа, которого нужно будет удалить
	private int characterIdDelete;
	//введенное имя персонажа, данные которого мы хотим получит
	private String nameForDisplayData;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		//начальная инициализация
		init();
	}
	
	@Override
	protected void onStart(){
		super.onStart();
		
		//обнуляем/очищаем массивы данных
		charactersId.clear();
		characters.clear();
		
		//выводим данные из базы данных на экран
		displayDatabaseInfo();
	}
	
	//начальная инициализация
	public void init(){
		//находим компоненты
		buttonGetData = (Button)findViewById(R.id.buttonGet);
		buttonClearData = (Button)findViewById(R.id.buttonClear);
		buttonDeleteData = (Button)findViewById(R.id.buttonDelete);
		
		listCharacters = (ListView)findViewById(R.id.listCharacters);
		
		editName = (EditText)findViewById(R.id.editName);
		
		//устанавливаем слушателей
		buttonGetData.setOnTouchListener(this);
		buttonClearData.setOnTouchListener(this);
		buttonDeleteData.setOnTouchListener(this);
		
		listCharacters.setOnItemClickListener(this);
		listCharacters.setOnItemLongClickListener(this);
		
		//создаем базу данных
		DbHelper = new SuperheroDbHelper(this);
		
		//создаеи массивы для хранения персонажей и их id
		characters = new ArrayList<Character>();
		charactersId = new ArrayList<Integer>();
		
		//в самом начале, нет персонажей дла удаления
		characterIdDelete = -1;
		//и нет введенного имени
		nameForDisplayData = "";
	}
	
	//для удаления элемента из базы данных
	public void deleteCharacter(){
		//берем базу данных для записи
		SQLiteDatabase db = DbHelper.getWritableDatabase();
		
		/*если у нас есть персонаж для удаления [если id > 0], 
		 то удаляем его из базы данных*/
		if(characterIdDelete > 0){
			/*удаляем персонажа по id
			 Общий запрос: DELETE FROM TABLE_NAME WHERE ID = characterIdDelete*/ 
			db.delete(
					SuperheroEntry.TABLE_NAME,
					SuperheroEntry._ID + "=?",
					new String[]{"" + characterIdDelete}
					);
		
			//обнуляем массивы, чтобы позже обновить содержимое
			characters.clear();
			charactersId.clear();
		
			//сообщаем адаптеру, что данные изменилис
			customAdapter.notifyDataSetChanged();
		
			//заново выводим обновленные данные
			displayDatabaseInfo();
			
			//сообщаем об удалении
			Toast.makeText(
					this,
					"УДАЛЕНО!",
					Toast.LENGTH_LONG
				).show();
			
			//персонажа для удаления пока нет
			characterIdDelete = -1;
		}else{
			/*при ошибке удаления, например когда мы не выбрали  
			  персонажа для удаления - выводим сообщение об ошибке*/
			Toast.makeText(
					this,
					"Ошибка удаления!",
					Toast.LENGTH_LONG
				).show();
		}
	}
	
	//полное очищение персонажей из базы данных
	public void deleteCharactersAll(){
		//берем базу данных для записи
		SQLiteDatabase db = DbHelper.getWritableDatabase();
		
		/*если в базе данных есть данные по персонажам, 
		  значит и массив с id персонажами будет заполнен*/
		if(charactersId.size() > 0){
			/*пробегаемся по всем доступным id персонажей
			  и удаляем по одному
			  Общий запрос: DELETE FROM TABLE_NAME WHERE ID = characterId.get(i)*/
			for(int i = 0; i < charactersId.size(); i++)
			db.delete(
					SuperheroEntry.TABLE_NAME,
					SuperheroEntry._ID + "=?",
					new String[]{"" + charactersId.get(i)}
					);
		
			//обнуляем массивы
			characters.clear();
			charactersId.clear();
		
			//сообщаем адаптеру, что данные изменилис
			customAdapter.notifyDataSetChanged();
		
			//заново выводим обновленные данные
			displayDatabaseInfo();
			
			//сообщаем об очищении
			Toast.makeText(
					this,
					"Данные очищены!",
					Toast.LENGTH_LONG
				).show();
		}else{
			/*сообщаем об ошибке, например, когда данных 
			  для удаления вообще нет*/
			Toast.makeText(
					this,
					"Нет данных для удаления!",
					Toast.LENGTH_LONG
				).show();
		}
	}
	
	//отображение данных персонажей из базы данных
	public void displayDatabaseInfo(){
		//берем базу данных для чтения
		SQLiteDatabase db = DbHelper.getReadableDatabase();
		
		/*данные по каким столбцам мы хотим получить?
		  в нашем случае это id по базе данных, id по api, имя персонажа
		  и ссылка на изображение */
		String[] projection = {
				SuperheroEntry._ID,
				SuperheroEntry.COLUMN_ID,
				SuperheroEntry.COLUMN_CHARACTER,
				SuperheroEntry.COLUMN_IMAGE_URL
		};
		
		//условия вывода
		Cursor cursor = db.query(
					SuperheroEntry.TABLE_NAME, 
					projection,
					null,
					null,
					null,
					null,
					null
				);
		
		try{
			
			//получаем индексы столбцов
			int idIndex = cursor.getColumnIndex(SuperheroEntry._ID);
			int idMainIndex = cursor.getColumnIndex(SuperheroEntry.COLUMN_ID);
			int nameIndex = cursor.getColumnIndex(SuperheroEntry.COLUMN_CHARACTER);
			int imageIndex = cursor.getColumnIndex(SuperheroEntry.COLUMN_IMAGE_URL);
			
			//пробегаемся по всем данным из базы данных
			while(cursor.moveToNext()){
				//получаем нужные нам данные из базы данных
				int currentId = cursor.getInt(idIndex);
				int currentIdMain = cursor.getInt(idMainIndex);
				String currentCharacter = cursor.getString(nameIndex);
				String currentImageUrl = cursor.getString(imageIndex);
				
				/*добавляем персонажа с изображением и именем в наш массив,
				  чтобы потом вывести его на экран, в виде списка*/
				characters.add(new Character(currentIdMain + ". " + currentCharacter, currentImageUrl));
				
				/*запоминаем id элементов, которые есть на экране
				  это нужно, чтобы после удаления элементов из списка - 
				  мы знали какие именно элементы остались, по id базы данных, а 
				  не по id самого списка*/
				charactersId.add(currentId);
				
				//создаем адаптер для списка
				customAdapter = new CustomAdapter(
						this,
						R.layout.new_list_item,
						characters
					);
			
				//связываем список с данными персонажей
				listCharacters.setAdapter(customAdapter);
				
				//обновляем адаптер
				customAdapter.notifyDataSetChanged();
			}
			
		}catch(Exception ex){
			
		}finally{
			//закрываем базу данных
			db.close();
			cursor.close();
		}
	}
	
	/*получение данных персонажей по api
	  вводим имя персонажа и запрашиваем данные*/
	public void getCharacterData1(){
		//данные получит наш подписчик
		Subscriber<Characters> subscriber = new Subscriber<Characters>(){
			//загрузка данных
			public void onNext(@NonNull Characters charactersAll){
				//получаем всех персонажей на странице
				Results[] results = charactersAll.getResults();
				
				//проверка, чтобы у нас были данные для вывода
				if(results != null)
					//пробегаемся по всем полученным персонажам
					for(int i = 0; i < results.length; i++){
						//берем персонажа
						Results result = results[i];
						
						//получаем данные персонажа
						int characterId = result.getId();
	
						String characterCharacter = result.getName();

						/*в самих получаемых данных Json, в некоторых местах бывает null,
						  поэтому, нужно заранее проверять: null или нет. Проверка нужна
						  только у данных типа int
						  в нашем случае это интеллект, сила и скорость*/
						int characterIntelligence;
						String characterIntelligenceTemp = result.getPowerstats().getIntelligence();
					
						if(characterIntelligenceTemp.equals("null"))
							characterIntelligence = 0;
						else
							characterIntelligence = Integer.parseInt(characterIntelligenceTemp);
					
						int characterPower;
						String characterPowerTemp = result.getPowerstats().getPower();
					
						if(characterPowerTemp.equals("null"))
							characterPower = 0;
						else
							characterPower = Integer.parseInt(characterPowerTemp);
					
						int characterSpeed;
						String characterSpeedTemp = result.getPowerstats().getSpeed();
					
						if(characterSpeedTemp.equals("null"))
							characterSpeed = 0;
						else
							characterSpeed = Integer.parseInt(characterSpeedTemp);
					
						String characterName = result.getBiography().getFullName();
						String characterFirstAppearance = result.getBiography().getFirstAppearance();
						String characterPublisher = result.getBiography().getPublisher();
					
						String characterRace = result.getAppearance().getRace();
						String characterGender = result.getAppearance().getGender();
					
						String characterHeight = result.getAppearance().getHeight()[1];
						String characterWeight = result.getAppearance().getWeight()[1];
					
						String characterOccupation = result.getWork().getOccupation();
					
						String characterImage = result.getImage().getUrl();
						
						//и сразу добавляем в базу данных
						SQLiteDatabase db = DbHelper.getWritableDatabase();
						ContentValues values = new ContentValues();
						
						values.put(SuperheroEntry.COLUMN_ID, characterId);
						values.put(SuperheroEntry.COLUMN_CHARACTER, characterCharacter);
						values.put(SuperheroEntry.COLUMN_INTELLIGENCE, characterIntelligence);
						values.put(SuperheroEntry.COLUMN_POWER, characterPower);
						values.put(SuperheroEntry.COLUMN_SPEED, characterSpeed);
						values.put(SuperheroEntry.COLUMN_NAME, characterName);
						values.put(SuperheroEntry.COLUMN_RACE, characterRace);
						values.put(SuperheroEntry.COLUMN_GENDER, characterGender);
						values.put(SuperheroEntry.COLUMN_HEIGHT, characterHeight);
						values.put(SuperheroEntry.COLUMN_WEIGHT, characterWeight);
						values.put(SuperheroEntry.COLUMN_OCCUPATION, characterOccupation);
						values.put(SuperheroEntry.COLUMN_APPEARANCE, characterFirstAppearance);
						values.put(SuperheroEntry.COLUMN_PUBLISHER, characterPublisher);
						values.put(SuperheroEntry.COLUMN_IMAGE_URL, characterImage);
						
						long newRowId = db.insert(
								SuperheroEntry.TABLE_NAME,
									null,
									values
								);
						
						//очищаем списки с персонажами и их id, для обновления
						characters.clear();
						charactersId.clear();
						
						//заново отображаем данные из базы данных
						displayDatabaseInfo();
					}else
						Toast.makeText(
								getApplicationContext(),
								"Введите корректное имя супергероя!",
								Toast.LENGTH_LONG
								).show();
			}
				
			//ошибки при загрузки данных 
			public void onError(Throwable e){
				//выводим сообщение об ошибке
				Toast.makeText(
						getApplicationContext(),
						"Ошибка загрузки данных: " + e,
						Toast.LENGTH_LONG
					).show();
				
				Log.v("SUPER HERO", "ERROR: " + e);
			}
				
			//удачное завершение загрузки данных
			public void onComplete(){
				//выводим сообщение об удачном завершении загрузки
				Toast.makeText(
						getApplicationContext(),
						"Данные будут загружены!",
						Toast.LENGTH_LONG
					).show();
			}
				
			//нужно явно запросить данные
			public void onSubscribe(Subscription s){
				s.request(Long.MAX_VALUE);
			}
		};
			
		/*запрос api, используя паттерн "Одиночка"
		  загружаем по введенному имени nameForDisplayData
		  общий запрос: https://www.superheroapi.com/api.php/894384864360671/search/batman
		  где число это ключ, который дает при регистрации*/
		SuperheroService
				.getInstance()
				.getSuperheroApi()
				.getCharacter("894384864360671", nameForDisplayData)
				.subscribeOn(Schedulers.io())
				.observeOn(AndroidSchedulers.mainThread())
				.subscribe(subscriber);
					
	}
	
	/*при долгом нажатии на персонажа из списка,
	  будем удалять персонажа из базы данных*/
	@Override
	public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id){
		//получаем id персонажа, на которого мы нажали
		characterIdDelete = charactersId.get(position);
		
		//диалоговое окно для подтверждения удаления персонажа 
		FragmentManager manager = getSupportFragmentManager();
		DeleteDialog dialog = new DeleteDialog();
		dialog.show(manager, "delete_dialog");
		
		return true;
	}
	
	/*при обычно нажатии на персонажа из списка,
	  будет сделан переход на другое окно, для отображения
	  данных по выбранному персонажу*/
	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id){
		//переход на окно CurrentData
		intent = new Intent(this, CurrentData.class);
		
		//получаем id персонажа из списка, на которого мы нажали
		int characterId = charactersId.get(position);
		
		/*передаем в другое окно id персонажа [из базы данных]
		  чтобы знать данные какого персонажа выводить*/
		intent.putExtra(KEY_ID, characterId);
		
		startActivity(intent);
	}
	
	//обработка касаний экрана
	@Override
	public boolean onTouch(View view, MotionEvent event){
		
		if(event.getAction() == MotionEvent.ACTION_DOWN){
			//кнопка загрузки данных супергероя
			if(view == buttonGetData){
				nameForDisplayData = editName.getText().toString().trim();
				
				if(nameForDisplayData.length() > 0){
					getCharacterData1();
					nameForDisplayData = "";
				}else{
					Toast.makeText(
							this,
							"Введите имя супергероя",
							Toast.LENGTH_LONG
						).show();
				}
			}
			
			//кнопка очищения данных из базы данных
			if(view == buttonClearData){
				//диалоговое окно для подтверждения очистики базы данных
				FragmentManager manager = getSupportFragmentManager();
				DeleteAllDialog dialog = new DeleteAllDialog();
				dialog.show(manager, "delete_dialog_all");
				
				/*можно полностью удалить базу данных и создать новую
				  SQLiteDatabase db = DbHelper.getWritableDatabase();
				  DbHelper.onUpgrade(db, 1, 1);
				
				  charactersId.clear();
				  characters.clear();
				
				  displayDatabaseInfo();*/
			}
			
			//кнопка удаления персонажа
			if(view == buttonDeleteData){
				//выводим сообщение с указаниями для удаления
				Toast.makeText(
							this,
							"Для удаление элемента - удерживайте его нажатым",
							Toast.LENGTH_LONG
						).show();

			}
		}
		
		return false;
	}
}