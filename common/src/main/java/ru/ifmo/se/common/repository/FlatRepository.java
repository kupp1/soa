package ru.ifmo.se.common.repository;

import org.springframework.data.repository.PagingAndSortingRepository;
import ru.ifmo.se.common.Flat;

import java.util.Optional;

public interface FlatRepository extends PagingAndSortingRepository<Flat, Long> {
   Long countById(Long id);
   Optional<Flat> findFirstByOrderByArea();
}
