package io.summerx.framework.core.pagination;

import java.io.Serializable;

/**
 * 页面排序
 */
public class PageOrder implements Serializable {

    private static final long serialVersionUID = 5865149274955936838L;

    // 排序的属性
    private String propertyName;

    // 是否降序
    private boolean descending;

    public PageOrder(String propertyName) {
        this(propertyName, true);
    }

    public PageOrder(String propertyName, boolean descending) {
        if (propertyName == null || propertyName.length() == 0) {
            throw new IllegalArgumentException("propertyName must not be empty.");
        }
        this.propertyName = propertyName;
        this.descending = descending;
    }

    public String getPropertyName() {
        return propertyName;
    }

    public boolean isAscending() {
        return !isDescending();
    }

    public boolean isDescending() {
        return descending;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof PageOrder)) return false;
        PageOrder another = (PageOrder) obj;
        return (propertyName.equals(another.getPropertyName())) && (descending == another.isDescending());
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = hash * 31 + this.getPropertyName().hashCode();
        hash = hash * 31 + (descending ? 1 : 2);
        return hash;
    }

    public static PageOrder asc(String propertyName) {
        return new PageOrder(propertyName, false);
    }

    public static PageOrder desc(String propertyName) {
        return new PageOrder(propertyName, true);
    }
}