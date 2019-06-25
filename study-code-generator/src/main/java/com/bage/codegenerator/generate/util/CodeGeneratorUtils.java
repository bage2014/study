package com.bage.codegenerator.generate.util;

import com.bage.codegenerator.generate.model.ClassInfo;
import java.io.IOException;

/**
 *
 */
public class CodeGeneratorUtils {

	/**
	 * process Table Into ClassInfo
	 *
	 * @param tableSql
	 * @return
	 */
	public static ClassInfo processTableIntoClassInfo(String tableSql) throws IOException {
		return TableParseUtil.processTableIntoClassInfo(tableSql);
	}

}