package fr.chsn.hostpingchecker.utils.connector;

import java.io.BufferedWriter;
import java.io.IOException;

public class FileSystemConnector extends AbstractFSConnector {

	private final BufferedWriter bufferedWriter;

	public FileSystemConnector(java.io.Writer writer) {
		bufferedWriter = new BufferedWriter(writer);
	}

	@Override
	public void write(String path, String content) throws IOException {
		bufferedWriter.write(content);
	}

	@Override
	public String read(String path) {
		return ""; //TODO: change me
	}

	@Override
	public void close() throws IOException {
		bufferedWriter.close();
	}

}
