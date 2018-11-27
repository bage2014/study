package com.bage.study.java.serialice;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.io.Serializable;

import com.bage.study.java.utils.FileUtils;

/**
 * Serializable Java序列化<br>
 * transient 修饰的变量仅存在于内存中，不会进行序列化(使用于比如密码等敏感信息)，
 * 只能修饰变量，而不能修饰方法和类<br>
 * 静态变量不管是否被transient修饰，均不能被序列化。<br>
 * Serializable序列化时不会调用默认的构造器，而Externalizable序列化时会调用默认构造器的,
 * 可以自定义保存内容
 * @author bage
 *
 */
public class MySerializable {

	public static void main(String[] args) {
		
		
		String path = "myobj.txt";
		System.out.println("--------继承Serializable------------");
		Object obj= new MyObj();
		((MyObj)obj).setPrivateF(100);
		System.out.println(obj);

		// 写入文件
		FileUtils.writeToFile(obj, path );
		// 从文件读取
		obj= FileUtils.readObjectFromFile(path );
		
		System.out.println(obj);

		System.out.println("--------继承externalizable------------");
		obj= new MyObj2(125);
		((MyObj2)obj).setPrivateF(100);
		System.out.println(obj);

		// 写入文件
		FileUtils.writeToFile(obj, path );
		// 从文件读取
		obj= FileUtils.readObjectFromFile(path );
		
		System.out.println(obj);

		
	}
	
}
/**
 * 序列化的Java类<br>
 * @author bage
 *
 */
class MyObj implements Serializable{
	
	private static final long serialVersionUID = 1L;
	
	private /*transient*/ int privateF = 1;
	// transient 
	protected int protectedF = 2;
	public static int publicF = 3;
	public static final int publicSF = 4;
	public final int publicFP = 5;
	
	public int getPrivateF() {
		return privateF;
	}
	public void setPrivateF(int privateF) {
		this.privateF = privateF;
	}
	public int getProtectedF() {
		return protectedF;
	}
	public void setProtectedF(int protectedF) {
		this.protectedF = protectedF;
	}
	public int getPublicFP() {
		return publicFP;
	}
	@Override
	public String toString() {
		return "MyObj [privateF=" + privateF + ", protectedF=" + protectedF + ", publicFP=" + publicFP + "]";
	}
	
}

/**
 * 继承Externalizable
 * @author bage
 *
 */
class MyObj2 implements Externalizable{
	
	private transient int privateF = 1;
	private int privateF2 = 2;

	public int getPrivateF2() {
		return privateF2;
	}

	public void setPrivateF2(int privateF2) {
		this.privateF2 = privateF2;
	}

	public MyObj2(int i){
		privateF2 = i;
	}
	
	public MyObj2(){
	}
	
	public int getPrivateF() {
		return privateF;
	}
	public void setPrivateF(int privateF) {
		this.privateF = privateF;
	}
	
	@Override
	public String toString() {
		return "MyObj2 [privateF=" + privateF + ", privateF2=" + privateF2 + "]";
	}

	public void writeExternal(ObjectOutput out) throws IOException {
		out.writeInt(privateF);
		out.writeInt(privateF2);


	}
	public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
		privateF2 = in.readInt();
		privateF = in.readInt();

	}
	
}
