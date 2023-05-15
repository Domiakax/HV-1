package dao;

import java.util.List;
import java.util.UUID;

public interface IDAO<T> {


   // CREATE
   void insert(T o);

   // READ
   T findById(UUID id);
   
   // READ
   List<T> getAll();
   
   // UPDATE
   boolean update(T o);
   
   void createTable();

   // DELETE
   boolean delete(UUID id);
   
   // DELETE
//   void delete(T o);
}
