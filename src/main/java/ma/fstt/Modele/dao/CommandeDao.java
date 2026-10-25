package ma.fstt.Modele.dao;

import jakarta.enterprise.context.ApplicationScoped;
import ma.fstt.Modele.entities.client;
import ma.fstt.Modele.entities.Commande;
import ma.fstt.Modele.entities.ligne_commande;
import ma.fstt.Modele.entities.Produit;
import ma.fstt.Modele.service.IDAO;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@ApplicationScoped
public class CommandeDao implements IDAO<Commande> {

    private static final String SELECT_BASE =
            "SELECT c.id, c.date_commande, c.statut, c.total, " +
                    "cl.id AS cl_id, cl.nom, cl.prenom, cl.email, cl.telephone, cl.adresse " +
                    "FROM commande c JOIN client cl ON c.id_client = cl.id";

    private Connection cnx() {
        return SingletonConnection.getConnection();
    }

    // ---------- CREATE : commande + ses lignes dans une transaction ----------
    @Override
    public void save(Commande cmd) {
        Connection cnx = cnx();
        String sql = "INSERT INTO commande (date_commande, statut, total, id_client) VALUES (?,?,?,?)";
        try {
            cnx.setAutoCommit(false);
            cmd.calculerTotal();
            try (PreparedStatement ps = cnx.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
                ps.setDate(1, Date.valueOf(cmd.getDateCommande()));
                ps.setString(2, cmd.getStatut());
                ps.setDouble(3, cmd.getTotal());
                ps.setInt(4, cmd.getClient().getId());
                ps.executeUpdate();
                try (ResultSet rs = ps.getGeneratedKeys()) {
                    if (rs.next()) cmd.setId(rs.getInt(1));
                }
            }
            insertLignes(cnx, cmd);
            cnx.commit();
        } catch (SQLException e) {
            rollback(cnx);
            throw new RuntimeException("Erreur save commande", e);
        } finally {
            reactiverAutoCommit(cnx);
        }
    }

    // ---------- UPDATE : on met à jour la commande puis on recrée les lignes ----------
    @Override
    public void update(Commande cmd) {
        Connection cnx = cnx();
        String sql = "UPDATE commande SET date_commande=?, statut=?, total=?, id_client=? WHERE id=?";
        try {
            cnx.setAutoCommit(false);
            cmd.calculerTotal();
            try (PreparedStatement ps = cnx.prepareStatement(sql)) {
                ps.setDate(1, Date.valueOf(cmd.getDateCommande()));
                ps.setString(2, cmd.getStatut());
                ps.setDouble(3, cmd.getTotal());
                ps.setInt(4, cmd.getClient().getId());
                ps.setInt(5, cmd.getId());
                ps.executeUpdate();
            }
            try (PreparedStatement ps = cnx.prepareStatement("DELETE FROM ligne_commande WHERE id_commande=?")) {
                ps.setInt(1, cmd.getId());
                ps.executeUpdate();
            }
            insertLignes(cnx, cmd);
            cnx.commit();
        } catch (SQLException e) {
            rollback(cnx);
            throw new RuntimeException("Erreur update commande", e);
        } finally {
            reactiverAutoCommit(cnx);
        }
    }

    // ---------- DELETE : les lignes partent avec la commande (ON DELETE CASCADE) ----------
    @Override
    public void delete(int id) {
        try (PreparedStatement ps = cnx().prepareStatement("DELETE FROM commande WHERE id=?")) {
            ps.setInt(1, id);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Erreur delete commande", e);
        }
    }

    // ---------- READ : une commande avec ses lignes ----------
    @Override
    public Commande findById(int id) {
        try (PreparedStatement ps = cnx().prepareStatement(SELECT_BASE + " WHERE c.id = ?")) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (!rs.next()) return null;
                Commande cmd = mapCommande(rs);
                cmd.setLignes(chargerLignes(id));
                return cmd;
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erreur findById commande", e);
        }
    }

    // ---------- READ : liste des commandes (sans les lignes) ----------
    @Override
    public List<Commande> findAll() {
        List<Commande> liste = new ArrayList<>();
        try (PreparedStatement ps = cnx().prepareStatement(SELECT_BASE + " ORDER BY c.id DESC");
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) liste.add(mapCommande(rs));
        } catch (SQLException e) {
            throw new RuntimeException("Erreur findAll commande", e);
        }
        return liste;
    }

    // ================= méthodes privées =================

    private void insertLignes(Connection cnx, Commande cmd) throws SQLException {
        String sql = "INSERT INTO ligne_commande (quantite, prix_unitaire, id_commande, id_produit) VALUES (?,?,?,?)";
        try (PreparedStatement ps = cnx.prepareStatement(sql)) {
            for (ligne_commande l : cmd.getLignes()) {
                ps.setInt(1, l.getQuantite());
                ps.setDouble(2, l.getPrixUnitaire());
                ps.setInt(3, cmd.getId());
                ps.setInt(4, l.getProduit().getId());
                ps.addBatch();
            }
            ps.executeBatch();
        }
    }

    private List<ligne_commande> chargerLignes(int idCommande) throws SQLException {
        String sql = "SELECT l.id, l.quantite, l.prix_unitaire, " +
                "p.id AS p_id, p.designation, p.prix, p.stock " +
                "FROM ligne_commande l JOIN produit p ON l.id_produit = p.id " +
                "WHERE l.id_commande = ?";
        List<ligne_commande> lignes = new ArrayList<>();
        try (PreparedStatement ps = cnx().prepareStatement(sql)) {
            ps.setInt(1, idCommande);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Produit p = new Produit(rs.getInt("p_id"), rs.getString("designation"),
                            rs.getDouble("prix"), rs.getInt("stock"));
                    lignes.add(new ligne_commande(rs.getInt("id"), rs.getInt("quantite"),
                            rs.getDouble("prix_unitaire"), p));
                }
            }
        }
        return lignes;
    }

    private Commande mapCommande(ResultSet rs) throws SQLException {
        client cl = new client(rs.getInt("cl_id"), rs.getString("nom"), rs.getString("prenom"),
                rs.getString("email"), rs.getString("telephone"), rs.getString("adresse"));
        return new Commande(rs.getInt("id"), rs.getDate("date_commande").toLocalDate(),
                rs.getString("statut"), rs.getDouble("total"), cl);
    }

    private void rollback(Connection cnx) {
        try { cnx.rollback(); } catch (SQLException ignored) { }
    }

    private void reactiverAutoCommit(Connection cnx) {
        try { cnx.setAutoCommit(true); } catch (SQLException ignored) { }
    }
}
