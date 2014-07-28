package com.maya.neuvaines;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import com.maya.neuvaines.R;
import com.maya.neuvaines.persitance.DBController;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;


public class StatisticsFragment extends Fragment {
	private static final String tag = "StatisticsActivity";
	protected static final int ACTIVITY_CREATE = 1;
	private LayoutInflater _inflater;
	private View _rootView;



	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		switch(requestCode) {
		case ACTIVITY_CREATE:
			Log.i(tag, "Retour de l'activity EXPORT");
		    break;
		}
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.statistics, container, false);
		_inflater = inflater;
		_rootView = rootView;
		
		// Lancement de l'activity pour l'export des données.
		Button buttonModal = (Button) _rootView
				.findViewById(R.id.ButtonDataExport);
		buttonModal.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// Ouvrir un écran fullscreen pour faire l'export des données.
				Intent i = new Intent(getActivity(), ExportActivity.class);
				startActivityForResult(i, ACTIVITY_CREATE);
			}
		});

		// Calcul des statistiques.
		computeStats();
		return rootView;
	}

	@Override
	public void onResume() {
		super.onResume();
		Log.i(tag, "Stat devient visible.");
		computeStats();
	}

	/**
	 * Calcul des statistiques depuis une lecture de la base de données.
	 */
	public int[] computeStats() {

		DBController MyDB = DBController
				.getDBController(_inflater.getContext());

		int[] stat = Statistics.getTabDayMonthYear(MyDB);

		// Date la plus petite.
		int[] dateSerialise = { stat[3], stat[4], stat[5] };
		Date minDate = MyDB.deSerialiseDate(dateSerialise);
		// TEST
		Log.i(tag, "Date la plus petite " + minDate.toString());

		// Nombre d'années
		TextView TextViewYears = (TextView) _rootView
				.findViewById(R.id.editTextCiergepascal);
		TextViewYears.setText(String.valueOf(stat[0]) + " "
				+ getString(R.string._years));

		// Nombre de mois
		TextView TextViewMonth = (TextView) _rootView
				.findViewById(R.id.editTextCiergemois);
		TextViewMonth.setText(String.valueOf(stat[1]) + " "
				+ getString(R.string._months));

		// Nombres de jours
		TextView TextViewLuminion = (TextView) _rootView
				.findViewById(R.id.editTextluminion);
		TextViewLuminion.setText(String.valueOf(stat[2]) + " "
				+ getString(R.string._days));

		TextView TextViewMinDate = (TextView) _rootView
				.findViewById(R.id.textViewStartDate);
		// i18n
		SimpleDateFormat dateFormatter = new SimpleDateFormat("dd-MMMM-yyyy",
				Locale.getDefault());

		TextViewMinDate.setText(getString(R.string.startDate) + " : "
				+ dateFormatter.format(minDate));

		return stat;
	}
}
