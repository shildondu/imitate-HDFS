package com.shildon.system;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

/**
 * 文件元数据对应的数据结构。
 * @author shildon<shildondu@gmail.com>
 * @date Dec 26, 2015 3:31:26 PM
 *
 */
public final class FileControllBlock implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	// 文件命名空间
	private String namespace;
	// 文件名称
	private String name;
	// 对应的数据块(包含副本)
	private List<DataBlocks> dataBlocks;
	
	public FileControllBlock() {
		namespace = "./";
		dataBlocks = new LinkedList<DataBlocks>();
	}

	/* ==================== getter and setter ==================== */
	public String getNamespace() {
		return namespace;
	}
	public void setNamespace(String namespace) {
		this.namespace = namespace;
	}

	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}

	public List<DataBlocks> getDataBlocks() {
		return dataBlocks;
	}
	public void setDataBlocks(List<DataBlocks> dataBlocks) {
		this.dataBlocks = dataBlocks;
	}

}
