package org.isf.menu.model;
import javax.persistence.Column;
import javax.persistence.ColumnResult;
import javax.persistence.Entity;
import javax.persistence.EntityResult;
import javax.persistence.FieldResult;
import javax.persistence.Id;
import javax.persistence.SqlResultSetMapping;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;

import org.isf.generaldata.MessageBundle;

/**
 * not pure model class
 * @author flavio
 *	an item in user menu
 */
/*------------------------------------------
 * User - model for the user entity
 * -----------------------------------------
 * modification history
 * ? 		  - flavio - first version 
 * 07/05/2016 - Antonio - ported to JPA
 * 
 *------------------------------------------*/
@Entity
@Table(name="MENUITEM")
@SqlResultSetMapping(name="UserMenuItemWithStatus",
	entities={
	    @EntityResult(entityClass=org.isf.menu.model.UserMenuItem.class, fields={
                @FieldResult(name="code", column="MNI_ID_A"),
                @FieldResult(name="buttonLabel", column="MNI_BTN_LABEL"), 
                @FieldResult(name="altLabel", column="MNI_LABEL"),
                @FieldResult(name="tooltip", column="MNI_TOOLTIP"),
                @FieldResult(name="shortcut", column="MNI_SHORTCUT"),
                @FieldResult(name="mySubmenu", column="MNI_SUBMENU"),
                @FieldResult(name="myClass", column="MNI_CLASS"),
                @FieldResult(name="isASubMenu", column="MNI_IS_SUBMENU"),
                @FieldResult(name="isActive", column="IS_ACTIVE"),
                @FieldResult(name="position", column="MNI_POSITION")})},
	columns={
	    @ColumnResult(name="is_active")}
	)
public class UserMenuItem 
{
	@Id 
	@Column(name="MNI_ID_A")	
	private String 	code;

	@NotNull
	@Column(name="MNI_BTN_LABEL")
	private String 	buttonLabel;

	@NotNull
	@Column(name="MNI_LABEL")
	private String 	altLabel;

	@Column(name="MNI_TOOLTIP")
	private String 	tooltip;

	@Column(name="MNI_SHORTCUT")
	private char	shortcut;

	@NotNull
	@Column(name="MNI_SUBMENU")
	private String	mySubmenu;

	@NotNull
	@Column(name="MNI_CLASS")
	private String	myClass;

	@NotNull
	@Column(name="MNI_IS_SUBMENU")
	private boolean	isASubMenu;

	@NotNull
	@Column(name="MNI_POSITION")
	private int 	position;

	@Transient
	private boolean isActive;
	
	@Transient
	private volatile int hashCode = 0;
	
	public UserMenuItem() {
		super();
	}
		
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
		return MessageBundle.getMessage(altLabel); 
	}
	public void setAltLabel(String altLabel) {
		this.altLabel = altLabel;
	}
	public String getButtonLabel() {
		return MessageBundle.getMessage(buttonLabel);
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
		
	@Override
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

	@Override
	public int hashCode() {
	    if (this.hashCode == 0) {
	        final int m = 23;
	        int c = 133;
	        
	        c = m * c + code.hashCode();
	        
	        this.hashCode = c;
	    }
	  
	    return this.hashCode;
	}		
}
