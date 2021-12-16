package ru.akirakozov.sd.refactoring.servlet;

import org.junit.Assert;
import org.mockito.Mockito;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.*;

public class TestHelper {
    final public static String testfile = "1.txt";

    public static void checkStatus(final HttpServletResponse response) {
        doThrow(new AssertionError("Expected only text/html")).when(response).setContentType(Mockito.anyString());
        doNothing().when(response).setContentType("text/html");

        doThrow(new AssertionError("Expected only OK status")).when(response).setStatus(Mockito.anyInt());
        doNothing().when(response).setStatus(HttpServletResponse.SC_OK);
    }

    public static void cmpFiles(final List<String> text) throws IOException {
        Assert.assertEquals(text, Files.readAllLines(Paths.get(testfile)));
    }

    public static void cmpFilesHTML(final List<String> text) throws IOException {
        ArrayList<String> anss = new ArrayList<>(text);
        anss.add(0, "<html><body>");
        anss.add("</body></html>");
        cmpFiles(anss);
    }

    public static void addProduct(final HttpServletRequest request, final HttpServletResponse response, final String name, final String val, final PrintWriter printWriter) throws IOException {
        when(request.getParameter("name")).thenReturn(name);
        when(request.getParameter("price")).thenReturn(val);
        when(response.getWriter()).thenReturn(printWriter);
        try {
            final AddProductServlet addProductServlet = new AddProductServlet();
            addProductServlet.doGet(request, response);
        } catch (final Exception ignored) {
            Assert.fail();
        }
    }

    public static void addProduct(final HttpServletRequest request, final HttpServletResponse response, final String name, final String val) throws IOException {
        addProduct(request, response, name, val, new PrintWriter(System.out));
    }

    private static void dropTable() throws SQLException {
        try (Connection c = DriverManager.getConnection("jdbc:sqlite:test.db")) {
            String sql = "DROP TABLE IF EXISTS PRODUCT";
            Statement stmt = c.createStatement();
            stmt.executeUpdate(sql);
            stmt.close();
        }
    }

    private static void createTable() throws SQLException {
        try (Connection c = DriverManager.getConnection("jdbc:sqlite:test.db")) {
            String sql = "CREATE TABLE IF NOT EXISTS PRODUCT" +
                    "(ID INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL," +
                    " NAME           TEXT    NOT NULL, " +
                    " PRICE          INT     NOT NULL)";
            Statement stmt = c.createStatement();
            stmt.executeUpdate(sql);
            stmt.close();
        }
    }

    public static void createNewTable() throws SQLException {
        dropTable();
        createTable();
    }
}
