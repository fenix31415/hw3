package ru.akirakozov.sd.refactoring.html;

import ru.akirakozov.sd.refactoring.database.DataBaseItem;

import java.io.PrintWriter;
import java.util.List;
import java.util.Optional;
import java.util.function.Supplier;

public class WriterHTML {
    public static void writeAddProducts(PrintWriter writer) {
        writer.println("OK");
    }

    public static void writeGetProducts(PrintWriter writer, final List<DataBaseItem> items) {
        writer.println("<html><body>");
        for (final DataBaseItem item : items) {
            writer.println(item.getName() + "\t" + item.getCost() + "</br>");
        }
        writer.println("</body></html>");
    }

    private static void writeQueryImpl(final PrintWriter writer, final String title, final Supplier<String> supplier) {
        writer.println("<html><body>");
        writer.println(title);
        writer.print(supplier.get());
        writer.println("</body></html>");
    }

    private static String writeOptional(final Optional<DataBaseItem> item) {
        return item.map(dataBaseItem -> dataBaseItem + "</br>\n").orElse("");
    }

    public static void writeQueryMax(final PrintWriter writer, final Optional<DataBaseItem> item) {
        writeQueryImpl(writer, "<h1>Product with max price: </h1>", () -> writeOptional(item));
    }

    public static void writeQueryMin(final PrintWriter writer, final Optional<DataBaseItem> item) {
        writeQueryImpl(writer, "<h1>Product with min price: </h1>", () -> writeOptional(item));
    }

    public static void writeQuerySum(final PrintWriter writer, final long sum) {
        writeQueryImpl(writer, "Summary price: ", () -> sum + "\n");
    }

    public static void writeQueryCou(final PrintWriter writer, final int count) {
        writeQueryImpl(writer, "Number of products: ", () -> count + "\n");
    }
}
