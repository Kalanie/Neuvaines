/**
 * 
 */
package com.maya.neuvaines;

import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Locale;

import android.util.Log;

import com.maya.neuvaines.persitance.DBController;

/**
 * Classe chargé de produire des statistiques 
 * et des préparation depuis les infos de la base de données.
 *  
 * @author maya
 * 
 */
public class Statistics {
	
	private static String tag = " Statistics ";
	private static final int DAY_OFFSET = 1;
	// Constantes partagées
	public static final String COLORBLACK = "BLACK";
	public static final String COLORGREY = "GREY";
	
	public static final String WEEKDAYS = "WEEKDAYS";
	public static final String TRAILDAYS = "TRAILDAYS";
	public static final String LEADINGDAYS = "LEADINGDAYS";
	public static final String CURRENTDAYS = "CURRENTDAYS";
	
	/**
	 * A partir de l'objet base de donnée, donne une analyse sous forme de tableau
	 * des données sauvegardées.
	 * @param MyDB Objet Base de données.
	 * @return tableau de 6 entiers.
  	 * - combien de : 
	 * nbAnnée, nbMois, nbJours,
	 * - Date de début :
	 * dateMinAnnee, dateMinMois, dateMinJours	 * 
 	 */
	public static int[] getTabDayMonthYear(DBController MyDB) {
		// Intégrité.
		if (MyDB == null) {
			throw new RuntimeException("MyDB ne doit pas être null.");
		} 
		
		// Toutes les dates en collection d'objets Date.
		// trier la collection, créer un comparer (fait à la lecture base).
		ArrayList<Date> ListDates = MyDB.getAllDatesSorted();

		
		// Calcul avec décalage si besoin le nombre d'années et de mois complets
		// depuis une liste de dates issues de la base de donnée.
		int[] stat = Statistics.DataAnalysis(ListDates);
		
		return stat;
	}
	
	/**
	 * Obtenir les jours en mode abrégé selon la locale.
	 * @param day numéro du jour de la semaine.
	 * @return un abrégé du jour selon la locale.
	 */
	public static String getShortDayName(int day) {
		Calendar c = Calendar.getInstance(Locale.getDefault());
		c.set(2011, 7, 7, 0, 0, 0);
		c.add(Calendar.DAY_OF_MONTH, day);
		return String.format("%ta", c);
	}
	
	/**
	 * Crée le contenu de l'objet list, qui représente toutes les données
	 * nécesaires pour réaliser un affichage de calendrier.
	 * 
	 * @param month
	 *            Mois en cours sous forme de numéro.
	 * @param year
	 *            Année sous forme de numéro.
	 */
	public static ArrayList<ArrayList<String>> buildListOfDays(int month, int year) {
		ArrayList<ArrayList<String>> list = new ArrayList<ArrayList<String>>();
		// Calendrier et date en cours dans la locale.
		final Calendar i18nCalendar;
		
		// Calendrier basé sur la local par défaut.
		i18nCalendar = Calendar.getInstance(Locale.getDefault());
		
		// On fixe le calendrier à ce mois (base 0) au 1er du mois.
		i18nCalendar.set(year, month - 1, 1);
		
		// Nombre de jours dans le mois.
		int daysInMonth;
		// nombre de jours à laisser en blanc au début de ce mois.
		int daysInPrevMonth = 0;
		// nombre de jours à laisser en blanc en fin de ce mois.
		int trailingSpaces = 0;

		// Mois en clair selon la Local english (ici on crée les tags et ils
		// ne sont pas en i18n).
		SimpleDateFormat sdf = new SimpleDateFormat("MMMM", Locale.ENGLISH);
		// Chiffres selons la local english (ici on crée les tags et ils ne
		// sont pas en i18n).
		NumberFormat nf = NumberFormat.getNumberInstance(Locale.ENGLISH);

		String currentMonthName = sdf.format(i18nCalendar.getTime());

		i18nCalendar.add(Calendar.MONTH, -1);
		int prevYear = i18nCalendar.get(Calendar.YEAR);
		String prevMonthAsString = sdf.format(i18nCalendar.getTime());
		daysInPrevMonth = i18nCalendar
				.getActualMaximum(Calendar.DAY_OF_MONTH);

		i18nCalendar.add(Calendar.MONTH, 2);
		int nextYear = i18nCalendar.get(Calendar.YEAR);
		String nextMonthAsString = sdf.format(i18nCalendar.getTime());

		i18nCalendar.add(Calendar.MONTH, -1); // retour au mois acctuel.

		daysInMonth = i18nCalendar.getActualMaximum(Calendar.DAY_OF_MONTH);

		// Gregorian Calendar : MINUS 1, set to FIRST OF MONTH
		GregorianCalendar cal = new GregorianCalendar(year, month - 1, 1);

		// Log.d(tag, "Gregorian Calendar:= " + cal.getTime().toString());

		// Compute how much to leave before the first day of the
		// month.
		// getDay() returns 0 for Sunday.
		int currentWeekDay = cal.get(Calendar.DAY_OF_WEEK) - 1;
		trailingSpaces = currentWeekDay;
		
		// Log.d(tag, "Week Day:" + currentWeekDay + " is "
		// + getWeekDayAsString(currentWeekDay));
		// Log.d(tag, "No. Trailing space to Add: " + trailingSpaces);
		// Log.d(tag, "No. of Days in Previous Month: " + daysInPrevMonth);

		/*
		 * Ce qui suit construit la list pour le mois, avec tous les détails
		 * qui seront exploités plus bas, lorsque le système demandera la
		 * vue avec getView.
		 * Offset => 0 le jour
		 * Offset => 1 le mois
		 * Offset => 2 l'année
		 * Offset => 3 couleur (de texte)
		 * Offset => 4 Style Cellule
		 */

		// Ajout des cellules pour les noms des jours de la semaine.
		for (int i = 0; i < 7; i++) {
			ArrayList<String> ListShortNameDay = new ArrayList<String>();
			ListShortNameDay.add(getShortDayName(i));
			ListShortNameDay.add(""); // Inutilisé
			ListShortNameDay.add(""); // Inutilisé
			
			ListShortNameDay.add(COLORBLACK); // Couleur police
			ListShortNameDay.add(WEEKDAYS); // Style cellule.
			ListShortNameDay.add("False"); // Event.
			list.add(ListShortNameDay);
		}
		
		
		// Trailing Month days
		for (int i = 0; i < trailingSpaces; i++) {
			ArrayList<String> ListTrailMontDay = new ArrayList<String>();
			// Numéro du jours dans le mois
			ListTrailMontDay.add(nf.format((daysInPrevMonth - trailingSpaces + DAY_OFFSET) + i));
			// Numéro du mois de ce jour
			ListTrailMontDay.add(prevMonthAsString);
			// Numéro de l'année de ce jour
			ListTrailMontDay.add(String.valueOf(prevYear));
			// Couleur de la police
			ListTrailMontDay.add(COLORGREY);
			// Type de la cellule.
			ListTrailMontDay.add(TRAILDAYS);
			// Event.
			ListTrailMontDay.add("False");
			list.add(ListTrailMontDay);
		}

		// Current Month Days
		for (int i = 1; i <= daysInMonth; i++) {
			ArrayList<String> ListCurrentMontDay = new ArrayList<String>();
			ListCurrentMontDay.add(String.valueOf(nf.format(i)));
			ListCurrentMontDay.add(currentMonthName);
			ListCurrentMontDay.add(String.valueOf(year));
			ListCurrentMontDay.add(COLORBLACK);
			ListCurrentMontDay.add(CURRENTDAYS);
			ListCurrentMontDay.add("False");
			list.add(ListCurrentMontDay);
		}

		// Leading Month days
		for (int i = 0; i < list.size() % 7; i++) {
			ArrayList<String> ListLeadingMontDay = new ArrayList<String>();
			ListLeadingMontDay.add(String.valueOf(nf.format(i + 1)));
			ListLeadingMontDay.add(nextMonthAsString);
			ListLeadingMontDay.add(String.valueOf(nextYear));
			ListLeadingMontDay.add(COLORGREY);
			ListLeadingMontDay.add(LEADINGDAYS);
			ListLeadingMontDay.add("False");
			list.add(ListLeadingMontDay);
		}
		
		return list;
	}

	/**
	 * Calcul avec décalage si besoin le nombre d'années et de mois complets
	 * depuis une liste de dates issues de la base de donnée.
	 * 
	 * @param listDates
	 *            Liste des dates à analyser.
	 * @return tableau donnant la décomposition Année, Mois, Jours.
	 */
	public static int[] DataAnalysis(ArrayList<Date> listDates) {
		// nbAnnée, nbMois, nbJours, dateMinAnnee, dateMinMois, dateMinJours 
		int[] Result = { 0, 0, 0 ,-1, -1, -1};
		// Pas de dates ?
		if (listDates.size() == 0) {
			return Result;
		}
		
		// Obtenir la plus basse (les dates sont triées dans l'ordre croissant).
		Date dateMin = listDates.get(0);
		Log.d(tag, "Date la plus basse : " + dateMin.toString());
		
		
		// Sérialiser la date la plus petite
		Calendar cal = Calendar.getInstance();
		cal.setTime(dateMin);
		
		Result[3]= cal.get(Calendar.YEAR);// Date Min Année
		Result[4]= cal.get(Calendar.MONTH);// Date Min Mois
		Result[5]= cal.get(Calendar.DAY_OF_MONTH);// Date Min Jour
		
		// Ajouter un mois, et trouver la date anniversaire du prochain mois.
		Date nextMonthDay = addMonth(dateMin, 1);
		// Log.d(tag, "Date prochain mois : " + nextMonthDay.toString());

		// Calculer le nombre de jours entre ces deux dates.
		long dayToNextMonth = getDaysBetween(dateMin, nextMonthDay);
		// Log.d(tag, "NB jout du mois : " + String.valueOf(dayToNextMonth));
		// Lire la liste des dates et prendre toutes les dates correspondant
		// entre ces
		// dates.

		for (Iterator iterator = listDates.iterator(); iterator.hasNext();) {
			Date currentDate = (Date) iterator.next();

			// Compter le nombre de mois de cette façon et le nombre d'années de
			// cette façon.

			// Un jour de plus.
			Result[2]++;

			if (Result[2] == dayToNextMonth) {
				// Tous les jour du mois sont comptés, on ajout 1
				Result[1]++;
				// Une année passée ?
				if (Result[1] == 12) {
					// Ajout d'une année.
					Result[0]++;
					// Réinit des mois.
					Result[1] = 0;
				}
				// Reinit pour la suite du comptage.
				Result[2] = 0;
				dateMin = currentDate;
				// Log.d(tag , "Date la plus basse : " + dateMin.toString());

				nextMonthDay = addMonth(dateMin, 1);
				// Log.d(tag, "Date prochain mois : " + nextMonthDay.toString());

				// Calculer le nombre de jours entre ces deux dates.
				dayToNextMonth = getDaysBetween(dateMin, nextMonthDay);
				// Log.d(tag,
				//		"NB jout du mois : " + String.valueOf(dayToNextMonth));
			}

		}

		return Result;
	}
	
	/**
	 * Sérialiser une date.
	 */
	/**
	 * Désérialiser une date.
	 */
	
	/**
	 * Ajout un jour à la date passée en paramètre (inutilisé).
	 * 
	 * @param nextMonthDay
	 * @return
	 */
	private static Date addDays(Date date, int nbMonths) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.add(Calendar.DATE, nbMonths);
		return cal.getTime();
	}
	
	/**
	 * La date est-elle entre les deux dates proposées?
	 * @param min date basse.
	 * @param max date haute.
	 * @param d date à tester
	 * @return vrai si la date est comprise entre la date base et haute.
	 */
	private static Boolean dateBetween(Date min, Date max, Date d) {
		return d.after(min) && d.before(max);
	}

	/**
	 * Calcul ne nombre de jours entre 2 dates.
	 * 
	 * @param dateStart
	 *            date de début.
	 * @param dateEnd
	 *            date de fin.
	 * @return
	 */
	private static long getDaysBetween(Date dateStart, Date dateEnd) {
		final long MILLISECONDS_PER_DAY = 1000 * 60 * 60 * 24;
		long delta = dateEnd.getTime() - dateStart.getTime();
		return delta / (MILLISECONDS_PER_DAY);
	}

	/**
	 * Ajout un nombre de mois à la date donnée.
	 * 
	 * @param date
	 *            date de départ
	 * @param nbMonths
	 *            nombre de mois à ajouter.
	 * @return
	 */
	private static  Date addMonth(Date date, int nbMonths) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.add(Calendar.MONTH, nbMonths);
		return cal.getTime();
	}

	/**
	 * Calcul les mois complètement remplit. (les dates sont triées dans un
	 * ordre croissant)
	 * 
	 * @param listDates
	 *            liste complète de toutes les dates Event Done
	 * @return nombre de mois plein.
	 */
	private int getFullMonth(ArrayList<Date> listDates) {
		// Calendrier.
		Calendar cal = new GregorianCalendar();
		cal.setLenient(false);

		int numFullMonth = 0;

		int dayInMonthCount = 0;

		// Intégrité
		if (listDates.size() == 0) {
			return 0;
		}

		// Init, date la plus basse.
		Date date = listDates.get(0);
		cal.setTime(date);

		// Nombre de jours dans le mois.
		int numDaysInMonth = cal.getActualMaximum(Calendar.DAY_OF_MONTH);
		// Numéro du mois le plus bas
		int numMonth = 1 + cal.get(Calendar.MONTH);
		int oldNumMonth = numMonth;
		// Année du mois le plus bas.
		int numYear = cal.get(Calendar.YEAR);
		int oldNumYear = numYear;

		for (Iterator iterator = listDates.iterator(); iterator.hasNext();) {
			date = (Date) iterator.next();

			dayInMonthCount++;
			// Log.d(tag, String.valueOf(dayInMonthCount));

			// Mois complet ?
			if (numDaysInMonth == dayInMonthCount) {
				numFullMonth++;
			}

			// Analyse de la date en cour.
			cal.setTime(date);
			numMonth = 1 + cal.get(Calendar.MONTH);
			numYear = cal.get(Calendar.YEAR);

			// Changement de mois ? d'année ?
			if (oldNumMonth != numMonth || oldNumYear != numYear) {
				oldNumMonth = numMonth;
				oldNumYear = numYear;
				numDaysInMonth = cal.getActualMaximum(Calendar.DAY_OF_MONTH);
				dayInMonthCount = 0;
			}
		}
		// Log.d(tag, String.valueOf(numFullMonth));
		return numFullMonth;
	}

	public static Date addYears(Date date, int nbYears) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.add(Calendar.YEAR, nbYears);
		return cal.getTime();
	}
	
	/**
	 * Splitter de dates donnée sous forme de liste triées.
	 * Les dates sont groupées par mois, puis placées dans une collection.
	 * @param aLDatesSorted fichier contenant les dates.
	 * @return Le tableau de tableau de dates groupées.
	 */
	public static ArrayList<ArrayList<Date>> DateSpliter(
			ArrayList<Date> aLDatesSorted) {
		
		ArrayList<ArrayList<Date>> ListSplitted = new ArrayList<ArrayList<Date>>();
		// Intégrité.
		if (aLDatesSorted == null || aLDatesSorted.size()==0) {
			// Log.i(tag, "Tableau vide !!");
			return ListSplitted; // Tableau vide.
		}
		// Calendrier sans i18n
		// Local par défaut. English.
		Calendar cal = Calendar.getInstance(Locale.ENGLISH);
		
		// On liste toutes les dates.
		Date PrevDate = null;
		ArrayList<Date> CurrentAList = null;
		for (Date date : aLDatesSorted) {
			// Début ?
			if (PrevDate == null) {
				CurrentAList = new ArrayList<Date>();
				ListSplitted.add(CurrentAList);
				PrevDate = date;
			}
			// Changement de mois ?
			int prevNumMonth = -1;
			// Date précedente, si non null. 
			// if (PrevDate!=null) {
			cal.setTime(PrevDate);
			prevNumMonth = cal.get(Calendar.MONTH);
			// }
			// Date courrante.
			cal.setTime(date);
			int currentNumMonth = cal.get(Calendar.MONTH);
			if (prevNumMonth!=currentNumMonth) {
				CurrentAList = new ArrayList<Date>();
				ListSplitted.add(CurrentAList);
				// Log.i(tag, " ");
			}
			
			// Ajout de la date dans la collection courrante.
			CurrentAList.add(date);
			// Log.i(tag, date.toString());
			// La date courrant passe en previous.
			PrevDate = date;
		}
		
		return ListSplitted;
	}
	/**
	 * Rassemble dans une même collection les données présentable dans le calendrier avec
	 * celles des events de la base de données.
	 * @param listDayMonth Tous les jours du mois avec les jours avant après pour un calage régulier
	 * lun. Mar. Mer. Jeu. Ven. Sam. Dim quelque soit le mois. 
	 * @param listMonthEvent liste des evenements pour ce mois. 
	 * @return une liste de type listDayMonth augmenté des events. 
	 */
	public static ArrayList<ArrayList<String>> monthAndEvent(
			ArrayList<ArrayList<String>> listDayMonth,
			ArrayList<Date> listMonthEvent) {
		// Conversion du tableau pour les recherches.
		HashMap<Date, Date> search = new HashMap<Date, Date>();
		Calendar cal = new GregorianCalendar();
		for (int position = 0; position < listMonthEvent.size(); position++) {
			search.put(listMonthEvent.get(position), listMonthEvent.get(position));
		}
		
		// Log.i(tag, String.format("%d", search.size()));
			
		// On commence à 7 car les cellule d'indice 0 à 6 représentent les
		// noms abrégés des jours de la semaine.
		SimpleDateFormat sdf = new SimpleDateFormat("MMMM", Locale.ENGLISH);
		for (int position = 7; position < listDayMonth.size(); position++) {
			// Event pour ce jour ?
			ArrayList<String> ListInfosDay = listDayMonth.get(position);
			
			Date dateMonth = null;
			try {
				dateMonth = sdf.parse(ListInfosDay.get(CalendarFragment.MONTH));
			} catch (ParseException e) {
				// Ne doit pas arriver, la BDD doit rester cohérente.
				Log.e(tag, "Parse Month Error.");
			}
			
			cal.setTime(dateMonth);
			int numMonth = cal.get(Calendar.MONTH);
			cal.set(
					Integer.valueOf(ListInfosDay.get(CalendarFragment.YEAR)), // Nombre
					numMonth, // Mois nom long english.
					Integer.valueOf(ListInfosDay.get(CalendarFragment.DAY))); // Nombre
			Date date = cal.getTime();
			// Jour courant seulement. Les jours de fin mois précédent ou de début
			// de mois suivant ne doivent pas montrer d'events (même s'il y en a).
			if (ListInfosDay.get(CalendarFragment.FONTCOLOR) == CalendarFragment.COLORCURRENT) {
				if (search.containsValue(date)) {
					// Event détécté. Ajout.
					// Log.i(tag, "Ajout Event !");
					ListInfosDay.set(CalendarFragment.EVENT, "True");
				}
			}
		}
		
		return listDayMonth;
	}
}
