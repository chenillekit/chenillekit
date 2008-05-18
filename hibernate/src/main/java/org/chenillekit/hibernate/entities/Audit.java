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
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Basic;

/**
 * extension for database entities that should be auditable.
 *
 * @author <a href="mailto:homburgs@gmail.com">S.Homburg</a>
 * @version $Id$
 */
@Embeddable
public class Audit implements Serializable
{
    private Date _created;
    private String _createdBy;
    private Date _updated;
    private String _updatedBy;

    public Audit()
    {
    }

    public Audit(Date created, String createdBy, Date updated, String updatedBy)
    {
        _created = created;
        _createdBy = createdBy;
        _updated = updated;
        _updatedBy = updatedBy;
    }

    /**
     * get the time/date when entity was inserted.
     */
    @Basic
    @Column(name = "created", nullable = true, columnDefinition = "DATETIME DEFAULT NULL")
    public Date getCreated()
    {
        return _created;
    }

    public void setCreated(Date created)
    {
        _created = created;
    }

    /**
     * who had entity inserted.
     */
    @Basic
    @Column(name = "created_by", nullable = true, columnDefinition = "VARCHAR(99) DEFAULT NULL")
    public String getCreatedBy()
    {
        return _createdBy;
    }

    public void setCreatedBy(String createdBy)
    {
        _createdBy = createdBy;
    }

    /**
     * get the time/date when entity was updated.
     */
    @Basic
    @Column(name = "updated", nullable = true, columnDefinition = "DATETIME DEFAULT NULL")
    public Date getUpdated()
    {
        return _updated;
    }

    public void setUpdated(Date updated)
    {
        _updated = updated;
    }

    /**
     * who had entity updated.
     */
    @Basic
    @Column(name = "updated_by", nullable = true, columnDefinition = "VARCHAR(99) DEFAULT NULL")
    public String getUpdatedBy()
    {
        return _updatedBy;
    }

    public void setUpdatedBy(String updatedBy)
    {
        _updatedBy = updatedBy;
    }

    public boolean equals(Object o)
    {
        if (this == o) return true;
        if (!(o instanceof Audit)) return false;

        Audit audit = (Audit) o;

        if (_created != null ? !_created.equals(audit._created) : audit._created != null) return false;
        if (_createdBy != null ? !_createdBy.equals(audit._createdBy) : audit._createdBy != null) return false;
        if (_updated != null ? !_updated.equals(audit._updated) : audit._updated != null) return false;
        if (_updatedBy != null ? !_updatedBy.equals(audit._updatedBy) : audit._updatedBy != null) return false;

        return true;
    }

    public int hashCode()
    {
        int result;
        result = (_created != null ? _created.hashCode() : 0);
        result = 31 * result + (_createdBy != null ? _createdBy.hashCode() : 0);
        result = 31 * result + (_updated != null ? _updated.hashCode() : 0);
        result = 31 * result + (_updatedBy != null ? _updatedBy.hashCode() : 0);
        return result;
    }

    @Override
    public String toString()
    {
        final StringBuilder sb = new StringBuilder();
        sb.append("Audit");
        sb.append("{_created=").append(_created);
        sb.append(", _createdBy='").append(_createdBy).append('\'');
        sb.append(", _updated=").append(_updated);
        sb.append(", _updatedBy='").append(_updatedBy).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
