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
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Embeddable;

import org.hibernate.annotations.AccessType;

/**
 * extension for database entities that should be auditable.
 *
 * @version $Id$
 */
@Embeddable
@AccessType("field")
public class Audit implements Serializable
{
	/**
	 * get the time/date when entity was inserted.
	 */
	@Basic
	@Column(name = "created", nullable = true, columnDefinition = "DATETIME DEFAULT NULL")
    private Date created;

	/**
	 * who had entity inserted.
	 */
	@Basic
	@Column(name = "created_by", nullable = true, columnDefinition = "VARCHAR(99) DEFAULT NULL")
	private String createdBy;

	/**
	 * get the time/date when entity was updated.
	 */
	@Basic
	@Column(name = "updated", nullable = true, columnDefinition = "DATETIME DEFAULT NULL")
    private Date updated;

	/**
	 * who had entity updated.
	 */
	@Basic
	@Column(name = "updated_by", nullable = true, columnDefinition = "VARCHAR(99) DEFAULT NULL")
    private String updatedBy;

    public Audit()
    {
    }

    public Audit(Date created, String createdBy, Date updated, String updatedBy)
    {
        this.created = created;
        this.createdBy = createdBy;
        this.updated = updated;
        this.updatedBy = updatedBy;
    }

    public Date getCreated()
    {
        return created;
    }

    public void setCreated(Date created)
    {
        this.created = created;
    }

    public String getCreatedBy()
    {
        return createdBy;
    }

    public void setCreatedBy(String createdBy)
    {
        this.createdBy = createdBy;
    }

    public Date getUpdated()
    {
        return updated;
    }

    public void setUpdated(Date updated)
    {
        this.updated = updated;
    }

    public String getUpdatedBy()
    {
        return updatedBy;
    }

    public void setUpdatedBy(String updatedBy)
    {
        this.updatedBy = updatedBy;
    }

    public boolean equals(Object o)
    {
        if (this == o) return true;
        if (!(o instanceof Audit)) return false;

        Audit audit = (Audit) o;

        if (created != null ? !created.equals(audit.created) : audit.created != null) return false;
        if (createdBy != null ? !createdBy.equals(audit.createdBy) : audit.createdBy != null) return false;
        if (updated != null ? !updated.equals(audit.updated) : audit.updated != null) return false;
        if (updatedBy != null ? !updatedBy.equals(audit.updatedBy) : audit.updatedBy != null) return false;

        return true;
    }

    public int hashCode()
    {
        int result;
        result = (created != null ? created.hashCode() : 0);
        result = 31 * result + (createdBy != null ? createdBy.hashCode() : 0);
        result = 31 * result + (updated != null ? updated.hashCode() : 0);
        result = 31 * result + (updatedBy != null ? updatedBy.hashCode() : 0);
        return result;
    }

    @Override
    public String toString()
    {
        final StringBuilder sb = new StringBuilder();
        sb.append("Audit");
        sb.append("{created=").append(created);
        sb.append(", createdBy='").append(createdBy).append('\'');
        sb.append(", updated=").append(updated);
        sb.append(", updatedBy='").append(updatedBy).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
