package com.maming.common.javaio;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

public class RandomAccessFileTest {

	String file = "E://linux//mysql.txt";
	String writefile = "E://linux//mysqlwrite.txt";
	
	public void testRead(){
		try{
			RandomAccessFile aFile = new RandomAccessFile(file, "rw");
			FileChannel inChannel = aFile.getChannel();
			ByteBuffer buf = ByteBuffer.allocate(48);
			System.out.println(buf);
			System.out.println(buf.remaining());
			buf.put((byte)68);//先存储一个字节
			System.out.println(buf);
			System.out.println(buf.remaining());
			//该方法会存储后续的47个字节,不会覆盖第一个字节
			int bytesRead = inChannel.read(buf); //从inChannel流中读取内容,存储到buf中,最多存储48个字节,返回具体读取了多少个字节
			System.out.println(bytesRead);
			System.out.println(buf);
			//int bytesWritten = inChannel.write(buf);//将buf的内容写入到inChannel输出流中,返回具体写入了多少个字节
		}catch(Exception ex){
			ex.printStackTrace();
		}
	}
	
	public void testWrite(){
		try{
			if(new File(writefile).exists()){
				new File(writefile).delete();
			}
			new File(writefile).createNewFile();
			
			RandomAccessFile aFile = new RandomAccessFile(writefile, "rw");
			FileChannel inChannel = aFile.getChannel();
			ByteBuffer buf = ByteBuffer.allocate(20);
			buf.put((byte)96);//先存储一个字节	
			System.out.println(buf);
			System.out.println(buf.remaining());
			System.out.println(inChannel.write(buf));//将buf的内容写入到inChannel输出流中,返回具体写入了多少个字节
			
			buf.put((byte)95);//先存储一个字节
			System.out.println(buf);
			System.out.println(buf.remaining());
			buf.rewind();
			
			
			System.out.println(inChannel.write(buf));;//将buf的内容写入到inChannel输出流中,返回具体写入了多少个字节
			inChannel.force(true);
			aFile.close();
			testReadFile();//打印文件内容
		}catch(Exception ex){
			ex.printStackTrace();
		}
	}
	
	public void testReadFile(){
		 StringBuffer sb=new StringBuffer();
	        String tempstr=null;
	        try
	        {
	            File file=new File(writefile);
//	            BufferedReader br=new BufferedReader(new FileReader(file));            
//	            while((tempstr=br.readLine())!=null)
//	                sb.append(tempstr);    
	            //另一种读取方式
	            FileInputStream fis=new FileInputStream(file);
	            BufferedReader br=new BufferedReader(new InputStreamReader(fis));
	            while((tempstr=br.readLine())!=null)
	                sb.append(tempstr);
	        }
	        catch(IOException ex)
	        {
	            System.out.println(ex.getStackTrace());
	        }

	        byte[] bs = sb.toString().getBytes();
	        for(byte b:bs){
	        	System.out.println(b);
	        }
	        System.out.println(bs.length);
	}
	
	public static void main(String[] args) {
		RandomAccessFileTest test = new RandomAccessFileTest();
		test.testWrite();
	}
}
