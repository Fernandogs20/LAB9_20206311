package com.example.lab9_base.Dao;

import com.example.lab9_base.Bean.Arbitro;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

public class DaoArbitros extends DaoBase {
    public ArrayList<Arbitro> listarArbitros() {
        ArrayList<Arbitro> arbitros = new ArrayList<>();

        String sql = "SELECT idArbitro, nombre, pais FROM lab9.arbitro";
        try (Connection conn = this.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                Arbitro arbitro = new Arbitro();
                arbitro.setIdArbitro(rs.getInt("idArbitro"));
                arbitro.setNombre(rs.getString("nombre"));
                arbitro.setPais(rs.getString("pais"));
                arbitros.add(arbitro);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return arbitros;
    }

    public void crearArbitro(Arbitro arbitro) {

        String sql = "INSERT INTO arbitro (nombre, pais) VALUES (?, ?)";
        try (Connection conn = this.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, arbitro.getNombre());
            pstmt.setString(2, arbitro.getPais());

            pstmt.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public ArrayList<Arbitro> busquedaPais(String pais) {

        ArrayList<Arbitro> arbitros = new ArrayList<>();

        String sql = "SELECT * FROM lab9.arbitro WHERE pais LIKE ?";

        try (Connection conn = this.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, "%" + pais + "%");
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    Arbitro arbitro = new Arbitro();
                    arbitro.setIdArbitro(rs.getInt("idArbitro"));
                    arbitro.setNombre(rs.getString("nombre"));
                    arbitro.setPais(rs.getString("pais"));
                    arbitros.add(arbitro);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return arbitros;
    }

    public ArrayList<Arbitro> busquedaNombre(String nombre) {

        ArrayList<Arbitro> arbitros = new ArrayList<>();
        String sql = "SELECT * FROM lab9.arbitro WHERE nombre LIKE ?";

        try (Connection conn = this.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, "%" + nombre + "%");
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    Arbitro arbitro = new Arbitro();
                    arbitro.setIdArbitro(rs.getInt("idArbitro"));
                    arbitro.setNombre(rs.getString("nombre"));
                    arbitro.setPais(rs.getString("pais"));
                    arbitros.add(arbitro);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return arbitros;
    }

    public Arbitro buscarArbitro(int id) {
        Arbitro arbitro = new Arbitro();
        String sql = "SELECT * FROM lab9.arbitro WHERE idArbitro = ?";

        try (Connection conn = this.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, id);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    arbitro.setIdArbitro(rs.getInt("idArbitro"));
                    arbitro.setNombre(rs.getString("nombre"));
                    arbitro.setPais(rs.getString("pais"));
                } else {
                    arbitro = null;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return arbitro;
    }

    public void borrarArbitro(int id) {
        String sql = "DELETE FROM arbitro WHERE idArbitro = ?";

        try (Connection conn = this.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, id);
            int affectedRows = pstmt.executeUpdate();

            if (affectedRows == 0) {
                System.out.println("No se encontró un árbitro con el ID especificado.");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
