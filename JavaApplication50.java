/* Java Miniproject - MINESWEEPER using Swing 
Note :- It's not Applet, it's swing application.
Applet needs a different browser to run while Swing can run independently.
JFrame class needs to be inherited for swing application.
First learn how to play minesweeper then it will be a piece of cake.

Algorithm :-
    Start
    Create an empty window
    Set label to show if you are winner
    Set Panel(Group of components) of 10x10 buttons
    Set bombs at maximum 20 random positions
    Set numbers for cells by calculating number of bombs around it
    Set clicking events to see if bomb is clicked
    Count how many right cells were clicked which should be 100(total) - 20(bombs) = 80(right places)
    If all right cells were clicked, label changes to 'You are winner'
    Wait for 5 seconds before closing the window
    Stop

Star notes are mensioned here. Read them for more details.

* - Events are objects that are created when something in GUI changes. eg. mouse click, mouse hover, key pressed etc
** - Here, game is only of 10x10 grid, but 12x12 grid is choosen and the edge are numbered as 0; so that when we start counting number of bombs around each cell using for loop, we can use (i-1), (j-1) or (i+1), (j+1) elements. If we don't have extra rows/columns, it will show array out of bound exception.
*** - JButton() accepts one argument that is label of button, we want a clear button in the beginning so we just pass "" string.
**** - Name is different property than Label. Label is reflected on screen where as name is just internal property used for programming.
***** - ActionListener is called when button gets clicked but mouseListener is called because we want to check if right click was pressed, if so, we will set red color to flag the cell
****** - Suppose number is 26, y=26 % 10 = 6 because 6 is remainder which is last digit. 26 / 10 = 2.6 which gets casted into int so answer is 2. which is first number.

*/

package javaapplication50;      //package declaration (not necessory here because whole code is in only one file)

import java.util.Random;        //for generating random number to set bombs
import java.util.concurrent.TimeUnit;       //To countdown for 5 seconds after winner/loser declaration to exit window
import java.awt.event.MouseListener;        //To handle mouse events
import java.awt.event.MouseEvent;           //To raise mouse events (See star note *)
import java.awt.event.ActionListener;       //To handle button clicked event
import java.awt.event.ActionEvent;          //To raise clicking event
import javax.swing.*;                       //Main componets for swing application eg JFrame, JPanel, JButtons
import java.awt.*;                          //AWT(Abstract Windowing Toolkit) for basic GUI and graphics


class Board extends JFrame implements ActionListener        //To create board layout and the whole window/Base for game
{
    JFrame frame = new JFrame("MineSweeper");       //Creates a new empty window with title bar
    JPanel panel = new JPanel();                    //Creates an empty panel to group buttons into single unit
    JButton[][] button = new JButton[12][12];       //Adding 12x12 button grid (See star note **)
    JLabel label = new JLabel("");                  //Creates label to show winner title                  
    Font f1 = new Font("Arial", Font.BOLD, 30);     //Fonts used for panel
    Font f2 = new Font("Arial", Font.BOLD, 40);     //Fonts used for buttons
    int correct = 0;                                //Total number of right cells clicked
    
    public Board()             //Sets up the new board for new game
    {   
        panel.setLayout(new GridLayout(12,12));     //Set grid layout for our 12x12 buttons in a panel and arrange them neatly
        panel.setFont(f1);      //Set f1 fonts in panel
        label.setFont(new Font("Cooper Black", Font.BOLD, 50));     //Sets Cooper black fonts for label     
        for(int i = 0; i < 12; i++)     
        {
            for(int j = 0; j < 12; j++)
            {
                button[i][j] = new JButton("");     //Initialize every button with no name (See Star note ***)
                button[i][j].setName("");           //Sets name of button as blank (See star note ****)
                button[i][j].setFont(f2);           //Sets f2 fonts to button
                //button[i][j].setForeground(Color.);
                button[i][j].addActionListener(this);       //Add ActionListener to handle clicking event
                button[i][j].addMouseListener(new MouseListener() {     //Specially added mouseListener (See star note *****)
                    public void mousePressed(MouseEvent me) { }     //Compulsory method to override
                    public void mouseReleased(MouseEvent me) { }    //Compulsory method to override
                    public void mouseEntered(MouseEvent me) { }     //Compulsory method to override
                    public void mouseExited(MouseEvent me) { }      //Compulsory method to override
                    public void mouseClicked(MouseEvent me) {       //To check which button was clicked
                        JButton jb = (JButton) me.getSource();      //Getting the source of click ie which button was clicked
                        if(me.getButton() == MouseEvent.BUTTON3) {    //Getting the button which was clicked (emulated in 3 button mouse so BUTTON3 means right click)
                            if(jb.getBackground().equals(Color.RED))    //Resets background color as default if it is red
                                jb.setBackground(new JButton().getBackground());    //For default color, we made new object of JButton and copied its background color
                            else        //If color is default then turn it to red
                                jb.setBackground(Color.RED);
                        }
                    }
                });
                panel.add(button[i][j]);        //Adding buttons to panel
                
                if(i == 0 || j == 0 || i == 11 || j == 11)      //If the buttons are on edge then we don't need them
                    button[i][j].setVisible(false);     //Set them invisible
            }
        }
        
        //Add components to window
        frame.add(label, BorderLayout.NORTH);       //Sets label at the top of window
        frame.add(panel);       //Adds panel after label
        frame.setFont(f1);      //Sets f1 font in windows
        frame.setSize(1000,1000);       //Set size of window as 1000x1000
        //frame.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        frame.setVisible(true);     //Show window on screen
    }
    
    /*Algorithm :-
        Start
        Create object of random
        Gets 20 random numbers of double digits
        separate digits and get button of same index 
        (eg if number is 58, we set button[5][8] as bomb)

        Check every cell from [1][1] to [10][10]
        check if cell is not bomb
        Count the number of adjacent cells which has bomb
        Set that number as name of button
        Stop
    */
    public void BombSet()       //Setting bombs at random places
    {
        Random rand = new Random();     //Creating new object of Random class
       
        for(int k = 0; k < 20; k++)     //For 20 random numbers
        {
            int place = rand.nextInt(100);      //Next random number
            int y = place % 10;                 //Gets last digit
            place /= 10;                        //Divide number by 10 to get first digit (See star note ******)
            int x = place % 10;                 //Gets first digit
            
            button[x+1][y+1].setName("D");      //Set chosen button as bomb
        }
        
        for(int i = 1; i < 11; i++)
        {
            for(int j = 1; j < 11; j++)         //Loops to check every visible cells
            {
                if(! button[i][j].getName().equals("D"))        //If cell is not bomb
                {
                    int cnt = 0;        //Count is 0 at starting
                    //checks adjacent cells to count bombs
                    if(button[i+1][j+1].getName().equals("D"))      
                        cnt++;
                    if(button[i-1][j-1].getName().equals("D"))
                        cnt++;
                    if(button[i-1][j+1].getName().equals("D"))
                        cnt++;
                    if(button[i+1][j-1].getName().equals("D"))
                        cnt++;
                    if(button[i-1][j].getName().equals("D"))
                        cnt++;
                    if(button[i+1][j].getName().equals("D"))
                        cnt++;
                    if(button[i][j+1].getName().equals("D"))
                        cnt++;
                    if(button[i][j-1].getName().equals("D"))
                        cnt++;
                    //seting that number as name of cell
                    button[i][j].setName(cnt + "");
                    //button[i][j].setLabel(cnt + "");
                }
            }
        }
    }
    
    //to handle clicking event of buttons
    public void actionPerformed(ActionEvent e)      //object e keeps track of event details
    {
        JButton jb = (JButton) e.getSource();       //jb stores the copy of button that was clicked
        //label.setText(correct + "");
        
        if(! jb.getBackground().equals(Color.RED))  //If background is not red, then we proceed to check
        {   
            if(jb.getName().equals("D"))        //If bomb is clicekd...
            {
                //label.setForeground(Color.red);
                label.setText("::: Try Again :::");     //Game is over so label is set
                //Every cell gives off their number
                for(int i = 0; i < 12; i++)     
                {
                    for(int j = 0; j < 12; j++)
                    {
                        jb.setLabel(jb.getName());      //Set the label as name given to that button
                        if(button[i][j].getName().equals("D"))      //If button is bomb...
                            button[i][j].setBackground(Color.RED);  //Change background as red
                        else
                            button[i][j].setLabel(button[i][j].getName()); //Otherwise just show label as name 
                    }
                }
                //Try is used becasue TimuUnit.SECONDS.wait() may throw inturrupted or other kind of exception
                try
                {
                    TimeUnit.SECONDS.wait(5); //Wait for 5 seconds
                    System.exit(0); //Exit the window
                }
                //Catch exception...
                catch(Exception ex)     
                { }
                
            }
            else  //If the button clicked was not bomb, then count of right buttons increases
            {
                if(jb.getLabel().equals(""))        //If the label is not there...(ie button is clicked for the first time)
                {
                    correct++;      //Increases the correct variable
                    jb.setLabel(jb.getName());      //Sets the label as the name of cells ie the number of adjacent bombs
                }
            }
            
            if(correct == 80)       //If 80 correct cells were clicked...
            {
                //label.setForeground(Color.red);
                label.setText("::: You Won :::");       //Sets label as 'You won'
                //Try block because it may throw Inturrupted Exception
                try     
                {
                    TimeUnit.SECONDS.wait(5);       //Wait for 5 seconds
                    System.exit(0);                 //Exit the window
                }
                //Catching the exception
                catch(Exception ex)
                { }
            }
        }
    }
}


//Main method
public class JavaApplication50 {
    public static void main(String[] args) {
        Board b = new Board();      //Create the object of board and call the constructor
        b.BombSet();                //Setting the bomb and game continues...
    }
}