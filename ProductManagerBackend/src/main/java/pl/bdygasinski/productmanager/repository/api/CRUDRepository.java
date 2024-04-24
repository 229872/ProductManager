package pl.bdygasinski.productmanager.repository.api;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import pl.bdygasinski.productmanager.model.AbstractEntity;

import java.util.Optional;

interface CRUDRepository <T extends AbstractEntity, ID> {

    Page<T> findAll(Pageable pageable);

    Optional<T> findById(ID id);

    T save(T entity);

    void delete(T entity);
}
