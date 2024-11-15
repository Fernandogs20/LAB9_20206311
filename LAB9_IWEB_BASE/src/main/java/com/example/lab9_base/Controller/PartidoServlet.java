package com.example.lab9_base.Controller;

import com.example.lab9_base.Bean.Arbitro;
import com.example.lab9_base.Bean.Partido;
import com.example.lab9_base.Bean.Seleccion;
import com.example.lab9_base.Dao.DaoArbitros;
import com.example.lab9_base.Dao.DaoPartidos;
import com.example.lab9_base.Dao.DaoSelecciones;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;

import java.util.ArrayList;

import java.io.IOException;
import java.sql.Date;

@WebServlet(name = "PartidoServlet", urlPatterns = {"/PartidoServlet"})
public class PartidoServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action") == null ? "guardar" : request.getParameter("action");
        RequestDispatcher view;

        switch (action) {

            case "guardar":
                try {
                    int idSeleccionLocal = Integer.parseInt(request.getParameter("seleccionLocal"));
                    int idSeleccionVisitante = Integer.parseInt(request.getParameter("seleccionVisitante"));
                    int idArbitro = Integer.parseInt(request.getParameter("arbitro"));
                    Date fecha = Date.valueOf(request.getParameter("fecha"));
                    int numeroJornada = Integer.parseInt(request.getParameter("jornada"));

                    if (idSeleccionLocal == idSeleccionVisitante) {
                        request.setAttribute("error", "La selección local no puede ser igual a la selección visitante.");
                        view = request.getRequestDispatcher("/partidos/form_partido.jsp");
                        view.forward(request, response);
                        return;
                    }

                    Partido partido = new Partido();
                    Seleccion seleccionLocal = new Seleccion();
                    seleccionLocal.setIdSeleccion(idSeleccionLocal);
                    partido.setSeleccionLocal(seleccionLocal);

                    Seleccion seleccionVisitante = new Seleccion();
                    seleccionVisitante.setIdSeleccion(idSeleccionVisitante);
                    partido.setSeleccionVisitante(seleccionVisitante);

                    Arbitro arbitro = new Arbitro();
                    arbitro.setIdArbitro(idArbitro);
                    partido.setArbitro(arbitro);

                    partido.setFecha(fecha);
                    partido.setNumeroJornada(numeroJornada);

                    DaoPartidos daoPartidos = new DaoPartidos();
                    if (daoPartidos.existePartido(partido)) {
                        request.setAttribute("error", "Este partido ya ha sido registrado.");
                        view = request.getRequestDispatcher("/partidos/form_partido.jsp");
                        view.forward(request, response);
                        return;
                    }

                    // Si todo es válido, registrar el partido
                    daoPartidos.crearPartido(partido);

                    response.sendRedirect("PartidoServlet?action=lista");

                } catch (Exception e) {
                    request.setAttribute("error", "Todos los campos son obligatorios.");
                    view = request.getRequestDispatcher("/partidos/form_partido.jsp");
                    view.forward(request, response);
                }
                break;

        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        String action = request.getParameter("action") == null ? "lista" : request.getParameter("action");
        RequestDispatcher view;
        switch (action) {
            case "lista":
                DaoPartidos daoPartidos = new DaoPartidos();
                ArrayList<Partido> listaPartidos = daoPartidos.listaDePartidos();
                request.setAttribute("listaPartidos", listaPartidos);
                view = request.getRequestDispatcher("index.jsp");
                view.forward(request, response);
                break;
            case "crear":
                DaoSelecciones daoSelecciones = new DaoSelecciones();
                DaoArbitros daoArbitros = new DaoArbitros();

                ArrayList<Seleccion> selecciones = daoSelecciones.listarSelecciones();
                ArrayList<Arbitro> arbitros = daoArbitros.listarArbitros();

                request.setAttribute("selecciones", selecciones);
                request.setAttribute("arbitros", arbitros);
                view = request.getRequestDispatcher("/partidos/form_partido.jsp");
                view.forward(request, response);
                break;

        }

    }
}
