package com.example.testTomcat;



import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DatabaseConnection {
    private final String USER = "root";
    private final String PASS = "admin";
    private final String DRIVER_CLASS = "com.mysql.jdbc.Driver";
    private final String NAME_DATABASE = "newlibrary";
    public static final String URL = "jdbc:mysql://localhost:3306";
    private String path;
    private Statement statement;
    private static DatabaseConnection instance = null;


    private DatabaseConnection() {
        try {
            Class.forName(DRIVER_CLASS);
            Connection connection = DriverManager.getConnection(URL, USER, PASS);
            statement = connection.createStatement();
            statement.execute("use " + NAME_DATABASE + ";");
            ResultSet showVar = statement.executeQuery
                    ("SHOW VARIABLES LIKE \"secure_file_priv\";");
            showVar.next();
            path = showVar.getString("Value");

            ResultSet resultSet = statement.executeQuery("show tables;");
            while (resultSet.next()) {
                System.out.println(resultSet.getString("Tables_in_newlibrary"));
            }
        } catch (ClassNotFoundException e) {
            TinyLog.warn("ClassNotFound: " + e.getMessage());
        } catch (SQLException e) {
            TinyLog.warn("Sql error: " + e.getMessage());
        }


    }

    public static DatabaseConnection getInstance() {
        synchronized (DatabaseConnection.class) {
            if (instance == null) {
                instance = new DatabaseConnection();
            }
        }
        return instance;

    }

    public ResultSet executeQuery(String command) {
        try {
            return statement.executeQuery(command);
        } catch (SQLException e) {
//            TinyLog.warn("ExecuteQuery Error: " + e.getMessage());
            return null;
        }
    }

    public boolean execute(String command) {
        try {
            return statement.execute(command);
        } catch (SQLException e) {
            TinyLog.warn("Execute Error: " + e.getMessage());
            return false;
        }
    }


    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public void loadAuthor() {
        String sql = "select authors.id, firstnames.firstName, " +
                "middlenames.middleName,lastnames.lastName, " +
                "authors.dateBirth,authors.goneDate, " +
                "authors.alias,authors.genre " +
                " from authors join firstnames " +
                " on authors.firstName = firstnames.id " +
                " join middlenames " +
                "on authors.middleName = middlenames.id " +
                " join lastnames " +
                "on authors.lastName = lastnames.id;";
        authorsList = new ArrayList<>();
        try {
            ResultSet resultSet = statement.executeQuery(sql);
            while (resultSet.next()) {
                long id = resultSet.getLong("id");
                String firstName = resultSet.getString("firstname");
                String middleName = resultSet.getString("middlename");
                String lastName = resultSet.getString("lastName");
                String alias = resultSet.getString("alias");
                Date dateBith = resultSet.getDate("dateBirth");
                Date goneDate = resultSet.getDate("goneDate");
                authorsList.add(new Author(id, firstName, middleName, lastName, alias, dateBith.toLocalDate(), goneDate.toLocalDate()));
            }
        } catch (SQLException e) {
            TinyLog.trace("Author not create: " + e.getMessage());
        }


    }

    public void loadBooks() {
        String sql = "select books.id, books.title, " +
                " books.description, books.realease_date, " +
                " books.coverLink, books.volume, books.isbn," +
                " books.authorId from books;";
        booksList = new ArrayList<>();
        try {
            ResultSet resultSet = statement.executeQuery(sql);
            while (resultSet.next()) {
                long id = resultSet.getLong("id");
                String title = resultSet.getString("title");
                String isbn = resultSet.getString("isbn");
                String description = resultSet.getString("description");
                Date realeaseDate = resultSet.getDate("realease_Date");
                String coverLink = resultSet.getString("coverLink");
                int volume = resultSet.getInt("volume");
                long authorId = resultSet.getLong("authorId");
                Author author = getAuthorById(authorId);
                booksList.add(new Book(title, author, realeaseDate.toLocalDate(), isbn, coverLink, description,volume, id));
            }
        } catch (SQLException e) {
            TinyLog.trace("Books nor create:" + e.getMessage());
        }

    }




}
