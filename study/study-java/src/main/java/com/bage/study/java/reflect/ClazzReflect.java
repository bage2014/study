package com.bage.study.java.reflect;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.Map;

/**
 * 对class、反射进行解析<br>
 * 通用框架
 * 
 * 
 * @author bage
 *
 */
@SuppressWarnings("unused")
public class ClazzReflect {

	private int privateF = 1;
	protected int protectedF = 2;
	public static int publicF = 3;
	@SuppressWarnings("rawtypes")
	public static Map map = new HashMap();

	
	private ClazzReflect(){ // private的无参数构造方法也可以 cls.newInstance()
		
	}
	public ClazzReflect(int i){ // private的无参数构造方法也可以 cls.newInstance()
		
	}
	
	public static void main(String[] args) throws Exception {

		// 获取Class对象
		Class<?> cls = Class.forName("com.bage.study.java.reflect.ClazzReflect");
		System.out.println(cls);
		System.out.println(new ClazzReflect().getClass());
		System.out.println(ClazzReflect.class);
		
		// 判断类型
		System.out.println(cls.isInstance(new ClazzReflect()));
		System.out.println(cls.newInstance() instanceof ClazzReflect);

		// 校验是否是同一个对象(简单通过hasCode，不能保证一定正确)
//		System.out.println(cls.hashCode());
//		System.out.println(new ClazzReflect().getClass().hashCode());
//		System.out.println(ClazzReflect.class.hashCode());
		
		// 获取包信息
		System.out.println(ClazzReflect.class.getPackage().getName());

		// 获取方法
		Method[] ms = cls.getMethods(); // 所有public方法、包括父类
		System.out.println("----- 方法 (所有public方法、包括父类)------------");
		printArrays(ms,null);
		System.out.println("----- 方法 (本类声明的方法)------------");
		ms = cls.getDeclaredMethods();
		printArrays(ms,cls.newInstance());

		// 获取字段
		Field[] fs = cls.getFields();
		System.out.println("----- 字段 ------------");
		printArrays(fs,null);
		System.out.println("----- 全部字段 ------------");
		fs = cls.getDeclaredFields();
		printArrays(fs,cls.newInstance());

		// 构造方法
		Constructor<?>[] cs = cls.getConstructors();
		System.out.println("----- 所有public构造方法(包括超类) ------------");
		printArrays(cs,cls.newInstance());
		cs = cls.getDeclaredConstructors();
		System.out.println("----- 全部本类声明的构造方法 ------------");
		printArrays(cs,cls.newInstance());
		
		// 获取Modifiers[即修饰符](也可以从方法、字段信息中获取) // 类型详见Modifier.XXX 
		System.out.println(Modifier.PUBLIC == cls.getModifiers());
		System.out.println(Modifier.toString(H.class.getModifiers()));

		
		
	}

	private static void printArrays(Object[] ms, Object obj) {
		for (int i = 0; i < ms.length; i++) {
			System.out.println(ms[i]);
			if(obj != null){
				if(ms[i] instanceof Method){
					Method m = (Method) ms[i];
					try {
//						if(!m.isAccessible()){
//							m.setAccessible(true); // 强行调用此方法
//						}
						m.invoke(obj);
						System.out.print("--------------\n");
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
				if(ms[i] instanceof Field){
					Field m = (Field) ms[i];
					try {
						Object f = m.get(obj);
						System.out.print("-------"+f+" -------\n");
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
				if(ms[i] instanceof Constructor<?>){
					Constructor<?> m = (Constructor<?>) ms[i];
					try {
						Object o = m.newInstance();
						System.out.print("-------"+o+" -------\n");
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}

		}
	}

	private void privateM() {
		System.out.println("private void privateM() is work");
	}

	public void publicM() {
		System.out.println("public void publicM() is work");
	}

	protected void protectedM() {
		System.out.println("protected void protectedM() is work");
	}

	private static void privateMS() {
		System.out.println("private static void privateMS() is work");
	}

	public static void publicMS() {
		System.out.println("public static void publicMS() is work");
	}

	protected static void protectedMS() {
		System.out.println("protected static void protectedMS() is work");
	}

	private static class H{
		
	}
}

