package ru.ifmo.se.common.repository;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;
import ru.ifmo.se.common.entity.Flat;

import java.util.Optional;

public interface FlatRepository extends PagingAndSortingRepository<Flat, Long>, JpaSpecificationExecutor<Flat> {
   Long countById(Long id);
   Optional<Flat> findFirstByOrderByArea();
}
