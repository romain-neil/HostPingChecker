package fr.chsn.hostpingchecker.utils.connector;

public class VirtualFileSystemConnector extends AbstractConnector {

	@Override
	public void write(String path, String content) {}

	@Override
	public String read(String path) {
		return null;
	}

	@Override
	public void close() {}

}
