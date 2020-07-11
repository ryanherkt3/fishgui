package fishtank;
import java.awt.Graphics;
import java.util.ArrayList;
import java.util.List;
/**
 * The FishShoal class for question 1 of the assignment.
 * 
 * @author Ryan Herkt (ID: 18022861)
 */
public class FishShoal 
{
    private List<Fish> fishList;
    
    public FishShoal()
    {
        this.fishList = new ArrayList<>();
    }
    
    public void add(Fish fish)
    {
        fishList.add(fish);
    }
    
    public synchronized void remove(Fish fish)
    {
        System.out.println("Fish killed!"); //verification check to console
        fishList.remove(fish);
    }
    
    public void drawShoal(Graphics g)
    {
        synchronized(this)
        {
            for (Fish fish : fishList)
            {
                fish.draw(g);
            }   
        }
    }
    
    public synchronized Fish canEat(Fish fish)
    {
        for (Fish target: fishList)
        {
            double aveSize = 0.0;
            double ratio = 0.0;
            double xRange = 0.0;
            double yRange = 0.0;
            
            if (fishList.size() <= 1)
                return null;
            else
            {
                ratio = fish.getSize() / target.getSize();
                aveSize = 0.5 * (fish.getSize() + target.getSize());
                xRange = fish.getX() - target.getX();
                yRange = fish.getY() - target.getY();
            }
            
            /* eg aveSize = 30. -30 <= xRange <= 30. xRange must be within both 
            the positive and negative bounds for the condition to be true */
            if (ratio >= 1.4 && (-aveSize <= xRange && xRange <= aveSize) && (-aveSize <= yRange && yRange <= aveSize))
            {
                return target;
            }
        }
        
        return null;    //if no fish can be found
    }
}