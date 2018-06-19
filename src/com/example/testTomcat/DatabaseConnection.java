package com.example.testTomcat;



import com.example.testTomcat.tinyLog.TinyLog;
import java.sql.*;




public class DatabaseConnection {
    private final String USER = "root";
    private final String PASS = "admin";
    private final String DRIVER_CLASS = "com.mysql.jdbc.Driver";
    private final String NAME_DATABASE = "users";
    public static final String URL = "jdbc:mysql://localhost:3306";
    private Statement statement;
    private Connection connection;
    private static DatabaseConnection instance = null;


    private DatabaseConnection() {
        try {

            Class.forName(DRIVER_CLASS);
            connection = DriverManager.getConnection(URL, USER, PASS);
            statement = connection.createStatement();
            statement.execute("use " + NAME_DATABASE + ";");
            ResultSet showVar = statement.executeQuery
                    ("SHOW VARIABLES LIKE \"secure_file_priv\";");
            showVar.next();


            ResultSet resultSet = statement.executeQuery("show tables;");
            while (resultSet.next()) {
                System.out.println(resultSet.getString("Tables_in_users"));
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
            TinyLog.warn("ExecuteQuery Error: " + e.getMessage());
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

    public String loadUserPass(String username, String email) {
        String sql = "SELECT * FROM users.usertable" +
                "where (username like '"+username+"') and" +
                "(email like '"+email+"');";
           String pass = null;
        try {
            ResultSet resultSet = statement.executeQuery(sql);

            if(resultSet.first()) {

                pass = resultSet.getString("pass");
                return pass;
            }
        } catch (SQLException e) {
            TinyLog.trace("User not load: " + e.getMessage());
        }

       return pass;
    }
    public void closeDriver(){
        try {
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
