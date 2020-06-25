//Основной класс Pojo для получения данных Json
//общая ссылка: https://www.superheroapi.com/api.php/894384864360671/search/batman

package com.example.myapplication_superhero.characterApi;

import com.google.gson.annotations.SerializedName;

public class Characters {
	@SerializedName("results")
	private Results[] results;
	
	public Characters(){}
	
	public Results[] getResults(){ return results; }
}