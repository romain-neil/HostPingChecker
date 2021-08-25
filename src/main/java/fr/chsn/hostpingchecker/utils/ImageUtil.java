package fr.chsn.hostpingchecker.utils;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.swing.*;
import java.awt.*;
import java.net.URL;

/**
 * Classe de fonctions utilitaires pour la manipulation d'images
 * @author Romain Neil
 * @since 1.6.5
 */
public class ImageUtil {

	private static final Logger logger = LogManager.getLogger("ImageUtil");

	/**
	 * Retourne un object ImageIcon
	 * @param base Classe parente qui gère les ressources
	 * @param path Chemin du fichier à charger
	 * @return L'object ImageIcon correspondant, ou null
	 */
	public static ImageIcon createImageIcon(Object base, String path) {
		if(path != null) {
			URL imgUrl = base.getClass().getResource(path);

			if(imgUrl != null) {
				return new ImageIcon(imgUrl);
			}

			logger.error("Couldn't find file: " + path);
		} else {
			logger.error("The image path is null");
		}

		return null;
	}

	/**
	 * Retourne un object ImageIcon redimensionné
	 * @param icon ImageIcon à redimensionner
	 * @param w largeur
	 * @param h hauteur
	 * @return Retourne l'object ImageIcon redimensionné
	 */
	public static ImageIcon getScaledImage(ImageIcon icon, int w, int h) {
		int nw = icon.getIconWidth();
		int nh = icon.getIconHeight();

		if(icon.getIconWidth() > w) {
			nw = w;
			nh = (nw * icon.getIconHeight()) / icon.getIconWidth();
		}

		if(nh > h) {
			nh = h;
			nw = (icon.getIconWidth() * nh) / icon.getIconHeight();
		}

		return new ImageIcon(icon.getImage().getScaledInstance(nw, nh, Image.SCALE_DEFAULT));
	}

}
