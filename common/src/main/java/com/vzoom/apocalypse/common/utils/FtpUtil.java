package com.vzoom.apocalypse.common.utils;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipInputStream;

/**
 * @author wans
 */
public class FtpUtil {
	
	private static final Log logger = LogFactory.getLog(FtpUtil.class);
    
	private FTPClient ftpclient = new FTPClient();
	
	public FtpUtil(String ftpIp, int ftpPort, String ftpLoginName, String ftpLoginPwd) {
		try {
			ftpclient.connect(ftpIp,ftpPort);
			ftpclient.login(ftpLoginName, ftpLoginPwd);
			ftpclient.changeWorkingDirectory("/");
			ftpclient.setBufferSize(1024);
			ftpclient.setControlEncoding("UTF-8");
			ftpclient.setFileType(FTPClient.BINARY_FILE_TYPE);
			ftpclient.enterLocalPassiveMode();
			logger.info("connect...ok...");
		} catch (Exception e) {
			logger.error("初始化ftp异常：" + e.getMessage());
		}
	}

	public void ftpClose() {
		
		if(ftpclient!=null){
			try {
				ftpclient.logout();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		if(ftpclient!=null){
			try {
				ftpclient.disconnect();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
	}

	/**
	 * 从ftp上下载一个文件
	 * @param fileName
	 * @return
	 */
	public List<String> FtpDownload(String fileName) throws IOException {
		InputStream in = null;
		InputStreamReader isr = null;
		BufferedReader br = null;
		List<String> result = new ArrayList<>(128);
		try {
			in = ftpclient.retrieveFileStream(fileName);
			isr = new InputStreamReader(in, StandardCharsets.UTF_8);
			br = new BufferedReader(isr);
			String s;
			while ((s = br.readLine()) != null) {
				logger.info("ftp文件返回内容:" + s);
				if(StringUtils.isBlank(s)){
					// 返回为空跳过
					continue;
				}
				result.add(s);
			}
		} catch (Exception e) {
			logger.error("下载ftp上面的文件异常：" + e.getMessage());
			throw e;
		} finally {
			if(br!=null){
				try {
					br.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}

			if(isr!=null){
				try {
					isr.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}

			if(in!=null){
				try {
					in.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}

			ftpClose();
		}

		return result;
	}

	/**
	 * 从ftp上下载一个文件
	 * @param fileName
	 * @return
	 */
	public List<String> FtpZipDownload(String fileName) throws IOException {
		InputStream in = null;
		InputStreamReader isr = null;
		BufferedReader br = null;
		ZipInputStream zin = null;
		List<String> result = new ArrayList<>(128);
		try {
			in = ftpclient.retrieveFileStream(fileName);
			//实例化对象，指明要解压的文件
			zin = new ZipInputStream(in);
			//如果entry不为空，并不在同一个目录下
			while (zin.getNextEntry() != null) {
				//字符流
				isr = new InputStreamReader(zin);
				br = new BufferedReader(isr);
				startRead(br, result);
			}
		} catch (Exception e) {
			logger.error("下载ftp上面的压缩文件异常：" + e.getMessage());
			throw e;
		} finally {
			if (zin != null) {
				zin.close();
			}
			if(br!=null){
				try {
					br.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}

			if(isr!=null){
				try {
					isr.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}

			if(in!=null){
				try {
					in.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}

			ftpClose();
		}

		return result;
	}

	private void closeStream(InputStream in, InputStreamReader isr, BufferedReader br) {

	}

	/**
	 * 上传一个文件到ftp
	 *
	 */
	public boolean FTPUpload(String path, String fileName, String text) throws Exception {
		boolean bool = false;
		InputStream is = null;
		String fullPath = path + fileName;
		try {
			createDirecroty(path);
			is = new ByteArrayInputStream(text.getBytes("UTF-8"));
			bool = ftpclient.storeFile(new String(fullPath.getBytes("UTF-8"), "iso-8859-1"), is);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("上传文件到ftp异常：" + e);
			throw new Exception();
		} finally {
			if(is!=null){
				is.close();
			}
			
			ftpClose();
		}
		return bool;
	}

	/**
	 * 在ftp服务器上创建目录
	 * @param remote
	 * @return
	 */
	public boolean createDirecroty(String remote) {
		boolean success = true;
		try {
			String directory = remote.substring(0, remote.lastIndexOf("/") + 1);
			// 如果远程目录不存在，则递归创建远程服务器目录
			if (!directory.equalsIgnoreCase("/") && !ftpclient.changeWorkingDirectory(new String(directory))) {
				int start = 0;
				int end = 0;
				if (directory.startsWith("/")) {
					start = 1;
				} else {
					start = 0;
				}
				end = directory.indexOf("/", start);
				while (true) {
					String subDirectory = new String(remote.substring(start, end));
					if (!ftpclient.changeWorkingDirectory(subDirectory)) {
						if (ftpclient.makeDirectory(subDirectory)) {
							ftpclient.changeWorkingDirectory(subDirectory);
						} else {
							logger.info("创建目录失败");
							success = false;
							return success;
						}
					}
					start = end + 1;
					end = directory.indexOf("/", start);
					// 检查所有目录是否创建完毕
					if (end <= start) {
						break;
					}
				}
			}
		} catch (Exception e) {
			logger.error("创建文件夹异常：" + e.getMessage());
		}
		return success;
	}

	/**
	 * 列出ftp文件夹中的目录
	 * @param directory
	 * @return
	 */
	public List<String> listFiles(String directory) {
		List<String> list = null;
		try {
			FTPFile[] ftpFilesList = ftpclient.listFiles(directory);

			list = new ArrayList<String>();
			for (FTPFile file : ftpFilesList) {
				list.add(file.getName());
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			ftpClose();
		}
		return list;
	}

	public static void startRead(BufferedReader br, List<String> result) throws IOException {
		String line;
		while ((line = br.readLine()) != null) {
			logger.info("文件返回内容:" + line);
			if (StringUtils.isBlank(line)) {
				// 返回为空跳过
				continue;
			}
			result.add(line.trim());
		}
	}

}
