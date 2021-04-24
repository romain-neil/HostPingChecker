package fr.chsn.hostpingchecker.actions;

import fr.chsn.hostpingchecker.MainWindow;
import fr.chsn.hostpingchecker.utils.HostListUtil;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.io.IOException;

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
		try {
			parent.getModel().setList(HostListUtil.get());
		} catch (IOException fileNotFoundException) {
			fileNotFoundException.printStackTrace();
			JOptionPane.showMessageDialog(parent, "Une erreur est survenue lors de la récupération de la liste des machines", "Erreur", JOptionPane.ERROR_MESSAGE);
		}
	}
}
