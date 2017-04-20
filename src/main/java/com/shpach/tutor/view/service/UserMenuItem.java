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

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (active ? 1231 : 1237);
		result = prime * result + ((command == null) ? 0 : command.hashCode());
		result = prime * result + itemsCount;
		result = prime * result + ((menuText == null) ? 0 : menuText.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		UserMenuItem other = (UserMenuItem) obj;
		if (active != other.active)
			return false;
		if (command == null) {
			if (other.command != null)
				return false;
		} else if (!command.equals(other.command))
			return false;
		if (itemsCount != other.itemsCount)
			return false;
		if (menuText == null) {
			if (other.menuText != null)
				return false;
		} else if (!menuText.equals(other.menuText))
			return false;
		return true;
	}

}
