package ma.fstt.Modele.service;

import java.util.List;

public interface IDAO<T> {
    void save(T t);
    void update(T t);
    void delete(int id);
    T findById(int id);
    List<T> findAll();
}