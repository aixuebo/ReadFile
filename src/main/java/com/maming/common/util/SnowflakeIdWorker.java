package com.maming.common.util;

import org.apache.commons.lang3.RandomUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.SystemUtils;
 
import java.net.Inet4Address;
import java.net.UnknownHostException;
 
/**
 * Twitter_Snowflake<br>
 * SnowFlake的结构如下(每部分用-分开):<br>
 * 0 - 0000000000 0000000000 0000000000 0000000000 0 - 00000 - 00000 - 000000000000 <br>
 * 1位标识，由于long基本类型在Java中是带符号的，最高位是符号位，正数是0，负数是1，所以id一般是正数，最高位是0<br>
 * 41位时间截(毫秒级)，注意，41位时间截不是存储当前时间的时间截，而是存储时间截的差值（当前时间截 - 开始时间截)
 * 得到的值），这里的的开始时间截，一般是我们的id生成器开始使用的时间，由我们程序来指定的（如下下面程序IdWorker类的startTime属性）。41位的时间截，可以使用69年，年T = (1L << 41) / (1000L * 60 * 60 * 24 * 365) = 69<br>
 * 10位的数据机器位，可以部署在1024个节点，包括5位datacenterId和5位workerId<br>
 * 12位序列，毫秒内的计数，12位的计数顺序号支持每个节点每毫秒(同一机器，同一时间截)产生4096个ID序号<br>
 * 加起来刚好64位，为一个Long型。<br>
 * SnowFlake的优点是，整体上按照时间自增排序，并且整个分布式系统内不会产生ID碰撞(由数据中心ID和机器ID作区分)，并且效率较高，经测试，SnowFlake每秒能够产生26万ID左右。
 * 
 * 
 * 
1位，不用。二进制中最高位为1的都是负数，但是我们生成的id一般都使用整数，所以这个最高位固定是0
41位，用来记录时间戳（毫秒）。
		- 41位可以表示 2^{41}-1 个数字，
如果只用来表示正整数（计算机中正数包含0），可以表示的数值范围是：0 至  2^{41}-1，减1是因为可表示的数值范围是从0开始算的，而不是1。
	- 也就是说41位可以表示 2^{41}-1 个毫秒的值，转化成单位年则是 (2^{41}-1) / (1000  60  60  24  365) = 69 年
10位，用来记录工作机器id。
		- 可以部署在 2^{10} = 1024 个节点，包括 5位 datacenterId 和 5位 workerId
		- 5位（bit）可以表示的最大正整数是 2^{5}-1 = 31 ，即可以用 0、1、2、3、....31 这 32 个数字，来表示不同的 datecenterId 或 workerId
	
12位，序列号，用来记录同毫秒内产生的不同id。
		- 12位（bit）可以表示的最大正整数是 2^{12}-1 = 4095 ，即可以用 0、1、2、3、....4094 这 4095 个数字，来表示同一机器同一时间截（毫秒)内产生的 4095 个 ID 序号。
	
由于在 Java 中 64bit 的整数是 long 类型，所以在 Java 中 SnowFlake 算法生成的 id 就是 long 来存储的。

SnowFlake可以保证：

同一台服务器所有生成的id按时间趋势递增
整个分布式系统内不会产生重复id（因为有datacenterId和workerId来做区分）
存在的问题：

机器ID（5位）和数据中心ID（5位）配置没有解决，分布式部署的时候会使用相同的配置，任然有ID重复的风险。
使用的时候需要实例化对象，没有形成开箱即用的工具类。
强依赖机器时钟，如果机器上时钟回拨，会导致发号重复或者服务会处于不可用状态。（这点在正常情况下是不会发生的）
针对上面问题，这里提供一种解决思路，workId 使用服务器 hostName 生成，dataCenterId 使用 IP 生成，这样可以最大限度防止 10 位机器码重复，但是由于两个 ID 都不能超过 32，只能取余数，还是难免产生重复，但是实际使用中，hostName 和 IP 的配置一般连续或相近，只要不是刚好相隔 32 位，就不会有问题，况且，hostName 和 IP 同时相隔 32 的情况更加是几乎不可能的事，平时做的分布式部署，一般也不会超过 10 台容器。

生产上使用docker配置一般是一次编译，然后分布式部署到不同容器，不会有不同的配置。这种情况就对上面提到的出现了不确定情况，这个在评论中会再出一篇参考文章。
 * 
 */
public class SnowflakeIdWorker {
 
    // ==============================Fields===========================================
    /** 开始时间截 (2015-01-01) 这个参数写死*/
    private final long twepoch = 1489111610226L;
 
    /** 机器id所占的位数 */
    private final long workerIdBits = 5L;
 
    /** 数据标识id所占的位数 */
    private final long dataCenterIdBits = 5L;
 
    /** 支持的最大机器id，结果是31 (这个移位算法可以很快的计算出几位二进制数所能表示的最大十进制数) */
    private final long maxWorkerId = -1L ^ (-1L << workerIdBits);
 
    /** 支持的最大数据标识id，结果是31 */
    private final long maxDataCenterId = -1L ^ (-1L << dataCenterIdBits);
 
    /** 序列在id中占的位数 */
    private final long sequenceBits = 12L;
 
    /** 机器ID向左移12位 */
    private final long workerIdShift = sequenceBits;
 
    /** 数据标识id向左移17位(12+5) */
    private final long dataCenterIdShift = sequenceBits + workerIdBits;
 
    /** 时间截向左移22位(5+5+12) */
    private final long timestampLeftShift = sequenceBits + workerIdBits + dataCenterIdBits;
 
    /** 生成序列的掩码，这里为4095 (0b111111111111=0xfff=4095) */
    private final long sequenceMask = -1L ^ (-1L << sequenceBits);
 
    /** 工作机器ID(0~31) */
    private long workerId;
 
    /** 数据中心ID(0~31) */
    private long dataCenterId;
 
    /** 毫秒内序列(0~4095) */
    private long sequence = 0L;
 
    /** 上次生成ID的时间截 */
    private long lastTimestamp = -1L;
 
    private static SnowflakeIdWorker idWorker;
 
    static {
        idWorker = new SnowflakeIdWorker(getWorkId(),getDataCenterId());
    }
 
    //==============================Constructors=====================================
    /**
     * 构造函数
     * @param workerId 工作ID (0~31)
     * @param dataCenterId 数据中心ID (0~31)
     */
    public SnowflakeIdWorker(long workerId, long dataCenterId) {
        if (workerId > maxWorkerId || workerId < 0) {
            throw new IllegalArgumentException(String.format("workerId can't be greater than %d or less than 0", maxWorkerId));
        }
        if (dataCenterId > maxDataCenterId || dataCenterId < 0) {
            throw new IllegalArgumentException(String.format("dataCenterId can't be greater than %d or less than 0", maxDataCenterId));
        }
        this.workerId = workerId;
        this.dataCenterId = dataCenterId;
    }
 
    // ==============================Methods==========================================
    /**
     * 获得下一个ID (该方法是线程安全的)
     * @return SnowflakeId
     */
    public synchronized long nextId() {
        long timestamp = timeGen();
 
        //如果当前时间小于上一次ID生成的时间戳，说明系统时钟回退过这个时候应当抛出异常
        if (timestamp < lastTimestamp) {
            throw new RuntimeException(
                    String.format("Clock moved backwards.  Refusing to generate id for %d milliseconds", lastTimestamp - timestamp));
        }
 
        //如果是同一时间生成的，则进行毫秒内序列
        if (lastTimestamp == timestamp) {
            sequence = (sequence + 1) & sequenceMask;
            //毫秒内序列溢出
            if (sequence == 0) {
                //阻塞到下一个毫秒,获得新的时间戳
                timestamp = tilNextMillis(lastTimestamp);
            }
        }
        //时间戳改变，毫秒内序列重置
        else {
            sequence = 0L;
        }
 
        //上次生成ID的时间截
        lastTimestamp = timestamp;
 
        //移位并通过或运算拼到一起组成64位的ID
        return ((timestamp - twepoch) << timestampLeftShift)
                | (dataCenterId << dataCenterIdShift)
                | (workerId << workerIdShift)
                | sequence;
    }
 
    /**
     * 阻塞到下一个毫秒，直到获得新的时间戳
     * @param lastTimestamp 上次生成ID的时间截
     * @return 当前时间戳
     */
    protected long tilNextMillis(long lastTimestamp) {
        long timestamp = timeGen();
        while (timestamp <= lastTimestamp) {
            timestamp = timeGen();
        }
        return timestamp;
    }
 
    /**
     * 返回以毫秒为单位的当前时间
     * @return 当前时间(毫秒)
     */
    protected long timeGen() {
        return System.currentTimeMillis();
    }
 
    private static Long getWorkId(){
        try {
            //String hostAddress = Inet4Address.getLocalHost().getHostAddress();
            //int[] ints = StringUtils.toCodePoints(hostAddress);
            int[] ints = new int[20];
            int sums = 0;
            for(int b : ints){
                sums += b;
            }
            return (long)(sums % 32);
        } catch (Exception e) {
            // 如果获取失败，则使用随机数备用
            return RandomUtils.nextLong(0,31);
        }
    }
 
    private static Long getDataCenterId(){
        //int[] ints = StringUtils.toCodePoints(SystemUtils.getHostName());
        int[] ints = new int[20];
        int sums = 0;
        for (int i: ints) {
            sums += i;
        }
        return (long)(sums % 32);
    }
 
 
    /**
     * 静态工具类
     *
     * @return
     */
    public static synchronized Long generateId(){
        long id = idWorker.nextId();
        return id;
    }
 
    //==============================Test=============================================
    /** 测试 */
    public static void main(String[] args) {
        System.out.println(System.currentTimeMillis());
        long startTime = System.nanoTime();
        for (int i = 0; i < 50000; i++) {
            long id = SnowflakeIdWorker.generateId();
            System.out.println(id);
        }
        System.out.println((System.nanoTime()-startTime)/1000000+"ms");
    }
}