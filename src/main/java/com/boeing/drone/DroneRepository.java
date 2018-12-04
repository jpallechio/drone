package com.boeing.drone;

import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface DroneRepository extends CrudRepository<Drone, Long> {
    List<Drone> findAll();
}
