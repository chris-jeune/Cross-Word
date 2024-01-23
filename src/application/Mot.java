package application;

import java.awt.Point;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import application.InterfaceGraphique.GestionSeleCase;
import javafx.scene.control.TextField;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;

/**
 * Contient toutes les caractéristiques d'un mot.
 *
 *
 * @version 1.0 2022-10-30
 * @author Christian Jeune
 */
public class Mot {
	private String strMot; // cha�ne de caractère qui correspond au mot
	private Orientation direction; // la direction du mot
	private Point position;// la position d'une lettre dans le mot
	private Point positionOrigin;// la position d'une lettre dans le mot
	private String indice;// indice du mot à trouver
	private TextField[] tabCases;// cases dans la grille qui appartiennent au mot
	private String lettreMel;// les lettres mélangés du mots
	private boolean isValide = false;// boolean pour déterminer si le mot a été validé
	private boolean lettreUtil = false;// boolean pour déterminer si le bouton lettre a été utilisé pour ce mot
	private boolean solutionUtil = false;// boolean pour déterminer si le bouton solution a été utilisé pour ce mot
	// couleurs des case pour les mots trouvés sans aide
	static BackgroundFill bfill = new BackgroundFill(Color.LIGHTGREEN, new CornerRadii(10), null);
	// couleurs des case pour les mot trouvés avec la solution
	static BackgroundFill bfill2 = new BackgroundFill(Color.LIGHTBLUE, new CornerRadii(10), null);
	// couleurs des cases pour les mots trouvés avec les lettres mélangés
	static BackgroundFill bfill3 = new BackgroundFill(Color.DARKTURQUOISE, new CornerRadii(10), null);
	// couleurs des cases pour une mauvaise réponse
	static BackgroundFill bfill4 = new BackgroundFill(Color.PALEVIOLETRED, new CornerRadii(10), null);

	/**
	 * Construit un objet mot � partir du mot, la direction du mot et la position
	 *
	 *
	 * @param strMot    le mot.
	 * @param direction la direction du mot.
	 * @param position  la position d'une lettre dans le mot.
	 */
	public Mot(String strMot, Orientation direction, String indice, Point position) {
		this.strMot = strMot;
		this.direction = direction;
		this.position = position;
		this.indice = indice;
		tabCases = new TextField[strMot.length()];
		positionOrigin = new Point((int) position.getX(), (int) position.getY());
		lettreMel = melMot();
	}

	/**
	 * Redéfinition de la méthode toString pour la classe Mot.
	 *
	 *
	 * @return les informations du mot.
	 */
	public String toString() {
		return String.format("%-15S%-16s%s\n", strMot,
				"(" + (int) positionOrigin.getX() + ", " + (int) positionOrigin.getY() + ")", direction.getDirection());
	}

	/**
	 * Getteur pour la direction.
	 *
	 *
	 * @return une Orientation qui correspond à la direction du mot.
	 */
	public Orientation getDirection() {
		return direction;
	}

	/**
	 * Getteur pour la position.
	 *
	 *
	 * @return un Point qui correspond à la position d'une lettre du mot.
	 */
	public Point getPosition() {
		return position;
	}

	/**
	 * Getteur pour strMot.
	 *
	 *
	 * @return un String qui correspond au mot.
	 */
	public String getStrMot() {
		return strMot;
	}

	/**
	 * Getteur pour la positionOrigin.
	 * 
	 * 
	 * @return un Point qui correspond à la position d'une lettre de strMot.
	 */
	public Point getPositionOrigin() {
		return positionOrigin;
	}

	/**
	 * Getteur pour indice
	 * 
	 * 
	 * @return un String qui correspond à l'indice du mot
	 */
	public String getIndice() {
		return indice;
	}

	/**
	 * Getteur pour lettreMel
	 * 
	 * 
	 * @return un String qui correspond aux lettres mélangés de strMot
	 */
	public String getLettreMel() {
		return lettreMel;
	}

	/**
	 * Getteur pour getTabCases
	 * 
	 * 
	 * @return un tableau TextField qui correspond aux grilles du jeu appartenant au
	 *         jeu
	 */
	public TextField[] getTabCases() {
		return tabCases;
	}

	/**
	 * Getteur pour isValide
	 * 
	 * 
	 * @return isValide
	 */
	public boolean isValide() {
		return isValide;
	}

	/**
	 * Setteur pour isValide
	 * 
	 * 
	 * @param isValide un boolean qui set this.isValide à isValide
	 */
	public void setValide(boolean isValide) {
		this.isValide = isValide;
	}

	/**
	 * Getteur pour lettreUtil
	 * 
	 * 
	 * @return lettreUtil
	 */
	public boolean isLettreUtil() {
		return lettreUtil;
	}

	/**
	 * Setteur pour lettreUtil
	 * 
	 * 
	 * @param lettreUtil un boolean qui set this.lettreUtil à lettreUtil
	 */
	public void setLettreUtil(boolean lettreUtil) {
		this.lettreUtil = lettreUtil;
	}

	/**
	 * Getteur pour solutionUtil
	 * 
	 * 
	 * @return solutionUtil
	 */
	public boolean isSolutionUtil() {
		return solutionUtil;
	}

	/**
	 * Setteur pour solutionUtil
	 * 
	 * 
	 * @param solutionUtil un boolean qui set this.solutionUtil à solutionUtil
	 */
	public void setSolutionUtil(boolean solutionUtil) {
		this.solutionUtil = solutionUtil;
	}

	/**
	 * Setteur pour tabCases
	 * 
	 * 
	 * @param gpane le pane dans lequel ce trouve l'ensemble des cases du jeu
	 * @param gsc   Gestionnaire d'évènement pour les grille texte
	 */
	public void setTabCases(GridPane gpane, GestionSeleCase gsc) {
		Point a = direction.getCoordonn();
		// on parcours la grille à la recherche des cases qui appartiennent à strMot
		for (int i = 0, j = 0; i < gpane.getChildren().size() && j < tabCases.length; i++) {
			// on compare la colonne et la rangée d'une case dans la grille à la position
			// désirée d'une lettre du mot dans la grille
			// et on ajoute cette case dans tabCases
			if ((GridPane.getColumnIndex(gpane.getChildren().get(i)) == (int) position.getY())
					&& (GridPane.getRowIndex(gpane.getChildren().get(i)) == (int) position.getX())
					&& gpane.getChildren().get(i).getClass().getSimpleName().equals("TextField")) {
				tabCases[j] = (TextField) gpane.getChildren().get(i);
				tabCases[j].setOnKeyReleased(gsc);
				j++;
				position.translate((int) a.getX(), (int) a.getY()); // on va à la prochaine lettre
			}
		}

	}

	/**
	 * Méthode pour mélanger les lettres d'un mot
	 * 
	 * 
	 * @return afterShuffle un mot qui correspond au lettres mélangés de strMot
	 */
	private String melMot() {
		List<String> characters = Arrays.asList(strMot.split(""));// on place chacune des lettres du mot dans une list
		Collections.shuffle(characters);// on mélange les lettres
		String afterShuffle = "";
		// on parcours la list et on append les lettres à afterShuffle
		for (String character : characters) {
			afterShuffle += character;
		}
		return afterShuffle;
	}

	/**
	 * Méthode pour vérifier si les cases dans tabCases correspondent au mot
	 * 
	 * 
	 * @return true si les mots correspondent, false sinon
	 */
	public boolean verifMot() {
		boolean isNull = false;// boolean pour vérifier si des cases sont vides
		// on parcours tabCases à la recherche de cases vides
		for (int i = 0; i < strMot.length(); i++) {
			if (!tabCases[i].getText().equals("")) {
				isNull = false;
			} else {
				isNull = true;
				break;
			}

		}
		// si une case est null retourne false
		if (isNull) {
			return false;
		} else {
			boolean lettreMatch = false;
			// parcours tabCases pour voir si les cases correspondent les lettres du mots
			for (int i = 0; i < strMot.length(); i++) {
				if (tabCases[i].getText().trim().equalsIgnoreCase(Character.toString(strMot.charAt(i)))) {
					lettreMatch = true;
				} else {
					lettreMatch = false;
					break;
				}

			}
			// si les lettres correspondent retourne true
			if (lettreMatch) {
				return lettreMatch;
			} else {
				return false;
			}
		}
	}

}
