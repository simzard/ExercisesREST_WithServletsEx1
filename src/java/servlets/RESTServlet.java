/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package servlets;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.Scanner;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author simon
 */
@WebServlet(name = "RESTServlet", urlPatterns = {"/api/quote/*"})
public class RESTServlet extends HttpServlet {

    private final static Random random = new Random();

    private static int nextId = 4;

    private final static Map<Integer, String> quotes = new HashMap() {
        {
            put(1, "Friends are kisses blown to us by angels");
            put(2, "Do not take life too seriously. "
                    + " You will never get out of it alive");
            put(3, "Behind every great man, is a woman rolling her eyes");
        }
    };

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String[] parts = request.getRequestURI().split("/");
        String parameter = null;
        if (parts.length == 5) {
            parameter = parts[4];
        }
        int key;

        if (parameter.equals("random")) {
            // this means we need a random quote returned
            // just change the index key
            key = random.nextInt(nextId); // +1 because of first quote starts at 1

        } else {
            key = Integer.parseInt(parameter);
            //Get the second quote
        }
        JsonObject quote = new JsonObject();
        quote.addProperty("quote", quotes.get(key));
        String jsonResponse = new Gson().toJson(quote);

        response.setContentType("application/json");
        response.getWriter().write(jsonResponse);
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        Scanner jsonScanner = new Scanner(request.getInputStream());
        String json = "";
        while (jsonScanner.hasNext()) {
            json += jsonScanner.nextLine();
        }
        //Get the quote text from the provided Json
        JsonObject newQuote = new JsonParser().parse(json).getAsJsonObject();
        String quote = newQuote.get("quote").getAsString();
        quotes.put(nextId++, quote);

        JsonObject returnJSON = new JsonObject();

        returnJSON.addProperty("id", nextId);
        returnJSON.addProperty("quote", quote);
        String jsonResponse = new Gson().toJson(returnJSON);

        response.setContentType("application/json");
        response.getWriter().write(jsonResponse);

    }

    public void doPut(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // get the id from the REST api
        String[] parts = request.getRequestURI().split("/");
        String parameter = null;
        if (parts.length == 5) {
            parameter = parts[4];
        }

        Scanner jsonScanner = new Scanner(request.getInputStream());
        String json = "";
        while (jsonScanner.hasNext()) {
            json += jsonScanner.nextLine();
        }

        //Get the quote text from the provided Json
        JsonObject newQuote = new JsonParser().parse(json).getAsJsonObject();
        String quote = newQuote.get("quote").getAsString();

        int key = Integer.parseInt(parameter);

        // update the text
        quotes.put(key, quote);

        // return response
        JsonObject returnJSON = new JsonObject();

        returnJSON.addProperty("id", key);
        returnJSON.addProperty("quote", quote);
        String jsonResponse = new Gson().toJson(returnJSON);

        response.setContentType("application/json");
        response.getWriter().write(jsonResponse);

    }

    public void doDelete(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String[] parts = request.getRequestURI().split("/");
        String parameter = null;
        if (parts.length == 5) {
            parameter = parts[4];
        }

        // return JSON response
        JsonObject quote = new JsonObject();
        int key = Integer.parseInt(parameter);
        quote.addProperty("quote", quotes.get(key));
        String jsonResponse = new Gson().toJson(quote);

        // delete the quote 
        quotes.remove(Integer.parseInt(parameter));

        response.setContentType("application/json");
        response.getWriter().write(jsonResponse);
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
