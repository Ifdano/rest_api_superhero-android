//������� ���� �� ������� ����������

package com.example.myapplication_superhero;

import android.os.Bundle;

//���������� ����
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.EditText;

//��� ������ � ������� �������
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;

//��������� �������
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.MotionEvent;

//������� �� ������ ����
import android.content.Intent;
//��� ��������� � �������� ������ �� ���� ������
import android.content.ContentValues;

//��� ������ � ����� ������
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;

import android.util.Log;

//����������
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

//��� ���������������
import io.reactivex.schedulers.Schedulers;
import io.reactivex.android.schedulers.AndroidSchedulers;

import io.reactivex.annotations.NonNull;

import java.util.ArrayList;

//��� ���������� �������� ������
import com.example.myapplication_superhero.customAdapter.CustomAdapter;
import com.example.myapplication_superhero.customAdapter.Character;

//��� ���� ������
import com.example.myapplication_superhero.data.SuperheroContract.SuperheroEntry;
import com.example.myapplication_superhero.data.SuperheroDbHelper;

//���������� ���� ��� ��������
import com.example.myapplication_superhero.dialogs.DeleteDialog;
import com.example.myapplication_superhero.dialogs.DeleteAllDialog;

//��� ��������� ������ �� api
import com.example.myapplication_superhero.characterApi.Characters;
import com.example.myapplication_superhero.characterApi.SuperheroService;
import com.example.myapplication_superhero.characterApi.Results;

//�������� �����, ����������� ���������� ��������� ������� ������, ������ � ����������� ������ 
public class Main extends FragmentActivity implements OnTouchListener, OnItemClickListener, OnItemLongClickListener{
	/*��� �������� ID �������� �� ���� ������ � ������ ����,
	 ����� �� ����� ������ ������ ��������� ����� ����� ������� �� �����*/ 
	public static final String KEY_ID = "key_id";

	//������ ������� �������� ������ � ��������
	private Button buttonGetData;
	private Button buttonClearData;
	private Button buttonDeleteData;
	
	//���� ��� ����� ����� ���������
	private EditText editName;
	
	//������ � �����������
	private ListView listCharacters;
	//������ � ������� ����������
	private ArrayList<Character> characters;
	//��������� �������
	private CustomAdapter customAdapter;
	//��� �������� id ���������
	private ArrayList<Integer> charactersId;
	
	private Intent intent;
	
	//���� ������
	private SuperheroDbHelper DbHelper;
	
	//id ���������, �������� ����� ����� �������
	private int characterIdDelete;
	//��������� ��� ���������, ������ �������� �� ����� �������
	private String nameForDisplayData;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		//��������� �������������
		init();
	}
	
	@Override
	protected void onStart(){
		super.onStart();
		
		//��������/������� ������� ������
		charactersId.clear();
		characters.clear();
		
		//������� ������ �� ���� ������ �� �����
		displayDatabaseInfo();
	}
	
	//��������� �������������
	public void init(){
		//������� ����������
		buttonGetData = (Button)findViewById(R.id.buttonGet);
		buttonClearData = (Button)findViewById(R.id.buttonClear);
		buttonDeleteData = (Button)findViewById(R.id.buttonDelete);
		
		listCharacters = (ListView)findViewById(R.id.listCharacters);
		
		editName = (EditText)findViewById(R.id.editName);
		
		//������������� ����������
		buttonGetData.setOnTouchListener(this);
		buttonClearData.setOnTouchListener(this);
		buttonDeleteData.setOnTouchListener(this);
		
		listCharacters.setOnItemClickListener(this);
		listCharacters.setOnItemLongClickListener(this);
		
		//������� ���� ������
		DbHelper = new SuperheroDbHelper(this);
		
		//������� ������� ��� �������� ���������� � �� id
		characters = new ArrayList<Character>();
		charactersId = new ArrayList<Integer>();
		
		//� ����� ������, ��� ���������� ��� ��������
		characterIdDelete = -1;
		//� ��� ���������� �����
		nameForDisplayData = "";
	}
	
	//��� �������� �������� �� ���� ������
	public void deleteCharacter(){
		//����� ���� ������ ��� ������
		SQLiteDatabase db = DbHelper.getWritableDatabase();
		
		/*���� � ��� ���� �������� ��� �������� [���� id > 0], 
		 �� ������� ��� �� ���� ������*/
		if(characterIdDelete > 0){
			/*������� ��������� �� id
			 ����� ������: DELETE FROM TABLE_NAME WHERE ID = characterIdDelete*/ 
			db.delete(
					SuperheroEntry.TABLE_NAME,
					SuperheroEntry._ID + "=?",
					new String[]{"" + characterIdDelete}
					);
		
			//�������� �������, ����� ����� �������� ����������
			characters.clear();
			charactersId.clear();
		
			//�������� ��������, ��� ������ ���������
			customAdapter.notifyDataSetChanged();
		
			//������ ������� ����������� ������
			displayDatabaseInfo();
			
			//�������� �� ��������
			Toast.makeText(
					this,
					"�������!",
					Toast.LENGTH_LONG
				).show();
			
			//��������� ��� �������� ���� ���
			characterIdDelete = -1;
		}else{
			/*��� ������ ��������, �������� ����� �� �� �������  
			  ��������� ��� �������� - ������� ��������� �� ������*/
			Toast.makeText(
					this,
					"������ ��������!",
					Toast.LENGTH_LONG
				).show();
		}
	}
	
	//������ �������� ���������� �� ���� ������
	public void deleteCharactersAll(){
		//����� ���� ������ ��� ������
		SQLiteDatabase db = DbHelper.getWritableDatabase();
		
		/*���� � ���� ������ ���� ������ �� ����������, 
		  ������ � ������ � id ����������� ����� ��������*/
		if(charactersId.size() > 0){
			/*����������� �� ���� ��������� id ����������
			  � ������� �� ������
			  ����� ������: DELETE FROM TABLE_NAME WHERE ID = characterId.get(i)*/
			for(int i = 0; i < charactersId.size(); i++)
			db.delete(
					SuperheroEntry.TABLE_NAME,
					SuperheroEntry._ID + "=?",
					new String[]{"" + charactersId.get(i)}
					);
		
			//�������� �������
			characters.clear();
			charactersId.clear();
		
			//�������� ��������, ��� ������ ���������
			customAdapter.notifyDataSetChanged();
		
			//������ ������� ����������� ������
			displayDatabaseInfo();
			
			//�������� �� ��������
			Toast.makeText(
					this,
					"������ �������!",
					Toast.LENGTH_LONG
				).show();
		}else{
			/*�������� �� ������, ��������, ����� ������ 
			  ��� �������� ������ ���*/
			Toast.makeText(
					this,
					"��� ������ ��� ��������!",
					Toast.LENGTH_LONG
				).show();
		}
	}
	
	//����������� ������ ���������� �� ���� ������
	public void displayDatabaseInfo(){
		//����� ���� ������ ��� ������
		SQLiteDatabase db = DbHelper.getReadableDatabase();
		
		/*������ �� ����� �������� �� ����� ��������?
		  � ����� ������ ��� id �� ���� ������, id �� api, ��� ���������
		  � ������ �� ����������� */
		String[] projection = {
				SuperheroEntry._ID,
				SuperheroEntry.COLUMN_ID,
				SuperheroEntry.COLUMN_CHARACTER,
				SuperheroEntry.COLUMN_IMAGE_URL
		};
		
		//������� ������
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
			
			//�������� ������� ��������
			int idIndex = cursor.getColumnIndex(SuperheroEntry._ID);
			int idMainIndex = cursor.getColumnIndex(SuperheroEntry.COLUMN_ID);
			int nameIndex = cursor.getColumnIndex(SuperheroEntry.COLUMN_CHARACTER);
			int imageIndex = cursor.getColumnIndex(SuperheroEntry.COLUMN_IMAGE_URL);
			
			//����������� �� ���� ������ �� ���� ������
			while(cursor.moveToNext()){
				//�������� ������ ��� ������ �� ���� ������
				int currentId = cursor.getInt(idIndex);
				int currentIdMain = cursor.getInt(idMainIndex);
				String currentCharacter = cursor.getString(nameIndex);
				String currentImageUrl = cursor.getString(imageIndex);
				
				/*��������� ��������� � ������������ � ������ � ��� ������,
				  ����� ����� ������� ��� �� �����, � ���� ������*/
				characters.add(new Character(currentIdMain + ". " + currentCharacter, currentImageUrl));
				
				/*���������� id ���������, ������� ���� �� ������
				  ��� �����, ����� ����� �������� ��������� �� ������ - 
				  �� ����� ����� ������ �������� ��������, �� id ���� ������, � 
				  �� �� id ������ ������*/
				charactersId.add(currentId);
				
				//������� ������� ��� ������
				customAdapter = new CustomAdapter(
						this,
						R.layout.new_list_item,
						characters
					);
			
				//��������� ������ � ������� ����������
				listCharacters.setAdapter(customAdapter);
				
				//��������� �������
				customAdapter.notifyDataSetChanged();
			}
			
		}catch(Exception ex){
			
		}finally{
			//��������� ���� ������
			db.close();
			cursor.close();
		}
	}
	
	/*��������� ������ ���������� �� api
	  ������ ��� ��������� � ����������� ������*/
	public void getCharacterData1(){
		//������ ������� ��� ���������
		Subscriber<Characters> subscriber = new Subscriber<Characters>(){
			//�������� ������
			public void onNext(@NonNull Characters charactersAll){
				//�������� ���� ���������� �� ��������
				Results[] results = charactersAll.getResults();
				
				//��������, ����� � ��� ���� ������ ��� ������
				if(results != null)
					//����������� �� ���� ���������� ����������
					for(int i = 0; i < results.length; i++){
						//����� ���������
						Results result = results[i];
						
						//�������� ������ ���������
						int characterId = result.getId();
	
						String characterCharacter = result.getName();

						/*� ����� ���������� ������ Json, � ��������� ������ ������ null,
						  �������, ����� ������� ���������: null ��� ���. �������� �����
						  ������ � ������ ���� int
						  � ����� ������ ��� ���������, ���� � ��������*/
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
						
						//� ����� ��������� � ���� ������
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
						
						//������� ������ � ����������� � �� id, ��� ����������
						characters.clear();
						charactersId.clear();
						
						//������ ���������� ������ �� ���� ������
						displayDatabaseInfo();
					}else
						Toast.makeText(
								getApplicationContext(),
								"������� ���������� ��� ����������!",
								Toast.LENGTH_LONG
								).show();
			}
				
			//������ ��� �������� ������ 
			public void onError(Throwable e){
				//������� ��������� �� ������
				Toast.makeText(
						getApplicationContext(),
						"������ �������� ������: " + e,
						Toast.LENGTH_LONG
					).show();
				
				Log.v("SUPER HERO", "ERROR: " + e);
			}
				
			//������� ���������� �������� ������
			public void onComplete(){
				//������� ��������� �� ������� ���������� ��������
				Toast.makeText(
						getApplicationContext(),
						"������ ����� ���������!",
						Toast.LENGTH_LONG
					).show();
			}
				
			//����� ���� ��������� ������
			public void onSubscribe(Subscription s){
				s.request(Long.MAX_VALUE);
			}
		};
			
		/*������ api, ��������� ������� "��������"
		  ��������� �� ���������� ����� nameForDisplayData
		  ����� ������: https://www.superheroapi.com/api.php/894384864360671/search/batman
		  ��� ����� ��� ����, ������� ���� ��� �����������*/
		SuperheroService
				.getInstance()
				.getSuperheroApi()
				.getCharacter("894384864360671", nameForDisplayData)
				.subscribeOn(Schedulers.io())
				.observeOn(AndroidSchedulers.mainThread())
				.subscribe(subscriber);
					
	}
	
	/*��� ������ ������� �� ��������� �� ������,
	  ����� ������� ��������� �� ���� ������*/
	@Override
	public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id){
		//�������� id ���������, �� �������� �� ������
		characterIdDelete = charactersId.get(position);
		
		//���������� ���� ��� ������������� �������� ��������� 
		FragmentManager manager = getSupportFragmentManager();
		DeleteDialog dialog = new DeleteDialog();
		dialog.show(manager, "delete_dialog");
		
		return true;
	}
	
	/*��� ������ ������� �� ��������� �� ������,
	  ����� ������ ������� �� ������ ����, ��� �����������
	  ������ �� ���������� ���������*/
	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id){
		//������� �� ���� CurrentData
		intent = new Intent(this, CurrentData.class);
		
		//�������� id ��������� �� ������, �� �������� �� ������
		int characterId = charactersId.get(position);
		
		/*�������� � ������ ���� id ��������� [�� ���� ������]
		  ����� ����� ������ ������ ��������� ��������*/
		intent.putExtra(KEY_ID, characterId);
		
		startActivity(intent);
	}
	
	//��������� ������� ������
	@Override
	public boolean onTouch(View view, MotionEvent event){
		
		if(event.getAction() == MotionEvent.ACTION_DOWN){
			//������ �������� ������ ����������
			if(view == buttonGetData){
				nameForDisplayData = editName.getText().toString().trim();
				
				if(nameForDisplayData.length() > 0){
					getCharacterData1();
					nameForDisplayData = "";
				}else{
					Toast.makeText(
							this,
							"������� ��� ����������",
							Toast.LENGTH_LONG
						).show();
				}
			}
			
			//������ �������� ������ �� ���� ������
			if(view == buttonClearData){
				//���������� ���� ��� ������������� �������� ���� ������
				FragmentManager manager = getSupportFragmentManager();
				DeleteAllDialog dialog = new DeleteAllDialog();
				dialog.show(manager, "delete_dialog_all");
				
				/*����� ��������� ������� ���� ������ � ������� �����
				  SQLiteDatabase db = DbHelper.getWritableDatabase();
				  DbHelper.onUpgrade(db, 1, 1);
				
				  charactersId.clear();
				  characters.clear();
				
				  displayDatabaseInfo();*/
			}
			
			//������ �������� ���������
			if(view == buttonDeleteData){
				//������� ��������� � ���������� ��� ��������
				Toast.makeText(
							this,
							"��� �������� �������� - ����������� ��� �������",
							Toast.LENGTH_LONG
						).show();

			}
		}
		
		return false;
	}
}