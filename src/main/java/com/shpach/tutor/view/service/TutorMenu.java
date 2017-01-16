package com.shpach.tutor.view.service;

public class TutorMenu {
	private String menuText;
	private int itemsCount;
	private String command;
	private boolean active;

	
	public TutorMenu() {
	}

	public TutorMenu(String menuText, int itemsCount, String command) {
		this.menuText = menuText;
		this.itemsCount = itemsCount;
		this.command = command;
	}

	public String getMenuText() {
		return menuText;
	}

	public void setMenuText(String menuText) {
		this.menuText = menuText;
	}

	public int getItemsCount() {
		return itemsCount;
	}

	public void setItemsCount(int itemsCount) {
		this.itemsCount = itemsCount;
	}

	public String getCommand() {
		return command;
	}

	public void setCommand(String command) {
		this.command = command;
	}

	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}

}
