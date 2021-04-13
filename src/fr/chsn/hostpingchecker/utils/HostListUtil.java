package fr.chsn.hostpingchecker.utils;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import fr.chsn.hostpingchecker.HostItem;
import org.apache.commons.io.IOUtils;

import java.io.*;
import java.lang.reflect.Type;
import java.util.List;

/**
 * @author Romain Neil
 * @since 1.11.0
 */
public class HostListUtil {

	public static final String SAVE_FILE = "host.json";

	/**
	 * Sauvegarde la liste des machines dans un fichier json
	 * @param liste la liste des machines à sauvegarder
	 */
	public static void save(List<HostItem> liste) throws IOException {
		Gson gson = new Gson();
		String json = gson.toJson(liste);

		BufferedWriter writer = new BufferedWriter(new FileWriter(SAVE_FILE));
		writer.write(json);

		writer.close();
	}

	/**
	 * Retourne la liste des machines depuis le fichier json
	 * @return la liste des machines sauvegardées
	 */
	public static List<HostItem> get() throws IOException {
		Gson gson = new Gson();
		FileInputStream fis = new FileInputStream(SAVE_FILE);
		String data = IOUtils.toString(fis, "UTF-8");

		Type type = new TypeToken<List<HostItem>>(){}.getType();
		return gson.fromJson(data, type);
	}

}
