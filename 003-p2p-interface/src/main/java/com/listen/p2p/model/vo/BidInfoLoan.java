package com.listen.p2p.model.vo;

import java.io.Serializable;
import java.util.Date;

/**
 * @Author: Listen
 * @Date: 2020/7/29
 */
public class BidInfoLoan implements Serializable {
    private String productName;
    private Double bidMoney;
    private Date bidTime;

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public Double getBidMoney() {
        return bidMoney;
    }

    public void setBidMoney(Double bidMoney) {
        this.bidMoney = bidMoney;
    }

    public Date getBidTime() {
        return bidTime;
    }

    public void setBidTime(Date bidTime) {
        this.bidTime = bidTime;
    }

    @Override
    public String toString() {
        return "BidInfoLoan{" +
                "productName='" + productName + '\'' +
                ", bidMoney=" + bidMoney +
                ", bidTime=" + bidTime +
                '}';
    }
}
