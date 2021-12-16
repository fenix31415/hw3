package ru.akirakozov.sd.refactoring.servlet;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import ru.akirakozov.sd.refactoring.database.DataBase;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import static org.mockito.Mockito.when;
import static ru.akirakozov.sd.refactoring.servlet.TestHelper.*;

public class QueryServletTest {
    final static DataBase dataBase = new DataBase("test", true);
    final static QueryServlet queryServlet = new QueryServlet(dataBase);

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;

    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);
        dataBase.create();
        checkStatus(response);
    }

    @After
    public void drop() {
        dataBase.drop();
    }

    private void testOperation_zero(final String command, List<String> ans) throws IOException {
        when(request.getParameter("command")).thenReturn(command);
        final PrintWriter printWriter = new PrintWriter(testfile);
        when(response.getWriter()).thenReturn(printWriter);

        try {
            queryServlet.doGet(request, response);
            printWriter.close();
            cmpFilesHTML(ans);
        } catch (final Exception e) {
            System.out.println(e.getMessage());
            Assert.fail();
        }
    }

    private void testOperation_single(final String name, final String val, final String command, List<String> ans) throws IOException {
        addProduct(request, response, name, val, dataBase);
        testOperation_zero(command, ans);
    }

    @Test
    public void badCommandTest() throws IOException {
        when(request.getParameter("command")).thenReturn("summ");
        final PrintWriter printWriter = new PrintWriter(testfile);
        when(response.getWriter()).thenReturn(printWriter);

        try {
            queryServlet.doGet(request, response);
            printWriter.close();
            cmpFiles(List.of("Unknown command: summ"));
        } catch (final Exception ignored) {
            Assert.fail();
        }
    }

    @Test
    public void sumOneTest() throws IOException {
        testOperation_single("item2", "31", "sum", List.of("Summary price: ", "31"));
    }

    @Test
    public void countOneTest() throws Exception {
        testOperation_single("item3", "314", "count", List.of("Number of products: ", "1"));
    }

    @Test
    public void maxOneTest() throws Exception {
        testOperation_single("item4", "3141", "max", List.of("<h1>Product with max price: </h1>", "item4\t3141</br>"));
    }

    @Test
    public void minOneTest() throws Exception {
        testOperation_single("item5", "31415", "min", List.of("<h1>Product with min price: </h1>", "item5\t31415</br>"));
    }

    @Test
    public void sumZeroTest() throws Exception {
        testOperation_zero("sum", List.of("Summary price: ", "0"));
    }

    @Test
    public void countZeroTest() throws Exception {
        testOperation_zero("count", List.of("Number of products: ", "0"));
    }

    @Test
    public void maxZeroTest() throws Exception {
        testOperation_zero("max", List.of("<h1>Product with max price: </h1>"));
    }

    @Test
    public void minZeroTest() throws Exception {
        testOperation_zero("min", List.of("<h1>Product with min price: </h1>"));
    }

    private void addItems() throws IOException {
        addProduct(request, response, "item1", "3", dataBase);
        addProduct(request, response, "item2", "31", dataBase);
        addProduct(request, response, "item3", "314", dataBase);
        addProduct(request, response, "item4", "3141", dataBase);
    }

    @Test
    public void sumManyTest() throws Exception {
        addItems();
        testOperation_zero("sum", List.of("Summary price: ", "3489"));
    }

    @Test
    public void countManyTest() throws Exception {
        addItems();
        testOperation_zero("count", List.of("Number of products: ", "4"));
    }

    @Test
    public void maxManyTest() throws Exception {
        addItems();
        testOperation_zero("max", List.of("<h1>Product with max price: </h1>", "item4\t3141</br>"));
    }

    @Test
    public void minManyTest() throws Exception {
        addItems();
        testOperation_zero("min", List.of("<h1>Product with min price: </h1>", "item1\t3</br>"));
    }
}
