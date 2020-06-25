/*���������� ���� � �������������� �������� ��������� �� ���� ������,
�������� �� ������� ������, ��� ������ ������� �� ��������� �� ������*/

package com.example.myapplication_superhero.dialogs;

import android.support.v4.app.DialogFragment;

import android.os.Bundle;

import android.app.Dialog;
import android.app.AlertDialog;

import android.content.DialogInterface;

import com.example.myapplication_superhero.Main;

public class DeleteDialog extends DialogFragment{
	public Dialog onCreateDialog(Bundle savedInstanceState){
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		
		builder
			.setTitle("��������!")
			.setMessage("�� ������������� �������� ��������� �� ���� ������?")
			.setCancelable(true)
			.setPositiveButton(
					"���",
					new DialogInterface.OnClickListener(){
						public void onClick(DialogInterface dialog, int id){
							dialog.cancel();
						}
					}
				)
			.setNegativeButton(
					"��",
					new DialogInterface.OnClickListener(){
						public void onClick(DialogInterface dialog, int id){
							/*���� ����������� ��������, �� �������� ����� deleteCharacter �� ������ Main,
							 ����� ������� ��������� �� ���� ������ */
							((Main)getActivity()).deleteCharacter();
						}
					}
				);
				
		return builder.create();
	}
}