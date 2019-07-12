package com.vaadin.training.productdb.backend.service;

import javax.persistence.EntityNotFoundException;

import org.springframework.data.jpa.repository.JpaRepository;

import com.vaadin.training.productdb.backend.data.entity.AbstractEntity;

public interface CrudService<T extends AbstractEntity> {
	
	JpaRepository<T, Integer> getRepository();
	
	default T save(T entity) {
		return this.getRepository().saveAndFlush(entity);
	}
	
	default void delete(T entity) {
		if (entity == null) {
			throw new EntityNotFoundException();
		}
		getRepository().delete(entity);
	}
	
	default void delete(int id) {
		delete(load(id));
	}
	
	default long count() {
		return this.getRepository().count();
	}
	
	default T load(int id) {
		T entity = this.getRepository().findById(id).orElse(null);
		if (entity == null) {
			throw new EntityNotFoundException();
		}
		return entity;
	}
	
	T createNew();
}