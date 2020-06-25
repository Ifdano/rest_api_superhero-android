/*���������� ���� � �������������� �������� ��������� �� ���� ������,
�������� �� ������ � ��������� �����������*/

package com.example.myapplication_superhero.dialogs;

import android.os.Bundle;

import android.support.v4.app.DialogFragment;

import android.app.Dialog;
import android.app.AlertDialog;

import android.content.DialogInterface;

import com.example.myapplication_superhero.CurrentData;

public class DeleteDialogCurrent extends DialogFragment{
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
;						}
					}
				)
			.setNegativeButton(
					"��",
					new DialogInterface.OnClickListener(){
						public void onClick(DialogInterface dialog, int id){
							/*���� ����������� ��������, �� �������� ����� deleteCharacter �� ������ CurrentData,
							 ����� ������� ��������� �� ���� ������ */
							((CurrentData)getActivity()).deleteCharacter();
						}
					}
				);
		
		return builder.create();
	}
}