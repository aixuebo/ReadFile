package com.maming.common.util;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.util.Set;
import java.util.TreeSet;
import java.util.concurrent.ArrayBlockingQueue;

import org.apache.logging.log4j.Logger;

/**
 * wildcardPath格式:
 * d:\\{mm,progrem}
 * d:\\{*}
 * <p/>
 * filename为正则表达式
 * <p/>
 * 例如:^20141009_.*(\\.xlsx|\\.csv)$
 */
public class DirFilter {

    private static Logger LOG = LogWriter.getOther();
    
    private static Queue<String> filterList = new ArrayBlockingQueue<String>(10000);

    /**
     * 查询文件集合，将文件的路径用逗号拆分后,组装成字符串
     */
    public static String findPath(String wildcardPath, String fileNamePattern) {
        StringBuilder sb = new StringBuilder();
        Set<File> fileList = DirFilter.findFile(wildcardPath, fileNamePattern);
        for (File file : fileList) {
            sb.append(file.getPath()).append(",");
        }
        return sb.toString();
    }

    /**
     * 查询文件集合
     *
     * @param wildcardPath
     * @param fileNamePattern
     * @return
     */
    public static Set<File> findFile(String wildcardPath, String fileNamePattern) {

        Set<File> resultList = new TreeSet<File>();

        try {
            filterList.add(wildcardPath);
            /**
             * 寻找目录集合
             */
            List<String> list = new ArrayList<String>();
            parse(list);

            /**
             * 遍历目录集合,在每一个目录下匹配合适文件
             */
            for (String path : list) {

                File[] files = new File(path).listFiles();

                for (File f : files) {
                    if (f.getName().matches(fileNamePattern)) {
                        resultList.add(f);
                    }
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return resultList;
    }

    /**
     * 将匹配的路径添加到集合list参数中
     */
    private static void parse(List<String> list) {
        while (filterList.size() > 0) {
            String wildcardPath = filterList.poll();
            if (wildcardPath.contains("{")) {//路径包含{},则提取--转换--添加到队列
                //{}之间字符串
                //{之前 }之后
                int begin = wildcardPath.indexOf("{");
                int end = wildcardPath.indexOf("}");
                String replace = wildcardPath.substring(begin + 1, end);//找到包裹的字符窜
                if (replace.equals("*")) {
                    LOG.info("parse replace file:{}",wildcardPath.substring(0, begin));
                    File[] files = new File(wildcardPath.substring(0, begin)).listFiles();
                    for (File p : files) {
                        File f = new File(p.getPath() + wildcardPath.substring(end + 1));
                        if (f.isDirectory() && !f.isHidden()) {
                            filterList.add(f.getPath());
                        }
                    }
                } else {
                    for (String p : replace.split(",")) {
                        filterList.add(wildcardPath.substring(0, begin) + p + wildcardPath.substring(end + 1));
                    }
                }
            } else {//不再包含{},则添加到最终集合中
                list.add(wildcardPath);
            }

        }

    }

    public static void main(String[] args) {
        String path = "d:\\{mm1,progrem1}\\";
        String filename = "^aa!.tar.gz$";
        Set<File> list = DirFilter.findFile(path, filename);
        System.out.println(list.size());
        for (File p : list) {
            System.out.println("===" + p.getPath());
        }
      //System.out.println("log.log_coohua_com.20150803.2310.gz".matches("^log(.)?log_coohua_com." + date + ".*.gz$"));
    }
}
