package com.shildon.main;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.LinkedList;
import java.util.List;

import org.junit.Test;

import com.shildon.system.DataNode;
import com.shildon.system.NameNode;

public class MainTest {
	
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
	
	@Test
	@SuppressWarnings("resource")
	public void testOpen() {
		DataNode dataNode0 = new DataNode("/home/shildon/Downloads/node0.txt");
		DataNode dataNode1 = new DataNode("/home/shildon/Downloads/node1.txt");
		DataNode dataNode2 = new DataNode("/home/shildon/Downloads/node2.txt");
		List<DataNode> dataNodes = new LinkedList<DataNode>();
		dataNodes.add(dataNode0);
		dataNodes.add(dataNode1);
		dataNodes.add(dataNode2);
		
		NameNode nameNode = NameNode.getInstance("/home/shildon/Downloads/fcbs.txt",
				128, dataNodes);

		InputStream inputStream = nameNode.openFile("/home/shildon/Downloads/", "test.txt");
		byte[] data = new byte[128];
		try {
			FileOutputStream fos = new FileOutputStream("/home/shildon/Downloads/new.txt");
			while (-1 != inputStream.read(data)) {
				fos.write(data);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
}
