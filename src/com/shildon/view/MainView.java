package com.shildon.view;

import java.awt.Toolkit;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;

public class MainView extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private JLabel saveLabel;
	private JLabel openLabel;
	private JTextField saveText;
	private JTextField openText;
	private JButton saveFileButton;
	private JButton openFileButton;
	private JButton saveButton;
	private JButton openButton;
	
	private JFileChooser fileChooser;
	
	public MainView() {
		defaultSetting();
		init();
	}
	
	private void defaultSetting() {
		int screenx = (int) Toolkit.getDefaultToolkit().getScreenSize().getWidth();
		int screeny = (int) Toolkit.getDefaultToolkit().getScreenSize().getHeight();
		
		int x = 600;
		int y = 400;
		setTitle("模拟Hadoop FileSystem");
		setSize(x, y);
		// 居中
		setLocation(screenx / 2 - x / 2, screeny / 2 - y / 2);
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
	}
		
	private void init() {
		saveLabel = new JLabel("save file: ");
		openLabel = new JLabel("open file: ");
		saveText = new JTextField();
		openText = new JTextField();
		saveFileButton = new JButton("select");
		openFileButton = new JButton("select");
		saveButton = new JButton("save");
		openButton = new JButton("open");
		
		setLayout(null);
		add(saveLabel);
		add(saveText);
		add(saveFileButton);
		add(saveButton);
		add(openLabel);
		add(openText);
		add(openFileButton);
		add(openButton);
		
		int x = 10, y = 30, height = 25;
		saveLabel.setBounds(x, y, 80, height);
		saveText.setBounds(x + 90, y, 150, height);
		saveFileButton.setBounds(x + 250, y, 90, height);
		saveButton.setBounds(x + 350, y, 90, height);
		
		y = y + 40;
		openLabel.setBounds(x, y, 80, height);
		openText.setBounds(x + 90, y, 150, height);
		openFileButton.setBounds(x + 250, y, 90, height);
		openButton.setBounds(x + 350, y, 90, height);
		
		fileChooser = new JFileChooser();
		// 设置只能选择文件
		fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
	}

}
