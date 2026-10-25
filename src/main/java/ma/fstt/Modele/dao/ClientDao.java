package ma.fstt.Modele.dao;

import jakarta.enterprise.context.ApplicationScoped;
import ma.fstt.Modele.entities.client;
import ma.fstt.Modele.service.IDAO;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@ApplicationScoped
public class ClientDao implements IDAO<client> {

    private Connection cnx() {
        return SingletonConnection.getConnection();
    }

    @Override
    public void save(client c) {
        String sql = "INSERT INTO client (nom, prenom, email, telephone, adresse) VALUES (?,?,?,?,?)";
        try (PreparedStatement ps = cnx().prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, c.getNom());
            ps.setString(2, c.getPrenom());
            ps.setString(3, c.getEmail());
            ps.setString(4, c.getTelephone());
            ps.setString(5, c.getAdresse());
            ps.executeUpdate();
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) c.setId(rs.getInt(1));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erreur save client", e);
        }
    }

    @Override
    public void update(client c) {
        String sql = "UPDATE client SET nom=?, prenom=?, email=?, telephone=?, adresse=? WHERE id=?";
        try (PreparedStatement ps = cnx().prepareStatement(sql)) {
            ps.setString(1, c.getNom());
            ps.setString(2, c.getPrenom());
            ps.setString(3, c.getEmail());
            ps.setString(4, c.getTelephone());
            ps.setString(5, c.getAdresse());
            ps.setInt(6, c.getId());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Erreur update client", e);
        }
    }

    @Override
    public void delete(int id) {
        try (PreparedStatement ps = cnx().prepareStatement("DELETE FROM client WHERE id=?")) {
            ps.setInt(1, id);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Erreur delete client", e);
        }
    }

    @Override
    public client findById(int id) {
        try (PreparedStatement ps = cnx().prepareStatement("SELECT * FROM client WHERE id=?")) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? map(rs) : null;
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erreur findById client", e);
        }
    }

    @Override
    public List<client> findAll() {
        List<client> liste = new ArrayList<>();
        try (PreparedStatement ps = cnx().prepareStatement("SELECT * FROM client ORDER BY id");
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) liste.add(map(rs));
        } catch (SQLException e) {
            throw new RuntimeException("Erreur findAll client", e);
        }
        return liste;
    }

    private client map(ResultSet rs) throws SQLException {
        return new client(
                rs.getInt("id"),
                rs.getString("nom"),
                rs.getString("prenom"),
                rs.getString("email"),
                rs.getString("telephone"),
                rs.getString("adresse"));
    }
}
