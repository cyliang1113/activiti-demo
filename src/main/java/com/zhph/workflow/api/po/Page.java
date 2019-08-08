package com.zhph.workflow.api.po;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

public class Page<T> implements Serializable {
    private static final long serialVersionUID = -7844869621967029651L;

    private Integer currentPage; //页数
    private Integer pageSize; //每页条数
    private Integer total; //总条数
    private List<T> list = new LinkedList<T>(); //列表

    public Integer getCurrentPage() {
        return currentPage;
    }

    public void setCurrentPage(Integer currentPage) {
        this.currentPage = currentPage;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }

    public Integer getTotal() {
        return total;
    }

    public void setTotal(Integer total) {
        this.total = total;
    }

    public List<T> getList() {
        return list;
    }

    public void setList(List<T> list) {
        this.list = list;
    }
}
