package com.example.lab9_base.Bean;

import java.sql.Date;

public class Partido {
    private int idPartido;
    private Date fecha;
    private int numeroJornada;
    private Seleccion seleccionLocal;
    private Seleccion seleccionVisitante;
    private Arbitro arbitro;

    public int getIdPartido() {
        return idPartido;
    }

    public void setIdPartido(int idPartido) {
        this.idPartido = idPartido;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public int getNumeroJornada() {
        return numeroJornada;
    }

    public void setNumeroJornada(int numeroJornada) {
        this.numeroJornada = numeroJornada;
    }

    public Seleccion getSeleccionLocal() {
        return seleccionLocal;
    }

    public void setSeleccionLocal(Seleccion seleccionLocal) {
        this.seleccionLocal = seleccionLocal;
    }

    public Seleccion getSeleccionVisitante() {
        return seleccionVisitante;
    }

    public void setSeleccionVisitante(Seleccion seleccionVisitante) {
        this.seleccionVisitante = seleccionVisitante;
    }

    public Arbitro getArbitro() {
        return arbitro;
    }

    public void setArbitro(Arbitro arbitro) {
        this.arbitro = arbitro;
    }
}
