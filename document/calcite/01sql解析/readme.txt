一、该文件夹主要解析的是calcite代码中 parse.jj语法 转换成 java对象的过程。

1.parse.jj为calcite源代码文件，目前只剩下窗口函数尚未翻译完成。
2.000关键词，为parse.jj中静态关键词变量内容。
3.01parsejj函数解析，将parse.jj中核心的段落整理。看该文档就可以简化看parse.jj
4.02sqlNode拆解，是描述了calcite设计理念，解析后的任何对象都转换成sqlNode。那sqlNode有哪些子类，每一个子类都是做什么的，在该文件中做补充描述。
5.03解析表达式，针对parse.jj中表达式部分内容比较晦涩难懂，专门提炼出来。
6.04Operator，专门针对parse.jj中操作部分进行提炼。

