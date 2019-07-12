package com.vaadin.training.productdb.backend.data.entity;

import java.io.Serializable;
import java.util.Objects;

import javax.persistence.MappedSuperclass;

/*
 * @MappedSuperclass
 * Designates a class whose mapping information is applied to the entities that inherit from it. A mapped superclass has no separate table defined for it.
 * A class designated with the MappedSuperclass annotation can be mapped in the same way as an entity except that the mappings will apply only to its subclasses 
 * since no table exists for the mapped superclass itself. 
 * When applied to the subclasses the inherited mappings will apply in the context of the subclass tables. 
 * Mapping information may be overridden in such subclasses by using the AttributeOverride and AssociationOverride annotations or corresponding XML elements.
 */
@SuppressWarnings("serial")
@MappedSuperclass
public abstract class AbstractEntity implements Serializable {
	// returns primary key
	public abstract Integer getId();

	@Override
	public int hashCode() {
		return Objects.hash(getId());
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}
		AbstractEntity that = (AbstractEntity) o;
		return Objects.equals(getId(), that.getId());
	}

}