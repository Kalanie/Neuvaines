package com.maya.neuvaines;

/**
 * Enregister les r�f�rences des activit�s en static pour pouvoir y acc�der
 * depuis partout.
 * Risqu�, � supprimer. Est cause d'erreurs.
 * @author maya
 * 
 */
public class ObjectsRegister {
	private static CalendarFragment CalendarFragment;
	private static StatisticsFragment StatisticsFragment;
	private static PrayerFragment PrayerFragment;

	public static CalendarFragment getCalendarFragment() {
		return CalendarFragment;
	}

	public static void setCalendarFragment(CalendarFragment calendarFragment) {
		CalendarFragment = calendarFragment;
	}

	public static StatisticsFragment getStatisticsFragment() {
		return StatisticsFragment;
	}

	public static void setStatisticsFragment(
			StatisticsFragment statisticsFragment) {
		StatisticsFragment = statisticsFragment;
	}

	public static void setPrayerFragment(PrayerFragment prayerFragment) {
		PrayerFragment = prayerFragment;
	}

	public static PrayerFragment getPrayerFragment() {
		return PrayerFragment;
	}
	
}
