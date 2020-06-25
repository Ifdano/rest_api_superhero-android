package com.example.myapplication_superhero.characterApi;

import com.google.gson.annotations.SerializedName;

public class Biography {
	@SerializedName("full-name")
	private String full_name;
	
	@SerializedName("first-appearance")
	private String first_appearance;
	
	@SerializedName("publisher")
	private String publisher;
	
	public Biography(){}
	
	public String getFullName(){ return full_name; }
	public String getFirstAppearance(){ return first_appearance; }
	public String getPublisher(){ return publisher; }
}