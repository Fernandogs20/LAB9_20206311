package com.example.lab9_base.Controller;

import com.example.lab9_base.Bean.Arbitro;
import com.example.lab9_base.Dao.DaoArbitros;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;

import java.io.IOException;
import java.util.ArrayList;

@WebServlet(name = "ArbitroServlet", urlPatterns = {"/ArbitroServlet"})
public class ArbitroServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action") == null ? "lista" : request.getParameter("action");
        RequestDispatcher view;
        ArrayList<String> opciones = new ArrayList<>();
        opciones.add("nombre");
        opciones.add("pais");

        switch (action) {

            case "buscar":
                String tipo = request.getParameter("tipo");
                String valorBusqueda = request.getParameter("buscar");
                ArrayList<Arbitro> listaArbitros = new ArrayList<>();
                DaoArbitros daoArbitro = new DaoArbitros();


                if ("nombre".equals(tipo)) {
                    listaArbitros = daoArbitro.busquedaNombre(valorBusqueda);
                } else if ("pais".equals(tipo)) {
                    listaArbitros = daoArbitro.busquedaPais(valorBusqueda);
                }

                request.setAttribute("listaArbitros", listaArbitros);

                view = request.getRequestDispatcher("/arbitros/list.jsp");
                view.forward(request, response);
                break;

            case "guardar":
                String nombre = request.getParameter("nombre");
                String pais = request.getParameter("pais");

                if (nombre == null || nombre.trim().isEmpty() || pais == null || pais.trim().isEmpty()) {
                    request.setAttribute("error", "Todos los campos son obligatorios.");
                    view = request.getRequestDispatcher("/arbitros/form.jsp");
                    view.forward(request, response);
                    return;
                }

                DaoArbitros daoArbitros = new DaoArbitros();

                ArrayList<Arbitro> arbitrosConMismoNombre = daoArbitros.busquedaNombre(nombre);
                if (!arbitrosConMismoNombre.isEmpty()) {
                    request.setAttribute("error", "El nombre del árbitro ya existe.");
                    view = request.getRequestDispatcher("/arbitros/form.jsp");
                    view.forward(request, response);
                    return;
                }

                Arbitro nuevoArbitro = new Arbitro();
                nuevoArbitro.setNombre(nombre);
                nuevoArbitro.setPais(pais);

                daoArbitros.crearArbitro(nuevoArbitro);

                response.sendRedirect(request.getContextPath() + "/ArbitroServlet?action=lista");
                break;

        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        String action = request.getParameter("action") == null ? "lista" : request.getParameter("action");
        RequestDispatcher view;
        ArrayList<String> paises = new ArrayList<>();
        paises.add("Peru");
        paises.add("Chile");
        paises.add("Argentina");
        paises.add("Paraguay");
        paises.add("Uruguay");
        paises.add("Colombia");
        ArrayList<String> opciones = new ArrayList<>();
        opciones.add("nombre");
        opciones.add("pais");

        DaoArbitros daoArbitro = new DaoArbitros();
        switch (action) {
            case "lista":

                ArrayList<Arbitro> listaArbitros = daoArbitro.listarArbitros();

                request.setAttribute("listaArbitros", listaArbitros);
                request.setAttribute("paises", paises);
                request.setAttribute("opciones", opciones);
                view = request.getRequestDispatcher("/arbitros/list.jsp");
                view.forward(request, response);
                break;
            case "crear":
                request.setAttribute("paises", paises);
                view = request.getRequestDispatcher("/arbitros/form.jsp");
                view.forward(request, response);
                break;
            case "borrar":

                int idArbitro = Integer.parseInt(request.getParameter("id"));

                Arbitro arbitro = daoArbitro.buscarArbitro(idArbitro);  // Método para verificar existencia

                if (arbitro != null) {
                    daoArbitro.borrarArbitro(idArbitro);
                    response.sendRedirect(request.getContextPath() + "/ArbitroServlet?action=lista");
                } else {
                    request.setAttribute("error", "El árbitro no existe.");
                    RequestDispatcher viewError = request.getRequestDispatcher("/arbitros/list.jsp");
                    viewError.forward(request, response);
                }
                break;
        }
    }
}
