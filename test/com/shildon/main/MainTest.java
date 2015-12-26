package com.shildon.main;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.LinkedList;
import java.util.List;

import org.junit.Test;

import com.shildon.system.DataNode;
import com.shildon.system.NameNode;

public class MainTest {
	
	@Test
	public void testSave() {
		DataNode dataNode0 = new DataNode("/home/shildon/Downloads/node0.txt");
		DataNode dataNode1 = new DataNode("/home/shildon/Downloads/node1.txt");
		DataNode dataNode2 = new DataNode("/home/shildon/Downloads/node2.txt");
		List<DataNode> dataNodes = new LinkedList<DataNode>();
		dataNodes.add(dataNode0);
		dataNodes.add(dataNode1);
		dataNodes.add(dataNode2);
		
		NameNode nameNode = NameNode.getInstance("/home/shildon/Downloads/fcbs.txt",
				128, dataNodes);
		try {
			FileInputStream fis = new FileInputStream("/home/shildon/Downloads/test.txt");
			nameNode.saveFile("/home/shildon/Downloads/", "test.txt", fis, 3);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}
	
}
