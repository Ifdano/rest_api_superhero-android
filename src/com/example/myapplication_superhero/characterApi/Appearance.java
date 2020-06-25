package com.example.myapplication_superhero.characterApi;

import com.google.gson.annotations.SerializedName;

public class Appearance {
	@SerializedName("gender")
	private String gender;
	
	@SerializedName("race")
	private String race;
	
	@SerializedName("height")
	private String[] height;
	
	@SerializedName("weight")
	private String[] weight;
	
	public Appearance(){}
	
	public String getGender(){ return gender; }
	public String getRace(){ return race; }
	
	public String[] getHeight(){ return height; }
	public String[] getWeight(){ return weight; }
}