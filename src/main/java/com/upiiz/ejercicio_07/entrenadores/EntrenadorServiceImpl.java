package com.upiiz.ejercicio_07.entrenadores;

import com.upiiz.ejercicio_07.entrenadores.dto.EntrenadorDto;
import com.upiiz.ejercicio_07.entrenadores.dto.NuevoEntrenadorDto;
import com.upiiz.ejercicio_07.entrenadores.entities.EntrenadorEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class EntrenadorServiceImpl implements EntrenadorService {

    @Autowired
    private EntrenadorRepository entrenadorRepository;

    @Override
    public EntrenadorDto save(NuevoEntrenadorDto nuevoEntrenadorDto) {
        // Regla de negocio: Validar que el correo no esté duplicado (ignorando mayúsculas/minúsculas)
        entrenadorRepository.findByCorreoIgnoreCase(nuevoEntrenadorDto.getCorreo()).ifPresent(e -> {
            throw new IllegalArgumentException("El correo ya existe en el sistema");
        });

        EntrenadorEntity nuevoEntrenador = new EntrenadorEntity(
                nuevoEntrenadorDto.getNombre(),
                nuevoEntrenadorDto.getEspecialidad(),
                nuevoEntrenadorDto.getExperiencia(),
                nuevoEntrenadorDto.getCorreo()
        );

        EntrenadorEntity entrenadorGuardado = entrenadorRepository.save(nuevoEntrenador);

        return new EntrenadorDto(
                entrenadorGuardado.getId(),
                entrenadorGuardado.getNombre(),
                entrenadorGuardado.getEspecialidad(),
                entrenadorGuardado.getExperiencia(),
                entrenadorGuardado.getCorreo()
        );
    }

    @Override
    public List<EntrenadorDto> findAll() {
        List<EntrenadorEntity> entidades = entrenadorRepository.findAll();
        // Convertir lista de Entidades a lista de DTOs
        return entidades.stream().map(e -> new EntrenadorDto(
                e.getId(),
                e.getNombre(),
                e.getEspecialidad(),
                e.getExperiencia(),
                e.getCorreo()
        )).collect(Collectors.toList());
    }

    @Override
    public EntrenadorDto findById(Long id) {
        Optional<EntrenadorEntity> entrenadorOpt = entrenadorRepository.findById(id);
        if (entrenadorOpt.isPresent()) {
            EntrenadorEntity e = entrenadorOpt.get();
            return new EntrenadorDto(
                    e.getId(),
                    e.getNombre(),
                    e.getEspecialidad(),
                    e.getExperiencia(),
                    e.getCorreo()
            );
        }
        return null; // O puedes lanzar una excepción personalizada si no existe
    }

    @Override
    public EntrenadorDto update(Long id, NuevoEntrenadorDto updateEntrenadorDto) {
        Optional<EntrenadorEntity> entrenadorOpt = entrenadorRepository.findById(id);
        if (entrenadorOpt.isPresent()) {
            EntrenadorEntity entrenador = entrenadorOpt.get();
            entrenador.setNombre(updateEntrenadorDto.getNombre());
            entrenador.setEspecialidad(updateEntrenadorDto.getEspecialidad());
            entrenador.setExperiencia(updateEntrenadorDto.getExperiencia());
            entrenador.setCorreo(updateEntrenadorDto.getCorreo());

            EntrenadorEntity actualizado = entrenadorRepository.save(entrenador);
            return new EntrenadorDto(
                    actualizado.getId(),
                    actualizado.getNombre(),
                    actualizado.getEspecialidad(),
                    actualizado.getExperiencia(),
                    actualizado.getCorreo()
            );
        }
        return null;
    }

    @Override
    public void delete(Long id) {
        if (entrenadorRepository.existsById(id)) {
            entrenadorRepository.deleteById(id);
        } else {
            throw new IllegalArgumentException("El entrenador con ID " + id + " no existe");
        }
    }
}