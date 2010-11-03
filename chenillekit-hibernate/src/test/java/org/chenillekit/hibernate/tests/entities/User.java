/*
 * Apache License
 * Version 2.0, January 2004
 * http://www.apache.org/licenses/
 *
 * Copyright 2008-2010 by chenillekit.org
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 */

package org.chenillekit.hibernate.tests.entities;

import org.apache.tapestry5.beaneditor.Validate;
import org.chenillekit.hibernate.entities.Audit;
import org.chenillekit.hibernate.entities.Auditable;
import org.hibernate.annotations.Cascade;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @version $Id$
 */
@Entity
@Table(name = "users")
public class User implements Serializable, Auditable
{
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "id", nullable = false, columnDefinition = "BIGINT")
	private long id;

	@Basic
	@Column(name = "login_name", nullable = false, unique = true, columnDefinition = "VARCHAR(8)")
	@Validate("required")
	private String loginName;

	@Basic
	@Column(name = "password", nullable = false, columnDefinition = "VARCHAR(32)")
	@Validate("required,minlength=4")
	private String password;

	@Basic
	@Column(name = "active", columnDefinition = "CHAR(1)")
	private boolean active;

	@Basic
	@Column(name = "last_login", length = 19, columnDefinition = "DATETIME")
	private Date lastLogin;

	@Basic(fetch = FetchType.LAZY)
	@javax.persistence.Lob
	@Column(name = "properties", length = 0)
	private byte[] properties;

	@OneToOne(targetEntity = Address.class, orphanRemoval = true)
	@Cascade(value = {org.hibernate.annotations.CascadeType.SAVE_UPDATE})
	@JoinColumn(name = "id_address", nullable = false, columnDefinition = "BIGINT")
	private Address address;

	@OneToMany(mappedBy = "user", orphanRemoval = true)
	@Cascade({org.hibernate.annotations.CascadeType.SAVE_UPDATE})
	private List<Pseudonym> pseudonyms = new ArrayList<Pseudonym>();

	@Embedded
	private Audit audit;

	public User(String loginName, String password)
	{
		this.loginName = loginName;
		this.password = password;
	}

	public long getId()
	{
		return id;
	}

	public void setId(long id)
	{
		this.id = id;
	}

	public String getLoginName()
	{
		return loginName;
	}

	public void setLoginName(String loginName)
	{
		this.loginName = loginName;
	}

	public String getPassword()
	{
		return password;
	}

	public void setPassword(String password)
	{
		this.password = password;
	}

	public boolean getActive()
	{
		return active;
	}

	public void setActive(boolean active)
	{
		this.active = active;
	}

	public Date getLastLogin()
	{
		return lastLogin;
	}

	public void setLastLogin(Date lastLogin)
	{
		this.lastLogin = lastLogin;
	}

	public byte[] getProperties()
	{
		return properties;
	}

	public void setProperties(byte[] properties)
	{
		this.properties = properties;
	}

	public Address getAddress()
	{
		return address;
	}

	public void setAddress(Address address)
	{
		this.address = address;
	}

	public List<Pseudonym> getPseudonyms()
	{
		return pseudonyms;
	}

	public void setPseudonyms(List<Pseudonym> pseudonyms)
	{
		this.pseudonyms = pseudonyms;
	}

	public Audit getAudit()
	{
		return audit;
	}

	public void setAudit(Audit audit)
	{
		this.audit = audit;
	}

	public boolean equals(Object o)
	{
		if (this == o) return true;
		if (!(o instanceof User)) return false;

		User user = (User) o;

		return id == user.getId();
	}

	public int hashCode()
	{
		int result;
		result = (int) (id ^ (id >>> 32));
		result = 31 * result + (loginName != null ? loginName.hashCode() : 0);
		result = 31 * result + (password != null ? password.hashCode() : 0);
		result = 31 * result + (active ? 1 : 0);
		result = 31 * result + (lastLogin != null ? lastLogin.hashCode() : 0);
		result = 31 * result + (address != null ? address.hashCode() : 0);
		return result;
	}

	@Override
	public String toString()
	{
		final StringBuilder sb = new StringBuilder();
		sb.append("User");
		sb.append("{_recId=").append(id);
		sb.append(", _loginName='").append(loginName).append('\'');
		sb.append('}');
		return sb.toString();
	}

}
