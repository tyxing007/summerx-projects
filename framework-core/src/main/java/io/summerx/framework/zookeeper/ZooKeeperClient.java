package io.summerx.framework.zookeeper;

import java.util.List;

public interface ZooKeeperClient {

	/**
	 * 设置znode的值
	 * @param path
	 * @param value
	 * @throws Exception
     */
	void put(String path, String value) throws Exception;

	/**
	 * 设置znode的值
	 * @param path
	 * @param value
	 * @param ephemeral
	 * @throws Exception
     */
	void put(String path, String value, boolean ephemeral) throws Exception;

	/**
	 * 设置znode的值
	 * @param path
	 * @param value
	 * @param ephemeral
	 * @param sequential
     * @throws Exception
     */
	void put(String path, String value, boolean ephemeral, boolean sequential) throws Exception;

	/**
	 * 获取znode的值
	 * @param path
	 * @return
	 * @throws Exception
     */
	String get(String path) throws Exception;

	/**
	 * 删除znode及其子节点
	 * @param path
	 * @throws Exception
     */
	void remove(String path) throws Exception;

	/**
	 * 获取znode的所有子节点
	 * @param path
	 * @return
	 * @throws Exception
     */
	List<String> getChildren(String path) throws Exception;
}
