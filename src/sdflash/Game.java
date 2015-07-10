package sdflash;

/**
 *
 * @author cran.io
 */

public class Game {

    private String name;
    private String description;
    private int IDpackage;
    private int version;
    
    public Game(String name, String description, int IDpackage, int version){
        this.name = name;
        this.description = description;
        this.IDpackage = IDpackage;
        this.version = version;
    }
    
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }
    
    public int getVersion(){
        return this.version;
    }
    
    public void setDescription(String description) {
        this.description = description;
    }

    public int getIDpackage() {
        return IDpackage;
    }

    public void setIDpackage(int IDpackage) {
        this.IDpackage = IDpackage;
    }
    
}
