import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;

public class Director implements FilmInformation{
    private int filmId = 0;

    public ArrayList<String> getDirectors() {
        return directors;
    }

    private ArrayList<String> directors = new ArrayList<>();
    public Director(int id){
        filmId = id;
    }
    public void downloadInf() throws IOException {
        Document doc = Jsoup.connect("https://www.imdb.com/title/tt" + filmId + "/fullcredits/").get();
        Elements elements = doc.getElementsByAttributeValueContaining("href", "ttfc_fc_dr");
        elements.forEach(element -> {
            directors.add(element.text());
        });
    }
}
