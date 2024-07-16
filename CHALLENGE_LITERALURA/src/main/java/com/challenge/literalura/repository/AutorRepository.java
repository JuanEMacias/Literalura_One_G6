package com.challenge.literalura.repository;

import com.challenge.literalura.models.Autor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface AutorRepository extends JpaRepository<Autor, Long> {
    Optional<Autor> findByNombre(String nombre);

    @Query("SELECT a FROM Autor a WHERE a.AnioMuerte >= :anio AND a.AnioNacimiento <= :anio")
    List<Autor> getAliveAuthors(Object year);

    @Query("SELECT a FROM Autor a WHERE a.nombre ILIKE %:apellido%")
    List<Autor> findAutorByName(String apellido);

    List<Autor> getAliveAuthors(Object year);
}
