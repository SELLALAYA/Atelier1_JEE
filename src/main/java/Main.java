import ma.fstt.Modele.dao.ClientDao;
import ma.fstt.Modele.entities.client;

public class Main {

    public static void main(String[] args) {

        ClientDao dao = new ClientDao();

       client c = new client();
        c.setNom("sellal");
        c.setPrenom("aya");
        c.setEmail("sellalaya@mail.com");

        dao.save(c);

        System.out.println(dao.findAll());
    }
}
