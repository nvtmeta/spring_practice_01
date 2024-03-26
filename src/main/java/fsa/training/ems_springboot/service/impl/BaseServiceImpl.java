package fsa.training.ems_springboot.service.impl;

import fsa.training.ems_springboot.model.entity.BaseEntity;
import fsa.training.ems_springboot.service.BaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.CrudRepository;

public class BaseServiceImpl<T extends BaseEntity, ID, R extends CrudRepository<T, ID>>
        implements BaseService<T, ID> {

    @Autowired
    private R repository;

    @Override
    public T save(T t) {
        t.setDeleted(false);
        return null;
    }

    @Override
    public T getById(ID id) {
        return null;
    }

    @Override
    public void deleteById(ID id) {

    }
}
