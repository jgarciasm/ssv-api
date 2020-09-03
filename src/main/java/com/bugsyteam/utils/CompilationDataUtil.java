package com.bugsyteam.utils;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Date;
import java.util.jar.JarFile;
import java.util.zip.ZipEntry;

import org.apache.maven.model.Model;
import org.apache.maven.model.io.xpp3.MavenXpp3Reader;
import org.codehaus.plexus.util.xml.pull.XmlPullParserException;

public class CompilationDataUtil {
	private static final Date buildDate = getClassBuildTime();
	private static URL resource;

	private static void init() {
		Class<?> currentClass = new Object() {
		}.getClass().getEnclosingClass();
		resource = currentClass.getResource(currentClass.getSimpleName() + ".class");
	}

	/**
	 * Handles files, jar entries, and deployed jar entries in a zip file (EAR).
	 *
	 * @return The date if it can be determined, or null if not.
	 */
	public static Date getClassBuildTime() {
		init();
		Date d = null;

		if (resource != null) {
			if (resource.getProtocol().equals("file")) {
				try {
					d = new Date(new File(resource.toURI()).lastModified());
				} catch (URISyntaxException ignored) {
				}
			} else if (resource.getProtocol().equals("jar")) {
				String path = resource.getPath();
				d = new Date(new File(path.substring(5, path.indexOf("!"))).lastModified());
			} else if (resource.getProtocol().equals("zip") || resource.getProtocol().equals("j2e")) {

				String path = resource.getPath();
				File jarFileOnDisk = new File(path.substring(0, path.indexOf("!")));
				try (JarFile jf = new JarFile(jarFileOnDisk)) {
					ZipEntry ze = jf.getEntry(path.substring(path.indexOf("!") + 1));// Skip the ! and the /
					long zeTimeLong = ze.getTime();
					Date zeTimeDate = new Date(zeTimeLong);
					d = zeTimeDate;
				} catch (IOException | RuntimeException ignored) {
				}
			}
		}
		return d;
	}

	public static String getVersion() throws IOException, XmlPullParserException {
		init();
		MavenXpp3Reader reader = new MavenXpp3Reader();
		Model model;
		if (resource != null) {
			if (resource.getProtocol().equals("file")) {
				model = reader.read(new FileReader("pom.xml"));
				return model.getVersion();
			} else if (resource.getProtocol().equals("jar")) {
				InputStream is = CompilationDataUtil.class.getResourceAsStream(
						"/META-INF/maven/com.anf.apidestinatarios.verticles/api-destinatarios/pom.xml");
				model = reader.read(is);
				return model.getVersion();
			} else if (resource.getProtocol().equals("zip") || resource.getProtocol().equals("j2e")) {
				String path = resource.getPath();
				File jarFileOnDisk = new File(path.substring(0, path.indexOf("!")));
				try (JarFile jf = new JarFile(jarFileOnDisk)) {
					ZipEntry ze = jf
							.getEntry("META-INF/maven/com.anf.apidestinatarios.verticles/api-destinatarios/pom.xml");// Skip
																														// the
																														// !
																														// and
																														// the
																														// /
					model = reader.read(jf.getInputStream(ze));
					System.out.println(model.getId());
					System.out.println(model.getGroupId());
					System.out.println(model.getArtifactId());
					System.out.println(model.getVersion());
					return model.getVersion();
				} catch (IOException | RuntimeException ignored) {
				}
			}
		}
		return "Unknown version";

	}
}
