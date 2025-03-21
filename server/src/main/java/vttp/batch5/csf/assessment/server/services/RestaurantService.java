package vttp.batch5.csf.assessment.server.services;

import java.text.ParseException;
import java.util.List;

import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jakarta.json.Json;
import jakarta.json.JsonArray;
import jakarta.json.JsonArrayBuilder;
import jakarta.json.JsonObject;
import vttp.batch5.csf.assessment.server.repositories.OrdersRepository;
import vttp.batch5.csf.assessment.server.repositories.RestaurantRepository;

@Service
public class RestaurantService {

  @Autowired
  private OrdersRepository ordersRepo;

  @Autowired
  private RestaurantRepository restaurantRepo;

  // TODO: Task 2.2
  // You may change the method's signature
  public JsonArray getMenu() {
    List<Document> documents = ordersRepo.getMenu();
    JsonArrayBuilder builder = Json.createArrayBuilder();
    for (Document d : documents) {
      JsonObject j = Json.createObjectBuilder()
          .add("id", d.getString("_id"))
          .add("name", d.getString("name"))
          .add("description", d.getString("description"))
          .add("price", d.getDouble("price"))
          .build();
      builder.add(j);
    }
    return builder.build();
  }

  // TODO: Task 4
  // add the order details to the server's database
  @Transactional
  public void insertIntoDatabase(JsonObject confirmation, String paymentResponse) throws ParseException{
    
    try{
      restaurantRepo.insertOrderSQL(paymentResponse, confirmation);
      ordersRepo.insertOrderMongo(confirmation, paymentResponse);
    } catch (Exception ex){
      ex.printStackTrace();
      throw ex;
    }
    

  }

  // @Transactional
  // public void _insertIntoDatabase(JsonObject confirmation, String paymentResponse) throws ParseException{

  //   // sql insertion
  //   restaurantRepo.insertOrderSQL(paymentResponse, confirmation);
  //   // mongo insertion
  //   ordersRepo.insertOrderMongo(confirmation, paymentResponse);

  // }

}
