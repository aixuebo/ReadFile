package com.maming.common.util;

import java.io.*;
import java.nio.file.*;
import java.nio.charset.StandardCharsets;
import java.util.HashSet;
import java.util.Set;

/**
 * 统计某一个文件夹下，子子孙孙文件中，内容字数
 */
public class ReadFileWordCounter {

        private static long totalCharCount = 0;

        private static Set<String> nonValidSet = new HashSet<String>();//无效可不统计的文件后缀

        public static void init(){
            nonValidSet.add("");
            nonValidSet.add("pdf");
            nonValidSet.add("pptx");
            nonValidSet.add("xlsx");

            //图片
            nonValidSet.add("graffle");
            nonValidSet.add("jpg");
            nonValidSet.add("jpeg");
            nonValidSet.add("heic");
            nonValidSet.add("png");

            //git
            nonValidSet.add("idx");
            nonValidSet.add("pack");
            nonValidSet.add("sample");

        }

        private static void processFolder(File folder) {
            File[] files = folder.listFiles();
            if (files == null) return;

            for (File file : files) {
                if (file.isDirectory()) {
                    // 递归处理子文件夹
                    processFolder(file);
                } else {
                    // 检查文件后缀
                    String fileName = file.getName().toLowerCase();

                    String fileSub = "";
                    if(fileName.indexOf(".") > 0){
                        fileSub = fileName.substring(fileName.indexOf(".")+1);
                    }

                    if (fileName.endsWith(".txt") || fileName.endsWith(".md")) {
                        long charCount = countCharsInFile(file);
                        if (charCount >= 0) {
                            //System.out.println(file.getAbsolutePath() + " -> " + charCount + " 字");
                            totalCharCount += charCount;
                        }

                    } else if(nonValidSet.contains(fileSub)){ // 过滤非统计的文件
                        //System.out.println("不统计");
                    } else {
                        System.out.println(file.getAbsolutePath());
                    }
                }
            }
        }

        private static long countCharsInFile(File file) {
            StringBuilder content = new StringBuilder();
            try (BufferedReader reader = Files.newBufferedReader(file.toPath(), StandardCharsets.UTF_8)) {
                String line;
                while ((line = reader.readLine()) != null) {
                    content.append(line).append(System.lineSeparator());
                }
            } catch (IOException e) {
                System.err.println("读取文件失败: " + file.getAbsolutePath() + " | 错误: " + e.getMessage());
                return -1;
            }
            return content.length(); // 字符数（包括换行符）
        }



    public static void main(String[] args) {
        try {

            init();//初始化

            // 修改为你的目标文件夹路径
            String folderPath = "/Users/Desktop/document/my/xxx";

            File rootFolder = new File(folderPath);

            if (!rootFolder.exists() || !rootFolder.isDirectory()) {
                System.err.println("指定的路径不存在或不是一个文件夹: " + folderPath);
                return;
            }

            System.out.println("开始统计文件夹: " + folderPath);
            processFolder(rootFolder);

            System.out.println("\n✅ 统计完成！");
            System.out.println("总字符数（字数）: " + totalCharCount);

            //临时输出
            for(String str:nonValidSet){
              //  System.out.println(str);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
