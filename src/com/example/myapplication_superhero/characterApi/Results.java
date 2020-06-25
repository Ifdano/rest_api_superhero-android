package com.example.myapplication_superhero.characterApi;

import com.google.gson.annotations.SerializedName;

public class Results {
	@SerializedName("id")
	private int id;
	
	@SerializedName("name")
	private String name;
	
	@SerializedName("powerstats")
	private Powerstats powerstats;
	
	@SerializedName("biography")
	private Biography biography;
	
	@SerializedName("appearance")
	private Appearance appearance;
	
	@SerializedName("work")
	private Work work;
	
	@SerializedName("image")
	private Image image;
	
	public Results(){}
	
	public int getId(){ return id; }
	public String getName(){ return name; }
	
	public Powerstats getPowerstats(){ return powerstats; }
	public Biography getBiography(){ return biography; }
	public Appearance getAppearance(){ return appearance; }
	public Work getWork(){ return work; }

	public Image getImage(){ return image; }
}