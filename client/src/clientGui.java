import org.glassfish.jersey.client.ClientConfig;

import javax.ws.rs.client.*;
import javax.ws.rs.core.*;
import java.net.URI;
import java.util.ArrayList;
import java.util.Scanner;



public class clientGui {

    static ClientConfig config = new ClientConfig();
    static Client client = ClientBuilder.newClient(config);
    static URI baseUri = UriBuilder.fromUri("http://localhost:8080/marketplace/rest/products/").build();
    static  WebTarget serviceTarget = client.target(baseUri);

    public static void getWelcomeMessage()
    {
        printMessage("Welcome");
    }
    public static void printMessage(String message)
    {
        System.out.println(message);
    }


    public static void getProductCount()
    {

        URI baseUri = UriBuilder.fromUri("http://localhost:8080/marketplace/rest/products/").build();
        serviceTarget = client.target(baseUri);
        Invocation.Builder requestBuilder = serviceTarget.path("count").request().accept(MediaType.TEXT_PLAIN);
        Response response = requestBuilder.get();

        if(response.getStatus() == 200)
        {
            Integer entity = response.readEntity(Integer.class);
            printMessage("We have "+ entity + " used phones for sale");
            printMessage("");

        }
        else
        {
            printMessage(response.toString());
        }



    }

    public static void getAllProducts()
    {
        WebTarget serviceTarget = client.target(baseUri);
        Invocation.Builder requestBuilder = serviceTarget.path("all").request().accept(MediaType.APPLICATION_JSON);
        Response response = requestBuilder.get();

        if(response.getStatus() == 200)
        {
            GenericType<ArrayList<Product>> genericType = new GenericType<>(){};
            ArrayList<Product> entity = response.readEntity(genericType);
            for(Product product: entity)
            {
                printMessage("id: "+" "+product.getId()+ ", Name: " +product.getName() + ", Description: " + product.getDescription() + ", Price: " +
                        product.getPrice() + ", Offer date: " + product.getOfferDate() + ", Seller:  " + product.getSeller().getName() );
            }

            printMessage("");
        }
    }

    public static void askCustomerStatus()
    {
        printMessage("Type 1 to buy a used phone or 2 to sell a used a phone");
        printMessage("Type 3 to see available phones");
        printMessage("Type 4 to update phone details you put up for sell");

        Scanner sc = new Scanner(System.in);
        int i = sc.nextInt();

        if(i == 1){
            printMessage("Type the id of the phone you want to buy :");
            int id = sc.nextInt();
            buyProduct(id);
            askCustomerStatus();
        }
        else if(i == 2)
        {
            sellProduct();
            getAllProducts();
            askCustomerStatus();
        }
        else if(i == 3)
        {
            getAllProducts();
            askCustomerStatus();
        }

        else if(i == 4)
        {
            updateProduct();
        }

    }

    public static void buyProduct(int id)
    {
        String pid = String.valueOf(id);
        WebTarget resourceTarget = serviceTarget.path(pid); // request resource with id
        Invocation.Builder requestBuilder = resourceTarget.request().accept(MediaType.TEXT_PLAIN);
        Response response = requestBuilder.delete();

        if(response.getStatus() == 204)
        {
            printMessage("Order successful, you will be sent an email to pay");
            printMessage("");
        }
        else
            {
                printMessage("Problem with buying product");
            }
    }

    public static void sellProduct()
    {
        printMessage("To sell a phone you need an account");
        printMessage("Name:");
        Scanner sc = new Scanner(System.in);
        String userName = sc.nextLine();
        printMessage("email:");
        String userEmail = sc.nextLine();

        User u = new User(userName,userEmail);
        printMessage("user: "+ userName +" created");
        printMessage("What do you wish to sell ?");
        printMessage("Name of phone to sell:");
        sc = new Scanner(System.in);
        String productName = sc.nextLine();
        printMessage("Unique Product ID :");
        sc = new Scanner(System.in);
        int productId = sc.nextInt();
        printMessage("Description of phone: ");
        sc = new Scanner(System.in);
        String pDescription = sc.nextLine();
        printMessage("Price of phone: ");
        double pPrice = sc.nextDouble();

        Product product = new Product(productId,productName,pDescription,pPrice,u);
        Form form = new Form();
        form.param("name",product.getName());
        form.param("id",String.valueOf(product.getId()));
        form.param("description",product.getDescription());
        form.param("price",String.valueOf(product.getPrice()));
        form.param("uname",u.getName());
        form.param("uemail",u.getEmail());

        Entity<Form> entity = Entity.entity(form,MediaType.APPLICATION_FORM_URLENCODED);
        Response response = serviceTarget.request().accept(MediaType.TEXT_PLAIN).post(entity);

        if(response.getStatus() == 204)
        {
            printMessage("success");
               printMessage(productName + " now available for sale");
               printMessage("");
               askCustomerStatus();
        }
        else
            {
                printMessage("Error approving phone for sale");
                printMessage((String.valueOf(response.getStatus())));
            }
    }


    public static boolean verifyUser(String email, String name, Product p)
    {
        String ownerName = p.getSeller().getName();
        String ownerEmail = p.getSeller().getEmail();

        if(ownerName.equals(name) && ownerEmail.equals(email))
        {
            return  true;
        }
        else
        {
           return false;
        }

    }

    public static void updateProduct()
    {
        printMessage("Type the id of the product you wish to update ?");
        Scanner sc = new Scanner(System.in);
        String id = sc.nextLine();
        Invocation.Builder builder = serviceTarget.path(id).request().accept(MediaType.APPLICATION_JSON);
        Response response = builder.get();

        Product entity = null;
        if(response.getStatus() == 200)
        {
            entity = response.readEntity(Product.class);
        }
        printMessage(entity.getName());

        printMessage("To verify you can only update your product please give some details");
        printMessage("What is your name ?");
        String name = sc.nextLine();
        printMessage("what is your email ?");
        String email = sc.nextLine();

        Boolean verify = verifyUser(email,name,entity);
        if(verify)
        {
            printMessage("verification success!");
            printMessage("Update details of Product: " + entity.getName());

            printMessage("Now you can update the product");
            printMessage("product name: ");
            String pName = sc.nextLine();
            printMessage("Product description: ");
            String pDesc = sc.nextLine();
            printMessage("Update Product Price: ");
            Double pPrice = sc.nextDouble();
            printMessage("Updating ......");
            entity.setName(pName);
            entity.setDescription(pDesc);
            entity.setPrice(pPrice);

            Entity <Product> pEntity = Entity.entity(entity,MediaType.APPLICATION_JSON);
            response =  serviceTarget.request().accept(MediaType.TEXT_PLAIN).put(pEntity);

            if(response.getStatus() == 204)
            {
                printMessage("Product Updated!");
                askCustomerStatus();
            }
            else
            {
                printMessage(response.toString());
                askCustomerStatus();
            }
        }
        else
        {
            printMessage("Not allowed to update a product that's not yours!");
            askCustomerStatus();
        }


    }


    public static void main(String[] args) {
        clientGui clientGui = new clientGui();
        getWelcomeMessage();
        getProductCount();
        getAllProducts();
        askCustomerStatus();
    }







}



