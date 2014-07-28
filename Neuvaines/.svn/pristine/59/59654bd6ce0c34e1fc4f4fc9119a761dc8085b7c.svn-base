package com.maya.neuvaines.persitance;


import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Locale;

import android.util.Log;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBController extends SQLiteOpenHelper {
	private static final String LOGCAT = "DBLOG";
	private static DBController MyDBController;
	// Table
	private static final String TABLEDATE = "datesDone";
	// Champs de la table.

	// public static final String ID = "id";
	public static final String YEAR = "year";
	public static final String MONTH = "month";
	public static final String DAY = "day";

	/**
	 * Singleton pour distribuer cet objet aux activités.
	 * 
	 * @return DBController.
	 */
	public static synchronized DBController getDBController(
			Context applicationcontext) {
		if (MyDBController == null) {
			MyDBController = new DBController(applicationcontext);
		}
		return MyDBController;
	}

	/**
	 * Constructeur d'instance de classe.
	 * 
	 * @param applicationcontext
	 *            Contexte pour permettre à android de connaitre le chemin de la
	 *            base pour l'application.
	 */
	private DBController(Context applicationcontext) {
		// Ma base de données pour cette application.
		super(applicationcontext, "androidsqlite.db", null, 1);
		Log.d(LOGCAT, "Created");
	}

	@Override
	/**
	 * Executé en cas de création de la base.
	 */
	public void onCreate(SQLiteDatabase database) {
		String query;

		// TEST CLEAN
		// query = "DROP TABLE IF EXISTS " + TABLEDATE;
		// database.execSQL(query);
		// TEST CLEAN

		// Table des dates. (que les dates et en cas de conflit avec un trio
		// existant,
		// remplacement.
		query = "CREATE TABLE " + TABLEDATE
				+ " ( "
				// + " " + ID + " INT PRIMARY KEY,"
				+ " " + YEAR + " TEXT," + " " + MONTH + " TEXT," + " " + DAY
				+ " TEXT," + " UNIQUE(" + YEAR + "," + MONTH + "," + DAY
				+ ") ON CONFLICT REPLACE)";
		database.execSQL(query);
		Log.d(LOGCAT, TABLEDATE + " Created");
	}

	@Override
	/**
	 * Executé en cas d'upgrade de la base.
	 */
	public void onUpgrade(SQLiteDatabase database, int version_old,
			int current_version) {
		String query = "DROP TABLE IF EXISTS " + TABLEDATE;
		database.execSQL(query);
		Log.d(LOGCAT, TABLEDATE + " Updated");
		onCreate(database);
	}

	/**
	 * Désérialiser une date
	 * @return une date.
	 * @throws RuntimeException si le tableau n'a pas assez d'élements ou
	 * si la date n'est pas parsable.
	 */
	public Date deSerialiseDate(int[] dateSerialise) {
		String tag ="serialiseDate";
		// Intégrité.
		if (dateSerialise.length!=3) {
			Log.e(tag, "le tableau n'a pas 3 elements pour créer une date Année, mois, jours");
			// Erreur on retourne la date du jour.
			return Calendar.getInstance().getTime();
			// throw new RuntimeException("le tableau n'a pas 3 elements pour créer une date Année, mois, jours");
		} 
		
		// Création des objets Date depuis les valeurs
		SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy",
				Locale.FRANCE);
		sdf.setLenient(false);
		
		// NB +1 pour le mois car on commence en janvier.
		String date = String.format("%s-%s-%s",
				dateSerialise[2], dateSerialise[1]+1,
				dateSerialise[0]);
		try {
			return sdf.parse(date);
		} catch (ParseException e) {
			// Erreur ?
			Log.e(tag, "Date non parsable !");
			// throw new RuntimeException("Date non parsable !");
			// Erreur on retourne la date du jour.
			return Calendar.getInstance().getTime();
		}
	}
	/**
	 * Sérialiser une date
	 * 
	 * @return int[] Année, Mois, Date
	 */
	public int[] serialiseDate(Date dateToSerialise) {
		// Création d'un tableau de 3 valeurs depuis une date (Année, Mois,
		// jours).
		int[] dateSerialised = { 0, 0, 0 };
		// Sérialiser la date la plus petite
		Calendar cal = Calendar.getInstance();
		cal.setTime(dateToSerialise);

		dateSerialised[0] = cal.get(Calendar.YEAR);// Date Année
		dateSerialised[1] = cal.get(Calendar.MONTH);// Date Mois
		dateSerialised[2] = cal.get(Calendar.DAY_OF_MONTH);// Date Jour

		return dateSerialised;
	}

	/**
	 * Insert une date l'annéee : yyyy le mois mm, le jour dd, dans la base.
	 * 
	 * @param Year
	 *            Année en format yyyy.
	 * @param month
	 *            Mois en format mm.
	 * @param day
	 *            Jour en format dd.
	 */
	public void insertDate(String Year, String month, String day) {
		// On demande la base en mode écriture.
		SQLiteDatabase database = this.getWritableDatabase();
		ContentValues values = new ContentValues();

		try {
			values.put(YEAR, Year);
			values.put(MONTH, month);
			values.put(DAY, day);
			// Insert dans la table des dates, valeurs formatées avec le
			// ContentValues.
			database.insert(TABLEDATE, null, values);

		} finally {
			database.close();
		}

	}

	/**
	 * Donne une collection des jours du mois.
	 * 
	 * @param year
	 *            Année choisie.
	 * @param month
	 *            Mois choisi
	 * @return Une Map avec les jours concernés par l'Event Done.
	 */
	public HashMap<Integer, Integer> getDateOfMonth(Integer year, Integer month) {
		// Prendre la base en lecture.
		SQLiteDatabase database = null;
		try {
			database = this.getReadableDatabase();
			HashMap<Integer, Integer> wordList = new HashMap<Integer, Integer>();
			// Colonnes
			String[] columns = new String[] { DAY };
			// La requête renvoi un Cursor.
			Cursor cursor = database
					.query(TABLEDATE,
							columns,
							YEAR + "=? and " + MONTH + "=?",
							new String[] { String.valueOf(year),
									String.valueOf(month) }, null, null, null);
			// Les events avec date.
			if (cursor.moveToFirst()) {
				do {
					wordList.put(cursor.getInt(0), 1);
				} while (cursor.moveToNext());
			}

			return wordList;

		} finally {
			database.close();
		}

	}

	/**
	 * Donne la liste complète des dates en enregistrements bruts.
	 * 
	 * @return ArrayList liste des dates.
	 */
	public ArrayList<HashMap<String, String>> getDatesAllRaw() {
		SQLiteDatabase database = null;
		try {
			// Prendre la bas en lecture.
			database = this.getReadableDatabase();
			// Collections pour les résultats.
			ArrayList<HashMap<String, String>> wordList = new ArrayList<HashMap<String, String>>();
			// Colonnes
			String[] columns = new String[] { YEAR, MONTH, DAY };
			// La requête renvoi un Cursor.
			Cursor cursor = database.query(TABLEDATE, columns, null, null,
					null, null, null);

			if (cursor.moveToFirst()) {
				do {
					HashMap<String, String> CurrentMapDate = new HashMap<String, String>();
					CurrentMapDate.put(YEAR, cursor.getString(0));
					CurrentMapDate.put(MONTH, cursor.getString(1));
					CurrentMapDate.put(DAY, cursor.getString(2));
					wordList.add(CurrentMapDate);
				} while (cursor.moveToNext());
			}

			return wordList;

		} finally {
			database.close();
		}

	}

	/**
	 * Inutilisé. Test de dates en double.
	 */
	public void checkDoubleDate() {
		// Obtenir toute les dates tirés.
		ArrayList<Date> allDate = getAllDatesSorted();
		Date prevDate = null;
		Date currentDate;
		for (Iterator<Date> iterator = allDate.iterator(); iterator.hasNext();) {
			currentDate = (Date) iterator.next();
			if (prevDate != null) {
				// Comparaison avec la précédente.
				if (prevDate.compareTo(currentDate) == 0) {
					// Double
					Log.i(LOGCAT, "Double trouvé!!!!");
				}
			}
			prevDate = currentDate;
		}

		Log.i(LOGCAT, "Fin check doubles");
	}

	/**
	 * Trouver la date la plus basse.
	 * 
	 */
	public Date getMinDate() {

		return null;
	}

	/**
	 * Donne la liste complète des dates sous forme d'une liste de date triée
	 * dans l'ordre croissant.
	 */
	public ArrayList<Date> getAllDatesSorted() {
		String tag = "getAllDatesSorted";
		// Collections pour les résultats.
		ArrayList<Date> ListDate = new ArrayList<Date>();
		SQLiteDatabase database = null;
		try {
			// Prendre la bas en lecture.
			database = this.getReadableDatabase();
			// Colonnes
			String[] columns = new String[] { DAY, MONTH, YEAR };
			// La requête renvoi un Cursor.
			Cursor cursor = database.query(TABLEDATE, columns, null, null,
					null, null, null);
			// Nb : la méthode de désérialisation n'est pas utilisée pour aller
			// plus vite.

			// Création des objets Date depuis les valeurs
			SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy",
					Locale.FRANCE);
			sdf.setLenient(false);
			if (cursor.moveToFirst()) {
				do {
					String date = String.format("%s-%s-%s",
							cursor.getString(0), cursor.getString(1),
							cursor.getString(2));
					try {
						ListDate.add(sdf.parse(date));
					} catch (ParseException e) {
						// Bad BDD ??
						Log.e(tag, "Date non parsable !");
					}
				} while (cursor.moveToNext());
			}
			// Tri.
			Collections.sort(ListDate);

			return ListDate;

		} finally {

			database.close();
		}

	}

	/**
	 * Suppression d'une date (ou plusieurs) par DAY, MONTH, YEARS.
	 * 
	 * @param day
	 *            , month, year de la date à supprimer.
	 * @return resultat de la suppression sous forme d'entier.
	 */

	public int deleteDate(String day, String month, String year) {
		// FIXME Erreur pas de suppressions !!!
		SQLiteDatabase database = null;
		try {
			// Obtenir la base en écriture
			database = this.getWritableDatabase();
			int response = database.delete(TABLEDATE, DAY + "=? AND " + MONTH
					+ "=? AND " + YEAR + "=?",
					new String[] { day, month, year });
			return response;
		} finally {
			if (database != null)
				database.close();
		}
	}

	/**
	 * Suppression d'une date par son ID.
	 * 
	 * @param id
	 *            de la date à supprimer.
	 * @return resultat de la suppression sous forme d'entier.
	 */
	public int deleteDate(String id) {
		SQLiteDatabase database = null;
		try {
			// Obtenir la base en écriture
			database = this.getWritableDatabase();
			int response = database.delete(TABLEDATE, "ROWID=?",
					new String[] { id });
			// Log.d("GridCellAdapter", id + " reponse = " +
			// String.valueOf(response));
			return response;
		} finally {
			if (database != null)
				database.close();
		}

	}

	/**
	 * Pour chaque groupe YEAR, MONTH, DAY effectue une recherche dans la table
	 * TABLEDATE et fait une mise à jour.
	 * 
	 * @param queryValues
	 *            Collection de valeurs représentant des données à mettre à
	 *            jour.
	 * @return Entier représentant le résultat.
	 */
	public int updateDate(HashMap<String, String> queryValues) {
		SQLiteDatabase database = null;
		try {
			// Obtenir la base en écriture
			database = this.getWritableDatabase();
			ContentValues values = new ContentValues();
			values.put(YEAR, queryValues.get(YEAR));
			values.put(MONTH, queryValues.get(MONTH));
			values.put(DAY, queryValues.get(DAY));

			return database.update(
					TABLEDATE,
					values,
					YEAR + "=? and " + MONTH + "=? and " + DAY + "=?",
					new String[] { queryValues.get(YEAR),
							queryValues.get(MONTH), queryValues.get(DAY) });
		} finally {
			if (database != null)
				database.close();
		}

	}

	/**
	 * Retourne les infos pour une date donnée avec une recherche année(yyyy),
	 * mois(mm) jour(dd)
	 * 
	 * @param year
	 *            année de la date.
	 * @param month
	 *            mois de la date.
	 * @param day
	 *            jour de la date.
	 * @return collection avec les infos pour la date si trouvée. Sinon
	 *         collection vide.
	 */
	public HashMap<String, String> getDateInfo(String year, String month,
			String day) {
		// Prendre la base en lecture.
		SQLiteDatabase database = null;
		try {
			database = this.getReadableDatabase();
			// Collections pour les résultats.
			HashMap<String, String> wordList = new HashMap<String, String>();
			// Colonnes
			String[] columns = new String[] { "ROWID", YEAR, MONTH, DAY };
			// La requête renvoi un Cursor.
			Cursor cursor = database.query(TABLEDATE, columns, YEAR + "=? and "
					+ MONTH + "=? and " + DAY + "=?", new String[] { year,
					month, day }, null, null, null);

			if (cursor.moveToFirst()) {
				do {
					wordList.put("ROWID", cursor.getString(0));
					wordList.put(YEAR, cursor.getString(1));
					wordList.put(MONTH, cursor.getString(2));
					wordList.put(DAY, cursor.getString(3));
				} while (cursor.moveToNext());
			}

			return wordList;

		} finally {
			database.close();
		}

	}



}