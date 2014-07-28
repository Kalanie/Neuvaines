package com.maya.neuvaines;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.TreeMap;

import com.maya.neuvaines.R;

import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;
import android.widget.LinearLayout.LayoutParams;
import android.app.Fragment;
import android.content.SharedPreferences;

public class NovenaFragment extends Fragment {

	/* Tag de débugage. */
	private static final String tag = "SelectFragment";

	/* Noms de clés pour la persistance. */
	public static final String CURRENTNOVENA = "CurrentNovena";
	public static final String CURRENTDAYNOVENA = "daynovena";
	
	public static final String TITLE = "_title";
	public static final Integer TITLEINDICE = -1;
	public static final String[] DAYS = {"_1", "_2", "_3", "_4", "_5", "_6", "_7", "_8", "_9"}; 
	

	private HashMap<String, Integer> mapString;
	private Button currentButtonNovena;
	
	// Les neuvaine sous forme de collection.
	private static TreeMap<Integer, TreeMap<Integer, String>> novenas;
	
	@SuppressWarnings("deprecation")
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		final View rootView = inflater.inflate(R.layout.select, container,
				false);

		// Tableau
		final String[] neuvaine = getResources().getStringArray(R.array.novena);
		
		// Obtenir le layout des selections.
		GridLayout selectLayout = (GridLayout) rootView
				.findViewById(R.id.GridLayoutNovena);

		// Créer le tableau comprenant toutes les infos de neuvaines.
		// Novena1 => Novena1_title (value), Novena1_1 (value) ...
		novenas = buildNovenasCollections();

		// Bouton de la neuvaine en cour ?
		SharedPreferences settings = rootView.getContext()
				.getSharedPreferences("UserInfo", 0);
		// Par défaut la neuvaine 0 est choisie.
		int currentNovena = settings.getInt(CURRENTNOVENA, 0);

		// Ajouter un bouton par neuvaine.
		for (int i = 0; i < neuvaine.length; i++) {
			// Log.i(tag, neuvaine[i]);
			Button novenaButton = new Button(rootView.getContext());
			GridLayout.LayoutParams param = new GridLayout.LayoutParams();
			param.height = LayoutParams.MATCH_PARENT;
			param.width = LayoutParams.MATCH_PARENT;
			param.rightMargin = 5;
			param.topMargin = 5;
			param.setGravity(Gravity.LEFT);
			param.columnSpec = GridLayout.spec(0);
			param.rowSpec = GridLayout.spec(i);
			novenaButton.setLayoutParams(param);
			// Introspection pour trouver l'id dans la classe auto générée.
			int id = getID().get(neuvaine[i] + TITLE);
			novenaButton.setText(getText(id));
			novenaButton.setId(i);

			// Est-ce la neuvaine en cour ?
			if (currentNovena == i) {
				novenaButton.setBackgroundDrawable(getResources().getDrawable(
						R.drawable.my_shapeselect));
				// Mémorise le bouton de la neuvaine en cour.
				currentButtonNovena = novenaButton;
			} else {
				novenaButton.setBackgroundDrawable(getResources().getDrawable(
						R.drawable.my_shape));
			}

			novenaButton.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// voir la vue comme un button
					Button currentButton = (Button) v;
					// Enregistrer le choix utilisateur en persistance
					SharedPreferences settings = getActivity()
							.getSharedPreferences("UserInfo", 0);
					SharedPreferences.Editor editor = settings.edit();
					// Choix de neuvaine
					editor.putInt(CURRENTNOVENA, currentButton.getId());
					editor.commit();
					// Jour 1 (indice 0)
					// TODO si l'indice n'est pas à zéro (neuvaine en cours) alors demander confirmation
					editor.putInt(CURRENTDAYNOVENA, 0);
					editor.commit();
					
					
					// Ce bouton prend le style select et le précent select
					// repasse au style normal.
					Button prevNovenaSelect = currentButtonNovena;
					currentButtonNovena = currentButton;
					prevNovenaSelect.setBackgroundDrawable(getResources()
							.getDrawable(R.drawable.my_shape));
					currentButtonNovena.setBackgroundDrawable(getResources()
							.getDrawable(R.drawable.my_shapeselect));

					/*
					 * 
					 * Modifier le texte de prière. Mettre en place le système
					 * qui compte les jours dans la prière.
					 */
					// Obtenir depuis la hashMap d'introspection le prière
					// complète et la transmettre
					// au fragment prière

					// A l'ouverture du fragment prière le bon texte s'affichera
					// Faire une persistance pour les jours déjà fait de la
					// neuvaine(calendrier ?).

					// ObjectsRegister.getPrayerFragment().
				}
			});

			selectLayout.addView(novenaButton);

		}

		// Au bouton créer un aperçu ou une séléction.
		return rootView;
	}

	/**
	 * Crée une collection ordonnée des neuvaines avec leur valeurs.
	 * 
	 * @return TreeMap<String, TreeMap<String, String>> exemple Novena1 =>
	 *         Novena1_title (value), Novena1_1 (value) ...
	 */
	private TreeMap<Integer, TreeMap<Integer, String>> buildNovenasCollections() {
		// Persistances neuvaines, 0,1,2,3...
		// Persistance jour neuvaine -1,0,1,2,3,4 ... (-1 titre; 0,1,2, => jours)
		TreeMap<Integer, TreeMap<Integer, String>> novenas = new TreeMap<Integer, TreeMap<Integer,String>>();
		final String[] neuvaine = getResources().getStringArray(R.array.novena);
		for (int numNovena = 0; numNovena < neuvaine.length; numNovena++) {
			// Collection pour la neuvaine n°i
			TreeMap<Integer, String> novena = new TreeMap<Integer, String>();
			novenas.put(numNovena, novena);
			// Ajout du text pour ces neuvaines.
			// Le titre
			int idTitle = getID().get(neuvaine[numNovena] + TITLE);
			getText(idTitle);
			novena.put(TITLEINDICE, (String) getText(idTitle));
			// Les jours
			for (int numDay = 0; numDay < DAYS.length; numDay++) {
				int idDay = getID().get(neuvaine[numNovena] + DAYS[numDay]);
				novena.put(numDay, String.valueOf(getText(idDay)));
			}
		}
		return novenas;
	}

	/**
	 * Crée par introspection de la classe R, une collection clé/valeur. (on ne
	 * s'interresse qu'au tableau de chaines)
	 * 
	 * @return HashMap<String, Integer> String nom de la clé, Integer valeur de
	 *         la clé.
	 */
	private HashMap<String, Integer> getID() throws RuntimeException {
		// Init de la collection ?
		if (mapString == null) {
			mapString = new HashMap<String, Integer>();
			// Obtenir tous les champs déclarés. (data-members):
			Field[] fields = R.string.class.getDeclaredFields();

			// parcourir le tableau de champs et obtenir les valeurs :
			try {
				for (int i = 0; i < fields.length; i++) {
					// names[i] = fields[i].getName();
					// stringsID[i] = fields[i].getInt(null);
					mapString.put(fields[i].getName(), fields[i].getInt(null));
				}
			} catch (Exception ex) {
				// Ne doit pas arriver.
			}
		}
		return mapString;
	}

	/**
	 * Getter pour la collection de neuvaines.
	 * @return TreeMap<Integer, TreeMap<Integer, String>> Les neuvaines sont numérotés et les jours aussi
	 * avec -1 comme indice pour le titre.
	 */
	public static TreeMap<Integer, TreeMap<Integer, String>> getNovenas() {
		return novenas;
	}

}