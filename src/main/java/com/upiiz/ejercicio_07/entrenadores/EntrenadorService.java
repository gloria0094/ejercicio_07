package com.upiiz.ejercicio_07.entrenadores;

import com.upiiz.ejercicio_07.entrenadores.dto.EntrenadorDto;
import com.upiiz.ejercicio_07.entrenadores.dto.NuevoEntrenadorDto;

import java.util.List;

public interface EntrenadorService {
    EntrenadorDto save(NuevoEntrenadorDto nuevoEntrenadorDto);
    //Muestre todos
    List<EntrenadorDto> findAll();
    //Muestra por id
    EntrenadorDto findById(Long id);
    //Actualice
    EntrenadorDto update(Long id, NuevoEntrenadorDto updateEntrenadorDto);
    //Eliminar
    void delete(Long id);
}
