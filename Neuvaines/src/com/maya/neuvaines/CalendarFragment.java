package com.maya.neuvaines;

import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

import com.maya.neuvaines.R;
import com.maya.neuvaines.persitance.DBController;


import android.app.Fragment;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;

public class CalendarFragment extends Fragment implements OnClickListener {
	// Constantes indiquant l'offset d'accès aux infos d'une journée.
	public static final int DAY = 0;
	public static final int MONTH = 1;
	public static final int YEAR = 2;
	public static final int FONTCOLOR = 3;
	public static final int STYLECELL = 4;
	public static final int EVENT = 5;
	
	public static final String COLORTRAIL = "GREY";
	public static final String COLORCURRENT = "BLACK";
	public static final String COLORLEAD = "GREY";
	
	// Tag de la classe (Débuggage)
	private static final String tag = "SimpleCalendarViewActivity";
	
	
	private Button selectedDayMonthYearButton;
	private Button currentMonth;
	private ImageView prevMonth;
	private ImageView nextMonth;
	private GridView calendarView;
	private GridCellAdapter adapter;
	private Calendar _calendar;
	private int month, year;
	// Formatter de date issue de la locale.
	private final SimpleDateFormat i18nDF_MMMM_yyyy = new SimpleDateFormat(
			"MMMM yyyy", Locale.getDefault());
	// Formatter de date issue de la locale.
	private final SimpleDateFormat i18nDF_dd_MMMM_yyyy = new SimpleDateFormat(
			"dd-MMMM-yyyy", Locale.getDefault());
	// private static final String dateTemplate = "";
	private LayoutInflater _inflater;

	/**
	 * Event création de vue.
	 * 
	 * @param inflater
	 *            Inflater : sert à créer une vue objet depuis sa description
	 *            sous forme XML.
	 * @param container
	 *            container : Parent de cette vue.
	 * @param savedInstanceState
	 *            sert à sauver l'etat de la vue acctuelle.
	 * 
	 */
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		_inflater = inflater;
		// On "gonfle" un objet vue avec comme modèle la descritpion XML dans le
		// Parent.
		View rootView = inflater.inflate(R.layout.calendar, container, false);

		// Calendier crée avec la date par défaut dans la Local par défaut.
		_calendar = Calendar.getInstance(Locale.getDefault());

		month = _calendar.get(Calendar.MONTH) + 1;
		year = _calendar.get(Calendar.YEAR);
		// Log.d(tag, "Calendar Instance:= " + "Month: " + month + " " +
		// "Year: " + year);

		// Bouton pour afficher la date choisie.
		selectedDayMonthYearButton = (Button) rootView
				.findViewById(R.id.selectedDayMonthYear);
		selectedDayMonthYearButton.setText("" + getString(R.string._choice)
				+ " : ");

		prevMonth = (ImageView) rootView.findViewById(R.id.prevMonth);
		prevMonth.setOnClickListener(this);

		currentMonth = (Button) rootView.findViewById(R.id.currentMonth);
		currentMonth.setText(i18nDF_MMMM_yyyy.format(_calendar.getTime()));

		nextMonth = (ImageView) rootView.findViewById(R.id.nextMonth);
		nextMonth.setOnClickListener(this);

		calendarView = (GridView) rootView.findViewById(R.id.calendar);

		// Initialized
		adapter = new GridCellAdapter(inflater.getContext(),
				R.id.calendar_day_gridcell, month, year);

		calendarView.setAdapter(adapter);
		ObjectsRegister.setCalendarFragment(this);
		return rootView;
	}

	/**
	 * Force l'adapteur avec une nouvelle date. <=> Changement de mois
	 * 
	 * @param month
	 * @param year
	 */
	public void setGridCellAdapterToDate(int month, int year) {

		adapter = new GridCellAdapter(_inflater.getContext(),
				R.id.calendar_day_gridcell, month, year);
		_calendar.set(year, month - 1, _calendar.get(Calendar.DAY_OF_MONTH));

		currentMonth.setText(i18nDF_MMMM_yyyy.format(_calendar.getTime()));
		adapter.notifyDataSetChanged();
		calendarView.setAdapter(adapter);
	}

	@Override
	public void onClick(View v) {
		if (v == prevMonth) {
			if (month <= 1) {
				month = 12;
				year--;
			} else {
				month--;
			}
			// Log.d(tag, "Setting Prev Month in GridCellAdapter: " + "Month: "
			// + month + " Year: " + year);
			setGridCellAdapterToDate(month, year);
		}
		if (v == nextMonth) {
			if (month > 11) {
				month = 1;
				year++;
			} else {
				month++;
			}
			// Log.d(tag, "Setting Next Month in GridCellAdapter: " + "Month: "
			// + month + " Year: " + year);
			setGridCellAdapterToDate(month, year);
		}

	}

	@Override
	public void onDestroy() {
		Log.d(tag, "Destroying View ...");
		super.onDestroy();
	}

	/**
	 * Classe interne pour la grille du calendrier.
	 * 
	 */
	public class GridCellAdapter extends BaseAdapter implements OnClickListener {
		// Tag de débug.
		private static final String tag = "GridCellAdapter";

		private final Context _context;

		// Collection possédant toutes les infos utiles à l'affichage. Format
		// unique non i18n
		private final ArrayList<ArrayList<String>> list;

		private Button gridcell;
		private final HashMap<Integer, Integer> eventsPerMonthMap;

		// Formatter de date stockée dans les TAGs.
		private final SimpleDateFormat tagDateFormatter = new SimpleDateFormat(
				"dd-MMMM-yyyy", Locale.ENGLISH);
		
		/**
		 * Constructeur. Jours dans le mois courrant.
		 * 
		 * @param context
		 * @param textViewResourceId
		 * @param month
		 * @param year
		 */
		public GridCellAdapter(Context context, int textViewResourceId,
				int month, int year) {
			super();

			// Log.d(tag, "==> Mois : " + month + " " + "Année : " + year);

			_context = context;

   		    // Log.d(tag, "New Calendar:= " + calendar.getTime().toString());
			// Log.d(tag, "CurrentDayOfWeek :" + getCurrentWeekDay());
			// Log.d(tag, "CurrentDayOfMonth :" + getCurrentDayOfMonth());
			
			// Liste contenant les infos pour réaliser l'affichage dans le
			// calendrier. i18n sauf pour les tags.

			list = Statistics.buildListOfDays(month, year);
			
			// Find Number of Events, retour des données comme une HashMap.
			eventsPerMonthMap = findNumberOfEventsPerMonth(year, month);
		}

		/**
		 * Donne les données d'une position.
		 */
		public ArrayList<String> getItem(int position) {
			return list.get(position);
		}

		/**
		 * Donne le nombre d'éléments à représenter.
		 */
		@Override
		public int getCount() {
			return list.size();
		}

		
		/**
		 * Recherche les dates de l'évènement pour ce mois dans la base.
		 * 
		 * @param year
		 *            Année de l'évènement.
		 * @param month
		 *            mois de l'évènement.
		 * @return Une collection de type HashMap avec la liste des Event Done.
		 */
		private HashMap<Integer, Integer> findNumberOfEventsPerMonth(int year,
				int month) {
			// SparseIntArray test = new SparseIntArray();
			HashMap<Integer, Integer> map = new HashMap<Integer, Integer>();
			// Lecture de la base pour le mois en cours
			DBController MyDB = DBController.getDBController(_inflater
					.getContext());
			map = MyDB.getDateOfMonth(year, month);
			return map;
		}

		@Override
		public long getItemId(int position) {
			return position;
		}


		
		/**
		 * Fournit la vue pour une cellule.
		 */
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			View row = convertView;
			if (row == null) {
				LayoutInflater inflater = (LayoutInflater) _context
						.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				row = inflater.inflate(R.layout.calendar_day_gridcell, parent,
						false);
			}
			
			// Get a reference to the Day gridcell
			gridcell = (Button) row.findViewById(R.id.calendar_day_gridcell);
			
			// Extrait des informations du tableau list pour cette position .
			ArrayList<String> ListDayInfo = list.get(position);
			
			// Cellules pour faire le nom des jours de la semaine.
			if (position < 7) {
				gridcell.setBackgroundResource(R.drawable.calendar_bg_orange_header);
				// Obtenir le nom du jour de la semaine en abrégé.
				gridcell.setText(ListDayInfo.get(CalendarFragment.DAY));
				return row;
			} else {
				gridcell.setOnClickListener(this);
			}

			// Affichage des bougies par jours en lisant la base.
			gridcell.setBackgroundResource(R.drawable.calendar_tile_small);

			if ((eventsPerMonthMap != null) && (!eventsPerMonthMap.isEmpty())) {
				if (eventsPerMonthMap.containsKey(Integer.valueOf(ListDayInfo.get(CalendarFragment.DAY)))) {
					// Log.d(tag, "Ce jour est dans la collection");
					if (ListDayInfo.get(CalendarFragment.FONTCOLOR).equals(CalendarFragment.COLORCURRENT)) {
						// gridcell.setBackgroundResource(R.drawable.calendar_tile_small_candle);
						gridcell.setBackgroundResource(R.drawable.calendar_tile_small_candle2);
					}
				}
			}

			// Mise en place des infos du jour pour cette GridCell.
			NumberFormat nf = NumberFormat.getNumberInstance(Locale.getDefault());

			// ici i18n
			gridcell.setText(nf.format(Integer.parseInt(ListDayInfo.get(CalendarFragment.DAY))));
			
			// Ici pas de i18n (tag).
			gridcell.setTag(ListDayInfo.get(CalendarFragment.DAY) 
					+ "-" + ListDayInfo.get(CalendarFragment.MONTH) 
					+ "-" + ListDayInfo.get(CalendarFragment.YEAR));

			// Couleur du nombre du jour.
			if (ListDayInfo.get(CalendarFragment.FONTCOLOR).equals(CalendarFragment.COLORLEAD)) {
				// TODO Externaliser les couleurs dans un fichier de paramètres
				gridcell.setTextColor(Color.LTGRAY);
			}
			
			if (ListDayInfo.get(CalendarFragment.FONTCOLOR).equals(CalendarFragment.COLORTRAIL)) {
				// TODO Externaliser les couleurs dans un fichier de paramètres
				gridcell.setTextColor(Color.LTGRAY);
			}
			
			if (ListDayInfo.get(CalendarFragment.FONTCOLOR).equals(CalendarFragment.COLORCURRENT)) {
				// TODO Externaliser les couleurs dans un fichier de paramètres
				gridcell.setTextColor(Color.BLACK);
			}
			return row;
		}

		/**
		 * Ce qui se passe si l'on clique un jour dans le calendrier.
		 */
		@Override
		public void onClick(View view) {
			// Lecture du tag
			String date_month_year = (String) view.getTag();
			// Log.d(tag, "tag brut =>" + date_month_year);

			try {
				// Le tag (English) est convertit en Date.
				Date parsedDate = tagDateFormatter.parse(date_month_year);
				
				// Mise à jour du bouton pour indiquer la date choisie, i18n.
				selectedDayMonthYearButton.setText(getString(R.string._choice)
						+ " : " + i18nDF_dd_MMMM_yyyy.format(parsedDate));
				
				
				// Local par défaut. English.
				Calendar cal = Calendar.getInstance(Locale.ENGLISH);
				cal.setTime(parsedDate);
				String day = String.valueOf(cal.get(Calendar.DAY_OF_MONTH));
				// NB : Commence à 0, janvier = 0
				String month = String.valueOf(1 + cal.get(Calendar.MONTH));
				String year = String.valueOf(cal.get(Calendar.YEAR));

				// Test
				// Log.d(tag, "Date pour BDD : " + day + " " + month + " " + year);

				DBController MyDB = DBController.getDBController(_inflater
						.getContext());

				// Obtenir la date depuis année, mois jour.
				HashMap<String, String> MyDate = MyDB.getDateInfo(year, month,
						day);

				synchronized (this) { // en cas d'event trop rapprochés.
					// Valeur de Done pour cette Date ?
					if (!MyDate.isEmpty()) {
						// Suppression.
						/*
						 * int result =
						 * MyDB.deleteDate(MyDate.get(DBController.DAY),
						 * MyDate.get(DBController.MONTH),
						 * MyDate.get(DBController.YEAR));
						 */
						MyDB.deleteDate(MyDate.get("ROWID"));
						// Log.d(tag, "Existe=> Suppression. ");
					} else {
						// Ajout
						MyDB.insertDate(year, month, day);
						// Log.d(tag, "Absente => Ajout.");
					}

					// Relire
					MyDate = MyDB.getDateInfo(year, month, day);
					// Bougie ou non ?
					if (!MyDate.isEmpty()) {
						// Ajouter une bougie dans le calendrier.
						view.setBackgroundResource(R.drawable.calendar_tile_small_candle2);
						// Log.d(tag, "Bougie ok");

					} else {
						// Mettre une case sans bougie.
						view.setBackgroundResource(R.drawable.calendar_tile_small);
						// Log.d(tag, "Pas de bougie");

					}
				}

				// Mise à jour des statistiques
				// if (ObjectsRegister.getStatisticsFragment() != null) {
				// ObjectsRegister.getStatisticsFragment().computeStats();
				// }

			} catch (ParseException e) {
				Log.e(tag, "Erreur " + e.getMessage());
			}
		}


	}

}