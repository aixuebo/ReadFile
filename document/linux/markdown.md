https://github.com/aixuebo/docsify-notes/blob/mm/markdown.md

# 一、常用功能
# title
> 内容描述 /docs/study/BigData/Flink
> 
> 内容描述

# 1.粗体标题
+ 1
  + 子目录 （子目录要用tab键）
  + 子目录

## 1.1 子标题

# 2.特殊文本
## 2.1 超链接
+ [超链接文字内容](https://www.baidu.com)

## 2.2 引用
+ 简单的引用 ```  ```
+ 特殊引用--可以根据格式,显示特殊的样式。
  + xml
  ```xml
   <?xml version="1.0" encoding="UTF-8"?>
  ```
  + java
  ```java
   import org.apache.flink.api.common.functions.FlatMapFunction;
  ```
  + 引用套用引用--注意 shell可以有回车样式
  ```java
   import org.apache.flink.api.common.functions.FlatMapFunction;
   ```shell
   输出:
    a b 
    c d
   ```
  ```
## 2.3 回车
  在一行的结束后,打2个空格，就可以起到回车的作用。  
  测试回车了吗？
## 2.4 图片
 ![img](https://img-blog.csdnimg.cn/20191124113558631.png)
## 2.5 分界线
 ---
 
# 二、其他标签
## 1.折叠用于笔记中，已经掌握的内容。
<details>
  <summary>折叠的内容标题</summary>
  折叠的文本内容，可以包含任意的 Markdown 格式的文本、列表、代码块等。
</details>
