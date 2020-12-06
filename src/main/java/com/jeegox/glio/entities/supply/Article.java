package com.jeegox.glio.entities.supply;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.google.common.base.MoreObjects;
import com.google.common.base.Objects;
import com.jeegox.glio.entities.admin.Company;
import com.jeegox.glio.entities.util.JComplexEntity;
import com.jeegox.glio.enumerators.Status;
import com.jeegox.glio.enumerators.Unity;
import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "article")
@JsonIgnoreProperties({"father","hibernateLazyInitializer", "handler"})
public class Article extends JComplexEntity<Integer, Company> implements Serializable{
    private String name;
    private String sku;
    private String description;
    private Double cost;
    private Double price;
    private Status status;
    private Unity unity;
    private CategoryArticle categoryArticle;
    private Size size;
    
    public Article(){
        
    }

    public Article(Integer id, String name, String sku, String description, Double cost, 
            Double price, Status status, Unity unity, Company company, CategoryArticle categoryArticle, Size size) {
        this.id = id;
        this.name = name;
        this.sku = sku;
        this.description = description;
        this.cost = cost;
        this.price = price;
        this.status = status;
        this.unity = unity;
        this.father = company;
        this.categoryArticle = categoryArticle;
        this.size = size;
    }
    
    

    @Id
    @Column(name = "id_article")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Override
    public Integer getId() {
        return id;
    }

    @Column(name = "name")
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Column(name = "sku")
    public String getSku() {
        return sku;
    }

    public void setSku(String sku) {
        this.sku = sku;
    }

    @Column(name = "description")
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Column(name = "cost")
    public Double getCost() {
        return cost;
    }

    public void setCost(Double cost) {
        this.cost = cost;
    }

    @Column(name = "price")
    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    @Column(name = "unity")
    @Enumerated(EnumType.STRING)
    public Unity getUnity() {
        return unity;
    }

    public void setUnity(Unity unity) {
        this.unity = unity;
    }

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "id_category_article", referencedColumnName = "id_category_article", nullable = false)
    public CategoryArticle getCategoryArticle() {
        return categoryArticle;
    }

    public void setCategoryArticle(CategoryArticle categoryArticle) {
        this.categoryArticle = categoryArticle;
    }

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "id_size", referencedColumnName = "id_size", nullable = false)
    public Size getSize() {
        return size;
    }

    public void setSize(Size size) {
        this.size = size;
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
        if (!(o instanceof Article)) return false;
        Article article = (Article) o;
        return Objects.equal(id, article.id) &&
                Objects.equal(name, article.name) &&
                Objects.equal(sku, article.sku) &&
                Objects.equal(description, article.description) &&
                Objects.equal(cost, article.cost) &&
                Objects.equal(price, article.price) &&
                status == article.status &&
                unity == article.unity &&
                Objects.equal(categoryArticle, article.categoryArticle) &&
                Objects.equal(size, article.size) &&
                Objects.equal(father, article.father);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id, name, sku, description, cost, price, status, unity, categoryArticle, size, father);
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("id", id)
                .add("name", name)
                .add("sku", sku)
                .add("description", description)
                .add("cost", cost)
                .add("price", price)
                .add("status", status)
                .add("unity", unity)
                .add("categoryArticle", categoryArticle)
                .add("size", size)
                .add("father", father)
                .toString();
    }
}
