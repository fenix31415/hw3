package ru.akirakozov.sd.refactoring.servlet;

import ru.akirakozov.sd.refactoring.database.DataBase;
import ru.akirakozov.sd.refactoring.database.DataBaseItem;
import ru.akirakozov.sd.refactoring.html.WriterHTML;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

/**
 * @author akirakozov
 */
public class GetProductsServlet extends HttpServlet {
    final DataBase dataBase;

    public GetProductsServlet(final DataBase dataBase) {
        this.dataBase = dataBase;
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        WriterHTML.writeGetProducts(response.getWriter(), dataBase.getProducts());

        response.setContentType("text/html");
        response.setStatus(HttpServletResponse.SC_OK);
    }
}
