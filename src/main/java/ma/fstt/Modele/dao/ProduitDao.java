package ma.fstt.Modele.dao;

import jakarta.enterprise.context.ApplicationScoped;
import ma.fstt.Modele.entities.Produit;
import ma.fstt.Modele.service.IDAO;
import ma.fstt.Modele.service.IDAO;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@ApplicationScoped
public class ProduitDao implements IDAO<Produit> {

    private Connection cnx() {
        return SingletonConnection.getConnection();
    }

    @Override
    public void save(Produit p) {
        String sql = "INSERT INTO produit (designation, prix, stock) VALUES (?,?,?)";
        try (PreparedStatement ps = cnx().prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, p.getDesignation());
            ps.setDouble(2, p.getPrix());
            ps.setInt(3, p.getStock());
            ps.executeUpdate();
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) p.setId(rs.getInt(1));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erreur save produit", e);
        }
    }

    @Override
    public void update(Produit p) {
        String sql = "UPDATE produit SET designation=?, prix=?, stock=? WHERE id=?";
        try (PreparedStatement ps = cnx().prepareStatement(sql)) {
            ps.setString(1, p.getDesignation());
            ps.setDouble(2, p.getPrix());
            ps.setInt(3, p.getStock());
            ps.setInt(4, p.getId());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Erreur update produit", e);
        }
    }

    @Override
    public void delete(int id) {
        try (PreparedStatement ps = cnx().prepareStatement("DELETE FROM produit WHERE id=?")) {
            ps.setInt(1, id);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Erreur delete produit", e);
        }
    }

    @Override
    public Produit findById(int id) {
        try (PreparedStatement ps = cnx().prepareStatement("SELECT * FROM produit WHERE id=?")) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? map(rs) : null;
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erreur findById produit", e);
        }
    }

    @Override
    public List<Produit> findAll() {
        List<Produit> liste = new ArrayList<>();
        try (PreparedStatement ps = cnx().prepareStatement("SELECT * FROM produit ORDER BY id");
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) liste.add(map(rs));
        } catch (SQLException e) {
            throw new RuntimeException("Erreur findAll produit", e);
        }
        return liste;
    }

    private Produit map(ResultSet rs) throws SQLException {
        return new Produit(
                rs.getInt("id"),
                rs.getString("designation"),
                rs.getDouble("prix"),
                rs.getInt("stock"));
    }
}
