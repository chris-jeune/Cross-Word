package application;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.RadioMenuItem;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.BorderStroke;
import javafx.scene.layout.BorderStrokeStyle;
import javafx.scene.layout.BorderWidths;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;

/**
 * Reproduit l’interface demandée.
 *
 *
 * @version 1.0 2022-10-30
 * @author Christian Jeune
 */
public class InterfaceGraphique extends Application {

	private BorderPane root = new BorderPane();// la racine de l'interface
	private BorderPane jeu = new BorderPane();// pour contenir la grille du jeu et le non du thème
	private Scene scene = new Scene(root, 700, 700);// la scène
	private VBox vbox = new VBox(); // pour contenir les composantes droites de l'interface
	private GridPane gpaneSol = new GridPane();// la grille de validation du jeu (boutons lettres,solutions,etc)
	private MenuBar menuBar = new MenuBar();// la bar de menu situé dans le haut de la racine
	private Menu menu1 = new Menu("Thème");// menu qui contient les thèmes situé dans la bar de menu
	private Menu menu2 = new Menu("Aide");// menu pour obtenir plus d'information situé dans la bar de menu
	private MenuItem item1 = new MenuItem("À propos de...");// item du menu Aide qui fournit les règles du jeu
	private String[] diffTheme = { "Sport", "Animaux", "Pluriel des noms" };// tableau qui contient les noms
																			// desdifférents thèmes du jeu
	private RadioMenuItem[] optionsThemes = new RadioMenuItem[diffTheme.length];// tableau qui contient les différents
																				// thèmes du jeu
	private ToggleGroup groupTheme = new ToggleGroup();// ToggleGroup pour les thèmes du jeu
	private Text nomTheme = new Text("Sport");// nom initial du thème du jeu
	// création d'une LectureDonnees qui les deux fichiers de données Sport et
	// initialise les structures de données adéquates
	private LectureDonnees lecteur = new LectureDonnees("donneesSport.txt", "grilleSport.txt", new GestionNumero(),
			new GestionSeleCase());
	private Text textIndice = new Text();// text qui correspond à l'indice d'un mot
	private Button lettres = new Button("LETTRES");// bouton qui sert à afficher les lettres mélangées d'un mot
	private Button solution = new Button("SOLUTION");// bouton qui sert à afficher la solution d'un mot
	private Button ok = new Button("OK");// bouton qui sert à valider un mot
	private Label objCourant;// clé du mot présentement sélectionné
	private TextField tFieldMot = new TextField();// TextField qui se trouve dans la grille de validation du mot
	private Label max = new Label(Integer.toString(lecteur.getScoreMax()));// Label qui affiche le score maximal de la
																			// grille
	private int scoreActuel = 0;// int qui correspond à notre score
	private Label score = new Label(Integer.toString(scoreActuel));// Label qui affiche notre score

	@Override
	public void start(Stage primaryStage) throws Exception {
		GestionTheme gt = new GestionTheme();// gestionnaire des thèmes
		GestionSeleCase gsc = new GestionSeleCase();// gestionnaire pour les différents racourcis clavier
		GestionBoutonVal gbv = new GestionBoutonVal();// gestionnaire pour les composantes de la grille de validation du
														// mot

		// on parcourt optionsThemes et on initialise les différents MenuItem
		for (int i = 0; i < diffTheme.length; i++) {
			optionsThemes[i] = new RadioMenuItem(diffTheme[i]);
			optionsThemes[i].setOnAction(gt);
			optionsThemes[i].setToggleGroup(groupTheme);
		}
		optionsThemes[0].setSelected(true);

		// on ajoute les items de menus dans leurs menus respéctif
		menu2.getItems().add(item1);
		menu1.getItems().addAll(optionsThemes);

		// création d'une alerte pour l'item 1 et le bouton aide qui affiche les régles
		// du jeu
		item1.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent arg0) {
				// TODO Auto-generated method stub
				Alert alertInfo = new Alert(AlertType.INFORMATION);
				alertInfo.setTitle("À propos de...");
				alertInfo.setHeaderText(
						"Auteur:  \nChristian Jeune, élève au Cégep Gérald-Godin dans le programme SIM(Science informatique et mathématique).");
				alertInfo.setContentText(
						"RÈGLES DU JEU: \nQuand vous cliquez sur un numéro, vous obtenez l’indice (texte descriptif) du mot "
								+ "recherché. "
								+ "Vous pouvez avoir la solution, pour le mot en cours, en cliquant sur le bouton "
								+ "correspondant. "
								+ "Le bouton Lettres vous fournit, en désordre, toutes les lettres du mot recherché. "
								+ "Pour répondre, vous écrivez le mot dans la grille correspondante et vous validez à "
								+ "l’aide du bouton OK ou la touche Entrée du clavier. Dans le cas d’une mauvaise "
								+ "réponse, la solution vous est donnée. Le bouton Ok est sans effet si la grille du mot "
								+ "courant n’est pas complète. Vous pouvez supprimer les caractères entrés à l’aide de "
								+ "la touche retour arrière (←) de votre clavier. Noter que les caractères des mots "
								+ "validés ne peuvent être effacés. Si vous essayez un autre mot avant d’avoir validé le "
								+ "mot courant, ce dernier est remis à son état initial (toutes les lettres entrées sont "
								+ "effacées) et le nouveau mot devient le mot courant. Un mot exact obtenu seulement à "
								+ "l’aide de l’indice donne un nombre de points égal"
								+ "à deux fois le nombre de lettres du mot et les cases deviennent vertes. Le recours aux lettres en désordre donne "
								+ "une fois ce nombre et les cases deviennent turquoise. Un mot solutionné, ne donne aucun point et les cases deviennent bleu pâle. "
								+ "Notez que le maximum de points pour toute la grille est affiché tout au long de la "
								+ "partie. Vous pouvez cliquer sur le bouton ContextMenu sur votre clavier pour accéder aux menus.");
				alertInfo.showAndWait();
			}

		});

		root.addEventFilter(KeyEvent.KEY_RELEASED, gsc);// ajoute gsc à la racine
		menuBar.getMenus().addAll(menu1, menu2);// ajout des différents menu à la bar menu

		// bordure noir pour les différentes composantes de la grille de validation
		BorderStroke bordureNoir = new BorderStroke(Color.BLACK, BorderStrokeStyle.SOLID, new CornerRadii(3),
				new BorderWidths(2), new Insets(0));
		Border bordure = new Border(bordureNoir);

		// ajustement de textIndice
		textIndice.setTextAlignment(TextAlignment.JUSTIFY);
		textIndice.setWrappingWidth(50 * 4);

		// création du text de bienvenue situé à la droite de l'interface
		Text textBien = new Text(
				"Bienvenue! Pour jouer cliquez sur un\nnuméro dans la grille, écrivez le mot et\ncliquez sur OK ou appuyez sur la\ntouche Entrée(Enter) pour continuer");
		textBien.setTextAlignment(TextAlignment.JUSTIFY);

		gpaneSol.setHgap(10);
		gpaneSol.setVgap(10);
		tFieldMot.setMaxHeight(2);
		tFieldMot.setFont(Font.font(15));
		tFieldMot.setBorder(bordure);
		BackgroundFill bfill = new BackgroundFill(Color.BEIGE, null, null);// arrière plan beige pour les boutons de la
																			// grille de validation
		BackgroundFill bfill1 = new BackgroundFill(Color.GOLD, new CornerRadii(8), null);// arrière plan pour les scores
																							// de la grille de
																							// validation
		gpaneSol.setBorder(bordure);

		lettres.setBackground(new Background(bfill));
		lettres.setFont(Font.font(STYLESHEET_CASPIAN, FontWeight.BOLD, 15));
		lettres.setBorder(bordure);
		lettres.addEventHandler(MouseEvent.MOUSE_CLICKED, gbv);// ajout de gbv à lettres

		solution.setBackground(new Background(bfill));
		solution.setFont(Font.font(STYLESHEET_CASPIAN, FontWeight.BOLD, 15));
		solution.setBorder(bordure);
		solution.addEventHandler(MouseEvent.MOUSE_CLICKED, gbv);// ajout de gbv à solution

		ok.setBackground(new Background(bfill));
		ok.setFont(Font.font(STYLESHEET_CASPIAN, FontWeight.BOLD, 15));
		ok.setBorder(bordure);
		ok.addEventHandler(MouseEvent.MOUSE_CLICKED, gbv);// ajout de gbv à ok

		Button aide = new Button("AIDE");// création du bouton aide de la grille de validation
		aide.setBackground(new Background(bfill));
		aide.setFont(Font.font(STYLESHEET_CASPIAN, FontWeight.BOLD, 15));
		aide.setBorder(bordure);
		aide.setOnAction(item1.getOnAction());// ajout de l'alerte à aide

		// ajustements afin d'afficher le score actuel
		VBox vbox2 = new VBox();
		vbox2.setBackground(new Background(bfill1));
		vbox2.setSpacing(5);
		Label mainText = new Label("VOTRE SCORE");
		mainText.setFont(Font.font(STYLESHEET_CASPIAN, FontWeight.EXTRA_BOLD, 12));
		mainText.setPadding(new Insets(0, 0, 0, 3));
		mainText.setTextFill(Color.BLACK);
		score.setPadding(new Insets(0, 40, 1, 40));
		score.setBackground(
				new Background(new BackgroundFill(Color.WHITE, new CornerRadii(1), new Insets(0, 5, 3, 5))));
		vbox2.getChildren().addAll(mainText, score);

		// ajustements afin d'ajuster le score maximal
		VBox vbox3 = new VBox();
		vbox3.setBackground(new Background(bfill1));
		vbox3.setSpacing(5);
		Label mainText1 = new Label("MAXIMUM");
		mainText1.setFont(Font.font(STYLESHEET_CASPIAN, FontWeight.EXTRA_BOLD, 12));
		mainText1.setPadding(new Insets(0, 0, 0, 18));
		mainText1.setTextFill(Color.BLACK);
		max.setPadding(new Insets(0, 40, 1, 40));
		max.setBackground(new Background(new BackgroundFill(Color.WHITE, new CornerRadii(1), new Insets(0, 5, 3, 5))));
		vbox3.getChildren().addAll(mainText1, max);

		// ajout des différents éléments à gpaneSol
		gpaneSol.add(vbox2, 0, 3);
		gpaneSol.add(vbox3, 1, 3);
		gpaneSol.add(tFieldMot, 0, 0, 2, 1);
		gpaneSol.add(lettres, 0, 1);
		gpaneSol.add(solution, 1, 1);
		gpaneSol.add(ok, 0, 2, 2, 1);
		gpaneSol.add(aide, 0, 4, 2, 1);
		GridPane.setHalignment(ok, HPos.CENTER);
		GridPane.setHalignment(aide, HPos.CENTER);
		GridPane.setHalignment(vbox2, HPos.RIGHT);
		GridPane.setHalignment(vbox3, HPos.RIGHT);
		gpaneSol.setPadding(new Insets(7, 7, 7, 7));

		// ajout des composantes au vbox pour former le coté droit de l'interface
		vbox.setSpacing(70);
		vbox.setPadding(new Insets(80, 10, 0, 0));
		vbox.getChildren().addAll(textIndice, textBien, gpaneSol);

		// ajout du nom du thème et de la grille de jeu à jeu
		nomTheme.setFont(Font.font(STYLESHEET_CASPIAN, FontWeight.BOLD, 30));
		jeu.setTop(nomTheme);
		jeu.setCenter(lecteur.getGpane());
		BorderPane.setAlignment(nomTheme, Pos.TOP_CENTER);

		// ajout de jeu, vbox et menuBar à la racine
		root.setCenter(jeu);
		root.setRight(vbox);
		root.setTop(menuBar);

		primaryStage.setResizable(false);
		primaryStage.setTitle("Mots Croisés");
		primaryStage.setScene(scene);
		primaryStage.show();
	}

	/**
	 * Création du gestionnaires des changements de thèmes
	 * 
	 * 
	 * @author CJ
	 *
	 */
	private class GestionTheme implements EventHandler<ActionEvent> {

		@Override
		public void handle(ActionEvent event) {
			// dépendamment du thèmes choisi parmi les items de menu
			// mise à jour du nom du thème
			// mise à jour du lecteur
			// mise à jour de la grille jeu
			// mise à jour du score max
			// mise à jour du score
			// mise à jour de l'objCourant
			if (event.getSource() == optionsThemes[0]) {
				nomTheme.setText(diffTheme[0]);
				lecteur = new LectureDonnees("donneesSport.txt", "grilleSport.txt", new GestionNumero(),
						new GestionSeleCase());
				jeu.setCenter(lecteur.getGpane());
				max.setText(Integer.toString(lecteur.getScoreMax()));
				scoreActuel = 0;
				score.setText(Integer.toString(scoreActuel));
				objCourant = null;
			} else if (event.getSource() == optionsThemes[1]) {
				nomTheme.setText(diffTheme[1]);
				lecteur = new LectureDonnees("donneesAnimaux.txt", "grilleAnimaux.txt", new GestionNumero(),
						new GestionSeleCase());
				jeu.setCenter(lecteur.getGpane());
				max.setText(Integer.toString(lecteur.getScoreMax()));
				scoreActuel = 0;
				score.setText(Integer.toString(scoreActuel));
				objCourant = null;
			} else {
				nomTheme.setText(diffTheme[2]);
				lecteur = new LectureDonnees("donneesPluriel.txt", "grillePluriel.txt", new GestionNumero(),
						new GestionSeleCase());
				jeu.setCenter(lecteur.getGpane());
				max.setText(Integer.toString(lecteur.getScoreMax()));
				scoreActuel = 0;
				score.setText(Integer.toString(scoreActuel));
				objCourant = null;
			}
		}

	}

	/**
	 * Création du gestionnaires du choix des mots sur lequel on travail
	 * 
	 * 
	 * @author CJ
	 *
	 */
	public class GestionNumero implements EventHandler<MouseEvent> {

		@Override
		public void handle(MouseEvent event) {
			// si un objet est déja choisi
			if (objCourant != null) {
				// si le mot de l'objet courant a déja été trouvé
				if (lecteur.getTableIndex().get(objCourant).isValide()) {
					// on parcours les cases du mots et on empêche les modifications
					for (int i = 0; i < lecteur.getTableIndex().get(objCourant).getStrMot().length(); i++) {
						lecteur.getTableIndex().get(objCourant).getTabCases()[i].setEditable(false);
					}
					// si le mot de l'objet courant n'a pas été trouvé
				} else if (!lecteur.getTableIndex().get(objCourant).isValide()) {
					// si l'orientation du mot est vertical
					if (lecteur.getTableIndex().get(objCourant).getDirection().getDirection().equals("V")) {
						int j = (int) lecteur.getTableIndex().get(objCourant).getPositionOrigin().getY();
						// on parcours les cases du mot
						for (int i = ((int) lecteur.getTableIndex().get(objCourant).getPositionOrigin()
								.getX()); i < ((int) lecteur.getTableIndex().get(objCourant).getPositionOrigin().getX()
										+ lecteur.getTableIndex().get(objCourant).getStrMot().length()); i++) {
							int e = i - ((int) lecteur.getTableIndex().get(objCourant).getPositionOrigin().getX());
							// si la case est marquée comme validée on empêche les modification
							if (lecteur.getTabCasesVal()[i][j]) {
								lecteur.getTableIndex().get(objCourant).getTabCases()[e].setEditable(false);
								// si la case n'est pas validé on efface le texte et on empêche les modification
							} else {
								lecteur.getTableIndex().get(objCourant).getTabCases()[e].setText("");
								lecteur.getTableIndex().get(objCourant).getTabCases()[e].setEditable(false);
							}

						}
						// si l'orientation du mot est horizontal
					} else if (lecteur.getTableIndex().get(objCourant).getDirection().getDirection().equals("H")) {
						int j = (int) lecteur.getTableIndex().get(objCourant).getPositionOrigin().getX();
						// on parcours les cases du mot
						for (int i = (int) lecteur.getTableIndex().get(objCourant).getPositionOrigin()
								.getY(); i < ((int) lecteur.getTableIndex().get(objCourant).getPositionOrigin().getY()
										+ lecteur.getTableIndex().get(objCourant).getStrMot().length()); i++) {
							int e = i - (int) lecteur.getTableIndex().get(objCourant).getPositionOrigin().getY();
							// si la case est marquée comme validée on empêche les modification
							if (lecteur.getTabCasesVal()[j][i]) {
								lecteur.getTableIndex().get(objCourant).getTabCases()[e].setEditable(false);
								// si la case n'est pas validé on efface le texte et on empêche les modification
							} else {
								lecteur.getTableIndex().get(objCourant).getTabCases()[e].setText("");
								lecteur.getTableIndex().get(objCourant).getTabCases()[e].setEditable(false);
							}

						}
					}
				}

			}
			// mise à jour de l'indice pour le nouvel objet choisi
			textIndice.setText("Indice : " + lecteur.getTableIndex().get(event.getSource()).getIndice());
			objCourant = (Label) event.getSource();// mise à jour de l'objCourant
			// on parcours les cases du nouvel objet courant et on permet les modifications
			for (int i = 0; i < lecteur.getTableIndex().get(objCourant).getStrMot().length(); i++) {
				lecteur.getTableIndex().get(objCourant).getTabCases()[i].setEditable(true);
			}
		}

	}

	/**
	 * Création du gestionnaire des boutons de la grille de validation du jeu
	 * 
	 * 
	 * @author CJ
	 *
	 */
	private class GestionBoutonVal implements EventHandler<MouseEvent> {
		@Override
		public void handle(MouseEvent event) {
			// si un objet est choisi on affiche les lettres mélangés et on met à jour
			// lettreUtil du mot
			if (objCourant != null) {
				// si le bouton lettre est utilisé et que le mot n'a pas déja été validé
				if ((event.getSource() == lettres) && !lecteur.getTableIndex().get(objCourant).verifMot()) {

					tFieldMot.setText(lecteur.getTableIndex().get(objCourant).getLettreMel());
					lecteur.getTableIndex().get(objCourant).setLettreUtil(true);
					// si le bouton solution est utilisé et que le mot n'a pas déja été validé
				} else if ((event.getSource() == solution) && !lecteur.getTableIndex().get(objCourant).verifMot()) {
					afficherSol();
					// si le bouton ok est utilisé
				} else if (event.getSource() == ok) {
					verification();
				}
			}

		}
	}

	/**
	 * Création du gestionnaires des racourcis claviers
	 * 
	 * 
	 * @author CJ
	 *
	 */
	public class GestionSeleCase implements EventHandler<KeyEvent> {

		@Override
		public void handle(KeyEvent e) {
			try {
				// si la touche est différente de la touche contextMenu
				if (!e.getCode().equals(KeyCode.CONTEXT_MENU)) {

					// quand la touche est relachée et si elle est différentes de shift et caps
					if (e.getEventType().equals(KeyEvent.KEY_RELEASED) && !e.getCode().equals(KeyCode.SHIFT)
							&& !e.getCode().equals(KeyCode.CAPS)) {

						// si la touche est égal à la flèche du haut
						if (e.getCode().equals(KeyCode.UP)) {

							// on parcours les cases du mot actuel à la recherche de la source et on donne
							// le focus à la case précédente
							for (int i = (lecteur.getTableIndex().get(objCourant).getStrMot().length()
									- 1); i > 0; i--) {
								if (lecteur.getTableIndex().get(objCourant).getTabCases()[i].equals(e.getSource())
										&& i > 0) {
									lecteur.getTableIndex().get(objCourant).getTabCases()[i - 1].requestFocus();
								}
							}
							// si la touche est égal à backspace
						} else if (e.getCode().equals(KeyCode.BACK_SPACE)) {

							// on parcours les cases du mot actuel à la recherche de la source, on efface le
							// caractère précédent et on donne le focus à la case précédente
							for (int i = (lecteur.getTableIndex().get(objCourant).getStrMot().length()
									- 1); i > 0; i--) {
								if (lecteur.getTableIndex().get(objCourant).getTabCases()[i].equals(e.getSource())
										&& i > 0) {

									if (lecteur.getTableIndex().get(objCourant).getTabCases()[i].getText().equals("")) {
										lecteur.getTableIndex().get(objCourant).getTabCases()[i - 1]
												.deletePreviousChar();
										lecteur.getTableIndex().get(objCourant).getTabCases()[i - 1].requestFocus();

									} else {
										lecteur.getTableIndex().get(objCourant).getTabCases()[i].deleteNextChar();
										lecteur.getTableIndex().get(objCourant).getTabCases()[i - 1].requestFocus();
									}
								}
							}
							// si la touche est égal à la touche Enter on vérifie le mot actuellement choisi
						} else if (e.getCode().equals(KeyCode.ENTER)) {
							verification();
							// les reste des touches possibles
						} else {
							// on parcours le tableau à la recherche de la source
							for (int i = 0; i < lecteur.getTableIndex().get(objCourant).getStrMot().length(); i++) {
								if (lecteur.getTableIndex().get(objCourant).getTabCases()[i].equals(e.getSource())
										&& i < (lecteur.getTableIndex().get(objCourant).getStrMot().length() - 1)) {

									// si la case suivant la source est disabled on donne le focus à la case suivant
									// la case suivante
									if (lecteur.getTableIndex().get(objCourant).getTabCases()[i + 1].isDisabled()
											&& i != (lecteur.getTableIndex().get(objCourant).getStrMot().length()
													- 2)) {
										lecteur.getTableIndex().get(objCourant).getTabCases()[i + 2].requestFocus();
										// sinon on donne le focus à la case suivante

									} else {
										lecteur.getTableIndex().get(objCourant).getTabCases()[i + 1].requestFocus();
									}
								}
							}
						}

					}
					// si la touche est égale à la touche menu, donne le focus à la barre de menu et
					// on affiche le menu1
				} else if (e.getCode().equals(KeyCode.CONTEXT_MENU)) {
					menuBar.requestFocus();
					menu1.show();
				}

			} catch (Exception except) {
				except.printStackTrace();
			}

		}

	}

	/**
	 * Méthode pour la vérication des mots.
	 * 
	 * Vérifie l'utilisation des boutons lettres et solution et ajuste la validation
	 * dépendamment
	 * 
	 */
	public void verification() {
		// si un mot est actuellement sélectionné
		if (objCourant != null) {
			// si le bouton lettre, mais pas le bouton solution, a été utilisé pour la
			// validation et que le mot entré est bon
			if (lecteur.getTableIndex().get(objCourant).isLettreUtil()
					&& !lecteur.getTableIndex().get(objCourant).isSolutionUtil()
					&& lecteur.getTableIndex().get(objCourant).verifMot()) {

				// on parcours les cases du mot, on change la couleur de leur case, on empêche
				// les modification et les disable
				for (int i = 0; i < lecteur.getTableIndex().get(objCourant).getStrMot().length(); i++) {
					lecteur.getTableIndex().get(objCourant).getTabCases()[i].setBackground(new Background(Mot.bfill3));
					lecteur.getTableIndex().get(objCourant).getTabCases()[i].setEditable(false);
					lecteur.getTableIndex().get(objCourant).getTabCases()[i].setDisable(true);
				}

				// on met à jour le tableau boolean des cases validées
				lecteur.setTabCasesVal(lecteur.getTableIndex().get(objCourant));

				// si le scoreActuel est plus petit que le max et que le mot n'a pas été validé
				// précédemment on met le score à jour
				if (scoreActuel < lecteur.getScoreMax() && !lecteur.getTableIndex().get(objCourant).isValide()) {
					scoreActuel += lecteur.getTableIndex().get(objCourant).getStrMot().length();
					score.setText(Integer.toString(scoreActuel));
				}
				// on indique le mot comme validé
				lecteur.getTableIndex().get(objCourant).setValide(true);

				// si le bouton solution a été utilisé pour la validation et que le mot entré
				// est bon
			} else if (lecteur.getTableIndex().get(objCourant).isSolutionUtil()
					&& lecteur.getTableIndex().get(objCourant).verifMot()) {

				// on parcours les cases du mot, on change la couleur de leur case, on empêche
				// les modification et les disable
				for (int i = 0; i < lecteur.getTableIndex().get(objCourant).getStrMot().length(); i++) {
					lecteur.getTableIndex().get(objCourant).getTabCases()[i].setBackground(new Background(Mot.bfill2));
					lecteur.getTableIndex().get(objCourant).getTabCases()[i].setEditable(false);
					lecteur.getTableIndex().get(objCourant).getTabCases()[i].setDisable(true);
				}
				// on met à jour le tableau boolean des cases validées
				lecteur.setTabCasesVal(lecteur.getTableIndex().get(objCourant));
				// on indique le mot comme validé
				lecteur.getTableIndex().get(objCourant).setValide(true);

				// si le mot entré est bon
			} else if (lecteur.getTableIndex().get(objCourant).verifMot()) {

				// on parcours les cases du mot, on change la couleur de leur case, on empêche
				// les modification et les disable
				for (int i = 0; i < lecteur.getTableIndex().get(objCourant).getStrMot().length(); i++) {
					lecteur.getTableIndex().get(objCourant).getTabCases()[i].setBackground(new Background(Mot.bfill));
					lecteur.getTableIndex().get(objCourant).getTabCases()[i].setEditable(false);
					lecteur.getTableIndex().get(objCourant).getTabCases()[i].setDisable(true);
				}
				// on met à jour le tableau boolean des cases validées
				lecteur.setTabCasesVal(lecteur.getTableIndex().get(objCourant));

				// si le scoreActuel est plus petit que le max et que le mot n'a pas été validé
				// précédemment on met le score à jour
				if (scoreActuel < lecteur.getScoreMax() && !lecteur.getTableIndex().get(objCourant).isValide()) {
					scoreActuel += lecteur.getTableIndex().get(objCourant).getStrMot().length() * 2;
					score.setText(Integer.toString(scoreActuel));
				}

				// on indique le mot comme validé
				lecteur.getTableIndex().get(objCourant).setValide(true);

				// si le mot entré n'est pas bon
			} else {
				// on parcours les cases du mot et on change la couleur de leur case
				for (int i = 0; i < lecteur.getTableIndex().get(objCourant).getStrMot().length(); i++) {
					lecteur.getTableIndex().get(objCourant).getTabCases()[i].setBackground(new Background(Mot.bfill4));
				}
				// on affiche la solution
				afficherSol();

				// on indique le mot comme non validé
				lecteur.getTableIndex().get(objCourant).setValide(false);
			}
		}
	}

	/**
	 * Méthode pour l'affichage de la solution
	 * 
	 */
	public void afficherSol() {
		// si un mot est actuellement sélectionné, on affiche le mot dans tFieldmot
		if (objCourant != null) {
			tFieldMot.setText(lecteur.getTableIndex().get(objCourant).getStrMot());
			lecteur.getTableIndex().get(objCourant).setSolutionUtil(true);
		}

	}

}
