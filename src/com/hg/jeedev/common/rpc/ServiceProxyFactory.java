package com.hg.jeedev.common.rpc;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

import com.hg.jeedev.common.rpc.common.RpcRequest;
import com.hg.jeedev.common.rpc.common.RpcResponse;
import com.hg.jeedev.common.rpc.zookeeper.ZkClient;


/**
 * 客户端服务类代理工厂
 * 
 * 1.由getProxy方法根据接口使用JDK动态代理生成代理对象
 * 2.当代理对象的方法被调用时，会被invoke方法拦截
 * 		1）封装参数
 * 		2）创建RpcClientHandler，链接服务端，发送消息
 * 		3）检查是否有异常，返回结果
 * 
 * @author QJM
 *
 */
public class ServiceProxyFactory implements InvocationHandler{
	//获取zookeeper中注册服务器
	private ZkClient zkClient;
	//服务器地址
	private String monitorServer;
	private String path;
	
	public ServiceProxyFactory(ZkClient zkClient){
		System.out.println(123);
		this.zkClient = zkClient;
	}
	
	public ServiceProxyFactory(String monitorServer){
		this.monitorServer = monitorServer;
	}
	
	/**
	 * 生成代理对象
	 * @param clazz 代理类型（接口）
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public <T>T getProxy(Class<T> clazz,String path){
		this.path=path;
		// clazz不是接口不能使用JDK动态代理
		return (T) Proxy.newProxyInstance(clazz.getClassLoader(), new Class<?>[]{clazz}, ServiceProxyFactory.this);
	}

	public Object invoke(Object obj, Method method, Object[] params) throws Throwable {
		//封装参数
		RpcRequest request = new RpcRequest();
		request.setMethodName(method.getName());
		request.setParameterTypes(method.getParameterTypes());
		request.setClassName(method.getDeclaringClass().getName());
		request.setParameters(params);
		
		//获取服务器地址
		String server = zkClient.getZk(path);
		if(server == null){
			throw new RuntimeException("找不到服务器");
		}
		String[] sv = server.split(":");
		RpcClientHandler client = new RpcClientHandler(sv[0], Integer.parseInt(sv[1]));
		//获取发送请求
		RpcResponse response = client.send(request);
		if(response.getError() != null){
			throw response.getError();
		}
		return response.getResult();
	}
	
}
