package sdflash;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author cran.io
 */

class Package
{
    private final String name;
    private final int id;
    private final int price;
    private String priceText;
    private int[] gamesInPackage;
    int gamesCounter = 0;
    
    public Package(String name, int value, int priceValue)
    {
        this.name = name;
        this.id = value;
        this.price = priceValue;
        float priceShow = (float) price / 100;
        this.priceText = Float.toString(priceShow);
    }

    @Override
    public String toString()
    {
        return name;
    }

    public int getId()
    {
        return id;
    }
    
    public String getPrice(){
        return priceText;
    }
    
    public int getFullPrice(){
        return price;
    }

    public void setSizeOfGames(int size) {
        gamesInPackage = new int[size];
    }

    public void addGame(int k, int aInt) {
        gamesInPackage[gamesCounter] = aInt;
        gamesCounter++;
    }

    public int getSizeOfGames() {
        return this.gamesCounter;
    }
    
    public int[] getGamesInPackage(){
        return this.gamesInPackage;
    }
    
}
