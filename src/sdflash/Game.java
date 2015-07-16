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
    private String shortDescription;
    private String logoURL;
    private String versionDescription;
    private String company;
    private String apk_link;
    private String[] images;
    
    public Game(String name, String description, int IDpackage, int version, String shortDescription, String logoURL, String versionDescription, String company, String apk_link, String[] imagesURL){
        this.name = name;
        this.description = description;
        this.IDpackage = IDpackage;
        this.version = version;
        this.shortDescription = shortDescription;
        this.logoURL = logoURL;
        this.versionDescription = versionDescription;
        this.company = company;
        this.apk_link = apk_link;
        this.images = imagesURL; 
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

    public void setVersion(int version) {
        this.version = version;
    }

    public String getShorDescription() {
        return shortDescription;
    }

    public void setShorDescription(String shorDescription) {
        this.shortDescription = shorDescription;
    }

    public String getLogoURL() {
        return logoURL;
    }

    public void setLogoURL(String logoURL) {
        this.logoURL = logoURL;
    }

    public String getVersionDescription() {
        return versionDescription;
    }

    public void setVersionDescription(String versionDescription) {
        this.versionDescription = versionDescription;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String getApk_link() {
        return apk_link;
    }

    public void setApk_link(String apk_link) {
        this.apk_link = apk_link;
    }

    public String[] getImages() {
        return images;
    }

    public void setImages(String[] images) {
        this.images = images;
    }
    
}
