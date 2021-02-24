package com.vzoom.apocalypse.api.strategy.child;

import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.SftpException;
import com.vzoom.apocalypse.api.service.ExceptionService;
import com.vzoom.apocalypse.api.strategy.ReadFeedbackFileStrategy;
import com.vzoom.apocalypse.common.utils.ConvertUtils;
import com.vzoom.apocalypse.common.utils.FtpUtil;
import com.vzoom.apocalypse.common.utils.SftpUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipInputStream;


/**
 * @author wans
 */
@Service
@Slf4j
public class ReadSftpZipFileServiceImpl implements ReadFeedbackFileStrategy {

    @Autowired
    private ExceptionService exceptionService;

    @Value("${}")
    private String SFTP_HOST;

    @Value("${}")
    private Integer SFTP_PORT;
    @Value("${}")
    private String SFTP_USERNAME;
    @Value("${}")
    private String SFTP_PASSWORD;
    @Value("${}")
    private String SFTP_KEY_PATH;

    @Override
    public List<String> readFeedbackData(String areaFilePath) throws IOException, SftpException, JSchException {
        log.info("读取sftp压缩文件内容方法开始");
        SftpUtils sftp = getSftpUtils();
        InputStream is = null;
        BufferedReader br = null;
        Reader rd = null;
        ZipInputStream zin = null;
        List<String> result = new ArrayList<>(128);
        // 替换为日期
        String url = ConvertUtils.replaceDate(areaFilePath);
        String[] urlList = url.split(";");
        for (String u : urlList) {
            try {
                sftp.connect();
                log.info("sftp压缩文件路径" + u);
                is = sftp.readFile(u);
                //实例化对象，指明要解压的文件
                zin = new ZipInputStream(is);
                //如果entry不为空，并不在同一个目录下
                while (zin.getNextEntry() != null) {
                    //字符流
                    rd = new InputStreamReader(zin);
                    br = new BufferedReader(rd);
                    FtpUtil.startRead(br, result);
                }
                log.info("读取sftp压缩文件内容方法结束");
            } catch (Exception e) {
                // 不抛出异常，添加异常日志
                String exceptionMsg = "读取sftp压缩文件出现异常：" + e.getMessage() + e.toString();
                log.info(exceptionMsg);
                exceptionService.insertAnomalyLogByException(e, exceptionMsg);
            } finally {
                if (zin != null) {
                    zin.close();
                }
                try {
                    if (br != null) {
                        br.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
                try {
                    if (rd != null) {
                        rd.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
                try {
                    if (is != null) {
                        is.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
                sftp.disconnect();
            }
        }

        return result;
    }

    private SftpUtils getSftpUtils() {
        SftpUtils sftp = new SftpUtils(SFTP_HOST, SFTP_USERNAME, SFTP_PASSWORD,SFTP_PORT,SFTP_KEY_PATH);
        log.info("SFTPUtils：" + sftp);
        return sftp;
    }
}
