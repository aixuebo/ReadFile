package com.maming.common.time;


import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 * 请使用java语言，帮我实现一个时间范围的合并操作，比如19:00-20:00,19:30-20:30,合并结果是19:00-20:30
 */
public class TimeRangeMerger {
	
	/**
	 * 实现原理
这段代码中，首先创建一个TreeMap，然后将所有的时间范围添加到TreeMap中。
由于TreeMap的特性，它会自动按照时间的开始时间进行排序。
然后遍历TreeMap，
如果当前的开始时间大于前一个时间范围的结束时间，说明这两个时间范围没有交集，可以将前一个时间范围添加到结果列表中，然后更新开始和结束时间。
否则，说明有交集，更新结束时间为当前时间范围的结束时间和前一个时间范围的结束时间中较大的一个。
最后，将最后一个时间范围添加到结果列表中。
	 */
    public static void main(String[] args) {
        List<TimeRange> list = new ArrayList<>();
        list.add(new TimeRange("19:00", "20:00"));
        list.add(new TimeRange("19:30", "20:30"));
        System.out.println(merge(list));
    }

    private static List<TimeRange> merge(List<TimeRange> list) {
        Map<String, String> map = new TreeMap<>();
        
        //将数据源按照开始时间排序
        for (TimeRange range : list) {
            map.put(range.start, range.end);
        }
        
        //最终merge后的结果
        List<TimeRange> result = new ArrayList<>();
        
        //记录最新的一段时间范围
        String start = null;
        String end = null;
        
        for (Map.Entry<String, String> entry : map.entrySet()) {
            if (start == null) { //第一条数据,更新start和end
                start = entry.getKey();
                end = entry.getValue();
            } else {//说明不是第一条数据
                if (entry.getKey().compareTo(end) > 0) {//无交集 //因为key一定比start大，所以只需要判断是否交集，即如果key比当前数据段的end还要大，说明没有交集
                    result.add(new TimeRange(start, end));//直接创建一个数据段
                    start = entry.getKey();
                    end = entry.getValue();
                } else {//有交集
                    end = entry.getValue().compareTo(end) > 0 
                    		? entry.getValue() : //当前的结尾时间 比 当前段的结尾还要大，因此更新end = value即可
                    			end;//说明当前的时间段 包含了当前entry的范围,所以不需要更新。
                }
            }
        }
        if (start != null) {
            result.add(new TimeRange(start, end));
        }
        return result;
    }

    static class TimeRange {
        String start;
        String end;

        public TimeRange(String start, String end) {
            this.start = start;
            this.end = end;
        }

        @Override
        public String toString() {
            return start + "-" + end;
        }
    }
}
