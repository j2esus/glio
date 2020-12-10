package com.jeegox.glio.entities.supply;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.google.common.base.MoreObjects;
import com.google.common.base.Objects;
import com.jeegox.glio.entities.admin.Company;
import com.jeegox.glio.entities.admin.User;
import com.jeegox.glio.entities.util.JComplexEntity;
import com.jeegox.glio.enumerators.StockType;
import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "stock")
@JsonIgnoreProperties({"father","hibernateLazyInitializer", "handler"})
public class Stock extends JComplexEntity<Integer, Company> implements Serializable {
    private Date date;
    private User user;
    private Depot depot;
    private Article article;
    private Integer quantity;
    private String description;
    private StockType stockType;

    public Stock() {
    }

    public Stock(Date date, User user, Depot depot, Article article, Integer quantity, String description,
                 StockType stockType, Company father) {
        this.id = id;
        this.date = date;
        this.user = user;
        this.depot = depot;
        this.article = article;
        this.quantity = quantity;
        this.description = description;
        this.stockType = stockType;
        this.father = father;
    }

    public Stock(Integer id, Date date, User user, Depot depot, Article article, Integer quantity, String description,
                 StockType stockType, Company father) {
        this.id = id;
        this.date = date;
        this.user = user;
        this.depot = depot;
        this.article = article;
        this.quantity = quantity;
        this.description = description;
        this.stockType = stockType;
        this.father = father;
    }

    @Id
    @Column(name = "id_stock", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Override
    public Integer getId() {
        return id;
    }

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "stock_date", nullable = false)
    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "id_user", referencedColumnName = "id_user", nullable = false)
    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "id_depot", referencedColumnName = "id_depot", nullable = false)
    public Depot getDepot() {
        return depot;
    }

    public void setDepot(Depot depot) {
        this.depot = depot;
    }

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "id_article", referencedColumnName = "id_article", nullable = false)
    public Article getArticle() {
        return article;
    }

    public void setArticle(Article article) {
        this.article = article;
    }

    @Column(name = "quantity", nullable = false)
    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    @Column(name = "description")
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Enumerated(EnumType.STRING)
    @Column(name = "type", nullable = false)
    public StockType getStockType() {
        return stockType;
    }

    public void setStockType(StockType stockType) {
        this.stockType = stockType;
    }

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "id_company", referencedColumnName = "id_company", nullable = false)
    @Override
    public Company getFather() {
        return father;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Stock)) return false;
        Stock stock = (Stock) o;
        return Objects.equal(id, stock.id) &&
                Objects.equal(date, stock.date) &&
                Objects.equal(user, stock.user) &&
                Objects.equal(depot, stock.depot) &&
                Objects.equal(article, stock.article) &&
                Objects.equal(quantity, stock.quantity) &&
                Objects.equal(description, stock.description) &&
                Objects.equal(father, stock.father) &&
                stockType == stock.stockType;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id, date, user, depot, article, quantity, description, stockType, father);
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("id", id)
                .add("date", date)
                .add("user", user)
                .add("depot", depot)
                .add("article", article)
                .add("quantity", quantity)
                .add("description", description)
                .add("stockType", stockType)
                .add("father", father)
                .toString();
    }
}
