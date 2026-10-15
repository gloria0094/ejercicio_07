package com.upiiz.ejercicio_07.entrenadores.dto;

import io.swagger.v3.oas.annotations.media.Schema;

public class NuevoEntrenadorDto {
    @Schema(description = "Nombre completo del entrenador", example = "Carlos Pérez")
    private String nombre;
    @Schema(description = "Especialidad deportiva", example = "Natación")
    private String especialidad;
    @Schema(description = "Años de experiencia", example = "5")
    private String experiencia;
    @Schema(description = "Correo electrónico único", example = "carlos@email.com")
    private String correo;

    public NuevoEntrenadorDto(String nombre, String especialidad, String experiencia, String correo) {
        this.nombre = nombre;
        this.especialidad = especialidad;
        this.experiencia = experiencia;
        this.correo = correo;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getEspecialidad() {
        return especialidad;
    }

    public void setEspecialidad(String especialidad) {
        this.especialidad = especialidad;
    }

    public String getExperiencia() {
        return experiencia;
    }

    public void setExperiencia(String experiencia) {
        this.experiencia = experiencia;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }
}
