package fr.chsn.hostpingchecker.utils;

import java.util.prefs.Preferences;

/**
 * @author Romain Neil
 * @since 1.9.6
 */
public class PreferencesManager {

	private final Preferences prefs;

	public PreferencesManager() {
		prefs = Preferences.userRoot().node(this.getClass().getName());
	}

	/**
	 * Wrapper for getInt
	 * @param name parameter name
	 * @param defaultVal default value
	 * @return parameter value if exists, defaultVal otherwise
	 */
	public int getInt(String name, int defaultVal) {
		return prefs.getInt(name, defaultVal);
	}

	public PreferencesManager setInt(String key, int val) {
		prefs.putInt(key, val);

		return this;
	}

	public String getString(String name, String defaultVal) {
		return prefs.get(name, defaultVal);
	}

	/**
	 * Défini le paramètre avec la valeur passé en paramètre
	 * @param key nom du paramètre
	 * @param val valeur du paramètre
	 * @return l'instance actuelle
	 */
	public PreferencesManager setString(String key, String val) {
		prefs.put(key, val);

		return this;
	}

	/**
	 * Défini le paramètre
	 * @param key nom du paramètre à définir
	 * @param value valeur
	 * @return l'instance actuelle
	 * @since 1.10.8
	 */
	public PreferencesManager setBool(String key, boolean value) {
		prefs.putBoolean(key, value);

		return this;
	}

	/**
	 * Retourne le paramètre
	 * @param key nom du paramètre
	 * @param defaultVal paramètre par défaut
	 * @return le paramètre si il est défini, defaultVal sinon
	 * @since 1.10.8
	 */
	public boolean getBool(String key, boolean defaultVal) {
		return prefs.getBoolean(key, defaultVal);
	}

}
