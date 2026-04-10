package com.finanscepte.common.service;

import java.util.List;

public interface GenericService<T, ID> {
    T create(T entity);
    T getById(ID id);
    List<T> getAll();
    void delete(ID id);
}
