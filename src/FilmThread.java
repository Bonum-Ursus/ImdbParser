import java.io.IOException;
import java.sql.SQLException;

public class FilmThread extends Thread{
    int i;
    public FilmThread(int i){
        this.i = i;
    }
    @Override
    public void run() {
        Film film = null;
        try {
            film = new Film(i);
        } catch (IOException e) {
            e.printStackTrace();
        }
        if(film.checkNot404()) {
            try {
                new DatabaseIMDb().exportToDB(film);
            } catch (SQLException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}