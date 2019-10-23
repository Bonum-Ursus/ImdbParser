import org.jsoup.HttpStatusException;
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

    public ArrayList<String> getDirectors() {
        return director.getDirectors();
    }

    public ArrayList<String> getWriters() {
        return writer.getWriters();
    }

    public ArrayList<String> getActors() {
        return cast.getActors();
    }

    public ArrayList<String> getCharacters() {
        return cast.getCharacters();
    }

    public ArrayList<String> getSummaries() {
        return summaries.getSummaries();
    }

    public String getName() {
        return name;
    }

    public String getYear() {
        return year;
    }

    public int getFilmId() {
        return filmId;
    }

    public void downloadInf() throws IOException {
        Document document = Jsoup.connect("https://www.imdb.com/title/tt" + filmId + "/plotsummary").get();
        name = document.getElementsByAttributeValueMatching("href", "ttpl_pl_tt").text();
        year = document.getElementsByAttributeValueMatching("class", "nobr").first().text();
        for (FilmInformation f : filmInformations) {
            f.downloadInf();
        }
    }

    public boolean checkNot404() {
        try {
            Document document = Jsoup.connect("https://www.imdb.com/title/tt" + filmId + "/plotsummary").get();
        } catch (IOException e) {
            return false;
        }
        return true;

    }
}




