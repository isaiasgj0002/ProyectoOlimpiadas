package com.isaias.projectmanager;

public class Proyecto {
    private String nombre, ubicacion, fechainicio, fechafinprev, descripcion;
    public Proyecto(){

    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getUbicacion() {
        return ubicacion;
    }

    public void setUbicacion(String ubicacion) {
        this.ubicacion = ubicacion;
    }

    public String getFechainicio() {
        return fechainicio;
    }

    public void setFechainicio(String fechainicio) {
        this.fechainicio = fechainicio;
    }

    public String getFechafinprev() {
        return fechafinprev;
    }

    public void setFechafinprev(String fechafinprev) {
        this.fechafinprev = fechafinprev;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public Proyecto(String nombre, String ubicacion, String fechainicio, String fechafinprev, String descripcion) {
        this.nombre = nombre;
        this.ubicacion = ubicacion;
        this.fechainicio = fechainicio;
        this.fechafinprev = fechafinprev;
        this.descripcion = descripcion;
    }
}
