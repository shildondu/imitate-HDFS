package com.shildon.system;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * 模拟集群中的单个服务器。一个节点一个，负责管理它所在节点上的存储。
 * @author shildon<shildondu@gmail.com>
 * @date Dec 26, 2015 3:20:21 PM
 *
 */
public final class DataNode {
	// 标志，这里指文件名
	private String dataNodeId;
	// 指向还未写入的行
	private long position;
	
	public DataNode(String dataNodeId) {
		this.dataNodeId = dataNodeId;
		position = 0;
	}
	
	/**
	 * 存储数据块。
	 * @param dataBlock
	 * @return
	 */
	public long save(DataBlock dataBlock) {
		ensure();
		try (RandomAccessFile raf = new RandomAccessFile(dataNodeId, "rw")) {
			raf.seek(position);
			raf.write(dataBlock.getData());
			position = raf.getFilePointer();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return position;
	}
	
	/**
	 * 保证文件存在。
	 */
	private void ensure() {
		Path path = Paths.get(dataNodeId);
		if (!Files.exists(path)) {
			try {
				Files.createFile(path);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

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

}
