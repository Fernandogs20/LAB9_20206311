package com.example.lab9_base.Dao;

import com.example.lab9_base.Bean.Arbitro;
import com.example.lab9_base.Bean.Estadio;
import com.example.lab9_base.Bean.Partido;
import com.example.lab9_base.Bean.Seleccion;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

public class DaoPartidos extends DaoBase{
    public ArrayList<Partido> listaDePartidos() {

        ArrayList<Partido> partidos = new ArrayList<>();

        String sql = "SELECT " +
                "p.numeroJornada AS Jornada, " +
                "p.fecha AS Fecha, " +
                "sl.idSeleccion AS idSeleccionLocal, sl.nombre AS SeleccionLocal, " +
                "sv.idSeleccion AS idSeleccionVisitante, sv.nombre AS SeleccionVisitante, " +
                "e.idEstadio AS idEstadio, e.nombre AS Estadio, " +
                "a.idArbitro AS idArbitro, a.nombre AS Arbitro " +
                "FROM lab9.partido p " +
                "JOIN lab9.seleccion sl ON p.seleccionLocal = sl.idSeleccion " +
                "JOIN lab9.seleccion sv ON p.seleccionVisitante = sv.idSeleccion " +
                "JOIN lab9.estadio e ON sl.estadio_idEstadio = e.idEstadio " +
                "JOIN lab9.arbitro a ON p.arbitro = a.idArbitro;";

        try (Connection conn = this.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                Partido partido = new Partido();
                partido.setNumeroJornada(rs.getInt("Jornada"));
                partido.setFecha(rs.getDate("Fecha"));

                Seleccion seleccionLocal = new Seleccion();
                seleccionLocal.setIdSeleccion(rs.getInt("idSeleccionLocal"));
                seleccionLocal.setNombre(rs.getString("SeleccionLocal"));
                partido.setSeleccionLocal(seleccionLocal);

                Seleccion seleccionVisitante = new Seleccion();
                seleccionVisitante.setIdSeleccion(rs.getInt("idSeleccionVisitante"));
                seleccionVisitante.setNombre(rs.getString("SeleccionVisitante"));
                partido.setSeleccionVisitante(seleccionVisitante);

                Estadio estadio = new Estadio();
                estadio.setIdEstadio(rs.getInt("idEstadio"));
                estadio.setNombre(rs.getString("Estadio"));
                seleccionLocal.setEstadio(estadio); // Asignar el estadio a seleccionLocal

                Arbitro arbitro = new Arbitro();
                arbitro.setIdArbitro(rs.getInt("idArbitro"));
                arbitro.setNombre(rs.getString("Arbitro"));
                partido.setArbitro(arbitro);

                partidos.add(partido);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return partidos;
    }

    public void crearPartido(Partido partido) {
        String sql = "INSERT INTO partido (seleccionLocal, seleccionVisitante, arbitro, fecha, numeroJornada) VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = this.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, partido.getSeleccionLocal().getIdSeleccion());
            pstmt.setInt(2, partido.getSeleccionVisitante().getIdSeleccion());
            pstmt.setInt(3, partido.getArbitro().getIdArbitro());
            pstmt.setDate(4, new java.sql.Date(partido.getFecha().getTime()));
            pstmt.setInt(5, partido.getNumeroJornada());

            pstmt.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public boolean existePartido(Partido partido) {
        String sql = "SELECT COUNT(*) FROM partido WHERE seleccionLocal = ? AND seleccionVisitante = ? AND fecha = ? AND numeroJornada = ?";

        try (Connection conn = this.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, partido.getSeleccionLocal().getIdSeleccion());
            pstmt.setInt(2, partido.getSeleccionVisitante().getIdSeleccion());
            pstmt.setDate(3, new java.sql.Date(partido.getFecha().getTime()));
            pstmt.setInt(4, partido.getNumeroJornada());

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next() && rs.getInt(1) > 0) {
                    return true;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

}
