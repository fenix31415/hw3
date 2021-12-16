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

import static ru.akirakozov.sd.refactoring.servlet.TestHelper.*;

public class AddProductTest {
    private AddProductServlet addProductServlet;

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;

    @Before
    public void init() throws Exception {
        MockitoAnnotations.initMocks(this);
        addProductServlet = new AddProductServlet();
        createNewTable();

        checkStatus(response);
    }

    @Test
    public void addProductTest() throws IOException {
        final PrintWriter printWriter = new PrintWriter(testfile);
        addProduct(request, response, "item0", "0", printWriter);

        try {
            addProductServlet.doGet(request, response);
            printWriter.close();
            cmpFiles(List.of("OK"));
        } catch (final Exception ignored) {
            Assert.fail();
        }
    }
}
