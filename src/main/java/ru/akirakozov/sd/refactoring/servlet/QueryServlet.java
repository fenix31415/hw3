package ru.akirakozov.sd.refactoring.servlet;

import ru.akirakozov.sd.refactoring.database.DataBase;
import ru.akirakozov.sd.refactoring.database.DataBaseItem;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Optional;
import java.util.function.Supplier;

import static ru.akirakozov.sd.refactoring.html.WriterHTML.*;

/**
 * @author akirakozov
 */
public class QueryServlet extends HttpServlet {
    final DataBase dataBase;

    public QueryServlet(final DataBase dataBase) {
        this.dataBase = dataBase;
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String command = request.getParameter("command");
        PrintWriter writer = response.getWriter();

        if ("max".equals(command)) {
            writeQueryMax(writer, dataBase.getMax());
        } else if ("min".equals(command)) {
            writeQueryMin(writer, dataBase.getMin());
        } else if ("sum".equals(command)) {
            writeQuerySum(writer, dataBase.getSum());
        } else if ("count".equals(command)) {
            writeQueryCou(writer, dataBase.getCount());
        } else {
            response.getWriter().println("Unknown command: " + command);
        }

        response.setContentType("text/html");
        response.setStatus(HttpServletResponse.SC_OK);
    }

}
