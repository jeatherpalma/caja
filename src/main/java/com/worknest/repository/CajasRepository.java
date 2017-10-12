package com.worknest.repository;

import com.worknest.domain.Cajas;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the Cajas entity.
 */
@SuppressWarnings("unused")
@Repository
public interface CajasRepository extends JpaRepository<Cajas, Long> {

}
