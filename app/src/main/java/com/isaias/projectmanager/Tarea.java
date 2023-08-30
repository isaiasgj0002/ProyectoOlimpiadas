package com.isaias.projectmanager;

public class Tarea {
    public String getTarea() {
        return tarea;
    }

    public void setTarea(String tarea) {
        this.tarea = tarea;
    }

    public String getFecha_inicio() {
        return fecha_inicio;
    }

    public void setFecha_inicio(String fecha_inicio) {
        this.fecha_inicio = fecha_inicio;
    }

    public String getPlazo_ejecucion() {
        return plazo_ejecucion;
    }

    public void setPlazo_ejecucion(String plazo_ejecucion) {
        this.plazo_ejecucion = plazo_ejecucion;
    }

    public String getFinalizada() {
        return finalizada;
    }

    public void setFinalizada(String finalizada) {
        this.finalizada = finalizada;
    }

    private String tarea, fecha_inicio, plazo_ejecucion, finalizada;

    public Tarea(String tarea, String fecha_inicio, String plazo_ejecucion, String finalizada) {
        this.tarea = tarea;
        this.fecha_inicio = fecha_inicio;
        this.plazo_ejecucion = plazo_ejecucion;
        this.finalizada = finalizada;
    }

    public Tarea(){

    }
}
