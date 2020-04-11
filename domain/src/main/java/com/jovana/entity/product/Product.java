package com.jovana.entity.product;

import com.jovana.entity.AbstractAuditingEntity;
import com.jovana.entity.product.image.Image;

import javax.persistence.*;
import javax.validation.constraints.DecimalMax;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;
import java.util.Set;

/**
 * Created by jovana on 07.04.2020
 */
@Entity
@Table(name = "product")
public class Product extends AbstractAuditingEntity implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Size(min = 3, max = 30)
    private String name;

    @NotNull
    @Size(min = 3, max = 100)
    private String material;

    @Size(max = 100)
    private String description;

    @NotNull
    @DecimalMin("1.0")
    @DecimalMax("500.0")
    private BigDecimal price;

    @ElementCollection(targetClass = SizeCode.class, fetch = FetchType.EAGER)
    @JoinTable(name = "product_size", joinColumns = @JoinColumn(name = "product_id"))
    @Column(name = "size", nullable = false)
    @Enumerated(EnumType.STRING)
    private Set<SizeCode> sizes;

    @ElementCollection(targetClass = ColorCode.class, fetch = FetchType.EAGER)
    @JoinTable(name = "product_color", joinColumns = @JoinColumn(name = "product_id"))
    @Column(name = "color", nullable = false)
    @Enumerated(EnumType.STRING)
    private Set<ColorCode> colors;

    @OneToOne(mappedBy = "product", cascade = CascadeType.REMOVE)
    private Stock stock;

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "product")
    private List<Image> images;

    public Product() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMaterial() {
        return material;
    }

    public void setMaterial(String material) {
        this.material = material;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public Set<SizeCode> getSizes() {
        return sizes;
    }

    public void setSizes(Set<SizeCode> sizes) {
        this.sizes = sizes;
    }

    public Set<ColorCode> getColors() {
        return colors;
    }

    public void setColors(Set<ColorCode> colors) {
        this.colors = colors;
    }

    public Stock getStock() {
        return stock;
    }

    public void setStock(Long numberOfUnitsInStock) {
        this.stock = new Stock(this, numberOfUnitsInStock);
    }

    public List<Image> getImages() {
        return images;
    }

    public void setImages(List<Image> images) {
        this.images = images;
    }

}
