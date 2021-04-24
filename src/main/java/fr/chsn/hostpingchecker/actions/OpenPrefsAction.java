package fr.chsn.hostpingchecker.actions;

import fr.chsn.hostpingchecker.MainWindow;
import fr.chsn.hostpingchecker.PreferencesWindow;

import javax.swing.*;
import java.awt.event.ActionEvent;

public class OpenPrefsAction extends AbstractAction {

	private final MainWindow parent;

	public OpenPrefsAction(MainWindow parent, String desc) {
		super(desc);

		this.parent = parent;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		new PreferencesWindow(parent);
	}
}
