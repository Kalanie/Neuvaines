package com.maya.neuvaines;

import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.net.MalformedURLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import com.maya.neuvaines.R;
import com.maya.neuvaines.persitance.DBController;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.text.Html;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.itextpdf.text.BadElementException;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Image;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

public class ExportActivity extends Activity {
	// Log debug.
	private String tag = "ExportActivity";

	private BaseFont bfBold;
	//
	private ArrayList<Image> list;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.dataexport);

		// Listening clic on Export Button.
		View _rootView = getWindow().getDecorView().getRootView();
		Button buttonExport = (Button) _rootView
				.findViewById(R.id.buttonExport);
		buttonExport.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				// Variables communes.
				File rwSharedFolder = null;
				String uniqueFileName = null;
				File folderAndFileName = null;
				ArrayList<ArrayList<Date>> splittedTab = null;
				String text = null;
				
				// Clear champ
				TextView textViewPathName = (TextView) findViewById(R.id.textViewPathName);
				textViewPathName.setText("");
				
				// Vérifier l'état du choix utilisateur
				RadioGroup radioGroup = (RadioGroup) findViewById(R.id.radioGroup1);
				int checkedId = radioGroup.getCheckedRadioButtonId();
				
				switch (checkedId) {

				case -1: // Rien de coché ?
					Log.v(tag, "Choices cleared!");
					break;

				case R.id.radioMailOnly:
					splittedTab = getSplitedTab();
					String content = htmlTextBuilder(splittedTab);
					sendMailOnly(content);
					break;
					
				case R.id.radioMailAndPDF:
					rwSharedFolder = getAvailableFolder();
					if (rwSharedFolder != null) {
						uniqueFileName = buildUniqueFileName();
						folderAndFileName = new File(rwSharedFolder, String
								.format("%s.%s", uniqueFileName, "pdf"));
						splittedTab = getSplitedTab();
						// On enrichit le fichier.
						folderAndFileName = pdfBuilder(folderAndFileName,
								splittedTab);
						if (folderAndFileName == null) {
							// Erreur écriture du fichier.
							Log.e(tag, "File write failed");
							Toast.makeText(getApplicationContext(),
									getText(R.string.FileWriteFailed),
									Toast.LENGTH_LONG).show();
						} else {
							textViewPathName.setText(folderAndFileName.toString());
							sendMailWithFileData(folderAndFileName);
						}

					} else {
						// Pas de quoi écrire un fichier.
						Toast.makeText(getApplicationContext(),
								getText(R.string.ExternalFolderUnavailable),
								Toast.LENGTH_LONG).show();
					}
					break;

				case R.id.radioMailAndTextFile:
					rwSharedFolder = getAvailableFolder();
					uniqueFileName = buildUniqueFileName();
					if (rwSharedFolder != null) {
						folderAndFileName = new File(rwSharedFolder, String
								.format("%s.%s", uniqueFileName, "txt"));
						splittedTab = getSplitedTab();
						text = textBuilder(splittedTab);
						try {
							Writer writer = new BufferedWriter(new FileWriter(
									folderAndFileName));
							writer.write(text);
							writer.close();
							textViewPathName.setText(folderAndFileName.toString());
							sendMailWithFileData(folderAndFileName);
						} catch (IOException e) {
							// Erreur écriture du fichier.
							Log.e(tag, "File write failed");
							Toast.makeText(getApplicationContext(),
									getText(R.string.FileWriteFailed),
									Toast.LENGTH_LONG).show();
						}

					} else {
						// Pas de quoi écrire un fichier.
						Toast.makeText(getApplicationContext(),
								getText(R.string.ExternalFolderUnavailable),
								Toast.LENGTH_LONG).show();
					}

					break;

				case R.id.radioDMPDF:
					rwSharedFolder = getAvailableFolder();
					if (rwSharedFolder != null) {
						uniqueFileName = buildUniqueFileName();
						folderAndFileName = new File(rwSharedFolder, String
								.format("%s.%s", uniqueFileName, "pdf"));
						splittedTab = getSplitedTab();
						// On enrichit le fichier.
						folderAndFileName = pdfBuilder(folderAndFileName,
								splittedTab);
						if (folderAndFileName == null) {
							// Erreur écriture du fichier.
							Log.e(tag, "File write failed");
							Toast.makeText(getApplicationContext(),
									getText(R.string.FileWriteFailed),
									Toast.LENGTH_LONG).show();
						} else {
							// intent view
							textViewPathName.setText(folderAndFileName.toString());
							openFile(folderAndFileName, "application/pdf");
						}

					} else {
						// Pas de quoi écrire un fichier.
						Toast.makeText(getApplicationContext(),
								getText(R.string.ExternalFolderUnavailable),
								Toast.LENGTH_LONG).show();
					}

					break;
					
				case R.id.radioDMText:
					rwSharedFolder = getAvailableFolder();
					if (rwSharedFolder != null) {
						uniqueFileName = buildUniqueFileName();
						folderAndFileName = new File(rwSharedFolder, String
								.format("%s.%s", uniqueFileName, "txt"));
						splittedTab = getSplitedTab();
						text = textBuilder(splittedTab);
						try {
							Writer writer = new BufferedWriter(new FileWriter(
									folderAndFileName));
							writer.write(text);
							writer.close();
							textViewPathName.setText(folderAndFileName.toString());
							openFile(folderAndFileName, "text/plain");
						} catch (IOException e) {
							// Erreur écriture du fichier.
							Log.e(tag, "File write failed");
							Toast.makeText(getApplicationContext(),
									getText(R.string.FileWriteFailed),
									Toast.LENGTH_LONG).show();
						}

					} else {
						// Pas de quoi écrire un fichier.
						Toast.makeText(getApplicationContext(),
								getText(R.string.ExternalFolderUnavailable),
								Toast.LENGTH_LONG).show();
					}

					break;
				default: // Un bouton radio inconnu ???
					Log.v(tag, "Huh?");
					break;
				}

			}

		});

		// Bouton retour.
		Button buttonClose = (Button) _rootView.findViewById(R.id.buttonReturn);
		buttonClose.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// Fin de l'activité.
				finish();
			}
		});

	}

	/**
	 * Tente de créer un nom de fichier unique à partir de la date.
	 * 
	 * @return Nom de fichier probalement unique, sans extension.
	 */
	private String buildUniqueFileName() {
		Date date = new Date();
		// Date en i18n.
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH-mm-ss", Locale.getDefault()) ;
		return getString(R.string.app_name)+ "-" + dateFormat.format(date);
	}

	/**
	 * Ecriture d'un texte centré dans la ressource image passée en paramètre
	 * 
	 * @param gContext
	 *            Contexte de l'application
	 * @param gResId
	 *            Id de la ressource
	 * @param gText
	 *            texte à écrire dans l'image.
	 * @return un bitmap modifié avec le texte.
	 */
	public Bitmap drawTextToBitmap(Context gContext, int gResId, String gText) {
		Resources resources = gContext.getResources();
		float scale = resources.getDisplayMetrics().density;
		Bitmap bitmap = BitmapFactory.decodeResource(resources, gResId);

		android.graphics.Bitmap.Config bitmapConfig = bitmap.getConfig();
		// set default bitmap config if none
		if (bitmapConfig == null) {
			bitmapConfig = android.graphics.Bitmap.Config.ARGB_8888;
		}
		// resource bitmaps are imutable,
		// so we need to convert it to mutable one
		bitmap = bitmap.copy(bitmapConfig, true);

		Canvas canvas = new Canvas(bitmap);
		// new antialised Paint
		Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
		// text color - #3D3D3D
		paint.setColor(Color.rgb(61, 61, 61));
		// text size in pixels
		paint.setTextSize((int) (20 * scale));
		// text shadow
		paint.setShadowLayer(1f, 0f, 1f, Color.WHITE);

		// draw text to the Canvas center
		Rect bounds = new Rect();
		paint.getTextBounds(gText, 0, gText.length(), bounds);
		int x = (bitmap.getWidth() - bounds.width()) / 2;
		int y = (bitmap.getHeight() + bounds.height()) / 2;

		canvas.drawText(gText, x, y, paint);

		return bitmap;
	}

	/**
	 * Création anticipée des images avec bougie pour chauque jour du mois. Cela
	 * permettra une execution plus rapide de la création du fichier PDF.
	 * 
	 * @return Arraylist<Image> avec les jours de 1 à 31 assossiciés à une
	 *         image.
	 */
	public ArrayList<Image> getMonthImageEvent() {
		if (list == null) {
			ArrayList<Image> list = new ArrayList<Image>();
			for (int i = 1; i < 32; i++) {
				// Image petite bougie rouge
				Bitmap bmp2 = drawTextToBitmap(getApplicationContext(),
						R.drawable.calendar_tile_small_candle2,
						String.valueOf(i));
				ByteArrayOutputStream stream2 = new ByteArrayOutputStream();
				bmp2.compress(Bitmap.CompressFormat.PNG, 100, stream2);

				Image imagelittlecandle;
				try {
					imagelittlecandle = Image
							.getInstance(stream2.toByteArray());
					list.add(imagelittlecandle);
				} catch (BadElementException e) {
					// Ne doit pas arriver
					Log.e(tag, e.getMessage());
				} catch (MalformedURLException e) {
					Log.e(tag, e.getMessage());
				} catch (IOException e) {
					Log.e(tag, e.getMessage());
				}
			}
			this.list = list;
		}
		return list;
	}

	/**
	 * Création du contenu PDF dans la référence de fichier passé en paramètre.
	 * 
	 * @param pdfFile
	 *            Fichier en lecture écriture pour générer le fichier PDF.
	 * @return Objet File avec le contenu PDF à l'intérieur.
	 */
	public File pdfBuilder(File pdfFile, ArrayList<ArrayList<Date>> splitedTab) {
		// Intégrité PDF.
		if (pdfFile == null) {
			Log.e(tag, "pdfFile null!");
		}

		// Création d'un objet document PDF.
		Document document = new Document();

		try {
			// Lien avec le flux de sortie.
			PdfWriter docWriter = PdfWriter.getInstance(document,
					new FileOutputStream(pdfFile));
			document.open();

			PdfContentByte cb = docWriter.getDirectContent();
			// initialize fonts for text printing
			initializeFonts();
			// Parcours de la collection splitedTab (tous les events et
			// infos pour le calendrier)
			
			// taille utilisable sur une page
			final int MAXHEIGHTPAGE = 800;
			// tables par pages.
			final int TABLEBYPAGE = 6;
			// Esapce laissé pour le titre.
			final int TITLEHEIGHT = 15;
			// hauteur Table.
			final int TABLEHEIGHT = 250;
			// Largeur table.
			final float TABLEWIDTH = 245;
			// position des tables de gauche
			final float TABLEPOSLEFT = document.leftMargin();
			// position des tables de droite
			final float TABLEPOSRIGHT = TABLEWIDTH + document.leftMargin() + 15;
			final float TITLEMONTHPOSLEFT = document.leftMargin() + TABLEWIDTH
					/ 2 - 15;
			final float TITLEMONTHPOSRIGHT = TABLEWIDTH + document.leftMargin()
					- 20 + TABLEWIDTH / 2;
			int pos = MAXHEIGHTPAGE; // init variable de position des
										// tables (titre compris).
			int tableCount = 0; // Comptage table.
			int column = 0; // Colonne 0 ou 1 des tables
			for (ArrayList<Date> listMonthEvent : splitedTab) {
				if (listMonthEvent.size() > 0) {

					// 1°) Position relative dans la page.
					// 2°) Placer le nom du mois getTitleMonth(date jour
					// 0 de la collection, posAbsolue).
					String monthStr = getTitleMonth(listMonthEvent.get(0));

					// 3°) (placer une bougie de mois s'il est complet).
					// 4°) Créer la table avec un placement en
					// posAbsolue getMonthTable().

					++tableCount;
					PdfPTable table = getTableMonth(listMonthEvent, TABLEWIDTH);
					// Mise en place de la table dans le document.

					if (column == 0) { // Colonne ?
						// Colonne 0 à gauche
						// Titre mois
						createHeadings(cb,
								TITLEMONTHPOSLEFT - monthStr.length(), pos,
								monthStr);
						pos = pos - TITLEHEIGHT;
						// Table
						table.writeSelectedRows(0, -1, TABLEPOSLEFT, pos,
								docWriter.getDirectContent());
						// Colonne suivante
						pos = pos + TITLEHEIGHT;
						column = 1;
						// Log.i(tag, "Table à gauche");
					} else {
						// Colonne 1 à droite
						// Titre mois
						createHeadings(cb,
								TITLEMONTHPOSRIGHT + monthStr.length(), pos,
								monthStr);
						pos = pos - TITLEHEIGHT;
						// Table
						table.writeSelectedRows(0, -1, TABLEPOSRIGHT, pos,
								docWriter.getDirectContent());
						// Ligne suivante
						pos = pos - TABLEHEIGHT;
						column = 0;
						// Log.i(tag, "Table à droite");
					}

					// document.add(table);
					// Log.i(tag, "Add table");
					// 5°) Selon le nombre de table de mois, créer une
					// nouvelle page et reset posAbs page.

					if (tableCount % TABLEBYPAGE == 0) {
						pos = MAXHEIGHTPAGE;
						document.newPage();
					}
				}
			}

			document.close();

		} catch (Exception e) {
			Log.e(tag, "File write failed");
			Toast.makeText(getApplicationContext(),
					getText(R.string.FileWriteFailed),
					Toast.LENGTH_LONG).show();
		}
		return pdfFile;

	}

	/**
	 * Construction d'un texte contenant les données.
	 * 
	 * @return
	 */
	public String textBuilder(ArrayList<ArrayList<Date>> splittedTab) {
		StringBuilder sb = new StringBuilder();

		// On parcour le tableau de tableau.

		// Calendrier sans i18n
		// Local par défaut. English.
		Calendar cal = Calendar.getInstance(Locale.ENGLISH);
		for (ArrayList<Date> listMonthEvent : splittedTab) {
			// Intégrité : Y a t'il des évènements ?
			if (listMonthEvent.size() > 0) {

				// Premiere date du mois pour prendre le numéro de mois
				// et d'année.
				Date date = listMonthEvent.get(0);
				cal.setTime(date);
				// Mois et année ?
				int month = cal.get(Calendar.MONTH);
				int year = cal.get(Calendar.YEAR);
				SimpleDateFormat sdf = new SimpleDateFormat("MMMM",
						Locale.getDefault());

				sb.append("\n\n");
				// Titre du mois.
				// +1 car les mois commencent à 0
				sb.append(sdf.format(date) + "-" + String.valueOf(year) + "\n");
				// Construire la liste des jours pour le mois selon la
				// représentation façon calendrier.
				ArrayList<ArrayList<String>> listDayMonth = Statistics
						.buildListOfDays(month + 1, year);
				// Log.i(tag, String.valueOf(month));
				// Ajout des events de ce mois (hors des jours Prev et
				// Tail).
				ArrayList<ArrayList<String>> listDayMonthEvent = Statistics
						.monthAndEvent(listDayMonth, listMonthEvent);

				// Parcour et affichage.
				// Short Name Days.
				/*
				 * for (int i = 0; i<7; i++) { ArrayList<String> ListInfoDay =
				 * listDayMonthEvent.get(i); // Nom du jour court String
				 * ShortDayName = ListInfoDay.get(CalendarFragment.DAY);
				 * sb.append(HTMLMailNameDayCellBuild(ShortDayName));
				 * sb.append("|"); }
				 */

				sb.append("\n");
				for (int i = 7; i < listDayMonthEvent.size(); i++) {

					// Nouvelle ligne ?
					/*
					 * if (count % 7 == 0) { sb.append("<br>"); }
					 */

					ArrayList<String> ListInfosDay = listDayMonthEvent.get(i);

					// Trailing Days ?
					/*
					 * if (ListInfosDay.get(CalendarFragment.FONTCOLOR)==
					 * CalendarFragment.COLORTRAIL) { //
					 * sb.append(HTMLMailTailDay
					 * (ListInfosDay.get(CalendarFragment.DAY))); }
					 */

					// Current Days ?
					if (ListInfosDay.get(CalendarFragment.FONTCOLOR) == CalendarFragment.COLORCURRENT) {
						sb.append(String.format("%s%3s", "|",
								ListInfosDay.get(CalendarFragment.DAY)));
						if (Boolean.valueOf(ListInfosDay
								.get(CalendarFragment.EVENT))) {
							sb.append("*");
							// Log.i(tag, "Event!");
						}

					}

					// Leading Days ?
					/*
					 * if (ListInfosDay.get(CalendarFragment.FONTCOLOR)==
					 * CalendarFragment.COLORLEAD) { sb.append(HTMLMailPrevDay
					 * (ListInfosDay.get(CalendarFragment.DAY))); }
					 */
				}
			}
		}

		sb.append("\n");

		return sb.toString();
	}

	/**
	 * Construction d'une chaine HTML contenant les données.
	 * 
	 * @param splitedTab
	 *            Tableau de donnée représentant toutes les données nécessaires
	 *            pour réaliser un export.
	 * 
	 * @return une chaine avec les données mises en formes.
	 */
	public String htmlTextBuilder(ArrayList<ArrayList<Date>> splitedTab) {
		StringBuilder sb = new StringBuilder();

		sb.append("<body>");
		// On parcour le tableau de tableau.

		// Calendrier sans i18n
		// Local par défaut. English.
		Calendar cal = Calendar.getInstance(Locale.ENGLISH);
		for (ArrayList<Date> listMonthEvent : splitedTab) {
			// Log.i(tag, " Taille tableau listMonthEvent " +
			// String.valueOf(listMonthEvent.size()));
			// Intégrité : Y a t'il des évènements ?
			if (listMonthEvent.size() > 0) {

				// Premiere date du mois pour prendre le numéro de mois
				// et d'année.
				Date date = listMonthEvent.get(0);
				cal.setTime(date);
				// Mois et année ?
				int month = cal.get(Calendar.MONTH);
				int year = cal.get(Calendar.YEAR);
				SimpleDateFormat sdf = new SimpleDateFormat("MMMM",
						Locale.getDefault());

				// HTML Titre du mois.
				// +1 car les mois commencent à 0
				// String.valueOf(month + 1)
				sb.append("<p>"
						+ HTMLMailTitleMonth(sdf.format(date) + "-"
								+ String.valueOf(year)) + "</p>");
				// Construire la liste des jours pour le mois selon la
				// représentation façon calendrier.
				ArrayList<ArrayList<String>> listDayMonth = Statistics
						.buildListOfDays(month + 1, year);
				// Log.i(tag, String.valueOf(month));
				// Ajout des events de ce mois (hors des jours Prev et
				// Tail).
				ArrayList<ArrayList<String>> listDayMonthEvent = Statistics
						.monthAndEvent(listDayMonth, listMonthEvent);

				// Parcour et affichage.
				// Short Name Days.
				/*
				 * for (int i = 0; i<7; i++) { ArrayList<String> ListInfoDay =
				 * listDayMonthEvent.get(i); // Nom du jour court String
				 * ShortDayName = ListInfoDay.get(CalendarFragment.DAY);
				 * sb.append(HTMLMailNameDayCellBuild(ShortDayName));
				 * sb.append("|"); }
				 */

				sb.append("<hr>");
				for (int i = 7; i < listDayMonthEvent.size(); i++) {

					// Nouvelle ligne ?
					/*
					 * if (count % 7 == 0) { sb.append("<br>"); }
					 */

					ArrayList<String> ListInfosDay = listDayMonthEvent.get(i);

					// Trailing Days ?
					/*
					 * if (ListInfosDay.get(CalendarFragment.FONTCOLOR)==
					 * CalendarFragment.COLORTRAIL) { //
					 * sb.append(HTMLMailTailDay
					 * (ListInfosDay.get(CalendarFragment.DAY))); }
					 */

					// Current Days ?
					if (ListInfosDay.get(CalendarFragment.FONTCOLOR) == CalendarFragment.COLORCURRENT) {
						sb.append(HTMLMailCurrentDay(String.format("%3s",
								ListInfosDay.get(CalendarFragment.DAY))));
						if (Boolean.valueOf(ListInfosDay
								.get(CalendarFragment.EVENT))) {
							sb.append("*");
							// Log.i(tag, "Event!");
						}

					}

					// Leading Days ?
					/*
					 * if (ListInfosDay.get(CalendarFragment.FONTCOLOR)==
					 * CalendarFragment.COLORLEAD) { sb.append(HTMLMailPrevDay
					 * (ListInfosDay.get(CalendarFragment.DAY))); }
					 */
				}
			}
		}

		sb.append("</body>");

		return sb.toString();
	}

	/**
	 * Fait une lecture de la BDD et retourne un tableau contenant toutes les
	 * données pour faire une export.
	 * 
	 * @return ArrayList<ArrayList<Date>> tableau avec toutes les données pour
	 *         un export.
	 */
	public ArrayList<ArrayList<Date>> getSplitedTab() {
		// Données issues de la base de données, toutes représentent un
		// event prière complète.
		DBController dbc = DBController
				.getDBController(getApplicationContext());
		ArrayList<Date> ALDatesSorted = dbc.getAllDatesSorted();
		// Spliter le tableau des dates triées en un collection
		// représentant
		// des tableaux avec tout les jours d'un mois.
		ArrayList<ArrayList<Date>> splitedTab = Statistics
				.DateSpliter(ALDatesSorted);
		return splitedTab;
	}

	/**
	 * Crée un objet table depuis la liste de dates du mois. Cette objet table
	 * comprend tous les évènements.
	 * 
	 * @param listMonthEvent
	 *            liste des evenénements du mois.
	 * @return un objet table itext PDF.
	 */
	public PdfPTable getTableMonth(ArrayList<Date> listMonthEvent,
			float tableWith) {
		// Largeur de colonne des jours de la semaine.
		float[] columnWidths = { 1f, 1f, 1f, 1f, 1f, 1f, 1f };
		// Créer une table PDF table avec les largeurs donnés.
		PdfPTable table = new PdfPTable(columnWidths);

		// Année et mois des dates de la liste.
		if (listMonthEvent == null || listMonthEvent.size() == 0) {
			// Intégrité
			Log.e(tag, "Table Month Event null or empty.");
			return table;
		}

		// Obtenir le mois et l'année pour cette liste
		Date date = listMonthEvent.get(0);
		Calendar cal = Calendar.getInstance(Locale.ENGLISH);
		cal.setTime(date);
		// Mois et année ?
		int month = cal.get(Calendar.MONTH);
		int year = cal.get(Calendar.YEAR);

		// Construire la liste des jours pour le mois selon la
		// représentation façon calendrier avec les cases en gris et
		// noir.
		ArrayList<ArrayList<String>> listDayMonth = Statistics.buildListOfDays(
				month + 1, year);
		// Log.i(tag, String.valueOf(month));
		// Ajout des events de ce mois (hors des jours Prev et
		// Tail).
		ArrayList<ArrayList<String>> listDayMonthEvent = Statistics
				.monthAndEvent(listDayMonth, listMonthEvent);

		// Couleur pour les cellules.
		BaseColor cellBGColor = new BaseColor(getResources().getColor(
				R.color.orangeCentreCarreNomsJoursSemaine));
		BaseColor cellBorderColor = new BaseColor(getResources().getColor(
				R.color.orangeBordsCarreNomsJoursSemaine));

		// set table width a percentage of the page width
		table.setTotalWidth(tableWith);

		table.setHeaderRows(0);

		// Jours de la semaine en abrégés.
		PdfPCell cell = new PdfPCell();

		Font fontGrey = new Font();
		fontGrey.setColor(BaseColor.GRAY);

		Font fontBlack = new Font();
		fontBlack.setColor(BaseColor.BLACK);

		for (ArrayList<String> infoDayList : listDayMonthEvent) {

			// 0- Numéro du jours dans le mois
			// 1- Numéro du mois de ce jour
			// 2- Numéro de l'année de ce jour
			// 3- Couleur de la police
			// 4- Type de la cellule.
			// 5- Event.

			if (infoDayList.get(4) == Statistics.WEEKDAYS) {
				// Jours de semaine
				cell = new PdfPCell(new Phrase(infoDayList.get(0)));
				cell.setBackgroundColor(cellBGColor);
				cell.setBorderWidth(2);
				cell.setBorderColor(cellBorderColor);
				cell.setHorizontalAlignment(Element.ALIGN_CENTER);
				cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
				cell.setFixedHeight(30f);

				table.addCell(cell);
			}

			if (infoDayList.get(4) == Statistics.TRAILDAYS) {
				// TrailingDays (jours de queue du mois prédécent, en
				// début de table)
				cell = new PdfPCell(new Phrase(infoDayList.get(0), fontGrey));
				cell.setBackgroundColor(cellBGColor);
				cell.setBorderWidth(2);
				cell.setBorderColor(cellBorderColor);
				cell.setHorizontalAlignment(Element.ALIGN_CENTER);
				cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
				cell.setFixedHeight(30f);
				table.addCell(cell);
			}

			if (infoDayList.get(4) == Statistics.LEADINGDAYS) {
				// LeadingDays (jours de tête en fin de table)
				cell = new PdfPCell(new Phrase(infoDayList.get(0), fontGrey));
				cell.setBackgroundColor(cellBGColor);
				cell.setBorderWidth(2);
				cell.setBorderColor(cellBorderColor);
				cell.setHorizontalAlignment(Element.ALIGN_CENTER);
				cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
				cell.setFixedHeight(30f);
				table.addCell(cell);
			}

			if (infoDayList.get(4) == Statistics.CURRENTDAYS) {

				if (infoDayList.get(5) == "True") {
					// Jour avec event.
					cell = new PdfPCell(getMonthImageEvent().get(
							Integer.valueOf(infoDayList.get(0)) - 1), true);
					cell.setBackgroundColor(cellBGColor);
					// cell.setBackgroundColor(BaseColor.RED);

				} else {
					// Jour sans event
					cell = new PdfPCell(new Phrase(infoDayList.get(0),
							fontBlack));
					cell.setBackgroundColor(cellBGColor);

				}

				cell.setBorderWidth(2);
				cell.setBorderColor(cellBorderColor);
				cell.setHorizontalAlignment(Element.ALIGN_CENTER);
				cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
				cell.setFixedHeight(30f);

				table.addCell(cell);
			}
		}

		return table;
	}

	/**
	 * Crée le titre pour le mois dans l'export PDF.
	 * 
	 * @param date
	 *            à partir duquel créer le titre pour le mois.
	 * @return
	 */
	public String getTitleMonth(Date date) {
		// Premiere date du mois pour prendre le numéro de
		// mois et d'année.
		Calendar cal = Calendar.getInstance(Locale.ENGLISH);
		cal.setTime(date);
		// Mois et année ?
		// int month = cal.get(Calendar.MONTH);
		int year = cal.get(Calendar.YEAR);
		SimpleDateFormat sdf = new SimpleDateFormat("MMMM", Locale.getDefault());

		return String.format("%s - %s", sdf.format(date), year);
	}

	/**
	 * Recherche dans la mémoire interne un lieu partagé ou il sera possible
	 * d'écrire un fichier.
	 * 
	 * @return un dossier disponible en lecture/écriture ou bien null si rien
	 *         n'est disponible.
	 */
	public File getAvailableFolder() {
		// Check chemin disponible pour sauver un fichier
		File dowloadDir = null;
		String extStorageState = Environment.getExternalStorageState();
		// Le dossier doit exister et ne pas être en lecture seule.
		dowloadDir = Environment.getExternalStorageDirectory();
		if (dowloadDir != null
				&& !Environment.MEDIA_MOUNTED_READ_ONLY.equals(extStorageState)) {
			return dowloadDir;
		}
		// Problème pas de dossier.
		return null;
	}

	private void openFile(File file, String mimeType) {
		Intent viewIntent = new Intent();
		viewIntent.setAction(Intent.ACTION_VIEW);
		viewIntent.setDataAndType(Uri.fromFile(file), mimeType);
		// using the packagemanager to query is faster than trying startActivity
		// and catching the activity not found exception, which causes a stack
		// unwind.
		List<ResolveInfo> resolved = getPackageManager().queryIntentActivities(
				viewIntent, 0);
		if (resolved != null && resolved.size() > 0) {
			startActivity(viewIntent);
		} else {
			// notify the user they can't open it.
		}
	}

	/**
	 * Création d'une cellule en HTML titre pour un mois.
	 */
	public String HTMLMailTitleMonth(String value) {

		return new StringBuilder()
		// .append("<td>")
				.append(value)
				// .append("</td>")
				.toString();
	}

	/**
	 * Création d'une cellule en HTML type nom de jour de la semaine.
	 */
	public String HTMLMailNameDayCellBuild(String value) {

		return new StringBuilder().append("<td>").append(value).append("</td>")
				.toString();

	}

	/**
	 * Création d'une cellule en HTML jour avant normal.
	 */
	public String HTMLMailPrevDay(String value) {

		return new StringBuilder().append("<td>").append(value).append("</td>")
				.toString();

	}

	/**
	 * Création d'une cellule en HTML jour normal.
	 */
	public String HTMLMailCurrentDay(String value) {

		return new StringBuilder().append("|").append(value)
		// .append("")
				.toString();

	}

	/**
	 * Création d'une cellule en HTML jour après normal.
	 */
	public String HTMLMailTailDay(String value) {

		return new StringBuilder().append("<td>").append(value).append("</td>")
				.toString();

	}

	@Override
	public View onCreateView(View parent, String name, Context context,
			AttributeSet attrs) {
		return super.onCreateView(parent, name, context, attrs);
	}

	/**
	 * Obtenir toute le contenu de la base de données sous forme CSV. Inutilisé.
	 */
	public StringBuilder SqLiteToCSV() {
		StringBuilder sb = new StringBuilder();
		DBController dbc = DBController
				.getDBController(getApplicationContext());
		ArrayList<Date> ALDatesSorted = dbc.getAllDatesSorted();
		SimpleDateFormat dateFormatter = new SimpleDateFormat("dd-MMMM-yyyy",
				Locale.getDefault());
		for (Date date : ALDatesSorted) {
			sb.append(dateFormatter.format(date));
			sb.append(";\n");
		}
		return sb;
	}

	/**
	 * Vérifier qu'un solution de mail est en place.
	 * 
	 * @param i
	 *            Intent dont on doit tester la présence.
	 */
	public boolean isIntendSendPresent(Intent i) {

		PackageManager packageManager = getPackageManager();
		List<ResolveInfo> activities = packageManager.queryIntentActivities(i,
				PackageManager.MATCH_DEFAULT_ONLY);
		boolean isIntentSafe = activities.size() > 0;
		return isIntentSafe;
	}

	/**
	 * Envoyer un mail avec les données directement placée dedans et une mise en
	 * forme texte sommaire.
	 * 
	 * @param content
	 *            Contenu a placer dans le content.
	 */
	public void sendMailOnly(String content) {
		// Tentative de lancement du logiciel de mail installé dans le
		// téléphone.
		final Intent shareIntent = new Intent(Intent.ACTION_SENDTO,
				Uri.parse("mailto:"));

		shareIntent.putExtra(Intent.EXTRA_SUBJECT, getText(R.string.app_name));
		shareIntent.putExtra(Intent.EXTRA_TEXT, Html.fromHtml(content));

		if (isIntendSendPresent(shareIntent)) {
			startActivity(shareIntent);
		} else {
			// Affichage long.
			Toast.makeText(getApplicationContext(),
					getString(R.string.NoSoftwareMail), Toast.LENGTH_LONG)
					.show();
			// Pas de i18n English.
			Log.i(tag, "No Software Mail.");
		}
	}

	/**
	 * Envoyer un mail avec les données placés dans le fichier passé en
	 * paramètre.
	 */
	public void sendMailWithFileData(File dataFile) {
		// Tentative de lancement du logiciel de mail installé dans le
		// téléphone.
		final Intent shareIntent = new Intent(Intent.ACTION_SEND,
				Uri.parse("mailto:"));

		shareIntent.putExtra(Intent.EXTRA_SUBJECT, getText(R.string.app_name));
		shareIntent.putExtra(Intent.EXTRA_TEXT, getText(R.string.app_name));
		Uri uri = Uri.fromFile(dataFile);
		shareIntent.putExtra(Intent.EXTRA_STREAM, uri);
		// shareIntent.FLAG_GRANT_READ_URI_PERMISSION
		// shareIntent.setDataAndType(uri, "application/pdf");
		shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
		shareIntent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
		shareIntent.setType("text/plain");

		if (isIntendSendPresent(shareIntent)) {
			startActivity(Intent.createChooser(shareIntent,
					"Choose an Email client :"));
			// startActivity(shareIntent);
		} else {
			// Affichage long.
			Toast.makeText(getApplicationContext(),
					getString(R.string.NoSoftwareMail), Toast.LENGTH_LONG)
					.show();
			// Pas de i18n English.
			Log.i(tag, "No Software Mail.");
		}
	}

	/**
	 * Envoyer un mail avec les données placés directement dans un fichier CSV.
	 */
	public void sendMailWithFileDataCSV() {

		// Log.i(tag, "Click ");
		String to = "guyot.raphael@gmail.com";
		String subject = "Backup stats de 7 Notre Père.";
		String message = "Export depuis l'application 7 Notre Père.";
		Intent i = new Intent(Intent.ACTION_SEND);
		i.setType("plain/text");
		try {
			// Former un nom de fichier avec la date du jour.
			Date dateVal = new Date();
			String filename = dateVal.toString();

			// Créer un fichier csv vide.
			File dataFile = File.createTempFile("CSVExport" + filename, ".csv");

			// FileWriter pour écrire dans ce fichier.
			FileWriter out = new FileWriter(dataFile);
			// Ecrire dans ce fichier l'export CSV produit depuis la base
			// SqlLite.
			out.write(SqLiteToCSV().toString());
			i.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(dataFile));
			i.putExtra(Intent.EXTRA_EMAIL, new String[] { to });
			i.putExtra(Intent.EXTRA_SUBJECT, subject);
			i.putExtra(Intent.EXTRA_TEXT, message);
			// Lancer l'activty avec l'Intent.
			getApplicationContext().startActivity(
					Intent.createChooser(i, "E-mail"));
			out.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	private static boolean isExternalStoragePublicDirectoryAvailable() {
		// Test
		File publicFolfder = Environment
				.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
		if (publicFolfder != null) {
			Log.i("ExportActivityStatic", "dir Download OK");
			return true;
		}
		return false;

	}

	private void initializeFonts() {

		try {
			bfBold = BaseFont.createFont(BaseFont.HELVETICA_BOLD,
					BaseFont.CP1252, BaseFont.NOT_EMBEDDED);

		} catch (DocumentException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void createHeadings(PdfContentByte cb, float x, float y, String text) {

		cb.beginText();
		cb.setFontAndSize(bfBold, 12);
		cb.setTextMatrix(x, y);
		cb.showText(text.trim());
		cb.endText();

	}
}
