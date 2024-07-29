# 一、LCM model
https://latent-consistency-models.github.io/
https://github.com/luosiallen/latent-consistency-model
## 1.原理与目的 -- 可以有效的缩短生成图片的时间
LCM model（Latent Class Model）即潜在类别模型，是一种统计模型，用于识别并解释观测数据中的潜在结构。LCM属于混合模型（mixture models）的一种，它假设数据是由多个不同的潜在类别（latent classes）生成的，每个潜在类别对应一组特定的参数，这些参数决定了该类别下数据的分布。
LCM模型的目标是基于观测数据推断出潜在类别的数量、每个类别的特征以及每个实例属于各个潜在类别的概率。这种模型通常使用极大似然估计或贝叶斯方法来估计模型参数，而EM算法（期望最大化算法）是求解这类问题的常用方法之一。
LCM模型在处理分类问题时提供了一种考虑潜在异质性的方法。

如何使用LCM model呢。需要用LCM Lora进行一个转换。
LCM models是一种特殊的模型，目标是减少抽样的步骤，找到相似的内容。
LCM Lora 用于将常规模型转换成LCM模型的Lora。

## 2.使用方式
下载LCM SDXL lora模型，然后改名字"lcm_lora_sdxl.safetensors"；然后放到models/loras文件夹下。
https://huggingface.co/latent-consistency/lcm-lora-sdxl/blob/main/pytorch_lora_weights.safetensors