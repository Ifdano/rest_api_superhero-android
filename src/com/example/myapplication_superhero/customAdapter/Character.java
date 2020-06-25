//����� POJO ��� �������� ������ ����������, ����� ��������� �� � ������

package com.example.myapplication_superhero.customAdapter;

public class Character {

	//������ ����������
	private String characterName;
	private String characterAvatarUrl;
	
	public Character(String characterName, String characterAvatarUrl){
		this.characterName = characterName;
		this.characterAvatarUrl = characterAvatarUrl;
	}
	
	//��������� ������
	public String getCharacterName(){ return characterName; }
	public String getCharacterAvatarUrl(){ return characterAvatarUrl; }
}
