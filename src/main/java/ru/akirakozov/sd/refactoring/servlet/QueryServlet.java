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

/**
 * @author akirakozov
 */
public class QueryServlet extends HttpServlet {
    final DataBase dataBase;

    public QueryServlet(final DataBase dataBase) {
        this.dataBase = dataBase;
    }

    private void writeAns(final HttpServletResponse response, final String title, final Supplier<String> supplier) throws IOException {
        PrintWriter writer = response.getWriter();
        writer.println("<html><body>");
        writer.println(title);
        writer.print(supplier.get());
        writer.println("</body></html>");
    }

    private static String writeOptional(final Optional<DataBaseItem> item) {
        return item.map(dataBaseItem -> dataBaseItem + "</br>\n").orElse("");
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String command = request.getParameter("command");

        if ("max".equals(command)) {
            writeAns(response, "<h1>Product with max price: </h1>", () -> writeOptional(dataBase.getMax()));
        } else if ("min".equals(command)) {
            writeAns(response, "<h1>Product with min price: </h1>", () -> writeOptional(dataBase.getMin()));
        } else if ("sum".equals(command)) {
            writeAns(response, "Summary price: ", () -> dataBase.getSum() + "\n");
        } else if ("count".equals(command)) {
            writeAns(response, "Number of products: ", () -> dataBase.getCount() + "\n");
        } else {
            response.getWriter().println("Unknown command: " + command);
        }

        response.setContentType("text/html");
        response.setStatus(HttpServletResponse.SC_OK);
    }

}
