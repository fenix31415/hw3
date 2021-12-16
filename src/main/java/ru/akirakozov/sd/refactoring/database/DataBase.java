package ru.akirakozov.sd.refactoring.database;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class DataBase {
    final String name;

    public DataBase(final String name, boolean dropOld) {
        this.name = name;

        if (dropOld) {
            drop();
        }
        create();
    }

    public void drop() {
        try (Connection c = DriverManager.getConnection("jdbc:sqlite:" + name + ".db")) {
            String sql = "drop table if exists Product";
            Statement stmt = c.createStatement();
            stmt.executeUpdate(sql);
            stmt.close();
        } catch (final SQLException exception) {
            throw new DataBaseException("Can't drop table", exception);
        }
    }

    public void create() {
        try (Connection c = DriverManager.getConnection("jdbc:sqlite:" + name + ".db")) {
            String sql = "CREATE TABLE IF NOT EXISTS PRODUCT" +
                    "(ID INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL," +
                    " NAME           TEXT    NOT NULL, " +
                    " PRICE          INT     NOT NULL)";
            Statement stmt = c.createStatement();
            stmt.executeUpdate(sql);
            stmt.close();
        } catch (final SQLException e) {
            throw new DataBaseException("Can't create database", e);
        }
    }

    private Optional<DataBaseItem> getMinMax(boolean min) {
        try (Connection c = DriverManager.getConnection("jdbc:sqlite:" + name + ".db")) {
            Statement stmt = c.createStatement();
            ResultSet rs = stmt.executeQuery("select * from Product order by price" + (min ? "" : " desc") + " limit 1");
            if (!rs.next()) {
                return Optional.empty();
            } else {
                return Optional.of(new DataBaseItem(rs.getString("name"), rs.getLong("price")));
            }
        } catch (final SQLException e) {
            throw new DataBaseException("Can't get " + (min ? "min" : "max"), e);
        }
    }

    public Optional<DataBaseItem> getMax() {
        return getMinMax(false);
    }

    public Optional<DataBaseItem> getMin() {
        return getMinMax(true);
    }

    private long getSumCount(boolean sum) {
        try (Connection c = DriverManager.getConnection("jdbc:sqlite:" + name + ".db")) {
            Statement stmt = c.createStatement();
            ResultSet rs = stmt.executeQuery("select " + (sum ? "sum(price)" : "count(*)") + " from Product");
            return rs.getLong(1);
        } catch (final SQLException exception) {
            System.out.println(exception.getMessage());
            throw new DataBaseException("Can't get sum", exception);
        }
    }

    public long getSum() {
        return getSumCount(true);
    }

    public int getCount() {
        return (int) getSumCount(false);
    }

    public void addProduct(final String product, final long price) {
        try (Connection c = DriverManager.getConnection("jdbc:sqlite:" + name + ".db")) {
            String sql =
                    "insert into Product " +
                            "(name, price) values " +
                            "('" + product + "', " + price + ")";
            Statement stmt = c.createStatement();
            stmt.executeUpdate(sql);
            stmt.close();
        } catch (final SQLException e) {
            throw new DataBaseException("Can't insert product", e);
        }
    }

    public List<DataBaseItem> getProducts() {
        final List<DataBaseItem> ans = new ArrayList<>();
        try (Connection c = DriverManager.getConnection("jdbc:sqlite:" + name + ".db")) {
            Statement stmt = c.createStatement();
            ResultSet rs = stmt.executeQuery("select name, price from Product");
            while (rs.next()) {
                ans.add(new DataBaseItem(rs.getString("name"), rs.getInt("price")));
            }
        } catch (final SQLException exception) {
            throw new DataBaseException("Can't select products", exception);
        }
        return ans;
    }
}
