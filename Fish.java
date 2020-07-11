package fishtank;
import java.awt.Color;
import java.awt.Graphics;
import java.util.Random;
/**
 * The Fish class for question 1 of the assignment
 * 
 * @author Ryan Herkt (ID: 18022861)
 */
public class Fish implements Runnable
{
    private double x, y;
    private double dx, dy;
    private double size;
    private Random random = new Random();
    private boolean isAlive;
    public static int world_width, world_height;
    private Color[] colour;
    private FishShoal shoal;
    
    /**
     * This is a constructor which instantiates a new fish object, with reference 
     * to a FishShoal collection (i.e. a List).
     * @param shoal 
     */
    public Fish(FishShoal shoal)
    {
        this.shoal = shoal; //define the shoal the fish lives in
        this.colour = new Color[3]; //create colour array
        this.size = (double)random.nextInt(41)+10; //10 to 50
        
        do
        {
            this.dx = (double)random.nextInt(7)-3; //-3 to 3
            this.dy = (double)random.nextInt(7)-3; //-3 to 3
        } while(dx != 0 || dy!= 0);
        
        this.x = Fish.world_width/2.0;
        this.y = Fish.world_height/2.0;
        
        isAlive = true;
        
        for (int i = 0; i < 3; ++i)
        {
            this.colour[i] = new Color(random.nextFloat(), random.nextFloat(), random.nextFloat());
        }  
    }
        
    /**
     * This run method is called when a new thread is created by pressing the 
     * 'Add Fish' button in the GUI. While the fish is alive (represented by a 
     * boolean), the move method is called, allowing the fish to move about in 
     * the shoal. Randomized noise factors are also created too.
     */
    @Override
    public void run() 
    {
        int moves = 0;  //count number of moves a fish has performed
        while(isAlive)
        {    
            //Improvement: a bit lighter on sync; am no longer syncing entire
            //run method.
            move();
            ++moves;

            //create random noise factors based on number of moves. 
            //I chose 20 as the limit because I felt it was the perfect point 
            //between not enough moves (where a fish's movement hinders 
            //their progress) and too many moves (where a fish's 
            //movement would be too much like a straight line):
            if (moves > 20)
            {
                if (random.nextInt(2) == 0)
                {
                    if (random.nextInt(2) == 0)
                    {
                        if (dx < 3) //keep dx below its max limit
                            this.dx += 0.5;
                        else    //otherwise do the opposite & increment
                            this.dx -= 0.5;
                    }
                    else
                    {
                        if (dy < 3) //keep dy below its max limit
                            this.dy += 0.5;
                        else    //otherwise do the opposite & increment
                            this.dy -= 0.5;
                    }
                }
                else
                {
                    if (random.nextInt(2) == 0)
                    {
                        if (dx > -3)    //keep dx above its min limit
                            this.dx -= 0.5;
                        else    //otherwise do the opposite & increment
                            this.dx += 0.5;
                    }
                    else
                    {
                        if (dy > -3)    //keep dy above its min limit
                            this.dy -= 0.5;
                        else    //otherwise do the opposite & increment
                            this.dy += 0.5;
                    }
                }
                moves = 0;  //reset
            }
            try 
            {
                Thread.sleep(5);
            } 
            catch (InterruptedException ex) {}
        }
    }
    
    /**
     * 
     * @return the x coordinate of the fish
     */
    public double getX()
    {
        return this.x;
    }
    
    /**
     * 
     * @return the y coordinate of the fish
     */
    public double getY()
    {
        return this.y;
    }
    
    /**
     * 
     * @return the size of the fish
     */
    public double getSize()
    {
        return this.size;
    }
    
    /**
     * Removes the fish from the shoal, and therefore the panel
     */
    public void kill()
    {
        shoal.remove(this);
    }
    
    /**
     * This method provides the logic and mathematical equations which change 
     * the fish's x and y locations
     */
    private void move()
    {
        if (dx == 0 || dy == 0)
        {
            if (dx == 0)
                dx = (double)random.nextInt(7)-3; //-3 to 3
            else
                dy = (double)random.nextInt(7)-3; //-3 to 3
        }
        
        if (y >= world_height || y <= 0)
        {
            dy *= -1;
        }
        if (x >= world_width || x <= 0)
        {
            dx *= -1;
        }
        
        synchronized(this)
        {
            eat(this);
        }
        x += dx;
        y += dy;
    }
    
    /**
     * Adds fish to the panel
     * @param g 
     */
    public void draw(Graphics g)
    {
        double speed = Math.sqrt((Math.pow(dx, 2)) + (Math.pow(dy, 2)));
        double velX = (this.size * dx) / (2 * speed);
        double velY = (this.size * dy) / (2 * speed);
        
        g.setColor(this.colour[0]);
        g.drawLine((int)(x - velX + velY), (int)(y - velX - velY), (int)x, (int)y);
        g.setColor(this.colour[1]);
        g.drawLine((int)(x - 2 * velX), (int)(y - 2 * velY), (int)x, (int)y);
        g.setColor(this.colour[2]);
        g.drawLine((int)(x - velX - velY), (int)(y + velX - velY), (int)x, (int)y);
    }
    
    /**
     * Checks if the parameter fish can be 'eaten' by the fish which calls 
     * this method
     * @param target 
     */
    public void eat(Fish target)
    {
        synchronized(this)
        {
            target = shoal.canEat(this);
        }
        
        if (target != null)
        {
            //Set a limit. Any fish bigger than this can no longer grow
            //Set size prior to killing the target fish
            if (this.size < 75)
            {
                double aDouble = (double)random.nextInt(1)+1;   //double from 1 to 2, typecast
                this.size += target.size/aDouble;               //from target.size to 0.5*target.size
                
                if (this.size > 75) //incase the increase goes beyond the 75 limit
                    this.size = 75;
            }
            
            target.isAlive = false;
            target.kill();
        }
    }
}