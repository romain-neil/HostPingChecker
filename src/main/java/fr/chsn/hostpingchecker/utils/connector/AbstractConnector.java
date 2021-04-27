package fr.chsn.hostpingchecker.utils.connector;

import java.io.IOException;

public abstract class AbstractConnector {

	public abstract void write(String path, String content) throws IOException;

	public abstract String read(String path);

	public abstract void close() throws IOException;

}
