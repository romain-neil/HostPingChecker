package fr.chsn.hostpingchecker.utils.connector;

public class VirtualFileSystemConnector extends AbstractFSConnector {

	private String buffer;

	@Override
	public void write(String content) {
		buffer = content;
	}

	@Override
	public String read(String path) {
		return buffer;
	}

	@Override
	public void close() {}

}
