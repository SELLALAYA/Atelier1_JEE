package ma.fstt.Modele.entities;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
public class Commande {
    private int id;
    private LocalDate dateCommande;
    private String statut;
    private double total;
    private client client;
    private List<ligne_commande> lignes = new ArrayList<>();

    public Commande() { }

    public Commande(int id, LocalDate dateCommande, String statut,
                    double total, client client) {
        this.id = id;
        this.dateCommande = dateCommande;
        this.statut = statut;
        this.total = total;
        this.client = client;
    }
    public double calculerTotal() {
        double somme = 0;
        for (ligne_commande l : lignes) {
            somme += l.getSousTotal();
        }
        this.total = somme;
        return somme;
}
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public LocalDate getDateCommande() { return dateCommande; }
    public void setDateCommande(LocalDate dateCommande) { this.dateCommande = dateCommande; }

    public String getStatut() { return statut; }
    public void setStatut(String statut) { this.statut = statut; }

    public double getTotal() { return total; }
    public void setTotal(double total) { this.total = total; }

    public client getClient() { return client; }
    public void setClient(client client) { this.client = client; }

    public List<ligne_commande> getLignes() { return lignes; }
    public void setLignes(List<ligne_commande> lignes) { this.lignes = lignes; }
    @Override
    public String toString() {
        return "Commande{id=" + id + ", date=" + dateCommande
                + ", statut=" + statut + ", total=" + total + "}";
    }
}

