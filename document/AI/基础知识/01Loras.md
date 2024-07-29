# 一、Loras --- 图1
## 1.目的
* Loras是应用在main model和CLIP main上层的补丁。
* 也可以用多个Loras，串联多个Loras，对图片进行处理。

## 2.使用方式
放置在models/loras目录下。然后使用LoraLoader加载该模型即可。

## 3.应用
正向关键词
* masterpiece best quality 杰作、高质量作品
* gril
反向关键词：
* bad hands 不利的因素