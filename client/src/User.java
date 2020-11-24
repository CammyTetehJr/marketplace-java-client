public class User {

    private static int unique = 0;


    private int id;
    private String name;
    private String email;


    public int getId() {
        return id;
    }

    public int setId() {
        return this.id = unique++;
    }



    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }



    public User(String name, String email) {

        this.id = setId();
        this.name = name;
        this.email = email;


    }

    public User() {
    }
}
