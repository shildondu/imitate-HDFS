package com.shildon.system;

import java.io.Serializable;

/**
 * 一个文件被分解的数据块。
 * @author shildon<shildondu@gmail.com>
 * @date Dec 26, 2015 3:41:19 PM
 *
 */
public final class DataBlock implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	// 位于哪个DataNode
	private String dataNodeId;
	// 位于哪个位置
	private long position;
	// 长度
	private long length;
	// 具体数据
	private transient byte[] data;

	public String getDataNodeId() {
		return dataNodeId;
	}
	public void setDataNodeId(String dataNodeId) {
		this.dataNodeId = dataNodeId;
	}

	public long getPosition() {
		return position;
	}
	public void setPosition(long position) {
		this.position = position;
	}

	public byte[] getData() {
		return data;
	}
	public void setData(byte[] data) {
		this.data = data;
	}

	public long getLength() {
		return length;
	}
	public void setLength(long length) {
		this.length = length;
	}

}
