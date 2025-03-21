package vttp.batch5.csf.assessment.server.repositories;

import java.io.StringReader;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;
import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;
import org.springframework.web.client.RestTemplate;

import jakarta.json.Json;
import jakarta.json.JsonArray;
import jakarta.json.JsonObject;
import jakarta.json.JsonReader;

import static vttp.batch5.csf.assessment.server.Utils.*;

@Repository
// Use the following class for MySQL database
public class RestaurantRepository {

    private static final Logger logger = Logger.getLogger(RestaurantRepository.class.getName());
    private static final String PAYEE_NAME = "Yap Hui Qing";
    private static final String PAYMENT_GATEWAY = "https://payment-service-production-a75a.up.railway.app/api/payment";

    @Autowired
    private JdbcTemplate template;

    private static final String CHECK_USERNAME = """
            select * from customers where username = ?
            """;

    private static final String INSERT_ORDER = """
            insert into place_orders (order_id, payment_id, order_date, total, username) 
            values (?, ?, ?, ?, ?);
            """;

    // validate username and password
    // check if username exists
    public Boolean validateUsername(String username, String password) throws NoSuchAlgorithmException {
        SqlRowSet rs = template.queryForRowSet(CHECK_USERNAME, username);
        if (!rs.next()) {
            return false;
        } else {
            String actualPassword = rs.getString("password");
            if (encryptPassword(password).equals(actualPassword))
                return true;
            return false;
        }
    }

    // create request body for payment service
    public JsonObject generatePaymentHttpRequest(JsonObject j){

        // generate a random 8 character string orderId
        String orderId = UUID.randomUUID().toString().substring(0,8);

        // calculate the total amount
        JsonArray arr = j.getJsonArray("items");
        float payment = 0;
        for (int i = 0; i < arr.size(); i ++){
            JsonObject item = arr.getJsonObject(i);
            int quantity = item.getInt("quantity");
            float price = (float) item.getJsonNumber("price").doubleValue();
            payment += (float) (quantity * price);
        }

        JsonObject obj = Json.createObjectBuilder()
            .add("order_id", orderId)
            .add("payer", j.getString("username"))
            .add("payee", PAYEE_NAME)
            .add("payment", payment)
            .build();
        logger.info(">>> payload for payment: %s".formatted(obj.toString()));
        return obj;
    }

    public String makePayment(JsonObject payload){

        // create a request
        RequestEntity<String> req = RequestEntity
            .post(PAYMENT_GATEWAY)
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON)
            .header("X-Authenticate", payload.getString("payer"))
            .body(payload.toString(), String.class);

        RestTemplate template = new RestTemplate();

        ResponseEntity<String> resp = template.exchange(req, String.class);
        String body = resp.getBody();
        System.out.printf(">>> %s\n", payload);
        return body;

    }

    public void insertOrderSQL(String paymentResponse, JsonObject confirmation) throws ParseException{
        
        // toJson
        JsonReader reader = Json.createReader(new StringReader(paymentResponse));
        JsonObject j = reader.readObject();

        // process timestamp
        Date orderDate = convertTimestamp(j.getJsonNumber("timestamp").longValue());

        template.update(INSERT_ORDER, j.getString("order_id"), 
            j.getString("payment_id"), orderDate, 
            j.getJsonNumber("total").doubleValue(), confirmation.getString("username"));
    }

    public String encryptPassword(String input) throws NoSuchAlgorithmException{
        // getInstance() method is called with algorithm SHA-224 
        MessageDigest md = MessageDigest.getInstance("SHA-224"); 

        // digest() method is called 
        // to calculate message digest of the input string 
        // returned as array of byte 
        byte[] messageDigest = md.digest(input.getBytes()); 

        // Convert byte array into signum representation 
        BigInteger no = new BigInteger(1, messageDigest); 

        // Convert message digest into hex value 
        String hashtext = no.toString(16); 

        // Add preceding 0s to make it 32 bit 
        while (hashtext.length() < 32) { 
            hashtext = "0" + hashtext; 
        } 

        logger.info("Hashed password: %s".formatted(hashtext));
        // return the HashText 
        return hashtext;
    }

}
