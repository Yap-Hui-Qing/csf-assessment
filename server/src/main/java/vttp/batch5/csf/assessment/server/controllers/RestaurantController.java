package vttp.batch5.csf.assessment.server.controllers;

import java.io.StringReader;
import java.security.NoSuchAlgorithmException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import jakarta.json.Json;
import jakarta.json.JsonArray;
import jakarta.json.JsonObject;
import jakarta.json.JsonReader;
import vttp.batch5.csf.assessment.server.repositories.RestaurantRepository;
import vttp.batch5.csf.assessment.server.services.RestaurantService;

@Controller
@RequestMapping("/api")
public class RestaurantController {

  @Autowired
  private RestaurantService restaurantSvc;

  @Autowired
  private RestaurantRepository restaurantRepo;

  // TODO: Task 2.2
  // You may change the method's signature
  @GetMapping(path = "/menu", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<String> getMenus() {
    JsonArray arr = restaurantSvc.getMenu();
    return ResponseEntity.ok(arr.toString());
  }

  // TODO: Task 4
  // Do not change the method's signature
  @PostMapping(path = "/food_order", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<String> postFoodOrder(@RequestBody String payload) throws NoSuchAlgorithmException {

    // validate request
    // convert payload to json
    JsonReader reader = Json.createReader(new StringReader(payload));
    JsonObject j = reader.readObject();

    Boolean validation = restaurantRepo.validateUsername(j.getString("username"), j.getString("password"));
    if (!validation){
      JsonObject error = Json.createObjectBuilder()
        .add("message", "Invalid username and/or password")
        .build();
      return ResponseEntity.status(401).body(error.toString());
    }

    // place order
    JsonObject request = restaurantRepo.generatePaymentHttpRequest(j);

    // make payment using the payment service
    String response = restaurantRepo.makePayment(request);

    return ResponseEntity.ok(response);
  }


}
