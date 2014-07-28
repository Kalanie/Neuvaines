package com.maya.neuvaines;

import android.app.ActionBar;
import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

// Je dédie cette application à Pierre Henri.

public class MainActivity extends Activity {
	/* tag de debugage.*/
	private static final String tag = "MainActivity";
	// Declare Tab Variable
	private ActionBar.Tab tabNeuvaines, tabPayer, tabCalendar, tabStatistics;
	private Fragment fragmentTabNovena = new NovenaFragment();
	private Fragment fragmentTabPrayer = new PrayerFragment();
	private Fragment fragmentTabCalendar = new CalendarFragment();
	private Fragment fragmentTabStatistics = new StatisticsFragment();

	/*
	 * //Méthode qui se déclenchera lorsque vous appuierez sur le bouton menu du
	 * téléphone
	 * 
	 * @Override public boolean onCreateOptionsMenu(Menu menu) {
	 * 
	 * //Création d'un MenuInflater qui va permettre d'instancier un Menu XML en
	 * un objet Menu MenuInflater inflater = getMenuInflater(); //Instanciation
	 * du menu XML spécifier en un objet Menu inflater.inflate(R.layout.menu,
	 * menu);
	 * 
	 * //Il n'est pas possible de modifier l'icône d'entête du sous-menu via le
	 * fichier XML on le fait donc en JAVA
	 * menu.getItem(0).getSubMenu().setHeaderIcon
	 * (R.drawable.calendar_bg_orange);
	 * 
	 * // Toast toast = Toast.makeText(this.getBaseContext(), "Coucou",
	 * Toast.LENGTH_SHORT); // toast.show(); // Log.i("main", "ContextMenu");
	 * return true;
	 * 
	 * }
	 */

	// Méthode qui se déclenchera au clic sur un item
	public boolean onOptionsItemSelected(MenuItem item) {
		// On regarde quel item a été cliqué grâce à son id et on déclenche une
		// action
		switch (item.getItemId()) {
		case R.id.option:
			Toast.makeText(MainActivity.this, "Option", Toast.LENGTH_SHORT)
					.show();
			return true;
		case R.id.texteSize:
			Toast.makeText(MainActivity.this, "Texte Size", Toast.LENGTH_SHORT)
					.show();
			return true;
		case R.id.notify:
			Toast.makeText(MainActivity.this, "Activer les notifications",
					Toast.LENGTH_SHORT).show();
			return true;
		case R.id.dataSaving:
			Toast.makeText(MainActivity.this, "Sauvegarder les données",
					Toast.LENGTH_SHORT).show();
			return true;
		case R.id.quitter:
			// Pour fermer l'application il suffit de faire finish()
			finish();
			return true;
		}
		return false;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_main);

		ActionBar actionBar = getActionBar();

		// Hide Action bar Icon
		actionBar.setDisplayShowHomeEnabled(false);

		// Hide Action bar Title
		actionBar.setDisplayShowTitleEnabled(false);

		// Create Action bar Tabs
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

		// Set Tab Icon and Titles
		tabNeuvaines = actionBar.newTab().setIcon(
				R.drawable.icon_neuvaines_config);
		tabPayer = actionBar.newTab().setIcon(R.drawable.icon_priere_config);
		tabCalendar = actionBar.newTab().setIcon(
				R.drawable.icon_calendar_config);
		tabStatistics = actionBar.newTab().setIcon(
				R.drawable.icon_statistics_config);

		// Set Tab Listeners
		tabNeuvaines.setTabListener(new TabListener<NovenaFragment>(this,
				"Neuvaines", NovenaFragment.class, fragmentTabNovena));

		tabPayer.setTabListener(new TabListener<PrayerFragment>(this,
				"TabPayer", PrayerFragment.class, fragmentTabPrayer));

		tabCalendar.setTabListener(new TabListener<CalendarFragment>(this,
				"TabCalendar", CalendarFragment.class, fragmentTabCalendar));

		tabStatistics.setTabListener(new TabListener<StatisticsFragment>(this,
				"TabStatistics", StatisticsFragment.class,
				fragmentTabStatistics));

		// Add tabs to actionbar
		actionBar.addTab(tabNeuvaines);
		actionBar.addTab(tabPayer);
		actionBar.addTab(tabCalendar);
		actionBar.addTab(tabStatistics);

		// Remettre la selection précedente.
		if (savedInstanceState != null) {
			int index = savedInstanceState.getInt("index");
			actionBar.setSelectedNavigationItem(index);
		} else {
			// Sélection de l'onglet numéro 2. Premier numéro 0.
			// On selectionne la prière par défaut.
			actionBar.setSelectedNavigationItem(1);
		}

	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);

		// Mémoriser l'index utilisé dans la barre de navigation.
		int i = getActionBar().getSelectedNavigationIndex();
		outState.putInt("index", i);

		// (Prière) Mémoriser la position dans le TextView du ScrollView pour
		// donner un équivalent en cas de rotation
		final ScrollView scrollView = (ScrollView) findViewById(R.id.scrollViewPriere);
		if (scrollView != null) {
			final LinearLayout linearLayout = (LinearLayout) scrollView
					.getChildAt(0);
			if (linearLayout != null) {
				final RelativeLayout relativeLayout = (RelativeLayout) linearLayout
						.getChildAt(0);
				if (relativeLayout != null) {
					final TextView textView = (TextView) relativeLayout
							.getChildAt(0);
					if (textView != null && textView.getLayout() != null) {
						final int firstVisableLineOffset = textView.getLayout()
								.getLineForVertical(scrollView.getScrollY());
						final int firstVisableCharacterOffset = textView
								.getLayout().getLineStart(
										firstVisableLineOffset);
						outState.putInt(
								"ScrollViewContainerTextViewFirstVisibleCharacterOffset",
								firstVisableCharacterOffset);
					}
				}
			}
		}

	}

	/*
	 * private Menu m = null;
	 * 
	 * @Override public boolean onCreateOptionsMenu(Menu menu) { MenuInflater
	 * inflater = getMenuInflater(); inflater.inflate(R.menu.game_menu, menu); m
	 * = menu; return true; }
	 */

	@Override
	protected void onRestoreInstanceState(Bundle savedInstanceState) {
		super.onRestoreInstanceState(savedInstanceState);

		// (Prière) Restituer la postion dans le texte en cas de rotation
		final int firstVisableCharacterOffset = savedInstanceState
				.getInt("ScrollViewContainerTextViewFirstVisibleCharacterOffset");

		final ScrollView scrollView = (ScrollView) findViewById(R.id.scrollViewPriere);
		if (scrollView != null) {
			scrollView.post(new Runnable() {
				public void run() {
					final TextView textView = (TextView) findViewById(R.id.textViewPriere);
					final int firstVisableLineOffset = textView.getLayout()
							.getLineForOffset(firstVisableCharacterOffset);
					final int pixelOffset = textView.getLayout().getLineTop(
							firstVisableLineOffset);
					scrollView.scrollTo(0, pixelOffset);
				}
			});
		}

		// (promesses) Restituer la postion dans le texte en cas de rotation
		final int firstVisableCharacterOffset2 = savedInstanceState
				.getInt("ScrollViewContainerTextViewFirstVisibleCharacterOffset2");

		/*
		 * final ScrollView scrollView2 = (ScrollView)
		 * findViewById(R.id.scrollViewPromesses); if (scrollView2 != null) {
		 * scrollView2.post(new Runnable() { public void run() { final TextView
		 * textView2 = (TextView) findViewById(R.id.textViewPromesses); final
		 * int firstVisableLineOffset2 = textView2.getLayout()
		 * .getLineForOffset(firstVisableCharacterOffset2); final int
		 * pixelOffset2 = textView2.getLayout().getLineTop(
		 * firstVisableLineOffset2); scrollView2.scrollTo(0, pixelOffset2); }
		 * });
		 * 
		 * }
		 */
	}

}