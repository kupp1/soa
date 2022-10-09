package ru.ifmo.se.common.repository;

import org.springframework.data.repository.CrudRepository;
import ru.ifmo.se.common.entity.House;

public interface HouseRepository extends CrudRepository <House, Long> {
}
