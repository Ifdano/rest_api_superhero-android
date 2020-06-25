//Наш API для запроса данных

package com.example.myapplication_superhero.characterApi;

import retrofit2.http.GET;
import retrofit2.http.Path;

import io.reactivex.Flowable;

public interface SuperheroAPI {
	//общая сслыка запроса: https://www.superheroapi.com/api.php/894384864360671/search/batman
	@GET("api.php/{user_key}/search/{superhero_name}")
	Flowable<Characters> getCharacter(
			@Path("user_key") String user_key,
			@Path("superhero_name") String superhero_name
		);
}