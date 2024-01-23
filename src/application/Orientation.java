package application;

import java.awt.Point;

/**
 * Sert à stocker les différentes directions possibles.
 *
 *
 * @version 1.0 2022-10-30
 * @author Christian Jeune
 */
public enum Orientation {
	V(new Point(1, 0), "V"), H(new Point(0, 1), "H");

	private final Point coordonn; // coordonn�es de l'orientation
	private final String direction; // le nom de la direction

	/**
	 * Initialise les variables � leur valeur par d�faut.
	 *
	 *
	 * @param coordonn  coordonn�es de l'orientation.
	 * @param direction le nom de la direction.
	 */
	private Orientation(Point coordonn, String direction) {
		this.coordonn = coordonn;
		this.direction = direction;
	}

	/**
	 * Getteur pour les coordonn�es.
	 *
	 *
	 * @return un Point qui corresponds aux coordonn�es d'une orientation.
	 */
	public Point getCoordonn() {
		return coordonn;
	}

	/**
	 * Getteur pour la direction.
	 *
	 *
	 * @return un String qui correspond au nom d'une direction.
	 */
	public String getDirection() {
		return direction;
	}

}
