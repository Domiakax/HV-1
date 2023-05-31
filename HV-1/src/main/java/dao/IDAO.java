package dao;

import java.util.List;
import java.util.UUID;

public interface IDAO<T> {


   // CREATE
   boolean insert(T o);

   // READ
   T findById(UUID id);
   
   // READ
   List<? extends T> getAll();
   
   // UPDATE
   boolean update(T o);
   
   void createTable();

   // DELETE
   boolean delete(UUID id);
   
   // DELETE ALL
   void truncate();
}
