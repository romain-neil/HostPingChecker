package fr.chsn.hostpingchecker.actions;

import fr.chsn.hostpingchecker.HostItem;
import fr.chsn.hostpingchecker.MainWindow;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.List;

/**
 * @author Romain Neil
 * @since 1.9.0
 */
public class SaveListAction extends AbstractAction {

	private final MainWindow parent;

	public SaveListAction(MainWindow parent, String title) {
		super(title);

		this.parent = parent;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		List<HostItem> list = parent.getModel().getHostList();

		if(!list.isEmpty()) {
			ObjectOutputStream oos = null;

			try {
				final FileOutputStream file = new FileOutputStream(parent.SAVE_FILE);
				oos = new ObjectOutputStream(file);
				oos.writeObject(list);
				oos.flush();
			} catch (IOException ex) {
				ex.printStackTrace();
				JOptionPane.showMessageDialog(parent, "Une erreur est survenue lors de l'enregistrement. Merci de reessayer", "Erreur", JOptionPane.ERROR_MESSAGE);
			} finally {
				try {
					if(oos != null) {
						oos.flush();
						oos.close();

						parent.setStatus("Sauvegarde effectuée");
					}
				} catch (IOException exception) {
					exception.printStackTrace();
				}
			}
		}
	}
}
