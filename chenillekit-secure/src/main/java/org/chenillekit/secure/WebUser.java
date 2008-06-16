/**
 * 
 */
package org.chenillekit.secure;

/**
 * JavaBean used to store infos about the user currently logged
 * in. Typically the final user want to extend this one. 
 *
 */
public class WebUser
{
	private int userId;
	private String name;
	
	// Both used for security constraints
    private int role = 1;
    private String group = "N/A";
    
    
	public int getUserId() {
		return userId;
	}
	public void setUserId(int userId) {
		this.userId = userId;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getRole() {
		return role;
	}
	public void setRole(int role) {
		this.role = role;
	}
	public String getGroup() {
		return group;
	}
	public void setGroup(String group) {
		this.group = group;
	}

}
