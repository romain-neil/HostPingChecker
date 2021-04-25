package fr.chsn.hostpingchecker.utils;

import java.nio.charset.StandardCharsets;
import java.util.Objects;
import java.util.Scanner;

/**
 * Classe qui gère le chargement de resources
 * @author Romain Neil
 * @since 1.10.3
 */
public class ResourceUtil {

	/**
	 * Retourne le contenu d'un fichier
	 * @param base classe appelante
	 * @param path chemin du fichier
	 * @return le contenu du fichier s'il existe
	 */
	public static String getResourceContent(Object base, String path) {
		return new Scanner(Objects.requireNonNull(base.getClass().getResourceAsStream(path)), StandardCharsets.UTF_8).next();
	}
}
