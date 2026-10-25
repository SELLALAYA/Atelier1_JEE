package ma.fstt.Modele.entities;

public class Produit {
    private int id;
    private String designation;
    private double prix;
    private int stock;
    public Produit() { };
    public Produit(int id, String designation, double prix, int stock) {
        this.id = id;
        this.designation = designation;
        this.prix = prix;
        this.stock = stock;
    }
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getDesignation() { return designation; }
    public void setDesignation(String designation) { this.designation = designation; }

    public double getPrix() { return prix; }
    public void setPrix(double prix) { this.prix = prix; }

    public int getStock() { return stock; }
    public void setStock(int stock) { this.stock = stock; }

    @Override
    public String toString() {
        return "Produit{id=" + id + ", designation=" + designation + ", prix=" + prix + "}";
    }

}
