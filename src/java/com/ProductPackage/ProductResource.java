/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ProductPackage;

import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.PathParam;
import javax.ws.rs.Consumes;
import javax.ws.rs.Produces;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PUT;
import static com.ProductPackage.Dbcredentials.getConnection;
import com.mysql.jdbc.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import javax.json.Json;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObjectBuilder;
import javax.ws.rs.POST;
import javax.ws.rs.QueryParam;
import static javax.ws.rs.client.Entity.json;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

/**
 * REST Web Service
 *
 * @author Hiren Patel
 */
@Path("product")
public class ProductResource {

     JsonObjectBuilder json = Json.createObjectBuilder();
    JsonArrayBuilder productJsonArray = Json.createArrayBuilder();
    ArrayList<Product> productList = new ArrayList<>();
    @Context
    private UriInfo context;

    /**
     * Creates a new instance of ProductResource
     */
    public ProductResource() {
    }

    /**
     * Retrieves representation of an instance of com.Package1.ProductResource
     *
     * @return an instance of java.lang.String
     */
    @GET
    @Path("/products")
    @Produces("application/json")
    public String getProducts() throws SQLException {
        //TODO return proper representation object
        // throw new UnsupportedOperationException();

        Connection conn = getConnection();
        if (conn == null) {
            //return "database connection error";
        }

        String query = "select * from product";
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery(query);

        while (rs.next()) {
            Product product = new Product(rs.getInt("productid"), rs.getString("name"), rs.getString("description"), rs.getInt("quantity"));
            productList.add(product);
            json = Json.createObjectBuilder()
                        .add("productid", rs.getInt("productid"))
                        .add("name", rs.getString("name"))
                        .add("description", rs.getString("description"))
                        .add("quantity", rs.getInt("quantity"));
               productJsonArray.add(json);

        }
        conn.close();
        String resultString = productJsonArray.build().toString();
        return resultString;
    }

    @GET
    @Path("/products/{id}")
    @Produces("application/json")
    public String getProductById(@PathParam("id") int productId) throws SQLException {
        //TODO return proper representation object
        // throw new UnsupportedOperationException();

        Connection conn = getConnection();
        if (conn == null) {
            //return "database connection error";
        }

        String query = "select * from product where productid=" + productId;
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery(query);

        while (rs.next()) {
            Product product = new Product(rs.getInt("productid"), rs.getString("name"), rs.getString("description"), rs.getInt("quantity"));
            productList.add(product);
            
             json = Json.createObjectBuilder()
                        .add("productID", rs.getInt("productID"))
                        .add("name", rs.getString("name"))
                        .add("description", rs.getString("description"))
                        .add("quantity", rs.getInt("quantity"));
               productJsonArray.add(json);
        }

       String resultString = productJsonArray.build().toString();
        conn.close();
        return resultString;

        
   
        
    }

    @POST
    @Path("/products")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response insertProduct(String product) throws ParseException, SQLException {
        JSONParser parser = new JSONParser();
        JSONObject json = (JSONObject) parser.parse(product);

        Object pid = json.get("productid");
        String strProductId = pid.toString();
        int productId = Integer.parseInt(strProductId);

        Object name = json.get("name");
        String strProductName = name.toString();

        Object description = json.get("description");
        String strProductDescription = description.toString();

        Object quantity = json.get("quantity");
        String strProductQuantity = quantity.toString();
        int productQuantity = Integer.parseInt(strProductQuantity);

        Connection conn = getConnection();
        Statement smt = conn.createStatement();
        smt.executeUpdate("insert into  product values ('" + productId + "','" + strProductName + "','" + strProductDescription + "','" + productQuantity + "' )");

        return Response.status(Status.CREATED).build();
    }

    /**
     * PUT method for updating or creating an instance of ProductResource
     *
     * @param content representation for the resource
     * @return an HTTP response with content of the updated or created resource.
     */
    @PUT
    @Path("/products")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public void updateProduct(@QueryParam("id") int id, @QueryParam("name") String name, @QueryParam("description") String description, @QueryParam("quantity") int quantity) throws SQLException {
       
        Connection conn = getConnection();
        Statement stmt = conn.createStatement();
        
        ResultSet rs = stmt.executeQuery("update product set productid ="+ id +", name =" + name + ", description =" + description + ", quantity =" + quantity + " where productid ="+ id);
    }
}
