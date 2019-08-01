package com.wujiahui.forum.entity;

/**
 * Created by NgCafai on 2019/7/28 18:15.
 */
public class Page {
    // current page number
    private int current = 1;
    // the number of posts in one page
    private int limit = 10;
    // the number of total posts
    private int rows;
    private String path;

    public int getCurrent() {
        return current;
    }

    public void setCurrent(int current) {
        this.current = current;
    }

    public int getLimit() {
        return this.limit;
    }

    public void setLimit(int limit) {
        this.limit = limit;
    }

    public int getRows() {
        return rows;
    }

    public void setRows(int rows) {
        this.rows = rows;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public int getOffset() {
        return (current - 1) * limit;
    }

    /**
     * get the number of total pages
     * @return
     */
    public int getLastPage() {
        int result = rows / limit;
        return (rows % limit == 0) ? result : result + 1;
    }

    public int getFrom() {
        if (current - 2 <= 0) {
            return 1;
        } else {
            return current - 2;
        }
    }

    public int getTo() {
        int lastPage = this.getLastPage();
        int from = this.getFrom();
        int to = from + 4;
        return to > lastPage ? lastPage : to;
    }
}
