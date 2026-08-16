package com.formula1.model;

import java.io.Serializable;
import java.util.Objects;

/**
 * Representa a un piloto oficial de Fórmula 1 (POJO).
 * Aplica Encapsulamiento con atributos privados y métodos de acceso.
 */
public class Piloto implements Serializable {
    private static final long serialVersionUID = 1L;

    private int id;             // Identificador único (1-20)
    private String nombre;      // Nombre completo del piloto
    private String equipo;      // Escudería a la que pertenece
    private String rol;         // "Líder" o "Escudero"
    private int experiencia;    // Ponderación de experiencia (1 a 100)
    private int habilidad;      // Destreza de conducción (1 a 100)

    public Piloto() {
    }

    // Constructor con asignación de estadísticas por defecto según rol
    public Piloto(int id, String nombre, String equipo, String rol) {
        this.id = id;
        this.nombre = nombre;
        this.equipo = equipo;
        this.rol = rol;
        if ("Líder".equalsIgnoreCase(rol)) {
            this.experiencia = 92;
            this.habilidad = 94;
        } else {
            this.experiencia = 85;
            this.habilidad = 87;
        }
    }

    // Constructor completo con métricas personalizadas
    public Piloto(int id, String nombre, String equipo, String rol, int experiencia, int habilidad) {
        this.id = id;
        this.nombre = nombre;
        this.equipo = equipo;
        this.rol = rol;
        this.experiencia = experiencia;
        this.habilidad = habilidad;
    }

    // Getters y Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getEquipo() { return equipo; }
    public void setEquipo(String equipo) { this.equipo = equipo; }

    public String getRol() { return rol; }
    public void setRol(String rol) { this.rol = rol; }

    public int getExperiencia() { return experiencia; }
    public void setExperiencia(int experiencia) { this.experiencia = experiencia; }

    public int getHabilidad() { return habilidad; }
    public void setHabilidad(int habilidad) { this.habilidad = habilidad; }

    // Comparación basada en ID único
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Piloto piloto = (Piloto) o;
        return id == piloto.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return String.format("[#%02d] %-20s | Equipo: %-22s | Rol: %-8s | Hab: %2d%% | Exp: %2d%%",
                id, nombre, equipo, rol, habilidad, experiencia);
    }
}
