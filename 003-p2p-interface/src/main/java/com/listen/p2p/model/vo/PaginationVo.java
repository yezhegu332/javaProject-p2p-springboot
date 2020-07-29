package com.listen.p2p.model.vo;

import java.io.Serializable;
import java.util.List;

/**
 * @Author: Listen
 * @Date: 2020/7/25
 */
public class PaginationVo<T> implements Serializable {
    private List<T> dataList;
    private Long total;

    public PaginationVo() {
    }

    public PaginationVo(List<T> dataList, Long total) {
        this.dataList = dataList;
        this.total = total;
    }

    public List<T> getDataList() {
        return dataList;
    }

    public void setDataList(List<T> dataList) {
        this.dataList = dataList;
    }

    public Long getTotal() {
        return total;
    }

    public void setTotal(Long total) {
        this.total = total;
    }

    @Override
    public String toString() {
        return "PaginationVo{" +
                "dataList=" + dataList +
                ", total=" + total +
                '}';
    }


}
