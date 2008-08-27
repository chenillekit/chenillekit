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
import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import java.util.List;
import java.util.ArrayList;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.apache.tapestry5.beaneditor.Validate;

import org.hibernate.annotations.Cascade;

/**
 * @author <a href="mailto:homburgs@gmail.com">S.Homburg</a>
 * @version $Id$
 */
@Entity
@Table(name = "users")
public class User implements Serializable, Auditable
{
    private long _id;
    private String _loginName;
    private String _password;
    private boolean _active;
    private Date _lastLogin;
    private byte[] _properties;
    private Address _address;
    private List<Pseudonym> pseudonyms = new ArrayList<Pseudonym>();
    private Audit _audit;

    public User(String loginName, String password)
    {
        _loginName = loginName;
        _password = password;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false, columnDefinition = "BIGINT")
    public long getId()
    {
        return _id;
    }

    public void setId(long id)
    {
        _id = id;
    }

    @Basic
    @Column(name = "login_name", nullable = false, unique = true, columnDefinition = "VARCHAR(8)")
    @Validate("required")
    public String getLoginName()
    {
        return _loginName;
    }

    public void setLoginName(String loginName)
    {
        _loginName = loginName;
    }

    @Basic
    @Column(name = "password", nullable = false, columnDefinition = "VARCHAR(32)")
    @Validate("required,minlength=4")
    public String getPassword()
    {
        return _password;
    }

    public void setPassword(String password)
    {
        _password = password;
    }

    @Basic
    @Column(name = "active", columnDefinition = "CHAR(1)")
    public boolean getActive()
    {
        return _active;
    }

    public void setActive(boolean active)
    {
        _active = active;
    }

    @Basic
    @Column(name = "last_login", length = 19, columnDefinition = "DATETIME")
    public Date getLastLogin()
    {
        return _lastLogin;
    }

    public void setLastLogin(Date lastLogin)
    {
        _lastLogin = lastLogin;
    }

    @Basic(fetch = FetchType.LAZY)
    @javax.persistence.Lob
    @Column(name = "properties", length = 0)
    public byte[] getProperties()
    {
        return _properties;
    }

    public void setProperties(byte[] properties)
    {
        _properties = properties;
    }

    @ManyToOne(targetEntity = Address.class)
    @Cascade(value = {org.hibernate.annotations.CascadeType.SAVE_UPDATE,
            org.hibernate.annotations.CascadeType.DELETE_ORPHAN})
    @JoinColumn(name = "ref_address", nullable = false)
    public Address getAddress()
    {
        return _address;
    }

    public void setAddress(Address address)
    {
        _address = address;
    }

    @OneToMany(mappedBy = "user")
    @Cascade({org.hibernate.annotations.CascadeType.SAVE_UPDATE, org.hibernate.annotations.CascadeType.DELETE_ORPHAN})
    public List<Pseudonym> getPseudonyms()
    {
        return pseudonyms;
    }

    public void setPseudonyms(List<Pseudonym> pseudonyms)
    {
        this.pseudonyms = pseudonyms;
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

    public boolean equals(Object o)
    {
        if (this == o) return true;
        if (!(o instanceof User)) return false;

        User user = (User) o;

        return _id == user.getId();
    }

    public int hashCode()
    {
        int result;
        result = (int) (_id ^ (_id >>> 32));
        result = 31 * result + (_loginName != null ? _loginName.hashCode() : 0);
        result = 31 * result + (_password != null ? _password.hashCode() : 0);
        result = 31 * result + (_active ? 1 : 0);
        result = 31 * result + (_lastLogin != null ? _lastLogin.hashCode() : 0);
        result = 31 * result + (_address != null ? _address.hashCode() : 0);
        return result;
    }

    @Override
    public String toString()
    {
        final StringBuilder sb = new StringBuilder();
        sb.append("User");
        sb.append("{_recId=").append(_id);
        sb.append(", _loginName='").append(_loginName).append('\'');
        sb.append('}');
        return sb.toString();
    }

}
