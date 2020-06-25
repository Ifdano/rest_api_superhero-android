package com.example.myapplication_superhero.characterApi;

import com.google.gson.annotations.SerializedName;

public class Work {
	@SerializedName("occupation")
	private String occupation;
	
	public Work(){}
	
	public String getOccupation(){ return occupation; }
}