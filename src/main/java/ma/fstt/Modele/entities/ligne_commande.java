package ma.fstt.Modele.entities;

public class ligne_commande {
    private int id;
    private int quantite;
    private double prixUnitaire;
    private Produit produit;
    public ligne_commande() { }

    public ligne_commande(int id, int quantite, double prixUnitaire, Produit produit) {
        this.id = id;
        this.quantite = quantite;
        this.prixUnitaire = prixUnitaire;
        this.produit = produit;
    }
    public double getSousTotal() {
        return quantite * prixUnitaire;
    }
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getQuantite() { return quantite; }
    public void setQuantite(int quantite) { this.quantite = quantite; }

    public double getPrixUnitaire() { return prixUnitaire; }
    public void setPrixUnitaire(double prixUnitaire) { this.prixUnitaire = prixUnitaire; }

    public Produit getProduit() { return produit; }
    public void setProduit(Produit produit) { this.produit = produit; }

    @Override
    public String toString() {
        return "LigneDeCommande{id=" + id + ", quantite=" + quantite
                + ", prixUnitaire=" + prixUnitaire + "}";
    }

}

