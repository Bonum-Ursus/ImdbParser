import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;

public class Summaries implements  FilmInformation{
    private int filmId = 0;
    ArrayList<String> summaries = new ArrayList<>();

    public Summaries(int id) {
        filmId = id;
    }

    public ArrayList<String> getSummaries() {
        return summaries;
    }

    public void downloadInf() throws IOException{
        Document doc = Jsoup.connect("https://www.imdb.com/title/tt" + filmId + "/plotsummary").get();
        Elements elements = doc.getElementsByAttributeValueContaining("id", "summary");
        elements.forEach(element -> {
            summaries.add(element.text());
        });
    }
}
