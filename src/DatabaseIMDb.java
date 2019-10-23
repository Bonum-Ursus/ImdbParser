import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;

public class DatabaseIMDb {

    public void createDBTables() throws SQLException, ClassNotFoundException {
        String userName = "root";
        String password = "Inferno454";
        String connectionUrl = "jdbc:mysql://localhost:3306/imdb_db";
        Class.forName("com.mysql.jdbc.Driver");

        try (Connection connection = DriverManager.getConnection(connectionUrl, userName, password);
             Statement statement = connection.createStatement()) {
            statement.executeUpdate("CREATE TABLE IF NOT EXISTS  cast (" +
                    "id INT NOT NULL AUTO_INCREMENT PRIMARY KEY, " +
                    "filmId INT NOT NULL," +
                    "filmName VARCHAR(100)," +
                    "actor VARCHAR(100), " +
                    "character_ VARCHAR(100))");
            statement.executeUpdate("CREATE TABLE IF NOT EXISTS  directors (" +
                    "id INT NOT NULL AUTO_INCREMENT PRIMARY KEY, " +
                    "filmId INT NOT NULL," +
                    "filmName VARCHAR(100)," +
                    "director VARCHAR(100))");
            statement.executeUpdate("CREATE TABLE IF NOT EXISTS  writers (" +
                    "id INT NOT NULL AUTO_INCREMENT PRIMARY KEY, " +
                    "filmId INT NOT NULL," +
                    "filmName VARCHAR(100)," +
                    "writer VARCHAR(100))");
            statement.executeUpdate("CREATE TABLE IF NOT EXISTS  summaries (" +
                    "id INT NOT NULL AUTO_INCREMENT PRIMARY KEY, " +
                    "filmId INT NOT NULL," +
                    "filmName VARCHAR(100)," +
                    "year VARCHAR(30)," +
                    "summaries VARCHAR(1000))");
        }
    }
    public synchronized void exportToDB (Film film) throws SQLException, ClassNotFoundException, IOException {
        film.downloadInf();
        ArrayList<String> actorsDB = film.getActors();
        ArrayList<String> charactersDB = film.getCharacters();
        ArrayList<String> directorsDB = film.getDirectors();
        ArrayList<String> writersDB = film.getWriters();
        ArrayList<String> summariesDB = film.getSummaries();


        String userName = "root";
        String password = "Inferno454";
        String connectionUrl = "jdbc:mysql://localhost:3306/imdb_db";
        Class.forName("com.mysql.jdbc.Driver");

        try (Connection connection = DriverManager.getConnection(connectionUrl, userName, password);
             Statement statement = connection.createStatement()) {
            connection.setAutoCommit(false);

            PreparedStatement ps = null;
            for (int i = 0; i < actorsDB.size(); i++) {
                ps = connection.prepareStatement("INSERT INTO cast (filmID, filmName, actor, character_) " +
                        "VALUES (" + film.getFilmId() + ", ?, ?, ?)");
                ps.setString(1, film.getName());
                ps.setString(2, actorsDB.get(i));
                ps.setString(3, charactersDB.get(i));
                ps.addBatch();
                ps.execute();
            }
            for (int i = 0; i < directorsDB.size(); i++) {
                ps = connection.prepareStatement("INSERT INTO directors (filmID, filmName, director) " +
                        "VALUES (" + film.getFilmId() + ", ?, ?)");
                ps.setString(1, film.getName());
                ps.setString(2, directorsDB.get(i));
                ps.addBatch();
                ps.execute();
            }
            for (int i = 0; i < writersDB.size(); i++) {
                ps = connection.prepareStatement("INSERT INTO writers (filmID, filmName, writer) " +
                        "VALUES (" + film.getFilmId() + ", ?, ?)");
                ps.setString(1, film.getName());
                ps.setString(2, writersDB.get(i));
                ps.addBatch();
                ps.execute();
            }
            for (int i = 0; i < summariesDB.size(); i++) {
                ps = connection.prepareStatement("INSERT INTO summaries (filmID, filmName, year, summaries) " +
                        "VALUES (" + film.getFilmId() + ", ?, ?, ?)");
                ps.setString(1, film.getName());
                ps.setString(2, film.getYear());
                ps.setString(3, summariesDB.get(i));
                ps.addBatch();
                ps.execute();
            }
            connection.commit();
        }
    }
}
