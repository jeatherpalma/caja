package com.worknest.repository;

import com.worknest.domain.Cajeros;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the Cajeros entity.
 */
@SuppressWarnings("unused")
@Repository
public interface CajerosRepository extends JpaRepository<Cajeros, Long> {

}
