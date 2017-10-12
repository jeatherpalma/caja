package com.worknest.repository;

import com.worknest.domain.HistorialAcciones;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the HistorialAcciones entity.
 */
@SuppressWarnings("unused")
@Repository
public interface HistorialAccionesRepository extends JpaRepository<HistorialAcciones, Long> {

}
