package com.test.takeout.vo;

import java.util.List;

public class PageResult<T> {
    private long total;
    private List<T> items;
    private int page;
    private int pageSize;

    public PageResult(long total, List<T> items, int page, int pageSize) {
        this.total = total;
        this.items = items;
        this.page = page;
        this.pageSize = pageSize;
    }

    public long getTotal() {
        return total;
    }

    public void setTotal(long total) {
        this.total = total;
    }

    public List<T> getItems() {
        return items;
    }

    public void setItems(List<T> items) {
        this.items = items;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }
}
