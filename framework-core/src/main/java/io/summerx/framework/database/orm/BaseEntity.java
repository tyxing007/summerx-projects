package io.summerx.framework.database.orm;

import java.io.Serializable;

/**
 * Entity基类
 */
@SuppressWarnings("serial")
public abstract class BaseEntity implements Serializable {

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (this == obj) {
            return true;
        }

        if (!(getClass().equals(obj.getClass()))) {
            return false;
        }

        BaseEntity other = (BaseEntity) obj;
        Serializable id = getId();
        Serializable otherId = other.getId();
        if (id == null && otherId == null) {
            return true;
        }
        if (id == null || otherId == null) {
            return false;
        }
        return id.equals(otherId);
    }

    @Override
    public int hashCode() {
        int hasCode = 17;
        Serializable id = getId();
        hasCode = hasCode + (id == null ? 0 : id.hashCode());
        return hasCode;
    }

    @Override
    public String toString() {
        return String.format("%s[id=%s]", getClass().getName(), getId());
    }

    public abstract Serializable getId();
}