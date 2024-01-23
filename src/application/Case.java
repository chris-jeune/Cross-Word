package application;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.control.TextFormatter.Change;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderStroke;
import javafx.scene.layout.BorderStrokeStyle;
import javafx.scene.layout.BorderWidths;
import javafx.scene.layout.CornerRadii;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;

/**
 * Crée les cases de la grille du jeu.
 *
 *
 * @version 1.0 2022-10-30
 * @author Christian Jeune
 */
public class Case {
	private Label cases;// les cases vides ou les cercles de l'interface
	private TextField tFieldcase = new TextField();// les grilles textes du jeu
	BorderStroke bordureNoir = new BorderStroke(Color.BLACK, BorderStrokeStyle.SOLID, new CornerRadii(5),
			new BorderWidths(2), new Insets(0)); // la bordure noir de la grille du jeu
	Border bordure = new Border(bordureNoir);// la bordure noir de la grille du jeu
	BackgroundFill bfill = new BackgroundFill(Color.YELLOW, new CornerRadii(10), null);// l'arrière plan des grilles
	BackgroundFill bfill2 = new BackgroundFill(Color.BLACK, new CornerRadii(10), null);// l'arrière plan des cercles
	BackgroundFill bfill3 = new BackgroundFill(Color.WHITESMOKE, new CornerRadii(10), null);// l'arrière plan des cases
																							// vides

	/**
	 * Construit les différentes cases du jeu à partir de donGrille
	 * 
	 * @param donGrille les données des grillesFichier
	 */
	public Case(int donGrille) {
		// création des grilles textes
		if (donGrille == 0) {
			tFieldcase.setPrefSize(40, 32);
			tFieldcase.setBorder(bordure);
			tFieldcase.setBackground(new Background(bfill));
			tFieldcase.setEditable(false);
			// pour s'assurer que seulement 1 caractère peu entrer dans les grilles
			tFieldcase.setTextFormatter(new TextFormatter<String>((Change change) -> {
				String newText = change.getControlNewText();
				if (newText.length() > 1) {
					return null;
				} else {
					return change;
				}
			}));
			// création des cercles/cases numérotés
		} else if (donGrille != 0 && donGrille != -1) {
			cases = new Label(Integer.toString(donGrille));
			cases.setShape(new Circle(28));
			cases.setBackground(new Background(bfill2));
			cases.setTextFill(Color.WHITE);
			cases.setPrefSize(32, 32);
			cases.setAlignment(Pos.CENTER);
			// création des cases vides qu'on set à disable
		} else {
			cases = new Label();
			cases.setShape(new Rectangle());
			cases.setBackground(new Background(bfill3));
			cases.setPrefSize(40, 32);
			cases.setDisable(true);
		}

	}

	/**
	 * Getteur pour les cases vides ou les cercles
	 * 
	 * 
	 * @return cases
	 */
	public Label getCases() {
		return cases;
	}

	/**
	 * Getteur pour les grilles textes
	 * 
	 * 
	 * @return tFieldcase
	 */
	public TextField gettFieldcase() {
		return tFieldcase;
	}

}
