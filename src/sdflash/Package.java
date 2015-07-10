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
    
    public Package(String name, int value, int priceValue)
    {
        this.name = name;
        this.id = value;
        this.price = priceValue;
        int priceCents = (priceValue % 100);
        int priceDecimal = priceValue / 100;
        this.priceText = Integer.toString(priceDecimal)+"."+Integer.toString(priceCents);
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
    
}
