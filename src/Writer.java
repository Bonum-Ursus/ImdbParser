import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;

public class Writer implements FilmInformation{
    private int filmId = 0;

    public ArrayList<String> getWriters() {
        return writers;
    }

    private ArrayList<String> writers = new ArrayList<>();
    public Writer(int id){
        filmId = id;
    }
    public void downloadInf() throws IOException {
        Document doc = Jsoup.connect("https://www.imdb.com/title/tt" + filmId + "/fullcredits/").get();
        Elements elements = doc.getElementsByAttributeValueContaining("href", "ttfc_fc_wr");
        elements.forEach(element -> {
            writers.add(element.text());
        });
    }
}
