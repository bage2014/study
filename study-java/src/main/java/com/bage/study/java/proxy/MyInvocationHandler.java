package com.bage.study.java.proxy;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

public class MyInvocationHandler implements InvocationHandler{

	  //　这个就是我们要代理的真实对象
    private Object proxy;
    
    //    构造方法，给我们要代理的真实对象赋初值
    public MyInvocationHandler(Object proxy)
    {
        this.proxy = proxy;
    }
    
    /**
     proxy - the proxy instance that the method was invoked on
     method - the Method instance corresponding to the interface method invoked on the proxy instance. 
     	The declaring class of the Method object will be the interface that the method was declared in, 
     	which may be a superinterface of the proxy interface that the proxy class inherits the method through.
     args - an array of objects containing the values of the arguments passed in the method invocation on the proxy instance, 
     	or null if interface method takes no arguments. Arguments of primitive types are wrapped in instances of the appropriate primitive wrapper class, 
     	such as java.lang.Integer or java.lang.Boolean.
     */
	@Override
	public Object invoke(Object obj, Method method, Object[] args) throws Throwable {
		//　　在代理真实对象前我们可以添加一些自己的操作
        System.out.println("before invoke");
        System.out.println(obj);
        System.out.println(method);
        System.out.println(args);
        
        //    当代理对象调用真实对象的方法时，其会自动的跳转到代理对象关联的handler对象的invoke方法来进行调用
        method.invoke(proxy, args);
        
        //　　在代理真实对象后我们也可以添加一些自己的操作
        System.out.println("after invoke");
		return null;
	}

}
