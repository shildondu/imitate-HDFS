package com.shildon.system;

import java.io.Serializable;

/**
 * 数据块集合，包含主数据块和副本数据块。
 * @author shildon<shildondu@gmail.com>
 * @date Dec 26, 2015 3:50:38 PM
 *
 */
public final class DataBlocks implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	// 主数据块
	private DataBlock mainDataBlock;
	// 副本数据块
	private DataBlock[] replications;

	/* ==================== getter and setter ==================== */
	public DataBlock getMainDataBlock() {
		return mainDataBlock;
	}
	public void setMainDataBlock(DataBlock mainDataBlock) {
		this.mainDataBlock = mainDataBlock;
	}

	public DataBlock[] getReplications() {
		return replications;
	}
	public void setReplications(DataBlock[] replications) {
		this.replications = replications;
	}

}
