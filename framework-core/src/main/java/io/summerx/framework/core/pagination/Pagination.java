package io.summerx.framework.core.pagination;

public class Pagination {

    // 每页默认记录数
    private static final int DEFAULT_PAGE_SIZE = 50;

    // 总记录数
    private int totalCount;

    // 每页记录数
    private int pageSize;

    // 当前页（从0开始）
    private int currentPageIndex;

    public Pagination() {
        this(DEFAULT_PAGE_SIZE, 0);
    }

    public Pagination(int pageSize) {
        this(pageSize, 0);
    }

    public Pagination(int pageSize, int pageIndex) {
        setPageSize(pageSize);
        setCurrentPageIndex(pageIndex);
    }

    /**
     * 获取总页数
     * @return
     */
    public int getPageTotal() {
        if (this.totalCount <= 0) {
            return 0;
        } else if (this.totalCount % this.pageSize > 0)
            return (this.totalCount / this.pageSize) + 1;
        else {
            return this.totalCount / this.pageSize;
        }
    }

    /**
     * 获取当前开始的记录数
     * @return
     */
    public int getFirstIndex() {
        return this.pageSize * this.currentPageIndex;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public int getCurrentPageIndex() {
        return currentPageIndex;
    }

    public void setCurrentPageIndex(int currentPageIndex) {
        this.currentPageIndex = currentPageIndex;
    }

    public int getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(int totalCount) {
        this.totalCount = totalCount;
    }
}
