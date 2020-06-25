//Класс с паттерном "Одиночка"

package com.example.myapplication_superhero.characterApi;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;

public class SuperheroService {
	private static final String BASE_URL = "https://www.superheroapi.com/";
	
	private static SuperheroService mInstance;
	private Retrofit retrofit;
	
	private SuperheroService(){
		retrofit = new Retrofit
							.Builder()
							.baseUrl(BASE_URL)
							.addConverterFactory(GsonConverterFactory.create())
							.addCallAdapterFactory(RxJava2CallAdapterFactory.create())
							.build();
	}
	
	public static SuperheroService getInstance(){
		if(mInstance == null)
			mInstance = new SuperheroService();
		
		return mInstance;
	}
	
	public SuperheroAPI getSuperheroApi(){
		return retrofit.create(SuperheroAPI.class);
	}
}
