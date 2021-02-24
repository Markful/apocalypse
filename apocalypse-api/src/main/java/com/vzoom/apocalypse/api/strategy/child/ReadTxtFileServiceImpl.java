package com.vzoom.apocalypse.api.strategy.child;

import com.vzoom.apocalypse.api.service.ExceptionService;
import com.vzoom.apocalypse.api.strategy.ReadFeedbackFileStrategy;
import com.vzoom.apocalypse.common.utils.ConvertUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
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
public class ReadTxtFileServiceImpl implements ReadFeedbackFileStrategy {

    @Autowired
    private ExceptionService exceptionService;

    @Override
    public List<String> readFeedbackData(String areaFilePath) throws IOException {
        log.info("读取txt文件内容方法开始");

        // 替换为日期
        String url = ConvertUtils.replaceDate(areaFilePath);
        String[] urlList = url.split(";");
        List<String> result = new ArrayList<>(128);
        for (String u : urlList) {
            log.info("txt文件路径" + u);
            File file = new File(u);
            InputStream is = null;
            Reader reader = null;
            BufferedReader bufferedReader = null;
            try {
                is = new FileInputStream(file);
                reader = new InputStreamReader(is, StandardCharsets.UTF_8);
                bufferedReader = new BufferedReader(reader);
                String line;
                while ((line = bufferedReader.readLine()) != null) {
                    log.info("txt文件返回内容：" + line);
                    if (StringUtils.isBlank(line)) {
                        // 返回为空跳过
                        continue;
                    }
                    result.add(line.trim());
                }
                log.info("读取txt文件结束！");
            } catch (Exception e) {
                // 不抛出异常，添加异常日志
                String exceptionMsg = "读取txt文件异常：" + e.getMessage();
                log.info(exceptionMsg);
                exceptionService.insertAnomalyLogByException(e, exceptionMsg);
            } finally {
                try {
                    if (null != bufferedReader) {
                        bufferedReader.close();
                    }
                    if (null != reader) {
                        reader.close();
                    }
                    if (null != is) {
                        is.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        return result;
    }

}
