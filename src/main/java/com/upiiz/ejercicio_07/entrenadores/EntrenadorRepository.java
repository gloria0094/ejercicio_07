package com.upiiz.ejercicio_07.entrenadores;

import com.upiiz.ejercicio_07.entrenadores.entities.EntrenadorEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface EntrenadorRepository extends JpaRepository<EntrenadorEntity, Long> {
    Optional<EntrenadorEntity> findByNombreIgnoreCase(String nombre);
    Optional<EntrenadorEntity> findByCorreoIgnoreCase(String correo);
}

