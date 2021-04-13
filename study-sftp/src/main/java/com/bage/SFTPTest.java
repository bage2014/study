package com.bage;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;

import java.util.Objects;
import java.util.Properties;

public class SFTPTest {

    private String host = "127.0.0.1";

    private String username = "bage";

    private String password = "bage";

    private int port = 22;

    private ChannelSftp sftp = null;

    private Session sshSession = null;


    /**
     * connect server via sftp
     */

    public void connect(String host, String username, String password, String prvKeyFile, int port) {
        try {
            JSch jsch = new JSch();
            if (Objects.nonNull(prvKeyFile)) {
                jsch.addIdentity(prvKeyFile);
            }
            jsch.getSession(username, host, port);
            sshSession = jsch.getSession(username, host, port);
            System.out.println("Session created.");
            sshSession.setPassword(password);
            Properties sshConfig = new Properties();
            sshConfig.put("StrictHostKeyChecking", "no");
            sshSession.setConfig(sshConfig);
            sshSession.connect();
            System.out.println("Session connected.");
            System.out.println("Opening Channel.");
            Channel channel = sshSession.openChannel("sftp");
            channel.connect();
            sftp = (ChannelSftp) channel;
            System.out.println("Connected to " + host + ".");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 关闭资源
     */

    public void disconnect() {

        if (this.sftp != null) {

            if (this.sftp.isConnected()) {

                this.sftp.disconnect();

                System.out.println("sftp is closed already");

            }

        }

        if (this.sshSession != null) {

            if (this.sshSession.isConnected()) {

                this.sshSession.disconnect();

                System.out.println("sshSession is closed already");

            }

        }

    }


    public static void main(String[] args) {

        SFTPTest ftp = new SFTPTest();

        String localPath = "D:\\sftp\\";

        String remotePath = "/home/itvsoap/file_interface/bill/lzj/test/";

        ftp.connect("127.0.0.1", "admin", "admin", null, 22);

        ftp.disconnect();

        System.exit(0);

    }

}