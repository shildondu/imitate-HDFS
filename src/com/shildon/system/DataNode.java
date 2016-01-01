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
	 * 找到数据块的数据，并存储在DataBlock中。
	 * @param dataBlock
	 * @throws IOException 
	 */
	public void find(DataBlock dataBlock) throws IOException {
		RandomAccessFile raf = new RandomAccessFile(dataNodeId, "r");
		byte[] data = new byte[(int) dataBlock.getLength()];
		raf.seek(dataBlock.getPosition());
		raf.read(data);
		dataBlock.setData(data);
		raf.close();
	}
	
	/**
	 * 存储数据块。
	 * @param dataBlock
	 * @return
	 */
	public void save(DataBlock dataBlock) {
		ensure();
		long oldPosition = position;
		dataBlock.setPosition(position);
		try (RandomAccessFile raf = new RandomAccessFile(dataNodeId, "rw")) {
			raf.seek(position);
			raf.write(dataBlock.getData());
			position = raf.getFilePointer();
			dataBlock.setLength(position - oldPosition);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
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

	/* ==================== getter and setter ==================== */
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
