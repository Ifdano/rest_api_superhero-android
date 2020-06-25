package com.example.myapplication_superhero.characterApi;

import com.google.gson.annotations.SerializedName;

public class Powerstats {
	@SerializedName("intelligence")
	private String intelligence;
	
	@SerializedName("speed")
	private String speed;
	
	@SerializedName("power")
	private String power;
	
	public Powerstats(){}
	
	public String getIntelligence(){ return intelligence; }
	public String getSpeed(){ return speed; }
	public String getPower(){ return power; }
}