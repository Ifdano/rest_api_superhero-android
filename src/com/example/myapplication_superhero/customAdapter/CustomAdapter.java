//����� ��� ���������� ��������

package com.example.myapplication_superhero.customAdapter;

import android.content.Context;

import android.view.View;
import android.view.ViewGroup;
import android.view.LayoutInflater;

import android.widget.ArrayAdapter;

import android.widget.TextView;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;
import com.squareup.picasso.Callback;

import java.util.ArrayList;

import android.util.Log;

import com.example.myapplication_superhero.R;

public class CustomAdapter extends ArrayAdapter{
	
	//������ ��� �������� ��������
	//��������
	private Context context;
	//����� � ��������� �������
	private int layoutId;
	//������ � ���������� ��� ������
	private ArrayList<Character> characters;
	
	//� ����������� �������� ��������, ����� ������ � ������ � �������
	public CustomAdapter(Context context, int layoutId, ArrayList<Character> characters){
		super(context, layoutId, characters);
		
		//�������� ������
		this.context = context;
		this.layoutId = layoutId;
		this.characters = characters;
	}
	
	//����� ��� ��������� ������ ���������� � ������ ������
	public View getView(int position, View customView, ViewGroup container){
		//���������� ����� ������ � ��� ������ ������
		LayoutInflater inflater = LayoutInflater.from(context);
		View view = inflater.inflate(layoutId, container, false);
		
		//����� ������� �� ������
		Character character = characters.get(position);
		
		//������� ���������� �� ������ ������
		TextView text = (TextView)view.findViewById(R.id.text);
		ImageView image = (ImageView)view.findViewById(R.id.image);
		
		//������������� ������ �������� [�������� � �����] � ����
		//������������� �����
		text.setText(character.getCharacterName());
		
		//������� Callback ��� ������ Picasso
		Callback callback = new Callback(){
			public void onSuccess(){
				Log.v("IMAGE LOAD", "SUCCESS");
			}
			
			public void onError(){
				Log.v("IMAGE LOAD", "ERROR");
			}
		};
		
		//������������� ��������
		Picasso
			.with(context)
			.load(character.getCharacterAvatarUrl())
			.placeholder(R.drawable.loadingimage)
			.error(R.drawable.errorimage)
			.into(image, callback);
		
		return view;
	}
}
