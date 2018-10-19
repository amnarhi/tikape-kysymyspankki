Kysymyspackage tikape.kysymyspankki.database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import tikape.kysymyspankki.domain.Kysymys;
import tikape.kysymyspankki.domain.Vastaus;

public class KysymysDao implements Dao<Kysymys, Integer> {

    private Database database;

    public KysymysDao(Database database) {
        this.database = database;
    }

    @Override
    public Kysymys findOne(Integer key) throws SQLException {
        try {
            Connection connection = database.getConnection();
            PreparedStatement stmt = connection.prepareStatement("SELECT * FROM Kysymys WHERE id = ?");
            stmt.setObject(1, key);
            
            ResultSet rs = stmt.executeQuery();
            boolean hasOne = rs.next();
            if (!hasOne) {
                return null;
            }
            
            Integer id = rs.getInt("id");
            String kurssi = rs.getString("kurssi");
            String aihe = rs.getString("aihe");
            String kysymysteksti = rs.getString("kysymysteksti");
            List<Vastaus> vastaukset = new VastausDao(database).findAllWhere(id);
            
            Kysymys k = new Kysymys(id, kurssi, aihe, kysymysteksti, vastaukset);
            
            rs.close();
            stmt.close();
            connection.close();
            
            return k;
        } catch (Exception ex) {
            Logger.getLogger(KysymysDao.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    @Override
    public List<Kysymys> findAll() throws SQLException {

        try {
            Connection connection = database.getConnection();
            PreparedStatement stmt = connection.prepareStatement("SELECT * FROM Kysymys");
            
            ResultSet rs = stmt.executeQuery();
            List<Kysymys> kysymykset = new ArrayList<>();
            while (rs.next()) {
                Integer id = rs.getInt("id");
                String kurssi = rs.getString("kurssi");
                String aihe = rs.getString("aihe");
                String kysymysteksti = rs.getString("kysymysteksti");
                
                kysymykset.add(new Kysymys(id, kurssi, aihe, kysymysteksti, new ArrayList<Vastaus>()));
            }
            
            rs.close();
            stmt.close();
            connection.close();
            
            return kysymykset;
        } catch (Exception ex) {
            Logger.getLogger(KysymysDao.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    @Override
    public void delete(Integer key) throws SQLException { 
        try {
        // poistaa kysymyksen ja kaikki siihen liittyvät vastaukset
        Connection connection = database.getConnection();
        PreparedStatement stmt = connection.prepareStatement("DELETE FROM Vastaus WHERE kysymys_id = ?");
        stmt.setObject(1, key);
        stmt.executeUpdate();
        stmt = connection.prepareStatement("DELETE FROM Kysymys WHERE id = ?");
        stmt.setObject(1, key);
        stmt.executeUpdate();
        } catch (Exception ex) {
            Logger.getLogger(KysymysDao.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public void saveOrUpdate(Kysymys kysymys) throws SQLException {

        try {
            Connection connection = database.getConnection();
            
            for (Kysymys k : this.findAll()) {
                if (k.getKysymysteksti().equals(kysymys.getKysymysteksti())) {
                    PreparedStatement stmt = connection.prepareStatement("UPDATE Kysymys SET kurssi = ?, aihe = ?"
                            + " WHERE id = ?");
                    stmt.setObject(1, kysymys.getKurssi());
                    stmt.setObject(2, kysymys.getAihe());
                    stmt.setObject(3, k.getId());
                    stmt.executeUpdate();
                    return;
                    
                }
            }
            
            PreparedStatement stmt = connection.prepareStatement("INSERT INTO Kysymys (kurssi, aihe, kysymysteksti)"
                    + " VALUES (?, ?, ?)");
            stmt.setObject(1, kysymys.getKurssi());
            stmt.setObject(2, kysymys.getAihe());
            stmt.setObject(3, kysymys.getKysymysteksti());
            stmt.executeUpdate();
        } catch (Exception ex) {
            Logger.getLogger(KysymysDao.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

}
