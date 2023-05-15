package model;

import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonTypeInfo;

@JsonTypeInfo(use = JsonTypeInfo.Id.CLASS)
public interface ICustomer {

   String getFirstname();

   void setFirstname(String firstName);
   
   UUID getUuid();
   
   void setUuid(UUID id);

   String getLastname();

   void setLastname(String lastName);
}
