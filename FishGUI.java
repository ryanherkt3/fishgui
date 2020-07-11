package fishtank;
/**
 * The FishGUI class for question 1 of the assignment, which contains a 
 * constructor for the GUI, as well as several helper methods and a main method 
 * which allow the GUI to run.
 * 
 * @author Ryan Herkt (ID: 18022861)
 */
import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.Timer;

public class FishGUI extends JPanel implements ActionListener
{
   private DrawPanel drawPanel;
   private JButton addFishButton;
   private FishShoal shoal = new FishShoal();
   private Thread aThread;
    
   public FishGUI()
   {  
      super(new BorderLayout());
      
      drawPanel = new DrawPanel();
      add(drawPanel, BorderLayout.CENTER);
      
      JPanel buttonPanel = new JPanel();
      addFishButton = new JButton("Add Fish");
      addFishButton.addActionListener(this);
      
      buttonPanel.add(addFishButton);
      add(buttonPanel, BorderLayout.SOUTH);
      
      Timer timer = new Timer(25, this);
      timer.start();
   }
   
   @Override
   public void actionPerformed(ActionEvent e)
   {  
       Object source = e.getSource();
       if (source == addFishButton)
       {
           System.out.println("Adding fish...");
           Fish fish = new Fish(shoal);
           shoal.add(fish);
           aThread = new Thread(fish);
           aThread.start();
       }
       
       drawPanel.repaint();
   }
   
   private class DrawPanel extends JPanel
   {
       public DrawPanel()
       {
           super();
           setPreferredSize(new Dimension(500, 500));
           setBackground(Color.white);
       }
       
       @Override
       public void paintComponent(Graphics g)
       {
           super.paintComponent(g);
           //make lines of fish thicker:
           Graphics2D g2 = (Graphics2D) g;
           g2.setStroke(new BasicStroke(3));
           //place fish world_width/height variables here so the frame size 
           //is dynamic rather than static. This allows the shoal to resize, 
           //meaning the fish are never bound to the original window size:
           Fish.world_width = drawPanel.getWidth();
           Fish.world_height = drawPanel.getHeight();
           shoal.drawShoal(g);
       }
   }
   
   public static void main(String[] args)
   {  
      JFrame frame = new JFrame("Fish Bowl");
      // kill all threads when frame closes
      frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
      frame.getContentPane().add(new FishGUI());
      frame.pack();
      // position the frame in the middle of the screen
      Toolkit tk = Toolkit.getDefaultToolkit();
      Dimension screenDimension = tk.getScreenSize();
      Dimension frameDimension = frame.getSize();
      frame.setLocation((screenDimension.width-frameDimension.width)/2,
         (screenDimension.height-frameDimension.height)/2);
      frame.setVisible(true);
   }
}