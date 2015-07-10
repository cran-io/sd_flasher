package sdflash;

public class Supplier {

    private int id;
    private String name;
    private int wallet;
    private String apiToken;
    
    public Supplier(int id, String name, int wallet, String api){
        this.id = id;
        this.name = name;
        this.wallet = wallet;
        this.apiToken = api;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }
    
    public String getApiToken() {
        return apiToken;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getWallet() {
        return wallet;
    }

    public void setWallet(int wallet) {
        this.wallet = wallet;
    }
    
}
