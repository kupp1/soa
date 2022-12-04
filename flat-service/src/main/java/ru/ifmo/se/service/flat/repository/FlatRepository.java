package ru.ifmo.se.service.flat.repository;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import ru.ifmo.se.common.entity.Flat;

import java.util.Optional;

public interface FlatRepository extends PagingAndSortingRepository<Flat, Long>, JpaSpecificationExecutor<Flat>,
        CrudRepository<Flat, Long> {
   Long countById(Long id);
   Optional<Flat> findFirstByOrderByArea();
}
