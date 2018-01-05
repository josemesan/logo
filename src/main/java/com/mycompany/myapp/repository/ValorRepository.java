package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.Valor;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the Valor entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ValorRepository extends JpaRepository<Valor, Long> {

}
