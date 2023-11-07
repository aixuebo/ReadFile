# 背景与总结
1.sum(case when then) 与 case when then sum()公式结果为什么不相同
计算预测值的时候，需要根据cube的字段维度组合，使用不同的公式计算。此时发现聚合结果有问题。
比如:
sum(case
	when type_id is not null and city_id is not null then pre_value1
	when type_id is not null and city_id is null then pre_value2
	when type_id is null and city_id is not null then pre_value3
	when type_id is null and city_id is null then pre_value4
	end) as pre_value
与
case
	when type_id is not null and city_id is not null then sum(pre_value1)
	when type_id is not null and city_id is null then sum(pre_value2)
	when type_id is null and city_id is not null then sum(pre_value3)
	when type_id is null and city_id is null then sum(pre_value4)
	end) as pre_value
	
# 案例1 计算正确，因为sum放在when后面了 --- 注意：前提条件，biao已经做了预处理,此时type_id和city_id不会出现null的情况
    select 
        coalesce(type_id, -1) as type_id,
        coalesce(city_id, -1) as city_id,
        case
            when type_id is null and city_id is null then sum(pre_value * 纯订单权重weight1) 
            when type_id is not null and city_id is null then sum(pre_value * 固定城市类型的订单权重weight2) 
            when type_id is null and city_id is not null then sum(pre_value * 固定城市的订单权重weight3) 
            when type_id is not null and city_id is not null then sum(pre_value * 固定城市 & 固定城市类型的订单权重weight4) 
        end as pre_value
    from
    (
        select 
            coalesce(type_id, 0) as type_id,
            coalesce(city_id, 0) as city_id,
            pre_value,
            -- 各个维度组合对应的权重
            weight / sum(weight) over() as weight1,
            weight / sum(weight) over(partition by type_id) as weight2,
            weight / sum(weight) over(partition by city_id) as weight3,
            weight / sum(weight) over(partition by type_id, city_id) as weight4
        from
        (
            select 
                type_id,
                city_id,
                pre_value,## 预测值
                weight ## 订单权重
            from biao
        )city_exp
    )weight_data
    group by type_id, city_id
    grouping sets(
        (),
        (type_id),
        (city_id),
        (type_id,city_id)
    )

# 案例2 计算错误，因为sum放在case前面了
    select 
        coalesce(type_id, -1) as type_id,
        coalesce(city_id, -1) as city_id,
        sum(case
            when type_id is null and city_id is null then pre_value * 纯订单权重weight1
            when type_id is not null and city_id is null then pre_value * 固定城市类型的订单权重weight2
            when type_id is null and city_id is not null then pre_value * 固定城市的订单权重weight3
            when type_id is not null and city_id is not null then pre_value * 固定城市 & 固定城市类型的订单权重weight4
        end) as pre_value
    from ...与上面内容一致

# 原因分析
1.因为biao已经做了预处理,此时type_id和city_id不会出现null的情况，所以from内部grouping sets时，type_id 一定是is not null，city_id也一定是is not not null，所以他们只会走到case 4逻辑里。
所以在sum聚合的话，他只能是走到case 4逻辑里处理。
2.如果不用sum聚合，case when的内容，都是group by的字段，所以语法是可以做判断的，所以会走各种case when的场景，遇到合适场景后，用sum做聚合就显得合理了。
即
case
when type_id is null and city_id is null then sum(pre_value * 纯订单权重weight1) 
end as pre_value

# 基于原因，改造（想让用户时候的时候，忽略sum放在不同地方带来的问题困惑，即预计算的结果在包一层，放在内部）
    select type_id,
        city_id,
        ## 参与case when的逻辑都是group by的内容，所以可以不用聚合函数
        case
            when type_id is not null and city_id is not null then model_pred_1
            when type_id is not null and city_id is null then model_pred_2
            when type_id is null and city_id is not null then model_pred_3
            when type_id is null and city_id is null then model_pred_4
        end as model_pred
    from (
        select
            coalesce(type_id, -1) as type_id,
            coalesce(city_id, -1) as city_id,
            sum(pre_value * weight1) as model_pred_1,###不同可能性的结果，是可以直接sum的
            sum(pre_value * weight2) as model_pred_2,
            sum(pre_value * weight3) as model_pred_3,
            sum(pre_value * weight4) as model_pred_4
        from
        (
            ## 清理cube维度字段null值
            select
	            coalesce(type_id, 0) as type_id,
	            coalesce(city_id, 0) as city_id,
	            pre_value,
	            weight / sum(weight) over() as weight1,
	            weight / sum(weight) over(partition by type_id) as weight2,
	            weight / sum(weight) over(partition by city_id) as weight3,
	            weight / sum(weight) over(partition by type_id, city_id) as weight4
            from 
            (
	            select 
	                type_id,
	                city_id,
	                pre_value,## 预测值
	                weight ## 订单权重
	            from biao
            )
        ) t
	    group by type_id, city_id
	    grouping sets(
	        (),
	        (type_id),
	        (city_id),
	        (type_id,city_id)
    ) temp



