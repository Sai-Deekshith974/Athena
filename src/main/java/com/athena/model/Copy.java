package com.athena.model;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import java.sql.Timestamp;
import java.util.Calendar;

/**
 * Created by Tommy on 2017/6/9.
 */
@MappedSuperclass
abstract public class Copy extends CopyInfo {
    protected Long id;
    protected Timestamp createdDate;
    protected Timestamp updatedDate;

    public Copy() {
        super();
        Timestamp timestamp = new Timestamp(Calendar.getInstance().getTimeInMillis());
        this.createdDate = timestamp;
        this.updatedDate = timestamp;
    }

    public Copy(CopyInfo copyInfo) {
        super(copyInfo.status);
        Timestamp timestamp = new Timestamp(Calendar.getInstance().getTimeInMillis());
        this.createdDate = timestamp;
        this.updatedDate = timestamp;
    }


    @Id
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }


    @Basic
    @Column(name = "created_date", nullable = true,table = "copy")
    public Timestamp getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Timestamp createdDate) {
        this.createdDate = createdDate;
    }

    @Basic
    @Column(name = "updated_date", nullable = true,table = "copy")
    public Timestamp getUpdatedDate() {
        return updatedDate;
    }

    public void setUpdatedDate(Timestamp updatedDate) {
        this.updatedDate = updatedDate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Copy copy = (Copy) o;

        if (id.equals(copy.id)) return false;
        if (status != null ? !status.equals(copy.status) : copy.status != null) return false;
        if (createdDate != null ? !createdDate.equals(copy.createdDate) : copy.createdDate != null) return false;
        if (updatedDate != null ? !updatedDate.equals(copy.updatedDate) : copy.updatedDate != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = id.hashCode();
        result = 31 * result + (status != null ? status.hashCode() : 0);
        result = 31 * result + (createdDate != null ? createdDate.hashCode() : 0);
        result = 31 * result + (updatedDate != null ? updatedDate.hashCode() : 0);
        return result;
    }


}
