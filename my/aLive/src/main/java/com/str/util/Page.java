package com.str.util;

public class Page {
    public static final int DEFAULT_PAGE_SIZE = 10;

    private int pageIndex;      // 页码
    private int pageSize;       // 每📃页记录数
    private int totalCount;     // 总记录数
    private int pageCount;      // 总页数

    public Page(int pageIndex, int pageSize) {
        if (pageIndex < 1) {
            pageIndex = 1;
        }
        if (pageSize < 1) {
            pageSize = 1;
        }
        this.pageIndex = pageIndex;
        this.pageSize = pageSize;
    }

    public Page(int pageIndex) {
        this(pageIndex, DEFAULT_PAGE_SIZE);
    }

    public int getPageIndex() {
        return pageIndex;
    }

    public int getPageSize() {
        return pageSize;
    }

    public int getTotalCount() {
        return totalCount;
    }

    public int getPageCount() {
        return pageCount;
    }

    // 相当于LIMIT中的offset
    public int getFirstResult() {
        return (pageIndex - 1) * pageSize;
    }

    public boolean getHasPrevious() {
        return pageIndex > 1;
    }

    public boolean getHasNext() {
        return pageIndex < pageCount;
    }

    public boolean isEmpty() {
        return totalCount == 0;
    }

    public void setTotalCount(int totalCount) {
        this.totalCount = totalCount;
        pageCount = totalCount / pageSize + (totalCount % pageSize == 0 ? 0 : 1);
        if (totalCount == 0) {
            if (pageIndex != 1) {
                throw new IndexOutOfBoundsException("Page index out of range.");
            }
        } else {
            if (pageIndex > pageCount) {
                throw new IndexOutOfBoundsException("page index out of range.");
            }
        }
    }
}
