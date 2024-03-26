package fsa.training.ems_springboot.service;

import fsa.training.ems_springboot.model.entity.BaseEntity;

public interface BaseService<T extends BaseEntity, ID> {
    T save(T entity);

    T getById(ID id);

    void deleteById(ID id);
}
