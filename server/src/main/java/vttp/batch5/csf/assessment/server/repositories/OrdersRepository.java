package vttp.batch5.csf.assessment.server.repositories;

import java.io.StringReader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.logging.Logger;

import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.ProjectionOperation;
import org.springframework.data.mongodb.core.aggregation.SortOperation;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import jakarta.json.Json;
import jakarta.json.JsonArray;
import jakarta.json.JsonArrayBuilder;
import jakarta.json.JsonObject;
import jakarta.json.JsonReader;

import static vttp.batch5.csf.assessment.server.Utils.*;

@Repository
public class OrdersRepository {

  private static final Logger logger = Logger.getLogger(OrdersRepository.class.getName());

  @Autowired
  private MongoTemplate mongoTemplate;

  // TODO: Task 2.2
  // You may change the method's signature
  // Write the native MongoDB query in the comment below
  //
  // Native MongoDB query here
  // db.menu.find({}).sort({name: 1})
  public List<Document> getMenu() {
    Query query = Query.query(new Criteria())
        .with(Sort.by(Sort.Direction.ASC, "name"));
    return mongoTemplate.find(query, Document.class, "menu");
  }

  // TODO: Task 4
  // Write the native MongoDB query for your access methods in the comment below
  //
  // Native MongoDB query here
  // db.orders.insert({...})

  public void insertOrderMongo(JsonObject payload, String paymentResponse) throws ParseException {

    // toJson
    JsonReader reader = Json.createReader(new StringReader(paymentResponse));
    JsonObject j = reader.readObject();

    // process timestamp
    Date orderDate = convertTimestamp(j.getJsonNumber("timestamp").longValue());

    JsonArrayBuilder builder = Json.createArrayBuilder();
    JsonArray arr = payload.getJsonArray("items");
    for (int i = 0; i < arr.size(); i ++){
        JsonObject item = arr.getJsonObject(i);
        builder.add(item);
    }

    Document docToInsert = Document.parse(Json.createObjectBuilder()
      .add("_id", j.getString("order_id"))
      .add("order_id", j.getString("order_id"))
      .add("payment_id", j.getString("payment_id"))
      .add("username", payload.getString("username"))
      .add("timestamp", orderDate.toString())
      .add("items", builder.build())
      .build().toString());

    logger.info(">>> inserting into mongo: %s".formatted(docToInsert.toString()));
    mongoTemplate.insert(docToInsert, "orders");
  }
}
