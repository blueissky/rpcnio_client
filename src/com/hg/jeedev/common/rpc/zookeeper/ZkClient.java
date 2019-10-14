package com.hg.jeedev.common.rpc.zookeeper;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.Watcher.Event.EventType;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.data.Stat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * 获取zookeeper中注册服务器
 * @author QJM
 *
 */
public class ZkClient {
	//日志
	private static final Logger LOGGER = LoggerFactory.getLogger(ZkClient.class);
	
	//链接超时时间
	private int timeout;
	//zookeeper服务器(如：weekend01:2181，多个间用逗号隔开)
	private String zkServeres;
	//zookeeper实例
	private ZooKeeper zk;
	//zookeeper监听器
	private Watcher watcher;
	
	public ZkClient(String zkServeres, int timeout){
		watcher = new Watcher(){
			public void process(WatchedEvent event) {}
		};
		try {
			zk = new ZooKeeper(zkServeres, timeout, watcher);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 获取zookeeper
	 * @return
	 */
	public String getZk(String path){
		try {
			//查看服务器根节点是否存在
			Stat stat = zk.exists(path, watcher);
			if(stat == null){
				throw new RuntimeException("服务器未启动！");
			}else{
				//获取所有服务器
				byte[] data = zk.getData(path, false, stat);
				return new String(data,"UTF-8"); 
			}
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	/**
	 * 关闭链接
	 * @return
	 */
	public void closeZk(){
		try {
			if(zk != null)
				zk.close();
		} catch (InterruptedException e) {
			throw new RuntimeException(e);
		}
	}
}
