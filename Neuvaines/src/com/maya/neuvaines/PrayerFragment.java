package com.maya.neuvaines;

import java.util.TreeMap;

import android.app.Fragment;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.maya.neuvaines.ScrollViewExt.OnScrollStoppedListener;

public class PrayerFragment extends Fragment {

	/** Tag de débugage */
	private String tag = "PrayerFragment";
	/** Toutes les neuvaines sous forme de collection. */
	private TreeMap<Integer, TreeMap<Integer, String>> novenas;
	/** Jour en cours dans cette neuvaine. */
	private int currentDayNovena;

	@SuppressWarnings("deprecation")
	@Override
	public View onCreateView(LayoutInflater inflater,
			final ViewGroup container, Bundle savedInstanceState) {
		final View rootView = inflater.inflate(R.layout.prayer, container,
				false);
		// Position dans le scrollView
		final ScrollViewExt scroll = (ScrollViewExt) rootView
				.findViewById(R.id.scrollViewPriere);

		scroll.post(new Runnable() {
			public void run() {
				// On va chercher la position
				SharedPreferences settings = rootView.getContext()
						.getSharedPreferences("UserInfo", 0);
				scroll.scrollTo(0, Integer.valueOf(settings.getString(
						"LinePosition", "0")));
			}
		});

		scroll.setScrollViewListener(new ScrollViewListener() {

			@Override
			public void onScrollChanged(ScrollViewExt scrollView, int x, int y,
					int oldx, int oldy) {
				// Coder ici un event à faire en cas de scroll.
			}

		});

		scroll.setOnTouchListener(new OnTouchListener() {

			public boolean onTouch(View v, MotionEvent event) {

				if (event.getAction() == MotionEvent.ACTION_UP) {
					scroll.startScrollerTask();
				}

				return false;
			}
		});

		scroll.setOnScrollStoppedListener(new OnScrollStoppedListener() {

			public void onScrollStopped() {

				// Ici on mémorise la position du scroll.
				if (getActivity() != null) {
					SharedPreferences settings = getActivity()
							.getSharedPreferences("UserInfo", 0);
					if (settings != null) {
						SharedPreferences.Editor editor = settings.edit();
						if (editor != null) {
							editor.putString("LinePosition",
									String.valueOf(scroll.getScrollY()));
							editor.commit();
						}
					}
				}
				// Log.i("TEST", "stopped");
			}
		});

		final TextView PayerView = (TextView) rootView
				.findViewById(R.id.textViewPriere);

		// Lecture de la sauvegarde
		SharedPreferences settings = rootView.getContext()
				.getSharedPreferences("UserInfo", 0);

		// Par défaut la neuvaine 0 est choisie (offset 0), sinon lecture de la
		// sauvegarde.
		Integer currentNovena = settings
				.getInt(NovenaFragment.CURRENTNOVENA, 0);

		// Par défaut le jour 0 est choisit (offset 0), sinon lecture de la
		// sauvegarde.
		currentDayNovena = settings.getInt(NovenaFragment.CURRENTDAYNOVENA, 0);

		// Init listes de toutes les neuvaines avec tous les textes.
		novenas = NovenaFragment.getNovenas();

		// Neuvaine en cour
		final TreeMap<Integer, String> novena = novenas.get(currentNovena);

		// Jour en cour
		PayerView.setText(novena.get(currentDayNovena));

		// Ecouteur pour mémoriser la position dans le texte.
		PayerView.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				// Toast.makeText(getBaseContext(),String.valueOf(scoll.getScrollY()),
				// Toast.LENGTH_SHORT).show();
				SharedPreferences settings = getActivity()
						.getSharedPreferences("UserInfo", 0);
				SharedPreferences.Editor editor = settings.edit();
				editor.putString("LinePosition",
						String.valueOf(scroll.getScrollY()));
				editor.commit();
			}
		});

		// Lecture et affichage du titre de la neuvaine en cours.
		final TextView titleView = (TextView) rootView
				.findViewById(R.id.textViewTitle);
		titleView.setText(novena.get(NovenaFragment.TITLEINDICE));

		// Texte pour le numéro du jour
		final TextView dayText = (TextView) rootView
				.findViewById(R.id.textViewDayNumber);

		// On met à jour le texte indiquant dans quel jour on est.
		dayText.setText(String.format("%s %d", getText(R.string._day),
				currentDayNovena + 1));
		
		final TextView btnTopPrev = (TextView) rootView
				.findViewById(R.id.buttonTopPrev);
		final TextView btnTopNext = (TextView) rootView
				.findViewById(R.id.buttonTopNext);
		final TextView btnBottomPrev = (TextView) rootView
				.findViewById(R.id.buttonBottomPrev);
		final TextView btnBottomNext = (TextView) rootView
				.findViewById(R.id.buttonBottomNext);
		
		// Style des bouttons
		if (currentDayNovena == 8) {
			// Style bouton Next invisibles.
			Log.i(tag, "Bouton Next gris");
			// Boutons Prev visibles
			btnBottomNext.setVisibility(View.INVISIBLE);
			btnTopNext.setVisibility(View.INVISIBLE);
		}
		
		if (currentDayNovena == 0) {
			// Boutons Prev invisibles.
			btnBottomPrev.setVisibility(View.INVISIBLE);
			btnTopPrev.setVisibility(View.INVISIBLE);
		}
		
		
		// Ecouteur prev
		OnClickListener onClickListenerPrev = new OnClickListener() {

			@Override
			public void onClick(View v) {
				// Prière précédente si possible.
				if (currentDayNovena > 0) {
					currentDayNovena--;

					PayerView.setText(novena.get(currentDayNovena));

					if (currentDayNovena == 0) {
						// Boutons Prev invisibles.
						btnBottomPrev.setVisibility(View.INVISIBLE);
						btnTopPrev.setVisibility(View.INVISIBLE);
					}
					
					// Boutons Next visibles
					btnBottomNext.setVisibility(View.VISIBLE);
					btnTopNext.setVisibility(View.VISIBLE);
					
					// On met à jour le texte indiquant dans quel jour on est.
					dayText.setText(String.format("%s %d",
							getText(R.string._day), currentDayNovena + 1));

					// On sauve la valeur en persistance.
					SharedPreferences settings = getActivity()
							.getSharedPreferences("UserInfo", 0);
					SharedPreferences.Editor editor = settings.edit();
					editor.putInt(NovenaFragment.CURRENTDAYNOVENA,
							currentDayNovena);
					editor.commit();

				}

			}

		};

		// Ecouteur next
		OnClickListener onClickListenerNext = new OnClickListener() {

			@Override
			public void onClick(View v) {
				// Prière suivante si possible (-1 car la taille n'est pas
				// l'indice).
				if (currentDayNovena < NovenaFragment.DAYS.length - 1) {
					currentDayNovena++;
					PayerView.setText(novena.get(currentDayNovena));
					// Scroll au tout début du texte.
					scroll.scrollTo(0, 0);
					// Style du bouton grisé (indice 8)?
					if (currentDayNovena == 8) {
						// Boutons Next invisibles
						btnBottomNext.setVisibility(View.INVISIBLE);
						btnTopNext.setVisibility(View.INVISIBLE);
					}

					// Boutons Prev visibles
					btnBottomPrev.setVisibility(View.VISIBLE);
					btnTopPrev.setVisibility(View.VISIBLE);
					
					// On met à jour le texte indiquant dans quel jour on est.
					dayText.setText(String.format("%s %d",
							getText(R.string._day), currentDayNovena + 1));
					
					// Toast personalisé luminion
					LayoutInflater inflater = getActivity()
							.getLayoutInflater();
					View view = inflater.inflate(
							R.layout.cust_toast_layout_day,
							(ViewGroup) rootView
									.findViewById(R.id.relativeLayout1));

					Toast toast = new Toast(getActivity().getBaseContext());
					toast.setView(view);
					toast.show();

					// On sauve la valeur en persistance.
					SharedPreferences settings = getActivity()
							.getSharedPreferences("UserInfo", 0);
					SharedPreferences.Editor editor = settings.edit();
					editor.putInt(NovenaFragment.CURRENTDAYNOVENA,
							currentDayNovena);
					editor.commit();

				}

			}
		};

		// Attribution des écouteurs
		btnTopPrev.setOnClickListener(onClickListenerPrev);
		btnBottomPrev.setOnClickListener(onClickListenerPrev);
		btnTopNext.setOnClickListener(onClickListenerNext);
		btnBottomNext.setOnClickListener(onClickListenerNext);

		// Partager la référence de ce fragment.
		ObjectsRegister.setPrayerFragment(this);
		return rootView;
	}

}