package com.bugsyteam.utils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.stream.Stream;

import org.apache.log4j.Logger;

public class FilesWork {

	private static final Logger LOGGER = Logger.getLogger(FilesWork.class);

	public static void emptyLogRoot(String filePath) {
		Path p = Paths.get(filePath);
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		Date date = new Date();

		if (Files.exists(p)) {
			delete(p, formatter.format(date) + ".log");

		}
	}

	public static void deleteFile(Path p) {

		try {
			Files.deleteIfExists(p);
		} catch (IOException e) {
			LOGGER.error(e);
		}

	}

	private static void delete(Path p, String excludeThisSufix) {

		try (Stream<Path> stream = Files.list(p)) {
			stream.forEach(s -> {
				if (!s.toString().endsWith(excludeThisSufix))
					try {
						Files.deleteIfExists(s);
					} catch (IOException e) {
						e.printStackTrace();
					}
			});
			stream.close();
		} catch (IOException e) {
			LOGGER.error(e);
		}

	}

	/**
	 * @param directory
	 * @return
	 */
	public static boolean delete(final File directory) {
		assert directory != null && directory.exists();
		if (!directory.isDirectory()) {
			return directory.delete();
		}
		for (final File f : directory.listFiles()) {
			delete(f);
		}
		return directory.delete();
	}

	/**
	 * Private constructor preventing outer initialization
	 */
	private FilesWork() {
		// Prevent initialization
	}

}
