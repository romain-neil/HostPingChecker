package fr.chsn.hostpingchecker;

import fr.chsn.hostpingchecker.utils.ImageUtil;

import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * @author Romain Neil
 * @since 1.0.0
 */
public class DynamicObjectModel extends AbstractTableModel {

	ImageIcon iconUnknown;
	ImageIcon iconBad;
	ImageIcon iconOk;

	/**
	 * Liste des machines
	 */
	private List<HostItem> items = new ArrayList<>();

	private final String[] headers = {"Nom de la machine", "Adresse", "Status"};

	public DynamicObjectModel() {
		super();

		iconUnknown = ImageUtil.getScaledImage(Objects.requireNonNull(ImageUtil.createImageIcon(this, "/UI/icons/question.png")), 16, 16);
		iconBad = ImageUtil.getScaledImage(Objects.requireNonNull(ImageUtil.createImageIcon(this, "/UI/icons/warning.png")), 16, 16);
		iconOk = ImageUtil.getScaledImage(Objects.requireNonNull(ImageUtil.createImageIcon(this, "/UI/icons/ok.png")), 16, 16);
	}

	/**
	 * Change la liste actuelle par la nouvelle
	 * @param newList la nouvelle liste à charger
	 * @since 1.9
	 */
	public void setList(List<HostItem> newList) {
		this.items = newList;

		fireTableDataChanged();
	}

	/**
	 * Retourne la liste des hôtes
	 * @return la liste des hôtes
	 */
	public List<HostItem> getHostList() {
		return items;
	}

	@Override
	public int getRowCount() {
		return items.size();
	}

	@Override
	public int getColumnCount() {
		return headers.length;
	}

	@Override
	public String getColumnName(int columnIndex) {
		return headers[columnIndex];
	}

	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		switch(columnIndex) {
			case 0:
				return items.get(rowIndex).getHostName();
			case 1:
				return items.get(rowIndex).getHostIP().toString();
			case 2:
				switch(items.get(rowIndex).getStatus()) {
					case BAD -> {
						return iconBad;
					}
					case OK -> {
						return iconOk;
					}
					default -> {
						return iconUnknown;
					}
				}
			default:
				return null;
		}
	}

	@Override
	public Class<?> getColumnClass(int columnIndex) {
		return switch (columnIndex) {
			case 0, 1 -> String.class;
			case 2 -> ImageIcon.class;
			default -> Object.class;
		};
	}

	@Override
	public boolean isCellEditable(int rowIndex, int columnIndex) {
		return (columnIndex == 0);
	}

	@Override
	public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
		items.get(rowIndex).setHostName(aValue.toString());
		fireTableDataChanged();
	}

	public void addItem(HostItem item) {
		items.add(item);

		fireTableRowsInserted(items.size() - 1, items.size() - 1);
	}

	public void removeItem(int rowIndex) {
		items.remove(rowIndex);

		fireTableRowsDeleted(rowIndex, rowIndex);
	}

	public void removeAllItems() {
		items.clear();

		fireTableDataChanged();
	}

	/**
	 * Refresh the ip for all hosts in the list
	 * @throws UnknownHostException if a host fails to resolve to a ip address
	 * @since 1.12.3
	 */
	public void refreshIPs() throws UnknownHostException {
		for(HostItem item : items) {
			item.setHostIP(InetAddress.getByName(item.getDNSHostName()));
		}
	}
}
