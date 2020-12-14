package com.jeegox.glio.dto.supply;

import com.google.common.base.MoreObjects;
import com.google.common.base.Objects;
import com.jeegox.glio.enumerators.Unity;
import java.io.Serializable;

public class ArticleStockDTO implements Serializable {
    private Integer idArticle;
    private String name;
    private String sku;
    private Unity unity;
    private String category;
    private String size;
    private Long stockTypeIn;
    private Long stockTypeOut;

    public ArticleStockDTO(Integer idArticle, String name, String sku, Unity unity, String category, String size,
                           Long stockTypeIn, Long stockTypeOut) {
        this.idArticle = idArticle;
        this.name = name;
        this.sku = sku;
        this.unity = unity;
        this.category = category;
        this.size = size;
        this.stockTypeIn = stockTypeIn == null ? 0 : stockTypeIn;
        this.stockTypeOut = stockTypeOut == null ? 0 : stockTypeOut;
    }

    public Integer getIdArticle() {
        return idArticle;
    }

    public void setIdArticle(Integer idArticle) {
        this.idArticle = idArticle;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSku() {
        return sku;
    }

    public void setSku(String sku) {
        this.sku = sku;
    }

    public Unity getUnity() {
        return unity;
    }

    public void setUnity(Unity unity) {
        this.unity = unity;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public Long getStockTypeIn() {
        return stockTypeIn;
    }

    public void setStockTypeIn(Long stockTypeIn) {
        this.stockTypeIn = stockTypeIn;
    }

    public Long getStockTypeOut() {
        return stockTypeOut;
    }

    public void setStockTypeOut(Long stockTypeOut) {
        this.stockTypeOut = stockTypeOut;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ArticleStockDTO)) return false;
        ArticleStockDTO that = (ArticleStockDTO) o;
        return Objects.equal(idArticle, that.idArticle) &&
                Objects.equal(name, that.name) &&
                Objects.equal(sku, that.sku) &&
                unity == that.unity &&
                Objects.equal(category, that.category) &&
                Objects.equal(size, that.size) &&
                Objects.equal(stockTypeIn, that.stockTypeIn) &&
                Objects.equal(stockTypeOut, that.stockTypeOut);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(idArticle, name, sku, unity, category, size, stockTypeIn, stockTypeOut);
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("idArticle", idArticle)
                .add("name", name)
                .add("sku", sku)
                .add("unity", unity)
                .add("category", category)
                .add("size", size)
                .add("stockTypeIn", stockTypeIn)
                .add("stockTypeOut", stockTypeOut)
                .toString();
    }
}
