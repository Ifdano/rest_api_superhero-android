//Диалоговое окно для подтверждения очистики базы данных

package com.example.myapplication_superhero.dialogs;

import android.os.Bundle;

import android.support.v4.app.DialogFragment;

import android.app.Dialog;
import android.app.AlertDialog;

import android.content.DialogInterface;

import com.example.myapplication_superhero.Main;

public class DeleteAllDialog extends DialogFragment{
	public Dialog onCreateDialog(Bundle savedInstanceState){
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		
		builder
			.setTitle("ВНИМАНИЕ!")
			.setMessage("Вы подтверждаете очистку базы данных?")
			.setCancelable(true)
			.setPositiveButton(
					"Нет",
					new DialogInterface.OnClickListener(){
						public void onClick(DialogInterface dialog, int id){
							dialog.cancel();
						}
					}
				)
			.setNegativeButton(
					"Да",
					new DialogInterface.OnClickListener(){
						public void onClick(DialogInterface dialog, int id){
							((Main)getActivity()).deleteCharactersAll();
						}
					}
				);
		
		return builder.create();
	}
}