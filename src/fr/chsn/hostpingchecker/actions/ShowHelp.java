package fr.chsn.hostpingchecker.actions;

import javax.swing.*;
import java.awt.event.ActionEvent;

public class ShowHelp extends AbstractAction {

	private final JFrame par;

	public ShowHelp(JFrame parent, String desc) {
		super(desc);

		par = parent;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		JOptionPane.showMessageDialog(par, "Développé par Romain Neil - © Centre hospitalier de Saint-Nazaire 2020", "A propos", JOptionPane.INFORMATION_MESSAGE);
	}

}
