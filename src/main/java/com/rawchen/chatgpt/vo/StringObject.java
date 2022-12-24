package com.rawchen.chatgpt.vo;

import javax.tools.SimpleJavaFileObject;
import java.io.IOException;
import java.net.URI;

/**
 * @author RawChen
 * @date 2022-12-24
 */
public class StringObject extends SimpleJavaFileObject {
	private String contents = null;

	public StringObject(String className, String contents) throws Exception {
		super(URI.create("String:///" + className.replaceAll("\\.", "/") + Kind.SOURCE.extension), Kind.SOURCE);
		this.contents = contents;
	}

	public CharSequence getCharContent(boolean ignoreEncodingErrors) throws IOException {
		return contents;
	}
}