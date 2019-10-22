import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;

public class Film {
    private int filmId;
    private ArrayList<FilmInformation> filmInformations = new ArrayList<>();
    private String name;
    private String year;
    private Director director;
    private Writer writer;
    private Cast cast;
    private Summaries summaries;

    public Film(int id) throws IOException {
        filmId = id;

        director = new Director(filmId);
        filmInformations.add(director);

        writer = new Writer(filmId);
        filmInformations.add(writer);

        cast = new Cast(filmId);
        filmInformations.add(cast);

        summaries = new Summaries(filmId);
        filmInformations.add(summaries);
    }
    private ArrayList<String> getDirectors (){
        return director.getDirectors();
    }
    private ArrayList<String> getWriters(){
        return writer.getWriters();
    }
    private ArrayList<String> getActors(){
        return cast.getActors();
    }
    private ArrayList<String> getCharacters(){
        return cast.getCharacters();
    }
    private ArrayList<String> getSummaries(){
        return summaries.getSummaries();
    }
    private String getName(){
        return name;
    }
    private String getYear(){
        return year;
    }
    private int getFilmId() {
        return filmId;
    }
    private void downloadInf() throws IOException{
        Document document = Jsoup.connect("https://www.imdb.com/title/tt" + filmId + "/plotsummary").get();
        name = document.getElementsByAttributeValueMatching("href", "ttpl_pl_tt").text();
        year = document.getElementsByAttributeValueMatching("class", "nobr").first().text();
        for (FilmInformation f : filmInformations) {
            f.downloadInf();
        }
    }
    public void exportToDB() throws SQLException, ClassNotFoundException, IOException {
        downloadInf();
        ArrayList<String> actorsDB = getActors();
        ArrayList<String> charactersDB = getCharacters();
        ArrayList<String> directorsDB = getDirectors();
        ArrayList<String> writersDB = getWriters();
        ArrayList<String> summariesDB = getSummaries();


        String userName = "root";
        String password = "Inferno454";
        String connectionUrl = "jdbc:mysql://localhost:3306/imdb_db";
        Class.forName("com.mysql.jdbc.Driver");

        try (Connection connection = DriverManager.getConnection(connectionUrl, userName, password);
             Statement statement = connection.createStatement()) {
            connection.setAutoCommit(false);
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

            connection.commit();

            PreparedStatement ps = null;
            for (int i = 0; i < actorsDB.size(); i++) {
                ps = connection.prepareStatement("INSERT INTO cast (filmID, filmName, actor, character_) " +
                        "VALUES (" + getFilmId() + ", ?, ?, ?)");
                ps.setString(1, getName());
                ps.setString(2, actorsDB.get(i));
                ps.setString(3, charactersDB.get(i));
                ps.addBatch();
                ps.execute();
            }
            for (int i = 0; i < directorsDB.size(); i++) {
                ps = connection.prepareStatement("INSERT INTO directors (filmID, filmName, director) " +
                        "VALUES (" + getFilmId() + ", ?, ?)");
                ps.setString(1, getName());
                ps.setString(2, directorsDB.get(i));
                ps.addBatch();
                ps.execute();
            }
            for (int i = 0; i < writersDB.size(); i++) {
                ps = connection.prepareStatement("INSERT INTO writers (filmID, filmName, writer) " +
                        "VALUES (" + getFilmId() + ", ?, ?)");
                ps.setString(1, getName());
                ps.setString(2, writersDB.get(i));
                ps.addBatch();
                ps.execute();
            }
            for (int i = 0; i < summariesDB.size(); i++) {
                ps = connection.prepareStatement("INSERT INTO summaries (filmID, filmName, year, summaries) " +
                        "VALUES (" + getFilmId() + ", ?, ?, ?)");
                ps.setString(1, getName());
                ps.setString(2, getYear());
                ps.setString(3, summariesDB.get(i));
                ps.addBatch();
                ps.execute();
            }
            connection.commit();
        }
    }


}
