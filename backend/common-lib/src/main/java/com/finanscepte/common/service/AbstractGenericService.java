package com.finanscepte.common.service;

import java.util.List;
import org.springframework.data.repository.CrudRepository;

public abstract class AbstractGenericService<T, ID> implements GenericService<T, ID> {

    protected abstract CrudRepository<T, ID> getRepository();
    protected abstract String getEntityName();

    @Override
    public T create(T entity) {
        return getRepository().save(entity);
    }

    @Override
    public T getById(ID id) {
        return getRepository().findById(id)
                .orElseThrow(() -> new RuntimeException(getEntityName() + " not found: " + id));
    }

    @Override
    public List<T> getAll() {
        return (List<T>) getRepository().findAll();
    }

    @Override
    public void delete(ID id) {
        getRepository().deleteById(id);
    }
}
