//����� ��� ����������� ������ ����������� ���������, �� id

package com.example.myapplication_superhero;

import android.os.Bundle;

import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;

import android.view.View;
import android.view.MotionEvent;
import android.view.View.OnTouchListener;

//���������� ����
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

//��� ���� ������
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import android.content.Intent;

//�������� ����������� � Picasso
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Callback;

import com.example.myapplication_superhero.data.SuperheroContract.SuperheroEntry;
import com.example.myapplication_superhero.data.SuperheroDbHelper;

//���������� ���� ��� �������� ���������
import com.example.myapplication_superhero.dialogs.DeleteDialogCurrent;

public class CurrentData extends FragmentActivity implements OnTouchListener{
	/*��� ���������/�������� ID ��������� �� ���� ������, �������� �� �������,
	  ����� ����� ������ ������ ��������� ����� �������*/
	public static final String KEY_ID = "key_id";
	
	//����������
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
	
	//������ ���������, ������� �� ������� ��� ��������
	private int currentId;
	
	//���� ������
	private SuperheroDbHelper DbHelper;
	
	protected void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_current);
		
		init();
		//��������� ������� ��������� ��� ��������
		getCharacterId();
	}
	
	//��������� �������������
	public void init(){
		//������� ����������
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
		
		//������������� ����������
		buttonUpdate.setOnTouchListener(this);
		buttonDelete.setOnTouchListener(this);
		buttonBack.setOnTouchListener(this);
		
		//������� ���� ������
		DbHelper = new SuperheroDbHelper(this);
	}
	
	//�������� id ���������, ������� �� �������� � ��� ����
	public void getCharacterId(){
		intent = getIntent();
		
		//�������� id
		currentId = intent.getIntExtra(KEY_ID, -1);
		
		/*���� �� �������� id, �� �������� ����� ��� ������ ������ ��
		 ���� ������ �� �����*/
		if(currentId > 0){
			displayDatabaseInfo();
		}else{
			//���� id �� �������� - �������� �� ����
			Toast.makeText(
					getApplicationContext(),
					"������ ��������� ID",
					Toast.LENGTH_LONG
				).show();
		}
	}
	
	//����������� ������ ��������� �� ���� ������
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
		
		/*������� �� ���� ������
		  � ����� ������, �� ������� ������ ��������� �� id*/
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
				//�������� ��� ������ �� ���� ������
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
				
				//������������� �����������
				Callback callback = new Callback(){
					public void onSuccess(){}
					
					public void onError(){}
				};
				
				//��������� ����������� �� ������
				Picasso
					.with(getApplicationContext())
					.load(currentImageUrl)
					.placeholder(R.drawable.loadingimage)
					.error(R.drawable.errorimage)
					.into(avatarImage);
				
				//������������� ������ � ����
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
	
	//�������� ���������
	public void deleteCharacter(){
		SQLiteDatabase db = DbHelper.getWritableDatabase();
		
		//������� �� id ���������
		db.delete(
				SuperheroEntry.TABLE_NAME,
				SuperheroEntry._ID + "=?",
				new String[]{"" + currentId}
			);
		
		Toast.makeText(
					getApplicationContext(),
					"�������!",
					Toast.LENGTH_LONG
				).show();
		
		//����� �������� - ������������ � ������� ����
		intent = new Intent(this, Main.class);
		startActivity(intent);
	}
	
	public boolean onTouch(View view, MotionEvent event){
		
		if(event.getAction() == MotionEvent.ACTION_DOWN){
			//������ ���������� ������ ���������
			if(view == buttonUpdate){
				//��������� � ���� UpdateData
				intent = new Intent(this, UpdateData.class);
				
				//�������� id ���������, ������ �������� �� ����� ��������
				intent.putExtra(KEY_ID, currentId);
				
				startActivity(intent);
			}
			
			//������ �������� ���������
			if(view == buttonDelete){
				//���������� ���� ��� ������������� ��������
				FragmentManager manager = getSupportFragmentManager();
				DeleteDialogCurrent dialog = new DeleteDialogCurrent();
				dialog.show(manager, "delete_dialog_current");
			}
			
			//������ ����������� �� ������� �����
			if(view == buttonBack){
				intent = new Intent(this, Main.class);
				startActivity(intent);
			}
		}
		
		return false;
	}
}