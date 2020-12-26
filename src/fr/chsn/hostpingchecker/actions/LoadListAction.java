package fr.chsn.hostpingchecker.actions;

import fr.chsn.hostpingchecker.HostItem;
import fr.chsn.hostpingchecker.MainWindow;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.io.*;
import java.util.List;

/**
 * @author Romain Neil
 * @since 1.9.0
 */
public class LoadListAction extends AbstractAction {

	private final MainWindow parent;

	public LoadListAction(MainWindow parent, String title) {
		super(title);

		this.parent = parent;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		List<HostItem> list;

		ObjectInputStream ois = null;

		File f = new File(parent.SAVE_FILE);

		if(f.exists()) {
			try {
				final FileInputStream file = new FileInputStream(parent.SAVE_FILE);
				ois = new ObjectInputStream(file);
				list = (List<HostItem>) ois.readObject();
				parent.getModel().setList(list);
			} catch(final IOException | ClassNotFoundException ex) {
				ex.printStackTrace();

				if(ex instanceof InvalidClassException) {
					//Mauvaise version du fichier de la liste des machines
					JOptionPane.showMessageDialog(parent, "La liste des machines n'est pas compatible avec cette version", "Erreur", JOptionPane.ERROR_MESSAGE);
				} else {
					JOptionPane.showMessageDialog(parent, "Une erreur est survenue lors du chargement. Merci de reessayer", "Erreur", JOptionPane.ERROR_MESSAGE);
				}
			} finally {
				try {
					if(ois != null) {
						ois.close();
					}
				} catch (IOException ex) {
					ex.printStackTrace();
				}
			}
		}
	}
}
