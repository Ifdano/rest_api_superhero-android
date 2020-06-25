package com.example.myapplication_superhero.characterApi;

import com.google.gson.annotations.SerializedName;

public class Image {
	@SerializedName("url")
	private String url;
	
	public Image(){}
	
	public String getUrl(){ return url; }
}