import android.widget.ScrollView;
import android.widget.TextView;

import com.maya.neuvaines.R;

		// (Promesses) Mémoriser la position dans le TextView du ScrollView pour
		// donner un équivalent en cas de rotation

		final ScrollView scrollView2 = (ScrollView) findViewById(R.id.scrollViewPromesses);
		if (scrollView2 != null) {
			final TextView textView2 = (TextView) scrollView2.getChildAt(0);
			final int firstVisableLineOffset2 = textView2.getLayout()
					.getLineForVertical(scrollView2.getScrollY());
			final int firstVisableCharacterOffset2 = textView2.getLayout()
					.getLineStart(firstVisableLineOffset2);
			outState.putInt(
					"ScrollViewContainerTextViewFirstVisibleCharacterOffset2",
					firstVisableCharacterOffset2);
		}

android1:text="@string/priere"
                		<GridLayout
                android1:id="@+id/gridButtonPrevNextDay"
                android:layout_width="wrap_content"
                android:layout_height="wrap_content"
                android:layout_marginLeft="48dp"
                android:layout_marginRight="48dp"
                android:columnCount="2" >

                <Button
                    android:id="@+id/buttonPrevDay"
                    android:layout_width="wrap_content"
                    android:layout_height="wrap_content"
                    android:layout_column="0"
                    android:background="@drawable/my_shape"
                    android:padding="7dp"
                    android:text="@string/prevday" />

                <Button
                    android:id="@+id/buttonNextDay"
                    android:layout_width="wrap_content"
                    android:layout_height="wrap_content"
                    android:layout_column="1"
                    android:background="@drawable/my_shape"
                    android:padding="7dp"
                    android:text="@string/nextday" />
            </GridLayout>    

// Bouton de validation et remonter au début.
		final Button ButtonValidateAndGoUp = (Button) rootView
				.findViewById(R.id.buttonValidateAndGoUp);
		ButtonValidateAndGoUp.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// Retour au tout début.
				scroll.scrollTo(0, 0);
				// On mémorise la nouvelle position.

				// Ici on mémorise la position du scroll.
				SharedPreferences settings = getActivity()
						.getSharedPreferences("UserInfo", 0);
				SharedPreferences.Editor editor = settings.edit();
				editor.putString("LinePosition", "0");
				editor.commit();
				
				// Ajout d'un event pour ce jour Done dans la base de donnée.

				Calendar cal = Calendar.getInstance();
				cal.setTime(new Date());
				String day = String.valueOf(cal.get(Calendar.DAY_OF_MONTH));
				// NB : Commence à 0, janvier = 0
				String month = String.valueOf(1 + cal.get(Calendar.MONTH));
				String year = String.valueOf(cal.get(Calendar.YEAR));
				// Log.i(tag, "Date lue dans la base : " + day + " " + month + " " + year);
				DBController MyDB = DBController.getDBController(getActivity()
						.getApplicationContext());
				
				// Obtenir la date depuis année, mois jour dans la base.
				HashMap<String, String> MyDate = MyDB.getDateInfo(year, month,
						day);
				// stat avant.
				int[] statBefore = Statistics.getTabDayMonthYear(MyDB);
				int[] statAfter = null;
				// Log.i(tag, "Test de passage");
				// Valeur de Done absente pour cette Date ?
				if (MyDate.isEmpty()) {
					// Log.i(tag, "Date vide");
					// Ajout.
					MyDB.insertDate(year, month, day);
					// Mise à jour du calendrier.
					if (ObjectsRegister.getCalendarFragment() != null) {
						ObjectsRegister.getCalendarFragment()
								.setGridCellAdapterToDate(
										Integer.valueOf(month),
										Integer.valueOf(year));
					}
					
					statAfter = Statistics.getTabDayMonthYear(MyDB);
					// Mise à jour des statistiques
					//if (ObjectsRegister.getStatisticsFragment() != null) {
					//	ObjectsRegister.getStatisticsFragment().computeStats();
					//	Log.i(tag, "Compute stats.");
					//}
					
					// Vérifier un changement de jour/ mois / année.
					int[] statToday = { statAfter[0] - statBefore[0],
							statAfter[1] - statBefore[1],
							statAfter[2] - statBefore[2] };

					// Un jour de plus !
					if (statToday[2] == 1) {
						// Log.i(tag, "Un jour de plus !");
						// Toast personalisé
						LayoutInflater inflater = getActivity()
								.getLayoutInflater();
						View view = inflater.inflate(
								R.layout.cust_toast_layout_day,
								(ViewGroup) rootView
										.findViewById(R.id.relativeLayout1));

						Toast toast = new Toast(getActivity().getBaseContext());
						toast.setView(view);
						toast.show();
					}
					
					// Un mois de plus, cierge moyen.
					if (statToday[1] == 1) {
						Log.i(tag, "Un mois de plus, cierge moyen.");
						// Toast personalisé
						LayoutInflater inflater = getActivity()
								.getLayoutInflater();
						View view = inflater.inflate(
								R.layout.cust_toast_layout_month,
								(ViewGroup) rootView
										.findViewById(R.id.relativeLayout1));

						Toast toast = new Toast(getActivity().getBaseContext());
						toast.setView(view);
						toast.show();
					}
					
					// Un an de plus cierge Pascal.
					if (statToday[0] == 1) {
						Log.i(tag, "Un an de plus cierge Pascal.");
						// Toast personalisé
						LayoutInflater inflater = getActivity()
								.getLayoutInflater();
						View view = inflater.inflate(
								R.layout.cust_toast_layout_year,
								(ViewGroup) rootView
										.findViewById(R.id.relativeLayout1));

						Toast toast = new Toast(getActivity().getBaseContext());
						toast.setView(view);
						toast.show();
					}


				}

			}
		});
      		
      					Toast toast = Toast.makeText(getActivity().getApplicationContext(), "Coucou", Toast.LENGTH_LONG);
					toast.show();
      
      
        <LinearLayout
            android:id="@+id/linearLayoutNovena"
            android:layout_width="match_parent"
            android:layout_height="wrap_content"
            android:orientation="vertical" >
        </LinearLayout>
        
        
        			
			// rootView.findViewById(R.drawable.luminion);
			// ImageView iw = (ImageView) getView().findViewById(R.drawable.luminion);
			
			if (iw==null) {
				Log.i(tag, "iw null!");
			}
			
	        GridLayout.LayoutParams paramImage = new GridLayout.LayoutParams();
	        paramImage.height = LayoutParams.WRAP_CONTENT;
	        paramImage.width = LayoutParams.WRAP_CONTENT;
	        paramImage.rightMargin = 5;
	        paramImage.topMargin = 5;
	        paramImage.setGravity(Gravity.CENTER);
	        paramImage.columnSpec = GridLayout.spec(2);
	        paramImage.rowSpec = GridLayout.spec(i);
	        iw.setLayoutParams (paramImage);
	        selectLayout.addView(iw);
	        
	        			Button novenaButtonIMG = new Button(rootView.getContext());
			GridLayout.LayoutParams paramIMG = new GridLayout.LayoutParams();
			paramIMG.height = LayoutParams.MATCH_PARENT;
			paramIMG.width = LayoutParams.WRAP_CONTENT;
			paramIMG.rightMargin = 1;
			paramIMG.topMargin = 1;
			
			paramIMG.setGravity(Gravity.RIGHT);
			paramIMG.columnSpec = GridLayout.spec(1);
			paramIMG.rowSpec = GridLayout.spec(i);
	        novenaButtonIMG.setLayoutParams(paramIMG);
			novenaButtonIMG.setBackgroundDrawable(getResources().getDrawable(
					R.drawable.luminion));
			novenaButtonIMG.setHeight(89);
			novenaButtonIMG.setWidth(89);
			
			selectLayout.addView(novenaButtonIMG);