package com.jeegox.glio.dto.supply;

import com.google.common.base.MoreObjects;
import com.google.common.base.Objects;

import java.io.Serializable;
import java.math.BigDecimal;

public class CategoryStockDTO implements Serializable {
    private Integer id;
    private String name;
    private BigDecimal stockTypeIn;
    private BigDecimal stockTypeOut;

    public CategoryStockDTO() {
    }

    public CategoryStockDTO(Integer id, String name, BigDecimal stockTypeIn, BigDecimal stockTypeOut) {
        this.id = id;
        this.name = name;
        this.stockTypeIn = stockTypeIn;
        this.stockTypeOut = stockTypeOut;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public BigDecimal getStockTypeIn() {
        return stockTypeIn;
    }

    public void setStockTypeIn(BigDecimal stockTypeIn) {
        this.stockTypeIn = stockTypeIn;
    }

    public BigDecimal getStockTypeOut() {
        return stockTypeOut;
    }

    public void setStockTypeOut(BigDecimal stockTypeOut) {
        this.stockTypeOut = stockTypeOut;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof CategoryStockDTO)) return false;
        CategoryStockDTO that = (CategoryStockDTO) o;
        return Objects.equal(id, that.id) &&
                Objects.equal(name, that.name) &&
                Objects.equal(stockTypeIn, that.stockTypeIn) &&
                Objects.equal(stockTypeOut, that.stockTypeOut);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id, name, stockTypeIn, stockTypeOut);
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("id", id)
                .add("name", name)
                .add("stockTypeIn", stockTypeIn)
                .add("stockTypeOut", stockTypeOut)
                .toString();
    }
}