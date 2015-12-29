package com.shildon.view;

import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.LinkedList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

import com.shildon.system.DataNode;
import com.shildon.system.NameNode;

public class MainView extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private NameNode nameNode;
	
	private JLabel saveLabel;
	private JLabel openLabel;
	private JLabel downloadLabel;
	private JTextField saveText;
	private JTextField openText;
	private JTextField downloadText;
	private JButton saveFileButton;
	private JButton saveButton;
	private JButton openButton;
	private JButton downloadButton;
	
	private JFileChooser fileChooser;
	
	public MainView() {
		defaultSetting();
		init();
	}
	
	private void defaultSetting() {
		int screenx = (int) Toolkit.getDefaultToolkit().getScreenSize().getWidth();
		int screeny = (int) Toolkit.getDefaultToolkit().getScreenSize().getHeight();
		
		int x = 600;
		int y = 300;
		setTitle("模拟Hadoop FileSystem");
		setSize(x, y);
		// 居中
		setLocation(screenx / 2 - x / 2, screeny / 2 - y / 2);
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
	}
		
	private void init() {
		DataNode dataNode0 = new DataNode("/home/shildon/Downloads/node0.txt");
		DataNode dataNode1 = new DataNode("/home/shildon/Downloads/node1.txt");
		DataNode dataNode2 = new DataNode("/home/shildon/Downloads/node2.txt");
		List<DataNode> dataNodes = new LinkedList<DataNode>();
		dataNodes.add(dataNode0);
		dataNodes.add(dataNode1);
		dataNodes.add(dataNode2);

		nameNode = NameNode.getInstance("/home/shildon/Downloads/fcbs.txt",
				128, dataNodes);

		saveLabel = new JLabel("save file: ");
		openLabel = new JLabel("open file: ");
		downloadLabel = new JLabel("download directory: ");
		saveText = new JTextField();
		openText = new JTextField();
		downloadText = new JTextField();
		saveFileButton = new JButton("select");
		saveButton = new JButton("save");
		openButton = new JButton("select");
		downloadButton = new JButton("download");
		
		setLayout(null);
		add(saveLabel);
		add(saveText);
		add(saveFileButton);
		add(saveButton);
		add(openLabel);
		add(openText);
		add(openButton);
		add(downloadLabel);
		add(downloadText);
		add(downloadButton);
		
		int x = 30, y = 70, height = 25;
		saveLabel.setBounds(x, y, 80, height);
		saveText.setBounds(x + 90, y, 150, height);
		saveFileButton.setBounds(x + 250, y, 90, height);
		saveButton.setBounds(x + 350, y, 90, height);
		
		y = y + 80;
		openLabel.setBounds(x, y, 80, height);
		openText.setBounds(x + 90, y, 150, height);
		
		y = y + 40;
		downloadLabel.setBounds(x, y, 160, height);
		downloadText.setBounds(x + 170, y, 150, height);
		openButton.setBounds(x + 330, y, 90, height);
		downloadButton.setBounds(x + 430, y, 130, height);
		
		
		fileChooser = new JFileChooser();
		// 设置只能选择文件
		fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);

		// 增加点击事件
		addActionListener();
	}
	
	private void addActionListener() {
		saveFileButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				fileChooser.showOpenDialog(null);
				File file = fileChooser.getSelectedFile();
				if (null != file) {
					saveText.setText(file.getAbsolutePath());
				}
			}
		});
		
		saveButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				String absolutePath = saveText.getText();
				if (null != absolutePath && !"".equals(absolutePath)) {
					
					int last = absolutePath.lastIndexOf("/");
					String namespace = absolutePath.substring(0, last + 1);
					String name = absolutePath.substring(last + 1);
					
					FileInputStream inputStream = null;
					try {
						inputStream = new FileInputStream(absolutePath);
					} catch (FileNotFoundException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					nameNode.saveFile(namespace, name, inputStream, 3);
					JOptionPane.showMessageDialog(null, "Save file successfully!");
					saveText.setText("");
				}
			}
		});
		
		openButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				fileChooser.showSaveDialog(null);
				File file = fileChooser.getSelectedFile();
				if (null != file) {
					downloadText.setText(file.getAbsolutePath());
				}
			}
		});
		
		downloadButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				String from = openText.getText();
				Path path = Paths.get(from);
				
				if (Files.exists(path)) {
					String to = downloadText.getText();
					if (null == to) {
						JOptionPane.showMessageDialog(null, "The download directory can not be null");
						return;
					}
					int last = from.lastIndexOf("/");
					String namespace = from.substring(0, last + 1);
					String name = from.substring(last + 1);

					InputStream inputStream = nameNode.openFile(namespace, name);
					byte[] data = new byte[128];
					try (FileOutputStream outputStream = new FileOutputStream(to)) {
						while (-1 != inputStream.read(data)) {
							outputStream.write(data);
						}
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					
					JOptionPane.showMessageDialog(null, "Download file successfully!");
					openText.setText("");
					downloadText.setText("");
				} else {
					JOptionPane.showMessageDialog(null, "The file is not existing!");
					openText.setText("");
				}
			}
		});
	}

}
