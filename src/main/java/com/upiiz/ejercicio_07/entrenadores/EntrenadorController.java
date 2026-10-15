package com.upiiz.ejercicio_07.entrenadores;

import com.upiiz.ejercicio_07.entrenadores.dto.EntrenadorDto;
import com.upiiz.ejercicio_07.entrenadores.dto.NuevoEntrenadorDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@RestController
@RequestMapping("/api/v1/entrenadores")
@Tag(name = "Entrenadores", description = "Endpoints para la gestión de entrenadores con HATEOAS")
public class EntrenadorController {
    @Autowired
    private EntrenadorService entrenadorService;

    // 1. Listar todos (GET)
    @Operation(summary = "Listar todos los entrenadores", description = "Retorna la lista completa de entrenadores con sus respectivos enlaces HATEOAS.")
    @ApiResponse(responseCode = "200", description = "Lista obtenida exitosamente")
    @GetMapping
    public ResponseEntity<List<EntrenadorDto>> listarEntrenadores() {
        List<EntrenadorDto> entrenadores = entrenadorService.findAll();

        // Aplicar enlaces HATEOAS a cada elemento de la lista
        for (EntrenadorDto entrenador : entrenadores) {
            long id = entrenador.getId();
            entrenador.add(linkTo(methodOn(EntrenadorController.class).obtenerEntrenadorPorId(id)).withSelfRel());
            entrenador.add(linkTo(methodOn(EntrenadorController.class).eliminarEntrenador(id)).withRel("eliminar"));
        }

        return ResponseEntity.ok(entrenadores);
    }

    // 2. Obtener por ID (GET por ID) - Aquí agregamos el Nivel 3 completo
    @Operation(summary = "Obtener un entrenador por ID", description = "Busca un entrenador específico por su ID y devuelve sus datos junto con el Nivel 3 de Richardson (HATEOAS).")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Entrenador encontrado"),
            @ApiResponse(responseCode = "404", description = "Entrenador no encontrado")
    })
    @GetMapping("/{id}")
    public ResponseEntity<EntrenadorDto> obtenerEntrenadorPorId(@PathVariable Long id) {
        EntrenadorDto entrenador = entrenadorService.findById(id);

        if (entrenador != null) {
            // Enlaces HATEOAS detallados (Nivel 3 de Richardson)
            entrenador.add(linkTo(methodOn(EntrenadorController.class).obtenerEntrenadorPorId(id)).withSelfRel());
            entrenador.add(linkTo(methodOn(EntrenadorController.class).listarEntrenadores()).withRel("entrenadores-lista"));
            entrenador.add(linkTo(methodOn(EntrenadorController.class).actualizarEntrenador(id, null)).withRel("actualizar"));
            entrenador.add(linkTo(methodOn(EntrenadorController.class).eliminarEntrenador(id)).withRel("eliminar"));

            return ResponseEntity.ok(entrenador);
        }
        return ResponseEntity.notFound().build();
    }

    // 3. Crear (POST)
    @Operation(summary = "Crear un nuevo entrenador", description = "Registra un nuevo entrenador validando que el correo no esté duplicado.")
    @ApiResponse(responseCode = "201", description = "Entrenador creado exitosamente")
    @PostMapping
    public ResponseEntity<EntrenadorDto> crearEntrenador(@RequestBody NuevoEntrenadorDto nuevoEntrenadorDto) {
        EntrenadorDto nuevoEntrenador = entrenadorService.save(nuevoEntrenadorDto);

        // Agregar enlace self al recurso recién creado
        nuevoEntrenador.add(linkTo(methodOn(EntrenadorController.class).obtenerEntrenadorPorId(nuevoEntrenador.getId())).withSelfRel());

        return ResponseEntity.status(201).body(nuevoEntrenador);
    }

    // 4. Actualizar (PUT)
    @Operation(summary = "Actualizar un entrenador", description = "Modifica los datos de un entrenador existente usando su ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Entrenador actualizado con éxito"),
            @ApiResponse(responseCode = "404", description = "Entrenador no encontrado")
    })
    @PutMapping("/{id}")
    public ResponseEntity<EntrenadorDto> actualizarEntrenador(@PathVariable Long id, @RequestBody NuevoEntrenadorDto updateEntrenadorDto) {
        EntrenadorDto entrenadorActualizado = entrenadorService.update(id, updateEntrenadorDto);

        if (entrenadorActualizado != null) {
            // Agregar HATEOAS a la respuesta de actualización
            entrenadorActualizado.add(linkTo(methodOn(EntrenadorController.class).obtenerEntrenadorPorId(id)).withSelfRel());
            return ResponseEntity.ok(entrenadorActualizado);
        }
        return ResponseEntity.notFound().build();
    }

    // 5. Eliminar (DELETE)
    @Operation(summary = "Eliminar un entrenador", description = "Da de baja a un entrenador del sistema utilizando su ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Eliminado correctamente"),
            @ApiResponse(responseCode = "404", description = "Entrenador no encontrado")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarEntrenador(@PathVariable Long id) {
        try {
            entrenadorService.delete(id);
            return ResponseEntity.noContent().build(); // Código 204 No Content
        } catch (Exception e) {
            return ResponseEntity.notFound().build(); // Si no existe o falla al borrar
        }
    }

}
