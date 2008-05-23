/*
 * Apache License
 * Version 2.0, January 2004
 * http://www.apache.org/licenses/
 *
 * Copyright 2008 by chenillekit.org
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 */

package org.chenillekit.hibernate.entities;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * @author <a href="mailto:homburgs@gmail.com">S.Homburg</a>
 * @version $Id$
 */
@Entity
@Table(name = "address")
public class Address implements Serializable, Auditable
{
    private long _id;
    private String _name1;
    private String _name2;
    private String _street;
    private String _zip;
    private String _city;
    private String _email;
    private Audit _audit;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", length = 20)
    public long getId()
    {
        return _id;
    }

    public void setId(long id)
    {
        _id = id;
    }

    @Basic
    @Column(name = "name_1", length = 30)
    public String getName1()
    {
        return _name1;
    }

    public void setName1(String name1)
    {
        _name1 = name1;
    }

    @Basic
    @Column(name = "name_2", length = 30)
    public String getName2()
    {
        return _name2;
    }

    public void setName2(String name2)
    {
        _name2 = name2;
    }

    @Basic
    @Column(name = "street", length = 30)
    public String getStreet()
    {
        return _street;
    }

    public void setStreet(String street)
    {
        _street = street;
    }

    @Basic
    @Column(name = "zip", length = 6)
    public String getZip()
    {
        return _zip;
    }

    public void setZip(String zip)
    {
        _zip = zip;
    }

    @Basic
    @Column(name = "city", length = 30)
    public String getCity()
    {
        return _city;
    }

    public void setCity(String city)
    {
        _city = city;
    }

    @Basic
    @Column(name = "email", length = 50)
    public String getEmail()
    {
        return _email;
    }

    public void setEmail(String email)
    {
        _email = email;
    }

    @Embedded
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
        sb.append("{_id=").append(_id);
        sb.append(", _name1='").append(_name1).append('\'');
        sb.append('}');
        return sb.toString();
    }

    public boolean equals(Object o)
    {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Address address = (Address) o;

        return (_id != address._id);
    }

    public int hashCode()
    {
        int result;
        result = (int) (_id ^ (_id >>> 32));
        result = 31 * result + (_name1 != null ? _name1.hashCode() : 0);
        result = 31 * result + (_name2 != null ? _name2.hashCode() : 0);
        result = 31 * result + (_street != null ? _street.hashCode() : 0);
        result = 31 * result + (_zip != null ? _zip.hashCode() : 0);
        result = 31 * result + (_city != null ? _city.hashCode() : 0);
        result = 31 * result + (_email != null ? _email.hashCode() : 0);
        return result;
    }
}
