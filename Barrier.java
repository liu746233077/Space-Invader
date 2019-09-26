
/**
 * Write a description of class Barrier here.
 *
 * @author (your name)
 * @version (a version number or a date)
 */
public class Barrier extends Entity
{
    // instance variables - replace the example below with your own
    private int _x;
    private int _y;
    private int _life = 3;
    private java.awt.Color color;
   public Barrier(int x, int y, GamePanel _panel)
    {
        super(java.awt.Color.GREEN, _panel, 100, 50);
        this.setLocation(x,y);
    }

    public void setLocation(int x, int y){
        _x = x;
        _y = y;
        piece.setLocation(x,y);
    }
    public void hit(){
        _life--;
        if(_life == 2){
        color = java.awt.Color.BLUE;
        }
        else if(_life == 1){
        color = java.awt.Color.RED;   
        }
        else{
            setVisible(false);
        }
        piece.setColor(color);
    }
}
