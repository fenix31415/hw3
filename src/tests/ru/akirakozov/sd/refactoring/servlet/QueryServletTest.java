package ru.akirakozov.sd.refactoring.servlet;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import static org.mockito.Mockito.when;
import static ru.akirakozov.sd.refactoring.servlet.TestHelper.*;

public class QueryServletTest {
    private QueryServlet queryServlet;

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;

    @Before
    public void init() throws Exception {
        MockitoAnnotations.initMocks(this);
        queryServlet = new QueryServlet();
        createNewTable();

        checkStatus(response);
    }

    private void testOperation_single(final String name, final String val, final String command, List<String> ans) throws IOException {
        addProduct(request, response, name, val);

        try {
            final AddProductServlet addProductServlet = new AddProductServlet();
            addProductServlet.doGet(request, response);
        } catch (final Exception ignored) {
            Assert.fail();
        }

        when(request.getParameter("command")).thenReturn(command);
        final PrintWriter printWriter = new PrintWriter(testfile);
        when(response.getWriter()).thenReturn(printWriter);

        try {
            queryServlet.doGet(request, response);
            printWriter.close();
            cmpFilesHTML(ans);
        } catch (final Exception ignored) {
            Assert.fail();
        }
    }

    @Test
    public void sumOneProductTest() throws IOException {
        testOperation_single("item2", "31", "sum", List.of("Summary price: ", "31"));
    }

    @Test
    public void countOneProductTest() throws Exception {
        testOperation_single("item3", "314", "count", List.of("Number of products: ", "1"));
    }

    @Test
    public void maxOneProductTest() throws Exception {
        testOperation_single("item4", "3141", "max", List.of("<h1>Product with max price: </h1>", "item4\t3141</br>"));
    }

    @Test
    public void minOneProductTest() throws Exception {
        testOperation_single("item5", "31415", "min", List.of("<h1>Product with min price: </h1>", "item5\t31415</br>"));
    }
}
