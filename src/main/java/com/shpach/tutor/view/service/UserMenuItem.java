package com.shpach.tutor.view.service;

/**Contains all information to visualize item of menu on jspx page
 * @author Shpachenko_A_K
 *
 */
public class UserMenuItem {
	private String menuText;
	private int itemsCount;
	private String command;
	private boolean active;

	
	public UserMenuItem() {
	}

	public UserMenuItem(String menuText, int itemsCount, String command) {
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
