import org.jsoup.HttpStatusException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Parser {

    public static void main(String[] args) throws IOException, ClassNotFoundException, SQLException {
        DatabaseIMDb db = new DatabaseIMDb();
        db.createDBTables();
        ExecutorService executorService = Executors.newFixedThreadPool(6);
        for (int i = 1_000_000; i < 1_001_000; i++) {
            executorService.submit(new FilmThread(i));
        }
        executorService.shutdown();

    }
}

