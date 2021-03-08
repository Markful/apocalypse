package com.vzoom.apocalypse.common.utils;


import com.jcraft.jsch.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import java.io.InputStream;
import java.util.Properties;
import java.util.Vector;

/**
 * @author zuoyelin
 * @date 2019/6/12 10:28
 */
@Component
@ToString
@Data
@AllArgsConstructor
@Slf4j
public class SftpUtils {
    /** 服务器连接ip */
    private String host;
    /** 用户名 */
    private String username;
    /** 密码 */
    private String password;
    /** 端口号 */
    private int port;
    /** 密钥路径 */
    private String keyPath;

    private ChannelSftp sftp = null;
    private Session sshSession = null;

    public SftpUtils(String host, String username, String password, int port) {
        this.host = host;
        this.username = username;
        this.password = password;
        this.port = port;
    }

    public SftpUtils(String host, String username , String password, int port, String keyPath) {
        this.host = host;
        this.username = username;
        this.password = password;
        this.port = port;
        this.keyPath = keyPath;
    }

    public SftpUtils(){}


    /**
     * 通过SFTP连接服务器
     */
    public void connect() throws JSchException {
        try
        {
            JSch jsch = new JSch();
            if(StringUtils.isNotBlank(keyPath)){
                jsch.addIdentity(keyPath);
            }
            sshSession = jsch.getSession(username, host, port);
            if (log.isInfoEnabled())
            {
                log.info("Session created.");
            }
            if(StringUtils.isNotBlank(password)){
                sshSession.setPassword(password);
            }
            Properties sshConfig = new Properties();
            sshConfig.put("StrictHostKeyChecking", "no");
            sshSession.setConfig(sshConfig);
            sshSession.setTimeout(100000);
            sshSession.connect();
            if (log.isInfoEnabled())
            {
                log.info("Session connected.");
            }
            Channel channel = sshSession.openChannel("sftp");
            channel.connect();
            if (log.isInfoEnabled())
            {
                log.info("Opening Channel.");
            }
            sftp = (ChannelSftp) channel;
            if (log.isInfoEnabled())
            {
                log.info("Connected to " + host + ".");
            }
        }
        catch (Exception e)
        {
            log.info("连接sftp服务器方法异常:"+e.toString()+e.getMessage());
            e.printStackTrace();
            throw e;
        }
    }

    /**
     * 关闭连接
     */
    public void disconnect()
    {
        if (this.sftp != null)
        {
            if (this.sftp.isConnected())
            {
                this.sftp.disconnect();
                if (log.isInfoEnabled())
                {
                    log.info("sftp is closed already");
                }
            }
        }
        if (this.sshSession != null)
        {
            if (this.sshSession.isConnected())
            {
                this.sshSession.disconnect();
                if (log.isInfoEnabled())
                {
                    log.info("sshSession is closed already");
                }
            }
        }
    }

    /**
     * 读取单个文件
     */
    public InputStream readFile( String path) throws SftpException {
        log.info("进入读取sftp文件方法地址:"+path);
        InputStream is =null;
        try{
            is = sftp.get(path);
            if (log.isInfoEnabled())
            {
                log.info("===DownloadFile:" + path + " success from sftp.");
            }
        }
        catch (SftpException e){
            log.info("读取sftp文件方法异常:"+e.toString()+e.getMessage());
            throw e;
        }
        return is;
    }

    /**
     * 删除stfp文件
     * @param directory：要删除文件所在目录
     * @param deleteFile：要删除的文件
     */
    public void deleteSftp(String directory, String deleteFile)
    {
        try
        {
            // sftp.cd(directory);
            sftp.rm(directory + deleteFile);
            if (log.isInfoEnabled())
            {
                log.info("delete file success from sftp.");
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
    /**
     * 列出目录下的文件
     *
     * @param directory：要列出的目录
     */
    public Vector listFiles(String directory) throws SftpException
    {
        return sftp.ls(directory);
    }
}
