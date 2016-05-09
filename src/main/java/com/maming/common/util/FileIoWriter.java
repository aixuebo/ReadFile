package com.maming.common.util;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.charset.Charset;

import org.apache.logging.log4j.Logger;


public class FileIoWriter {

    private static Logger LOG = LogWriter.getOther();

    private BufferedWriter writer = null;

    /**
     * 默认不追加文件内容
     */
    public FileIoWriter(String filePath) {
        this(new File(filePath));
    }

    public FileIoWriter(String filePath, boolean append) {
        this(new File(filePath), append);
    }

    /**
     * 默认不追加文件内容
     */
    public FileIoWriter(File file) {
        this(file, false);
    }

    public FileIoWriter(File file, boolean append) {
        try {
            file.getParentFile().mkdirs();
            //如果追加,则不需要删除该文件
            if (!append && file.exists()) {
                file.delete();
            }
            writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file, append), Charset.forName("UTF-8")), Constants.BUFFER_SIZE);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            LOG.error("create FileIoWriter failed.", e);
        }
    }

    public FileIoWriter write(String value) {
        try {
            writer.write(value);
        } catch (IOException e) {
            LOG.error("write failed" + value, e);
        }
        return this;
    }

    public FileIoWriter writeLine(String value) {
        try {
            writer.write(value + "\n");
        } catch (IOException e) {
            LOG.error("write failed" + value, e);
        }
        return this;
    }

    public FileIoWriter writeLine(Object value) {
        try {
            writer.write(value + "\n");
        } catch (IOException e) {
            LOG.error("write failed" + value, e);
        }
        return this;
    }

    public void close() {
        if (this.writer != null) {
            try {
                this.writer.flush();
                this.writer.close();
            } catch (IOException e) {
                LOG.error("close failed.", e);
            }
        }
    }

    public static void main(String[] args) {
        new FileIoWriter("e://aa//bb/cc", true).writeLine("ssss").close();
        new FileIoWriter("e://aa//bb/cc", true).writeLine("aaaa").close();
    }
}
