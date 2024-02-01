package com.rawchen.chatgpt.util;

import org.springframework.core.io.ClassPathResource;
import org.springframework.util.FileCopyUtils;

import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

/**
 * @author RawChen
 * @date 2022-12-24
 */
public class FileUtil {

	public static String getFileContent(String filePath) throws IOException {
		ClassPathResource resource = new ClassPathResource(filePath);
		try (InputStreamReader reader = new InputStreamReader(resource.getInputStream(), StandardCharsets.UTF_8)) {
			return FileCopyUtils.copyToString(reader);
		}
	}
}

