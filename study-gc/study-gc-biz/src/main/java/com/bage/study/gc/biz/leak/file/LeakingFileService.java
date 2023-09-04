package com.bage.study.gc.biz.leak.file;

import java.io.*;

public class LeakingFileService {

    public String leak(String fileName, boolean close) {
        StringBuilder sb = new StringBuilder();

        InputStream is = null;
        InputStreamReader ir = null;
        BufferedReader br = null;
        try {
            is = new FileInputStream(fileName);
            ir = new InputStreamReader(is);
            br = new BufferedReader(ir);
            String strTmp = "";
            while ((strTmp = br.readLine()) != null) {
                sb.append(strTmp).append("\n");
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (close) {
                if (br != null) {
                    try {
                        br.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                if (ir != null) {
                    try {
                        ir.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                if (is != null) {
                    try {
                        is.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        return sb.toString();
    }

}