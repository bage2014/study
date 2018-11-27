package com.bage.study.java.pattern.prototype;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

/**
 * 原型
 * @author bage
 *
 */
public class Prototype implements Cloneable,Serializable{

	private static final long serialVersionUID = 1L;
	
	private int id;
	private final String name; 
	private /*final*/ ObjectAttr attr; // 不能有final对象属性
	
	public Prototype(int id, String name, ObjectAttr attr) {
		super();
		this.id = id;
		this.name = name;
		this.attr = attr;
	}

	public ObjectAttr getAttr() {
		return attr;
	}
	public void setAttr(ObjectAttr attr) {
		this.attr = attr;
	}
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
//	public void setName(String name) {
//		this.name = name;
//	}
	
	@Override
	public String toString() {
		return "Prototype [id=" + id + ", name=" + name + ", attr=" + attr + "]";
	}
	
	/**
	 * 实现 Cloneable 接口，重写clone 实现原型模式
	 */
	@Override
	public Prototype clone(){
		// 深克隆需要把对象属性也要进行克隆、赋值
		Prototype p = null;
		try {
			p = (Prototype) super.clone();
			if(attr != null){
				p.attr = attr.clone();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return p;
	}
	
	/**
	 * 序列化反序列化实现对象的克隆
	 * @return
	 */
	public Prototype serializable(){
		Prototype obj = null;
		
		ByteArrayInputStream baips = null;
		ObjectInputStream oips = null;
		ByteArrayOutputStream baops = null;
		ObjectOutputStream oops = null;
		try {
			baops = new ByteArrayOutputStream();
			oops = new ObjectOutputStream(baops);
			oops.writeObject(this);
			oops.flush();
			
			byte[] bytes = baops.toByteArray();
			baips = new ByteArrayInputStream(bytes);
			oips = new ObjectInputStream(baips);
			obj = (Prototype) oips.readObject();
			
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if(oops != null){
					oops.close();
				}
				if(baops != null){
					baops.close();
				}
				if(baips != null){
					baips.close();
				}
				if(oips != null){
					oips.close();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return obj;
	}
}
