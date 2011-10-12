package com.music.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * @title
 * @author XiangYuan mailto:liyajie1209@gmail.com
 * @version 2011-7-22 上午11:45:11
 */
public class FileUtils {

	/**
	 * @param src
	 * @param dest
	 */
	public static void fileCopy(File src,File dest) {
		try {
			FileInputStream fis = new FileInputStream(src);
			FileOutputStream fos = new FileOutputStream(dest);
			int len = 0;
			byte[] buff = new byte[1024 * 2];
			while ((len = fis.read(buff)) != -1) {
				fos.write(buff, 0, len);
			}
			fos.flush();
			fis.close();
			fos.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
