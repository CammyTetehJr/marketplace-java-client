import javax.inject.Singleton;
import javax.ws.rs.*;
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("Duplicates")
@Singleton
@Path("products")
public class ProductResources {

    private List<Product> productList = new ArrayList<>();
    private List<User> userList = new ArrayList<>();

    public ProductResources() {
        User u = new User("Michaels","michael@gmail.com");
        addToUserList(u);
        Product p1 = new Product(100,"iphone7","gold red color used",250,u);
        addToProductList(p1);

        u = new User("Rijk","rijk@gmail.com");
        addToUserList(u);
        Product p2 = new Product(101,"iphone10","Used phone, silver color",750,u);
        addToProductList(p2);

        u = new User("Jean","jean@gmail.com");
        addToUserList(u);
        Product p3 = new Product(102,"Samsung s9+","Used phone with warranty",250,u);
        addToProductList(p3);

        u = new User("Corne","corne@gmail.com");
        addToUserList(u);
        Product p4 = new Product(103,"Nokia","With carton and accessories",30,u);
        addToProductList(p4);

        u = new User("Nienke","nienke@gmail.com");
        addToUserList(u);
        Product p5 = new Product(104, "Sony","minor scratches",80,u);
        addToProductList(p5);

        u = new User("Madelon","madelon@gmail.com");
        addToUserList(u);
        Product p6 = new Product(105,"Blackberry","gift from friend",50,u);
        addToProductList(p6);

        u = new User("Cammy","cammy@gmail.com");
        addToUserList(u);
        Product p7 = new Product(106,"Nexus","Like brand new",150,u);
        addToProductList(p7);

        u = new User("Dangote","dangote@gmail.com");
        addToUserList(u);
        Product p8 = new Product(107,"Tecno","No scratches",350,u);
        addToProductList(p8);

        u = new User("Fenne","fenne@gmail.com");
        addToUserList(u);
        Product p9 = new Product(108,"iphone x","Used for one month",1250,u);
        addToProductList(p9);

        u = new User("Janine","janine@gmail.com");
        addToUserList(u);
        Product p10 = new Product(109,"iphone7","Used phone",250,u);
        addToProductList(p10);
    }

    private void addToUserList(User u)
    {
        userList.add(u);
    }
    private void addToProductList(Product p)
    {
        productList.add(p);
    }


    @GET
    @Path("test")
    @Produces(MediaType.TEXT_PLAIN)
    public String test()
    {
        return "hello your service works!";
    }

    @GET //GET at http://localhost:9090/products/count
    @Path("count")
    @Produces(MediaType.TEXT_PLAIN)
    public int GetNumberOfProducts()
    {
        return productList.size();
    }

    @GET //GET at http://localhost:9090/products/first
    @Path("first")
    @Produces(MediaType.APPLICATION_JSON)
    public Response first()
    {
        Product entity;
        if(productList.size() > 0)
        {
            entity = productList.get(0);
            return Response.ok(entity).build();

        }
        else
        {
            return Response.serverError().entity("There are no products in the list").build();

        }

    }

    @GET // http://localhost:9090/students/all
    @Path("all")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getProducts()
    {
        List<Product> products = new ArrayList<Product>();
        products = getAllProducts();
        if(products != null){
            GenericEntity<List<Product>> entity = new GenericEntity<List<Product>>(products) {};
            return Response.ok(entity).build();
        }
        else{
            return Response.serverError().entity("No products in the List").build();
        }
    }

    private List<Product> getAllProducts()
    {
        List<Product> temp = null;
        if(productList.size() > 0){
            temp = new ArrayList<>();
            for(Product p: productList)
            {
                temp.add(p);
            }
        }
        return temp;
    }

    private Product getProduct(int id)
    {
        //Product product = null;
        for(Product p: getAllProducts())
        {
            if(p.getId() == id){
                //product = p;
                return p;
            }
        }
        //return product;
        return null;
    }
    @GET // http://localhost:9090/products/?id
    public Response getProductQuery(@QueryParam("id") int idNr) throws Exception
    {
        Product product = getProduct(idNr);
        if(product == null){
            return Response.serverError().entity("No Product with that ID").build();
        }
        return Response.ok(product).build();
    }

    @GET //http:localhost:9090/products/{id}
    @Path("{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getProductPath(@PathParam("id") int idNr) throws Exception
    {
        Product entity = getProduct(idNr);
        if(entity == null){
            return Response.serverError().entity("No product found with that ID").build();
        }
        return Response.ok(entity).build();
    }



    @DELETE //http:localhost:9090/products/{id}
    @Path("{id}")
    public Response deleteProduct(@PathParam("id") int id) throws Exception
    {
        Product product = getProduct(id);
        if(product != null)
        {
            if(checkProduct(id))
            {
                productList.remove(product);

            }
            return Response.noContent().build();
        }
        else
        {
            return Response.serverError().entity("Cannot find product with id nr: " + id).build();

        }
    }
    @POST //http://localhost:9090/products/
    @Consumes(MediaType.APPLICATION_JSON)
    public Response createProduct(Product product) throws Exception
    {
        Product p = getProduct(product.getId());
        if(p == null)
        {
            productList.add(p);
            return Response.noContent().build();
        }
        else
        {
            return Response.serverError().entity("Error with post/create, product already exists with id: " +
                    product.getId()).build();

        }
    }

    @PUT // at http://localhost:9090/products/
    @Consumes(MediaType.APPLICATION_JSON)
    public Response updateProduct(Product product) throws Exception
    {
        Product p = getProduct(product.getId());
        if (p != null)
        {
            p.setName(product.getName());
            p.setDescription(product.getDescription());
            p.setPrice(product.getPrice());
            p.setAvailable(true);
            LocalDate ldt = LocalDate.now();
            p.setOfferDate(ldt);
            p.setSellDate(null);

            return Response.noContent().build();
        }
        else
        {
            return Response.serverError().entity("Error: put/update, product with id: " +
                    product.getId() + " does not exist!").build();

        }

    }

    @POST //Post at http://localhost:9090/products/{entity}
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    public Response createProductForm(@FormParam("name") String name,
    @FormParam("description") String description,@FormParam("price") double price,@FormParam("uname") String
                                      uname,@FormParam("uemail") String uemail,@FormParam("id") int id) throws Exception
    {

            User u = new User(uname,uemail);

            Product product = new Product(id,name,description,price,u);
            product.setAvailable(true);
            product.setOfferDate(LocalDate.now());
            product.setSellDate(null);
            productList.add(product);
            return Response.noContent().build();


    }
    @Path("update")
    @PUT //Put at http://localhost:9090/products/{entity}
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    public Response updateProductForm(@FormParam("name") String name,
                                      @FormParam("description") String description,@FormParam("price") double price,@FormParam("uname") String
                                              uname,@FormParam("uemail") String uemail,@FormParam("id") int id) throws Exception
    {
        Product p = getProduct(id);
        if(p != null)
        {
            Product productToUpdate = p;
            productToUpdate.setName(name);
            productToUpdate.setDescription(description);
            productToUpdate.setPrice(price);

            return Response.noContent().build();
        }

        else
        {
            return Response.serverError().entity("Error: put/update, product with id: " +
                    id + " does not exist!").build();
        }



    }

    private boolean checkProduct(int id)
    {
        for(Product p: productList)
        {
            if(p.getId() == id && p.getAvailable()){
                return true;
            }
        }
        return false;
    }








}
