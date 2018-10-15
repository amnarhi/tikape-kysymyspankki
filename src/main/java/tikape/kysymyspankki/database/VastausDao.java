package tikape.kysymyspankki.database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import tikape.kysymyspankki.domain.Vastaus;

public class VastausDao implements Dao<Vastaus, Integer> {

    private Database database;

    public VastausDao(Database database) {
        this.database = database;
    }

    // hakee vastauksen id:n perusteella
    @Override
    public Vastaus findOne(Integer key) throws SQLException {
        
        try {
            Connection connection = database.getConnection();
            PreparedStatement stmt = connection.prepareStatement("SELECT * FROM Vastaus WHERE id = ?");
            stmt.setObject(1, key);
            
            ResultSet rs = stmt.executeQuery();
            boolean hasOne = rs.next();
            if (!hasOne) {
                return null;
            }
            
            Integer id = rs.getInt("id");
            String vastausteksti = rs.getString("vastausteksti");
            Boolean oikein = rs.getBoolean("oikein");
            Integer kysymys_id = rs.getInt("kysymys_id");
            
            Vastaus v = new Vastaus(id, vastausteksti, oikein, kysymys_id);
            
            rs.close();
            stmt.close();
            connection.close();
            
            return v;
        } catch (Exception ex) {
            Logger.getLogger(VastausDao.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    // hakee kaikki vastaukset
    @Override
    public List<Vastaus> findAll() throws SQLException {

        try {
            Connection connection = database.getConnection();
            PreparedStatement stmt = connection.prepareStatement("SELECT * FROM Vastaus");
            
            ResultSet rs = stmt.executeQuery();
            List<Vastaus> vastaukset = new ArrayList<>();
            while (rs.next()) {
                
                Integer id = rs.getInt("id");
                Vastaus v = this.findOne(id);
                vastaukset.add(v);
            }
            rs.close();
            stmt.close();
            connection.close();
            
            return vastaukset;
        } catch (Exception ex) {
            Logger.getLogger(VastausDao.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    // hakee tiettyyn kysymykseen liittyvät vastaukset kysymys_id:n perusteella
    public List<Vastaus> findAllWhere(Integer k_id) throws SQLException {

        try {
            Connection connection = database.getConnection();
            PreparedStatement stmt = connection.prepareStatement("SELECT * FROM Vastaus WHERE kysymys_id = ?");
            stmt.setObject(1, k_id);
            
            ResultSet rs = stmt.executeQuery();
            List<Vastaus> vastaukset = new ArrayList<>();
            while (rs.next()) {
                
                Integer id = rs.getInt("id");
                Vastaus v = this.findOne(id);
                vastaukset.add(v);
            }
            rs.close();
            stmt.close();
            connection.close();
            
            return vastaukset;
        } catch (Exception ex) {
            Logger.getLogger(VastausDao.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    // poistaa vastauksen id:n perusteella
    @Override
    public void delete(Integer key) throws SQLException {
        try {
            Connection connection = database.getConnection();
            PreparedStatement stmt = connection.prepareStatement("DELETE FROM Vastaus WHERE id = ?");
            stmt.setObject(1, key);
            stmt.executeUpdate();
        } catch (Exception ex) {
            Logger.getLogger(VastausDao.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    // lisää uuden vastauksen tai päivittää olemassaolevan
    public void saveOrUpdate(Vastaus vastaus) throws SQLException {
        try {
            Connection connection = database.getConnection();
            for (Vastaus n : this.findAll()) {
                if (n.getVastausteksti().equals(vastaus.getVastausteksti())) {
                    PreparedStatement stmt = connection.prepareStatement("UPDATE Vastaus SET vastausteksti = ?, oikein = ?"
                            + " WHERE id = ?");
                    stmt.setObject(1, vastaus.getVastausteksti());
                    stmt.setObject(2, vastaus.getOikein());
                    stmt.setObject(3, vastaus.getId());
                    stmt.executeUpdate();
                    return;
                    
                }
            }
            
            PreparedStatement stmt = connection.prepareStatement("INSERT INTO Vastaus (vastausteksti, oikein, kysymys_id)"
                    + " VALUES (?, ?, ?)");
            stmt.setObject(1, vastaus.getVastausteksti());
            stmt.setObject(2, vastaus.getOikein());
            stmt.setObject(3, vastaus.getKysymys_id());
            stmt.executeUpdate();
        } catch (Exception ex) {
            Logger.getLogger(VastausDao.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

}
