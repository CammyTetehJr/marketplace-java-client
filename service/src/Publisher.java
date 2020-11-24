
import org.glassfish.jersey.jdkhttp.JdkHttpServerFactory;
import org.glassfish.jersey.server.ResourceConfig;
import javax.ws.rs.core.UriBuilder;
import java.net.URI;

public class Publisher {

    public static void main(String[] args)
    {
        int port = 9090;
        URI baseUri = UriBuilder.fromUri("http://localhost/").port(port).build();
        ResourceConfig resourceConfig1 = new ResourceConfig(ProductResources.class);
        JdkHttpServerFactory.createHttpServer(baseUri,resourceConfig1,true);

        System.out.println("hosting service at : http://localhost:"+ port);


    }

}
