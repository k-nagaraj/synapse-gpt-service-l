package com.smartdocs.gpt.document.service;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.net.URLConnection;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import com.smartdocs.gpt.document.model.Metadata;


public class DocumentSource {
	private static final String FILE_NAME = "file_name";
	private static final String ABSOLUTE_DIRECTORY_PATH = "absolute_directory_path";

	private final Path path;
	private static final String URL = "url";

	private final URL url;

	public DocumentSource(Path path, URL url) {
		this.path = path;
		this.url = url;
	}

	public InputStream fileSystemInputStream() throws IOException {
		return Files.newInputStream(path);
	}

	public static DocumentSource from(Path filePath) {
		return new DocumentSource(filePath, null);
	}

	public static DocumentSource fromFile(String filePath) {
		return new DocumentSource(Paths.get(filePath), null);
	}

	public static DocumentSource fromFile(URI fileUri) {
		return new DocumentSource(Paths.get(fileUri), null);
	}

	public static DocumentSource fromFile(File file) {
		return new DocumentSource(file.toPath(), null);
	}

	public Metadata fileSystemSourceMetadata() {
		return new Metadata().add(FILE_NAME, path.getFileName().toString()).add(ABSOLUTE_DIRECTORY_PATH,
				path.getParent().toAbsolutePath().toString());
	}

	public InputStream urlInputStream() throws IOException {
		URLConnection connection = url.openConnection();
		return connection.getInputStream();
	}

	public static DocumentSource from(String url) {
		try {
			return new DocumentSource(null, new URL(url));
		} catch (MalformedURLException e) {
			throw new RuntimeException(e);
		}
	}

	public static DocumentSource from(URL url) {
		return new DocumentSource(null, url);
	}

	public static DocumentSource from(URI uri) {
		try {
			return new DocumentSource(null, uri.toURL());
		} catch (MalformedURLException e) {
			throw new RuntimeException(e);
		}
	}

	public Metadata sourceMetadata() {
		return new Metadata().add(URL, url.toString());
	}
}
