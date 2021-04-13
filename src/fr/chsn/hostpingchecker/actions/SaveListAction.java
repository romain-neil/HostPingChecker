package fr.chsn.hostpingchecker.actions;

import fr.chsn.hostpingchecker.HostItem;
import fr.chsn.hostpingchecker.MainWindow;
import fr.chsn.hostpingchecker.utils.HostListUtil;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.io.IOException;
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
			try {
				HostListUtil.save(list);
			} catch (IOException ioException) {
				ioException.printStackTrace();
				JOptionPane.showMessageDialog(parent, "Une ereeur est survenue lors de l'enregistrement de la liste des machines", "Erreur", JOptionPane.ERROR_MESSAGE);
			}
		}
	}
}
