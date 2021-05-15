package fr.chsn.hostpingchecker;

import javax.swing.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ProgramEntry {

	public static void main(String[] args) {

		MainWindow fen;

		try {
			fen = new MainWindow();

			fen.setLocationByPlatform(true);
			fen.setVisible(true);
		} catch (Exception ex) {
			Logger.getLogger(ProgramEntry.class.getName()).log(Level.SEVERE, null, ex);
			JOptionPane.showMessageDialog(null, "Une erreur est survenue lors du démarrage du logiciel", "Erreur innatendue", JOptionPane.ERROR_MESSAGE);
		}

	}

}
