package cn.home.maventool;

import java.io.BufferedReader;
import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.StringTokenizer;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;


/**
 * @author lihzh-home
 */
public class Main {
	
	private static final Log _log = LogFactory.getLog(Main.class);
	private static PropertyHelper propHelper = new PropertyHelper("config");
	private static Runtime _runRuntime = Runtime.getRuntime();
	private static boolean isDelete = Boolean.valueOf(propHelper.getValue("delete-installed-jar"));
	private static boolean isMove = Boolean.valueOf(propHelper.getValue("move-installed-jar"));
	private static final String KEY_JARPATH = "jar-path";
	private static final String KEY_BACKUPPATH = "back-path";
	private static final String ENCODE = "gbk";
	private static final String INSTALL_PATH = propHelper.getValue(KEY_JARPATH);
	private static  String CMD_INSTALL_FILE;
	private static  String CMD_BACKUP_JAR;
	
	public static void main(String[] args) {
		
		_log.info("The path of the jars is ["+INSTALL_PATH+"].");
		File file = new File(INSTALL_PATH);
		if(!file.isDirectory()){
			_log.warn("The path must be a directory.");
			return;
		}
//		FilenameFilter filter = new JarFilter();
//		File[] jarFiles = file.listFiles(filter);
//		for(File jar: jarFiles){
//			installJarToMaven(jar);
//			if(isDelete){
//				_log.info("Delete the original jar file ["+jar.getName()+"].");
//				jar.delete();
//			}else{
//				if(isMove){
//					String backupPath = propHelper.getValue(KEY_BACKUPPATH);
//					backupJar(jar,file,backupPath);
//				}
//			}
//		}
	}

	private static void backupJar(File jar, File file, String backupPath) {
		CMD_BACKUP_JAR = "copy "+INSTALL_PATH+File.separator+jar.getName()+" "+backupPath;
		String[] cmds = new String[]{"cmd", "/C",CMD_BACKUP_JAR};
		try {
			Process process =_runRuntime.exec(cmds,null,file);
			printResult(process);
		} catch (IOException e) {
			e.printStackTrace();
		}
			_log.info("The jar ["+jar.getName()+"]  is backup, it's will be deleted.");
			jar.delete();
		
	}

	private static void installJarToMaven(File file) {
		String fileName = file.getName();
		String jarName = getJarName(fileName);
		StringTokenizer strToken = new StringTokenizer(jarName, "-");
		String groupId=null;
		String artifactId=null;
		String version=null;
		if(strToken.hasMoreTokens()){
			if(groupId==null){
				groupId = strToken.nextToken();
				if(strToken.hasMoreTokens()){
					artifactId = strToken.nextToken();
					if(strToken.hasMoreTokens()){
						version = strToken.nextToken();
					}
				}else{
					version = artifactId = groupId;
				}
			}
		}
		_log.info("Jar ["+jarName+"] will be installed with the groupId="+groupId+" ,"
				+"artifactId="+artifactId+" , version="+version+".");
		executeInstall( groupId,  artifactId,  version, file.getPath());
	}

	private static void executeInstall(String groupId, String artifactId,
			String version, String path) {
		CMD_INSTALL_FILE = createInstallFileCMD( groupId,  artifactId,
				 version,  path);
		String[] cmds = new String[]{"cmd", "/C",CMD_INSTALL_FILE};
		try {
			Process process = _runRuntime.exec(cmds);
			printResult(process);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private static void printResult(Process process) throws IOException {
		InputStream is = process.getInputStream();
		BufferedReader br = new BufferedReader(new InputStreamReader(is,ENCODE));
		String lineStr;
		while((lineStr= br.readLine()) !=null){
			System.out.println(lineStr);
		}
	}

	private static String createInstallFileCMD(String groupId,
			String artifactId, String version, String path) {
		StringBuffer sb = new StringBuffer();
		sb.append("mvn install:install-file -DgroupId=").append(groupId)
			.append(" -DartifactId=").append(artifactId)
			.append(" -Dversion=").append(version)
			.append(" -Dpackaging=jar")
			.append(" -Dfile=").append(path);
		_log.debug(sb.toString());
		return sb.toString();
	}

	private static String getJarName(String fileName) {
		int index = fileName.indexOf(".jar");
		return fileName.substring(0, index);
	}

}
