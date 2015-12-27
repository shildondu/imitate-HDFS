package com.shildon.main;

import com.shildon.view.MainView;

/**
 * 启动图形界面。
 * @author shildon<shildondu@gmail.com>
 * @date Dec 27, 2015 8:59:12 PM
 *
 */
public class BootStrap {
	
	public static void main(String[] args) {
		MainView mainView = new MainView();
		mainView.setVisible(true);
	}

}
