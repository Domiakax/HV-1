package model;

import java.time.LocalDate;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonTypeInfo;

@JsonTypeInfo(use = JsonTypeInfo.Id.CLASS)
public interface IReading {

   String getComment();

   ICustomer getCustomer();

   LocalDate getDateOfReading();

   UUID getUuid();

   String getKindOfMeter();

   Double getMetercount();

   String getMeterId();

   Boolean getSubstitute();

   void setComment(String comment);

   void setCustomer(ICustomer customer);

   void setDateOfReading(LocalDate dateOfReading);

   void setUuid(UUID uuid);

   void setKindOfMeter(String kindOfMeter);

   void setMetercount(Double meterCount);

   void setMeterId(String meterId);

   void setSubstitute(Boolean substitute);

}
