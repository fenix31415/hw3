package ru.akirakozov.sd.refactoring.servlet;

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
import java.sql.SQLException;
import java.util.List;

import static org.mockito.Mockito.when;
import static ru.akirakozov.sd.refactoring.servlet.TestHelper.*;

public class GetProductsTest {
    private final DataBase dataBase = new DataBase("test", true);
    private final GetProductsServlet getProductsServlet = new GetProductsServlet(dataBase);

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;

    @Before
    public void init() throws SQLException {
        MockitoAnnotations.initMocks(this);
        createNewTable();

        checkStatus(response);
    }

    private void cmpAns(final List<String> ans) throws IOException {
        final PrintWriter printWriter = new PrintWriter(testfile);
        when(response.getWriter()).thenReturn(printWriter);

        try {
            getProductsServlet.doGet(request, response);
            printWriter.close();
            cmpFilesHTML(ans);
        } catch (final Exception e) {
            System.out.println(e.getMessage());
            Assert.fail();
        }
    }

    @Test
    public void singleTest() throws IOException {
        addProduct(request, response, "item1", "3", dataBase);
        cmpAns(List.of("item1\t3</br>"));
    }

    @Test
    public void noTest() throws IOException {
        cmpAns(List.of());
    }

    @Test
    public void manyTest() throws Exception {
        addProduct(request, response, "item1", "3", dataBase);
        addProduct(request, response, "item2", "31", dataBase);
        addProduct(request, response, "item3", "314", dataBase);
        addProduct(request, response, "item4", "3141", dataBase);
        cmpAns(List.of("item1\t3</br>", "item2\t31</br>", "item3\t314</br>", "item4\t3141</br>"));
    }
}
