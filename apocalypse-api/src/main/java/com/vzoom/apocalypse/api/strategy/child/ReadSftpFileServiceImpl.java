package com.vzoom.apocalypse.api.strategy.child;

import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.SftpException;
import com.vzoom.apocalypse.api.service.ExceptionService;
import com.vzoom.apocalypse.api.strategy.ReadFeedbackFileStrategy;
import com.vzoom.apocalypse.common.utils.ConvertUtils;
import com.vzoom.apocalypse.common.utils.SftpUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;


/**
 * @author wans
 */
@Service
@Slf4j
public class ReadSftpFileServiceImpl implements ReadFeedbackFileStrategy {

    @Autowired
    private ExceptionService exceptionService;

    @Value("${test}")
    private String SFTP_HOST;
    @Value("${test}")
    private Integer SFTP_PORT;
    @Value("${test}")
    private String SFTP_USERNAME;
    @Value("${test}")
    private String SFTP_PASSWORD;
    @Value("${test}")
    private String SFTP_KEY_PATH;


    @Override
    public List<String> readFeedbackData(String areaFilePath) throws JSchException, SftpException, IOException {
        log.info("读取sftp文件内容方法开始");

        // 替换为日期
        String url = ConvertUtils.replaceDate(areaFilePath);
        SftpUtils sftp = new SftpUtils(SFTP_HOST, SFTP_USERNAME, SFTP_PASSWORD,SFTP_PORT,SFTP_KEY_PATH);
        log.info("SFTPUtils：" + sftp);
        InputStream is = null;
        Reader rd = null;
        BufferedReader br = null;
        List<String> result = new ArrayList<>(128);
        String[] urlList = url.split(";");
        for (String u : urlList) {
            try {
                sftp.connect();
                log.info("sftp文件路径" + u);
                is = sftp.readFile(u);
                // 字符流
                rd = new InputStreamReader(is, StandardCharsets.UTF_8);
                br = new BufferedReader(rd);
                String line;
                while ((line = br.readLine()) != null) {
                    log.info("sftp文件返回内容:" + line);
                    if (StringUtils.isBlank(line)) {
                        // 返回为空跳过
                        continue;
                    }
                    result.add(line.trim());
                }
                log.info("读取sftp文件内容方法结束");
            } catch (Exception e) {
                // 不抛出异常，添加异常日志
                String exceptionMsg = "读取sftp文件出现异常：" + e.getMessage() + e.toString();
                log.info(exceptionMsg);
                exceptionService.insertAnomalyLogByException(e, exceptionMsg);
            } finally {
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
}
