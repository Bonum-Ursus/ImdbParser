import org.jsoup.HttpStatusException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;

public class Parser {

    public static void main(String[] args) throws IOException, ClassNotFoundException, SQLException {
        Film film = new Film(7390646);
        film.exportToDB();
    }
}

