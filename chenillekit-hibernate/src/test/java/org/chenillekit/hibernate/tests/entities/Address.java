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

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.chenillekit.hibernate.entities.Audit;
import org.chenillekit.hibernate.entities.Auditable;

/**
 * @version $Id$
 */
@Entity
@Table(name = "address")
public class Address implements Serializable, Auditable
{
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "id", length = 20, columnDefinition = "BIGINT")
	private long id;

	@Basic
	@Column(name = "name_1", length = 30, columnDefinition = "VARCHAR(30)")
	private String name1;

	@Basic
	@Column(name = "name_2", length = 30, columnDefinition = "VARCHAR(30)")
	private String name2;

	@Basic
	@Column(name = "street", length = 30, columnDefinition = "VARCHAR(30)")
	private String street;

	@Basic
	@Column(name = "zip", length = 6, columnDefinition = "CHAR(6)")
	private String zip;

	@Basic
	@Column(name = "city", length = 30, columnDefinition = "VARCHAR(30)")
	private String city;

	@Basic
	@Column(name = "email", length = 50, columnDefinition = "VARCHAR(50)")
	private String email;

	@Embedded
	private Audit _audit;

	public long getId()
	{
		return id;
	}

	public void setId(long id)
	{
		this.id = id;
	}

	public String getName1()
	{
		return name1;
	}

	public void setName1(String name1)
	{
		this.name1 = name1;
	}

	public String getName2()
	{
		return name2;
	}

	public void setName2(String name2)
	{
		this.name2 = name2;
	}

	public String getStreet()
	{
		return street;
	}

	public void setStreet(String street)
	{
		this.street = street;
	}

	public String getZip()
	{
		return zip;
	}

	public void setZip(String zip)
	{
		this.zip = zip;
	}

	public String getCity()
	{
		return city;
	}

	public void setCity(String city)
	{
		this.city = city;
	}

	public String getEmail()
	{
		return email;
	}

	public void setEmail(String email)
	{
		this.email = email;
	}

	public Audit getAudit()
	{
		return _audit;
	}

	public void setAudit(Audit audit)
	{
		_audit = audit;
	}

	@Override
	public String toString()
	{
		final StringBuilder sb = new StringBuilder();
		sb.append("Address");
		sb.append("{_id=").append(id);
		sb.append(", _name1='").append(name1).append('\'');
		sb.append('}');
		return sb.toString();
	}

	public boolean equals(Object o)
	{
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;

		Address address = (Address) o;

		return (id != address.id);
	}

	public int hashCode()
	{
		int result;
		result = (int) (id ^ (id >>> 32));
		result = 31 * result + (name1 != null ? name1.hashCode() : 0);
		result = 31 * result + (name2 != null ? name2.hashCode() : 0);
		result = 31 * result + (street != null ? street.hashCode() : 0);
		result = 31 * result + (zip != null ? zip.hashCode() : 0);
		result = 31 * result + (city != null ? city.hashCode() : 0);
		result = 31 * result + (email != null ? email.hashCode() : 0);
		return result;
	}
}
