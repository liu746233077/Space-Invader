
/**
 * Write a description of class Player here.
 *
 * @author (your name)
 * @version (a version number or a date)
 */
public class Player extends Entity
{
    // instance variables - replace the example below with your own
    
    private int lives = 3;
    private int _score = 0;
    /**
     * Constructor for objects of class Player
     */
    public Player(int x, int y, GamePanel _panel)
    {
        super(java.awt.Color.BLUE, _panel, 20, 20);
        this.setLocation(x, y);
        lives = 3;
    }
    public void setLocation(int x, int y){
        _x = x;
        _y = y;
        piece.setLocation(x,y);
    }
    public void moveRight(){
        
        _x = getX() + 10;
        piece.setLocation(_x, _y);
    }
    public void moveLeft(){
        _x = getX() - 10;
        piece.setLocation(_x, _y);
    }
    public boolean hit(){
        lives --;
        if (lives <= 0) 
            return true;
        
           
        return false;
    }
    public void addScore(int score){
        _score += score;
    }
    
    public int getScore(){
        return _score;
    }
    public void addLive(){
        lives++;
    }
    public int getLives(){
        return lives;
    }
    public void loseLive(){
        lives--;
    }
}
