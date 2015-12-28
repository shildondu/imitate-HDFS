package com.shildon.system;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.LinkedList;
import java.util.List;

/**
 * 模拟中心服务器，用来负责管理文件系统的名字空间(namespace)以及客户端对文件的访问，
 * 也负责确定数据块到具体Datanode节点的映射。一般为单例。
 * @author shildon<shildondu@gmail.com>
 * @date Dec 26, 2015 3:18:50 PM
 *
 */
public final class NameNode {
	// 饿汉式单例模式
	private static final NameNode NAME_NODE = new NameNode();
	
	// 默认fcb文件存放路径
	private String fcbPath;
	// 数据块的大小，单位是字节,默认是4kb
	private int size;
	private List<DataNode> dataNodes;
	// dataNodes的索引
	private int index;
	
	// 私有构造方法
	private NameNode() {
		fcbPath = "./fcb.txt";
		size = 4 * 1024;
		dataNodes = new LinkedList<DataNode>();
		index = 0;
	}
	
	// 工厂方法
	public static NameNode getInstance(String fcbPath, int size,
			List<DataNode> dataNodes) {
		if (null != fcbPath && !"".equals(fcbPath)) {
			NAME_NODE.fcbPath = fcbPath;
		}
		if (0 != size) {
			NAME_NODE.size = size;
		}
		if (null != dataNodes && 0 != dataNodes.size()) {
			NAME_NODE.dataNodes = dataNodes;
		}
		return NAME_NODE;
	}
	
	public InputStream openFile(String namespace, String name) {
		List<FileControllBlock> fcbs = getFcbs();
		List<DataBlocks> dataBlocks = null;
		for (FileControllBlock fcb : fcbs) {
			if (namespace.equals(fcb.getNamespace()) &&
					name.equals(fcb.getName())) {
				dataBlocks = fcb.getDataBlocks();
			}
		}
		
		if (null == dataBlocks) {
			// TODO Auto-generated catch block
		}
		return scatter(dataBlocks);
	}
	
	/**
	 * 收集数据块
	 * @return
	 */
	private InputStream scatter(List<DataBlocks> dataBlocks) {
		ByteArrayInputStream inputStream = null;
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

		for (DataBlocks blocks : dataBlocks) {
			DataBlock dataBlock = blocks.getMainDataBlock();
			DataNode dataNode = findDataNode(dataBlock.getDataNodeId());
			dataNode.find(dataBlock);

			try {
				outputStream.write(dataBlock.getData());
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();

				// 保证容错性，出错时寻找副本
				DataBlock[] replications = blocks.getReplications();
				for (DataBlock replication : replications) {
					DataNode tDataNode = findDataNode(replication.getDataNodeId());
					tDataNode.find(replication);

					try {
						outputStream.write(replication.getData());
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				}
			}
		}
		
		inputStream = new ByteArrayInputStream(outputStream.toByteArray());
		return inputStream;
	}
	
	/**
	 * 找到DataBlock对应的DataNode
	 * @param dataBlock
	 * @return
	 */
	private DataNode findDataNode(String dataNodeId) {
		for (DataNode dataNode : dataNodes) {
			if (dataNode.getDataNodeId().equals(dataNodeId)) {
				return dataNode;
			}
		}
		return null;
	}
	
	/**
	 * 保存文件。将文件划分成大小相同（除了最后一块）的数据块，同时按level创建副本，
	 * 保存到不同的DataNode。
	 * @param namespace 文件的命名空间
	 * @param name 文件名
	 * @param inputStream 文件输入流
	 * @param level 安全系数，即创建副本个数
	 * @return
	 */
	public boolean saveFile(String namespace, String name, 
			InputStream inputStream, int level) {
		FileControllBlock fcb = new FileControllBlock();
		fcb.setNamespace(namespace);
		fcb.setName(name);
		byte[] data = new byte[size];
		try {
			while (-1 != inputStream.read(data)) {
				DataBlocks dataBlocks = allocate(data, level);
				fcb.getDataBlocks().add(dataBlocks);
			}
			saveFcb(fcb);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return true;
	}
	
	/**
	 * 保证fcb写的文件存在。
	 */
	private void ensure() {
		Path path = Paths.get(fcbPath);
		if (!Files.exists(path)) {
			ObjectOutputStream oos = null;
			try {
				Files.createFile(path);
				oos = new ObjectOutputStream(new FileOutputStream(fcbPath));
				oos.writeObject(new LinkedList<FileControllBlock>());
				oos.flush();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} finally {
				try {
					oos.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}
	
	@SuppressWarnings("unchecked")
	private List<FileControllBlock> getFcbs() {
		List<FileControllBlock> fcbs = null;
		try (ObjectInputStream ois = 
				new ObjectInputStream(new FileInputStream(fcbPath))) {
			fcbs = (List<FileControllBlock>) ois.readObject();
		} catch (ClassNotFoundException | IOException e) {
			e.printStackTrace();
		}
		return fcbs;
	}
	
	/**
	 * 保存fcb。
	 * @param fcb
	 */
	private void saveFcb(FileControllBlock fcb) {
		ensure();
		List<FileControllBlock> fcbs = getFcbs();
		fcbs.add(fcb);
		// 读写流必须要分开，否则会报EOF异常。
		try (ObjectOutputStream oos = 
				new ObjectOutputStream(new FileOutputStream(fcbPath))) {
			oos.writeObject(fcbs);
			oos.flush();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/**
	 * 获取dataNode的下标，相当于对5取模。
	 * @return
	 */
	private int getIndex() {
		int tindex = index % dataNodes.size();
		index++;
		return tindex;
	}
	
	/**
	 * 分配算法，将数据块分配到不同的DataNode，默认是循环分配。
	 * @param data
	 * @param level
	 * @return
	 */
	private DataBlocks allocate(byte[] data, int level) {
		// 处理数据块
		DataBlock dataBlock = new DataBlock();
		dataBlock.setData(data);
		DataNode dataNode = dataNodes.get(getIndex());
		dataBlock.setDataNodeId(dataNode.getDataNodeId());
		dataNode.save(dataBlock);
		// 处理副本
		DataBlock[] replications = new DataBlock[level];
		for (int i = 0; i < level; i++) {
			DataBlock replication = new DataBlock();
			DataNode dn = dataNodes.get(getIndex());
			replication.setData(data);
			replication.setDataNodeId(dn.getDataNodeId());
			dn.save(replication);
			replications[i] = replication;
		}
		
		DataBlocks dataBlocks = new DataBlocks();
		dataBlocks.setMainDataBlock(dataBlock);
		dataBlocks.setReplications(replications);
		return dataBlocks;
	}

}
