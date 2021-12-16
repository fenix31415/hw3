package ru.akirakozov.sd.refactoring.servlet;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.List;

import static org.mockito.Mockito.when;
import static ru.akirakozov.sd.refactoring.servlet.TestHelper.*;

public class GetProductsTest {
    private GetProductsServlet getProductsServlet;

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;

    @Before
    public void init() throws SQLException {
        MockitoAnnotations.initMocks(this);

        getProductsServlet = new GetProductsServlet();
        createNewTable();

        checkStatus(response);
    }

    @Test
    public void singleTest() throws Exception {
        addProduct(request, response, "item1", "3");

        try {
            final AddProductServlet addProductServlet = new AddProductServlet();
            addProductServlet.doGet(request, response);
        } catch (final Exception ignored) {
            Assert.fail();
        }

        final PrintWriter printWriter = new PrintWriter(testfile);
        when(response.getWriter()).thenReturn(printWriter);

        try {
            getProductsServlet.doGet(request, response);
            printWriter.close();
            cmpFilesHTML(List.of("item1\t3</br>"));
        } catch (final Exception ignored) {
            Assert.fail();
        }
    }
}
