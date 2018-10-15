package tikape.kysymyspankki.database;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class Database {

    private String databaseAddress;

    public Database(String databaseAddress) throws ClassNotFoundException {
        this.databaseAddress = databaseAddress;
    }
    
    // yhdistää PostreSQL-tietokantaan
    public static Connection getConnection() throws Exception {
    String dbUrl = System.getenv("JDBC_DATABASE_URL");
    if (dbUrl != null && dbUrl.length() > 0) {
        return DriverManager.getConnection(dbUrl);
    }

    return DriverManager.getConnection("jdbc:sqlite:kysymyspankki.db");
}CRE
    
      // yhdistää paikalliseen sqlite3 tietokantaan
//    public Connection getConnection() throws SQLException {
//        return DriverManager.getConnection(databaseAddress);
//    }

    public void init() {
        List<String> lauseet = sqliteLauseet();

        // "try with resources" sulkee resurssin automaattisesti lopuksi
        try (Connection conn = getConnection()) {
            Statement st = conn.createStatement();

            // suoritetaan komennot
            for (String lause : lauseet) {
                System.out.println("Running command >> " + lause);
                st.executeUpdate(lause);
            }

        } catch (Throwable t) {
            // jos tietokantataulu on jo olemassa, ei komentoja suoriteta
            System.out.println("Error >> " + t.getMessage());
        }
    }

    private List<String> sqliteLauseet() {
        ArrayList<String> lista = new ArrayList<>();
        // tietokantataulujen luomiseen tarvittavat komennot suoritusjärjestyksessä
//        lista.add("CREATE TABLE Kysymys (id SERIAL PRIMARY KEY, kurssi varchar(200), aihe varchar(200), kysymysteksti varchar(500));");
//        lista.add("CEATE TABLE Vastaus (id SERIAL PRIMARY KEY, kysymys_id integer, vastausteksti varchar(500), oikein boolean, FOREIGN KEY (kysymys_id) REFERENCES Kysymys(id));");
//        
        return lista;
    }
}
