package com.vaadin.training.productdb.backend.data.entity;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.OptimisticLocking;
import org.hibernate.annotations.SelectBeforeUpdate;
import org.hibernate.annotations.OptimisticLockType;

/*
 * @OptimisticLocking(type = OptimisticLockType.DIRTY)
 * Versionless optimistic locking. Performs optimistic locking based on dirty fields as part of an expanded 
 * WHERE clause restriction for the UPDATE/DELETE SQL statements. Only the database columns that have been changed are used in the WHERE clause.
 * When using OptimisticLockType.DIRTY, you should also use @DynamicUpdate because the UPDATE statement must take into consideration all the dirty
 * entity property values, and also the @SelectBeforeUpdate annotation so that detached entities are properly handled by
 * the Session#update(entity) operation.
 */

@SuppressWarnings("serial")
@Entity
// For PostgreSQL table name is always in lowercase letters
@Table(name = "PRODUCTS", schema = "SCOTT")
//@Table(name = "PRODUCTS") // AWS deploying
@OptimisticLocking(type = OptimisticLockType.DIRTY)
@DynamicUpdate(value=true)
@SelectBeforeUpdate(value=true)
public class Product extends AbstractEntity {
	/*
	 * @Id Specifies the primary key of an entity. The field or property to which
	 * the Id annotation is applied should be one of the following types: any Java
	 * primitive type; any primitive wrapper type; String; java.util.Date;
	 * java.sql.Date; java.math.BigDecimal; java.math.BigInteger. The mapped column
	 * for the primary key of the entity is assumed to be the primary key of the
	 * primary table. If no Column annotation is specified, the primary key column
	 * name is assumed to be the name of the primary key property or field.
	 * 
	 * @GeneratedValue Provides for the specification of generation strategies for
	 * the values of primary keys. The GeneratedValue annotation may be applied to a
	 * primary key property or field of an entity or mapped superclass in
	 * conjunction with the Id annotation. The use of the GeneratedValue annotation
	 * is only required to be supported for simple primary keys.
	 * 
	 * An example @GeneratedValue(strategy=GenerationType.SEQUENCE,
	 * generator="CUST_SEQ")
	 * 
	 * @Column Is used to specify the mapped column for a persistent property or
	 * field. If no Column annotation is specified, the default values apply.
	 */
	@Id
	// http://docs.jboss.org/hibernate/orm/5.2/userguide/html_single/Hibernate_User_Guide.html#identifiers-generators
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "product-seq-generator")
	@SequenceGenerator(name = "product-seq-generator", sequenceName = "PRODUCT_SEQ", allocationSize = 1)
	@Column(name = "ID")
	private Integer id;

	@NotBlank(message = "{productdb.name.required}")
	@Size(min = 3, max = 128, message = "{productdb.name.limits}")
	@Column(name = "NAME", unique = true)
	private String name;

	@NotNull(message = "{productdb.quantity.required}")
	@Min(value = 1, message = "{productdb.quantity.limits}")
	@Max(value = 10000, message = "{productdb.quantity.limits}")
	@Column(name = "QUANTITY")
	private Integer quantity;

	@NotNull
	@Column(name = "PRICE")
	private BigDecimal price;

	@Column(name = "MANUFACTURED_DATE")
	private LocalDate manufacturedDate;

	@PrePersist
	@PreUpdate
	private void prepareData() {
		this.name = name.toUpperCase();
	}

	public Integer getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Integer getQuantity() {
		return quantity;
	}

	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
	}

	public BigDecimal getPrice() {
		return price;
	}

	public void setPrice(BigDecimal price) {
		// it is price, only two digits after comma!
		this.price = price.setScale(2, RoundingMode.CEILING);
	}

	public LocalDate getManufacturedDate() {
		return manufacturedDate;
	}

	public void setManufacturedDate(LocalDate manufacturedDate) {
		this.manufacturedDate = manufacturedDate;
	}
	
	@Override
	public String toString() {
		return name;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		
		if (o == null || getClass() != o.getClass()) {
			return false;
		}
		
		if (!super.equals(o)) {
			return false;
		}
		
		Product that = (Product) o;
		return Objects.equals(id, that.id);
	}

	@Override
	public int hashCode() {
		return Objects.hash(super.hashCode(), id);
	}
	
}