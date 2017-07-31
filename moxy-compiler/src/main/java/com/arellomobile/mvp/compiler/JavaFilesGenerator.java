package com.arellomobile.mvp.compiler;

import com.squareup.javapoet.JavaFile;

import java.util.List;

/**
 * Date: 27-Jul-17
 * Time: 10:26
 *
 * @author Evgeny Kursakov
 */
public abstract class JavaFilesGenerator<T> {
	public abstract List<JavaFile> generate(T input);
}

