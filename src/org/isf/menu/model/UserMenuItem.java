package org.isf.menu.model;
import org.isf.generaldata.MessageBundle;

/**
 * not pure model class
 * @author flavio
 *	an item in user menu
 */
public class UserMenuItem {

	private String 	code;
	private String 	buttonLabel;
	private String 	altLabel;
	private String 	tooltip;
	private char	shortcut;
	private String	mySubmenu;
	private String	myClass;
	private boolean	isASubMenu;
	private int 	position;
	private boolean isActive;
	
	
	public UserMenuItem(String code, String buttonLabel, String altLabel, String tooltip, char shortcut, String mySubmenu, String myClass, boolean isASubMenu, int position, boolean isActive) {
		super();
		this.code = code;
		this.buttonLabel = buttonLabel;
		this.altLabel = altLabel;
		this.tooltip = tooltip;
		this.shortcut = shortcut;
		this.mySubmenu = mySubmenu;
		this.myClass = myClass;
		this.isASubMenu = isASubMenu;
		this.position = position;
		this.isActive = isActive;
	}
	
	
	public String getAltLabel() {
		return altLabel;
	}
	public void setAltLabel(String altLabel) {
		this.altLabel = altLabel;
	}
	public String getButtonLabel() {
		return buttonLabel;
	}
	public void setButtonLabel(String buttonLabel) {
		this.buttonLabel = buttonLabel;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public boolean isActive() {
		return isActive;
	}
	public void setActive(boolean isActive) {
		this.isActive = isActive;
	}
	public boolean isASubMenu() {
		return isASubMenu;
	}
	public void setASubMenu(boolean isASubMenu) {
		this.isASubMenu = isASubMenu;
	}
	public String getMyClass() {
		return myClass;
	}
	public void setMyClass(String myClass) {
		this.myClass = myClass;
	}
	public String getMySubmenu() {
		return mySubmenu;
	}
	public void setMySubmenu(String mySubmenu) {
		this.mySubmenu = mySubmenu;
	}
	public int getPosition() {
		return position;
	}
	public void setPosition(int position) {
		this.position = position;
	}
	public char getShortcut() {
		return shortcut;
	}
	public void setShortcut(char shortcut) {
		this.shortcut = shortcut;
	}
	public String getTooltip() {
		return tooltip;
	}
	public void setTooltip(String tooltip) {
		this.tooltip = tooltip;
	}
	
	
	
	public boolean equals(Object anObject) {
        return (anObject == null) || !(anObject instanceof UserMenuItem) ? false
                : (getCode().equals(((UserMenuItem) anObject).getCode())
                  && getButtonLabel().equalsIgnoreCase(((UserMenuItem) anObject).getButtonLabel()) 
                  && getAltLabel().equals(((UserMenuItem) anObject).getAltLabel())
                  && getTooltip().equals(((UserMenuItem) anObject).getTooltip())
                  && getShortcut()==((UserMenuItem) anObject).getShortcut()
                  && getMySubmenu().equals(((UserMenuItem) anObject).getMySubmenu())
                  && getMyClass().equals(((UserMenuItem) anObject).getMyClass())
                  && isASubMenu()==((UserMenuItem) anObject).isASubMenu()
                  && getPosition()==((UserMenuItem) anObject).getPosition()
                  && (isActive()==((UserMenuItem) anObject).isActive()));
    }
	
	public String toString(){
		return getButtonLabel();
	}
	
	public String getDescription(){
		return MessageBundle.getMessage("angal.menu.usermenuitem")+getCode()+ MessageBundle.getMessage("angal.menu.labelstooltipshort")+getButtonLabel()+"-"+getAltLabel()+"-"+getTooltip()+
				"-"+getShortcut()+"...\n"+MessageBundle.getMessage("angal.menu.submenu")+getMySubmenu()+MessageBundle.getMessage("angal.menu.class")+getMyClass()+"...\n "+MessageBundle.getMessage("angal.menu.issubmenu")+isASubMenu()+
				MessageBundle.getMessage("angal.menu.isactive")+isActive()+MessageBundle.getMessage("angal.menu.inposition")+getPosition();
				
	}
	
}
