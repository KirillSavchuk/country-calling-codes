package lv.savchuk.country.calling.codes;

import java.io.IOException;
import java.io.InputStream;
import java.util.Objects;

public class TestUtils {

	public static String readFile(String pathToFile) throws IOException {
		return readFile(pathToFile, Thread.currentThread().getContextClassLoader());
	}

	public static String readFile(String pathToFile, Class<?> cls) throws IOException {
		return readFile(pathToFile, cls.getClassLoader());
	}

	public static String readFile(String pathToFile, ClassLoader classLoader) throws IOException {
		try (InputStream is = classLoader.getResourceAsStream(pathToFile)) {
			return new String(Objects.requireNonNull(is).readAllBytes());
		}
	}

}