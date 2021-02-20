package com.vzoom.apocalypse.api.strategy.child;

import com.vzoom.apocalypse.api.service.ExceptionService;
import com.vzoom.apocalypse.api.strategy.ReadFeedbackFileStrategy;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class ReadFtpFileServiceImpl implements ReadFeedbackFileStrategy {

    @Autowired
    private ExceptionService exceptionService;

    @Override
    public List<String> readFeedbackData(String areaFilePath) throws IOException {
        log.info("读取ftp文件内容方法开始");
        // 如果参数路径不为空则选择参数传来的路径读取
        if (StringUtils.isNotBlank(areaFilePath)) {
            FTP_FILE_PATH = areaFilePath;
        }
        // 替换为日期
        String url = replaceDate(FTP_FILE_PATH);
        List<String> content = new ArrayList<>(128);
        String[] urlList = url.split(";");
        for (String u : urlList) {
            try {
                FtpUtil ftp = new FtpUtil(FTP_HOST, FTP_PORT,FTP_LOGIN_NAME, FTP_LOGIN_PASSWORD);
                log.info("FTPUtils：" + ftp);
                log.info("ftp文件路径" + u);
                content.addAll(ftp.FtpDownload(u));
            } catch (Exception e) {
                // 不抛出异常，添加异常日志
                String exceptionMsg = "读取ftp文件出现异常：" + e.getMessage() + e.toString();
                log.info(exceptionMsg);
                exceptionService.insertAnomalyLogByException(e, exceptionMsg);
            }
        }
        log.info("读取ftp文件内容方法结束");

        return content;
    }
}
