package com.vzoom.apocalypse.api.strategy.child;

import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.SftpException;
import com.vzoom.apocalypse.api.strategy.ReadFeedbackFileStrategy;
import com.vzoom.apocalypse.common.utils.ConvertUtils;
import com.vzoom.apocalypse.common.utils.SftpUtils;
import com.vzoom.zxxt.service.AnomalyService;
import com.vzoom.zxxt.strategy.ReadFeedbackFileStrategy;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import static com.vzoom.zxxt.util.GlobalVariable.replaceDate;
import static com.vzoom.zxxt.util.ReadFileConfigConstant.*;

/**
 * @author wans
 */
@Service
public class ReadSftpFileServiceImpl implements ReadFeedbackFileStrategy {
    private static final Logger log = LoggerFactory.getLogger(ReadSftpFileServiceImpl.class);

    @Autowired
    private AnomalyService anomalyService;

    @Override
    public List<String> readFeedbackData(String areaFilePath) throws JSchException, SftpException, IOException {
        log.info("读取sftp文件内容方法开始");
        // 如果参数路径不为空则选择参数传来的路径读取
        if (StringUtils.isNotBlank(areaFilePath)) {
            SFTP_PATH_LIST = areaFilePath;
        }
        // 替换为日期
        String url = ConvertUtils.replaceDate(SFTP_PATH_LIST);
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
                anomalyService.insertAnomalyLogByException(e, exceptionMsg);
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
