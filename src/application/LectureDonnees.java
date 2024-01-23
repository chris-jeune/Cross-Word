package application;

import java.awt.Point;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;

import application.InterfaceGraphique.GestionNumero;
import application.InterfaceGraphique.GestionSeleCase;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;

/**
 * Lit les deux fichiers de données et initialise des structures de données
 * adéquates en mémoire.
 *
 *
 * @version 1.0 2022-10-30
 * @author Christian Jeune
 */
public class LectureDonnees {
	private HashMap<Label, Mot> tableIndex = new HashMap<Label, Mot>();// HashMap dont les clés sont les cases
																		// numérotées, et qui permet d'accéder aux mots
	private GridPane gpane = new GridPane();// GridPane correspondant à la grille de Jeu
	private int rangee;// rangee d'une grille
	private int colonne;// colonne d'une grille
	private String[][] tabMot;// tableau qui correspond à un ligne des fichiers données
	private String[] tabGrille;// tableau qui correspond à une ligne des fichiers grilles
	private boolean[][] tabCasesVal;// tableau de la grille de jeu sous forme boolean
	private Case cases;// pour les différentes cases qui seront initialisée lors de la lecture des
						// fichiers
	private int rangeeTot;// rangées total des fichiers données

	/**
	 * Constructeur pour les données du jeu
	 * 
	 * 
	 * @param nomFichier1 fichier qui contient les mot, leur indice et leur
	 *                    orientation
	 * @param nomFichier2 fichier qui contient toutes les informations pour
	 *                    construire la grille
	 * @param gn          gestionnaire des cases numérotées
	 * @param gsc         gestionnaires des racourcis claviers des grilles textes
	 */
	public LectureDonnees(String nomFichier1, String nomFichier2, GestionNumero gn, GestionSeleCase gsc) {
		String ligneLue;// une ligne dans un fichier

		try {
			// lecture du fichier1 afin de déterminer le nombre de rangées total
			InputStream source = new FileInputStream(nomFichier1);
			InputStreamReader lecteur = new InputStreamReader(source);
			BufferedReader input = new BufferedReader(lecteur);
			rangeeTot = 0;
			ligneLue = input.readLine();
			while (ligneLue != null) {
				rangeeTot += 1;
				ligneLue = input.readLine();
			}

			// lecteur du fichier 1
			InputStream source1 = new FileInputStream(nomFichier1);
			InputStreamReader lecteur1 = new InputStreamReader(source1);
			BufferedReader input1 = new BufferedReader(lecteur1);

			// lecteur du fichier 2
			InputStream source2 = new FileInputStream(nomFichier2);
			InputStreamReader lecteur2 = new InputStreamReader(source2);
			BufferedReader input2 = new BufferedReader(lecteur2);

			rangee = 0;
			colonne = 0;
			tabMot = new String[rangeeTot][3];// création de tabMot

			ligneLue = input1.readLine();
			while (ligneLue != null) {
				// on parcours un ligne du fichier 1 et ajoute les composantes à la rangée et la
				// colonne correspondante dans tabMot
				for (colonne = 0; colonne < tabMot[rangee].length; colonne++) {
					tabMot[rangee][colonne] = ligneLue.split(";")[colonne];
				}
				rangee += 1;
				ligneLue = input1.readLine();
			}

			rangee = 0;
			ligneLue = input2.readLine();
			tabGrille = ligneLue.split(",");
			tabCasesVal = new boolean[Integer.parseInt(tabGrille[0])][Integer.parseInt(tabGrille[1])];// création de
																										// tabCasesVal
			ligneLue = input2.readLine();
			while (ligneLue != null) {
				tabGrille = ligneLue.replaceAll(" ", "").split(",");// création de tabGrille
				// on parcourt tabGrille et dépendamment de la valeurs d'une grille, on
				// initialise les bonnes structures de données
				for (colonne = 0; colonne < tabGrille.length; colonne++) {
					int numGrille = Integer.parseInt(tabGrille[colonne]);

					// création des grilles textes
					if (numGrille == 0) {
						cases = new Case(numGrille);// création de case
						gpane.addRow(rangee, cases.gettFieldcase());// ajout dans gpane
						tabCasesVal[rangee][colonne] = false;// initialisation d'un élément dans tabCasesVal

						// création des grilles numérotés
					} else if (numGrille != 0 && numGrille != -1) {
						cases = new Case(numGrille);
						cases.getCases().addEventHandler(MouseEvent.MOUSE_CLICKED, gn);// ajout du gestionnaire
																						// d'évènement gn
						gpane.addRow(rangee, cases.getCases());
						tabCasesVal[rangee][colonne] = false;

						// dépendamment de l'orientation d'une rangée dans tabMot, création d'un mot et
						// ajout de ce dernier dans tableIndex
						if (tabMot[(numGrille - 1)][0].equals(Orientation.H.getDirection())) {
							tableIndex.put(cases.getCases(),
									new Mot(tabMot[(numGrille - 1)][2], Orientation.H, tabMot[(numGrille - 1)][1],
											new Point(rangee + Orientation.H.getCoordonn().x,
													colonne + Orientation.H.getCoordonn().y)));
						} else {
							tableIndex.put(cases.getCases(),
									new Mot(tabMot[(numGrille - 1)][2], Orientation.V, tabMot[(numGrille - 1)][1],
											new Point(rangee + Orientation.V.getCoordonn().x,
													colonne + Orientation.V.getCoordonn().y)));
						}

						// création des cases vides
					} else {
						cases = new Case(numGrille);
						gpane.addRow(rangee, cases.getCases());
						tabCasesVal[rangee][colonne] = false;
					}

				}
				rangee += 1;
				ligneLue = input2.readLine();
			}

			input1.close();
			input2.close();
		} catch (IOException err) {
			System.out.println("Le fichier est introuvable.\n*Fin du programme*");
			System.exit(0);
		}

		// parcours de tablesIndex pour set les cases appartenant aux mots
		for (Mot m : tableIndex.values()) {
			m.setTabCases(gpane, gsc);
		}
	}

	/**
	 * Getteur pour gpane
	 * 
	 * 
	 * @return gpane
	 */
	public GridPane getGpane() {
		gpane.setPadding(new Insets(15, 15, 15, 15));
		gpane.setHgap(1);
		gpane.setVgap(1);
		return gpane;
	}

	/**
	 * Getteur pour tableIndex
	 * 
	 * 
	 * @return tableIndex
	 */
	public HashMap<Label, Mot> getTableIndex() {
		return tableIndex;
	}

	/**
	 * Getteur pour tabCasesVal
	 * 
	 * 
	 * @return tabCasesVal
	 */
	public boolean[][] getTabCasesVal() {
		return tabCasesVal;
	}

	/**
	 * Setteur pour tabCasesVal
	 * 
	 * 
	 * @param mot mot dont les cases seront évalués
	 */
	public void setTabCasesVal(Mot mot) {
		Point a = mot.getDirection().getCoordonn();
		// on parcours la grille à la recherche des cases qui appartiennent à strMot
		for (int i = 0, j = 0; i < gpane.getChildren().size() && j < mot.getStrMot().length(); i++) {

			// on compare la colonne et la rangée d'une case dans la grille à la position
			// désirée d'une lettre du mot dans la grille
			// et on change la valeur de tabCasesVal à true
			if ((GridPane.getColumnIndex(gpane.getChildren().get(i)) == (int) mot.getPositionOrigin().getY())
					&& (GridPane.getRowIndex(gpane.getChildren().get(i)) == (int) mot.getPositionOrigin().getX())
					&& gpane.getChildren().get(i).getClass().getSimpleName().equals("TextField")) {

				tabCasesVal[GridPane.getRowIndex(gpane.getChildren().get(i))][(GridPane
						.getColumnIndex(gpane.getChildren().get(i)))] = true;
				j++;
				mot.getPositionOrigin().translate((int) a.getX(), (int) a.getY());// on va à la prochaine lettre
			}
		}
	}

	/**
	 * Calcul le score maximal possible pour une grille
	 * 
	 * 
	 * @return scoreMax
	 */
	public int getScoreMax() {
		int scoreMax = 0;
		// parcours les mots dans tableIndex, les multiplie par deux et les additionne à
		// scoreMax
		for (Mot m : tableIndex.values()) {
			scoreMax += 2 * m.getStrMot().length();
		}
		return scoreMax;
	}
}