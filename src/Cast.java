import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;

public class Cast implements FilmInformation{
    private int filmId = 0;
    private ArrayList<String> actors = new ArrayList<>();
    private ArrayList<String> characters = new ArrayList<>();

    public Cast(int id) throws IOException{
        filmId = id;
    }

    public int getFilmId() {
        return filmId;
    }

    public ArrayList<String> getActors() {
        return actors;
    }

    public ArrayList<String> getCharacters() {
        return characters;
    }

    public void downloadInf() throws IOException{
        Document doc = Jsoup.connect("https://www.imdb.com/title/tt" + filmId + "/fullcredits/").get();
        Elements outElement = doc.getElementsByAttributeValue("class", "cast_list");

        outElement.forEach(element ->
                element.getElementsByAttributeValueMatching("class", "odd|even").
                        forEach(innerElement -> {
                            String[] splitedElement = innerElement.text().split("(\\s\\.\\.\\.\\s)");
                            actors.add(splitedElement[0]);
                            if(splitedElement.length > 1)
                                characters.add(splitedElement[1]);
                            else
                                characters.add("-");
                        }));
    }
}

