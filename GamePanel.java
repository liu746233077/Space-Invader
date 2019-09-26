import java.awt.*;
import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.event.*;
import java.util.Random;
import java.awt.Color;
import java.util.ArrayList;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
/**
 * Write a description of class GamePanel here.
 *
 * @author (your name)
 * @version (a version number or a date)
 */
public class GamePanel extends JPanel implements ActionListener
{
    private Timer _timer;
    private ArrayList<Alien> aliens;
    private ArrayList<Projectile> alienBullets;
    private ArrayList<Projectile> playerBullets;
    private ArrayList<Mothership> motherships;
    private Player _player;
    private int[] _board;
    private boolean endGame = false;
    private boolean goRight = false;
    private boolean goLeft = false;
    private boolean goRightPlayer = true;
    private boolean goLeftPlayer = true;
    private boolean goDown = false;
    private boolean canShoot = true;
    private KeyPListener _pauseKey;
    private KeyInteractor _leftKey, _rightKey, _spaceKey;
    Random rand;
    private int count;
    private int killcount = 0;
    private int motherkillcount = 0;
    private int playerLives = 3;
    private int speedup = 0;
    private BufferedImage _playerImage;
    private BufferedImage _alienImage;
    private BufferedImage _mothershipImage;
    private BufferedImage _bulletImage;
    private BufferedImage _downbulletImage;
    private JLabel score;
    private JLabel lives;
    private boolean addLife = false;
    private Barrier barrierOne;
    private Barrier barrierTwo;
    private Barrier barrierThree;
    private boolean shouldBreak = false;
    public GamePanel(){
        
        _board = new int[1000];
        this.setBackground(Color.BLACK);
        this.setSize(new Dimension(600, 600));
         aliens = new ArrayList<>();
        alienBullets = new ArrayList<>();
        playerBullets = new ArrayList<>();
        motherships = new ArrayList<>();
        _player = new Player(300, 500, this);
        rand = new Random();
        _pauseKey = new KeyPListener(this);
        _rightKey = new KeyRightListener(this);
        _leftKey = new KeyLeftListener(this);
        _spaceKey = new KeySpaceListener(this);
        goRight = true;
        lives = new JLabel("Lives: " + _player.getLives());
        lives.setForeground(Color.WHITE);
        score = new JLabel("Score " + _player.getScore());
        score.setForeground(Color.WHITE);
        this.add(score);
        this.add(lives);
        
        try{
          _mothershipImage = ImageIO.read(new File("spaceship.png"));
       }
       catch (IOException e){
          System.out.println("Error opening image file.");
       }
        try{
          _alienImage = ImageIO.read(new File("alien.png"));
       }
       catch (IOException e){
          System.out.println("Error opening image file.");
       }
        try{
          _playerImage = ImageIO.read(new File("player.png"));
       }
       catch (IOException e){
          System.out.println("Error opening image file.");
       }
        try{
          _bulletImage = ImageIO.read(new File("bullet.png"));
       }
       catch (IOException e){
          System.out.println("Error opening image file.");
       }
        
        try{
          _downbulletImage = ImageIO.read(new File("bulletalien.png"));
       }
       catch (IOException e){
        System.out.println("Error opening image file.");
       }
       
        _timer = new Timer(40, this);
       // _timer.start();
        initGame();
    }
    private void initGame(){
              
        barrierOne = new Barrier(60, 400, this);
        barrierTwo = new Barrier(250, 400, this);
        barrierThree = new Barrier(440, 400, this);
        
        //Images
       
        for(int i = 0; i < 5; i++){
            for(int j = 0; j < 11; j++){
              Alien  alien = new Alien(100 + 40 * j, 50 + 40 * i, this);
                aliens.add(alien);
            }
        }
        for(Alien alien: aliens){
            _board[aliens.indexOf(alien)] = 1;
        }
        shouldBreak = false;
       _timer.start();
    }
    public void shoot() {
    
        Projectile playerBullet = new Projectile(_player.getX() + 10, 
                   _player.getY() - 2, this);  
        playerBullets.add(playerBullet); 
    }
    public void spawnMothership(){
        double rng = Math.random();
        rng = rng * 2 +1;
        int check = (int) rng;
        int direction = 1;
        int xloc = 0;
        if (check == 2) {
            direction = -1;
            xloc = 600;
        }
        Mothership newMother = new Mothership(xloc, 20, direction, this); 
        motherships.add(newMother);
        
    }
    public void paintComponent(java.awt.Graphics aBrush){
        super.paintComponent(aBrush);
        java.awt.Graphics2D betterBrush = (java.awt.Graphics2D) aBrush;
        aBrush.drawImage(_playerImage, _player.getX(), _player.getY(), 35, 35, null);

        
        if(_player.getLives() == 3){
          aBrush.drawImage(_playerImage, 20, 520, 30, 30, null);
          aBrush.drawImage(_playerImage, 50, 520, 30, 30, null);
        }
        if(_player.getLives() == 2){
          aBrush.drawImage(_playerImage, 20, 520, 30, 30, null); 
        }
        if(barrierOne.isVisible()){
            barrierOne.draw(betterBrush);
            barrierOne.fill(betterBrush);
        }
        if(barrierTwo.isVisible()){
            barrierTwo.draw(betterBrush);
            barrierTwo.fill(betterBrush);
        }
        if(barrierThree.isVisible()){
            barrierThree.draw(betterBrush);
            barrierThree.fill(betterBrush);
        }
        for(Alien alien: aliens){
            if(alien.isVisible()){
              aBrush.drawImage(_alienImage, alien.getX(), alien.getY(), 30, 30, null);
        }
        }
        for(Mothership ship: motherships){
            if(ship.isVisible()){
              aBrush.drawImage(_mothershipImage, ship.getX(), ship.getY(), 75, 75, null);
        }
        }
        for(Projectile bullet: alienBullets){
          if(bullet.isVisible()){
            aBrush.drawImage(_downbulletImage, bullet.getX(), bullet.getY(), 30, 30, null);
          }
        }
        for(Projectile bullet: playerBullets){
            if(bullet.isVisible()){
              aBrush.drawImage(_bulletImage, bullet.getX(), bullet.getY(), 30, 30, null);
        }
        }
    }
    public void newWave(){
        killcount = 0;
         
        initGame();
    }
    public void actionPerformed(ActionEvent e){
        score.setText("Score: " + _player.getScore());
        lives.setText("Lives: " + _player.getLives());
        if((_player.getScore() >= 1000) & !addLife){
            _player.addLive();
            addLife = true;
        }
        int x = 0;
         if (count % 520 == 0 && ((x = (int)(Math.random())) % 4 == 0)){
             spawnMothership();
            }
         
        if(count % 10 == 0){
        for(Alien alien: aliens){
             if(shouldBreak){
                 break;
             }
                x = (int)(Math.random() * 55);
                if(_board[aliens.indexOf(alien)] == x){
               if((_board[aliens.indexOf(alien) + 11] != 1)
                    & !alien.didShoot()){
                   Projectile alienBullet = new Projectile(alien.getX(), 
                   alien.getY() + 30, this);
                   
                   alienBullets.add(alienBullet);
                   //alien.setShot(true);
                }
            }
                if(alien.getX() > 540 && alien.isVisible()){
                    goDown = true;
                    goRight = false;
                    goLeft = true;
                }
                
                if(alien.getX() < 20 && alien.isVisible()){
                    goDown = true;
                    goRight = true;
                    goLeft = false;
                }
          
          
        }
    }
        if (killcount > 20) {
          speedup = 1;
        }
        if (killcount > 40) {
          speedup = 2;
        }
      //barriers for player
        if(_player.getX() > 530 && _player.isVisible()){
          goRightPlayer = false;
          goLeftPlayer = true;
        }
        else goRightPlayer=true;
      
        if(_player.getX() < 20 && _player.isVisible()){
          goRightPlayer = true;
          goLeftPlayer = false;
        }
        else goLeftPlayer = true;
        
        for(Alien alien: aliens){
            if(shouldBreak){
                 break;
             }
             if(goDown){
                alien.moveDown();    
            }
            if(goRight){
                alien.moveRight(speedup);
            }
            if(goLeft){
                alien.moveLeft(speedup);
                
            }
           
            if(alien.piece.intersects(_player.piece)){
                          JFrame f;
                          f = new JFrame();
                          JOptionPane.showMessageDialog(f,"GAME OVER \n SCORE: " + _player.getScore());
                          endGame = true;
            }
        }
        
          
        for(Alien alien: aliens){
            if(shouldBreak){
                 break;
             }
             if(alien.getY() >= 400 && alien.getY() <= 415 && endGame == false){
                    
                    _player.loseLive();
                    lives.setText("Lives: " + _player.getLives());
                    if (_player.getLives() <= 0) {
                          JFrame f = new JFrame();
                          JOptionPane.showMessageDialog(f,"GAME OVER \n SCORE: " + _player.getScore());
                          endGame = true;
                         
                        }
                }
            for(Projectile bullet: playerBullets){
                if(bullet.isVisible()){
                    if((barrierOne.piece.intersects(bullet.piece)) &&
                    (barrierOne.isVisible())){
                            barrierOne.hit();
                            bullet.setVisible(false);  
                        }
                        if((barrierTwo.piece.intersects(bullet.piece))&&
                        (barrierTwo.isVisible())){
                            barrierTwo.hit();
                            bullet.setVisible(false);  
                        }
                        if((barrierThree.piece.intersects(bullet.piece)) &&
                        (barrierThree.isVisible())){
                            barrierThree.hit();
                            bullet.setVisible(false);  
                        }
                 if((alien.piece.intersects(bullet.piece)) && 
                    (alien.isVisible())){
                        _board[aliens.indexOf(alien)] = 0;
                        alien.setVisible(false);
                        alien.setShot(true);
                        bullet.setVisible(false);
                        killcount ++; //keeps track of alien kills (not mothership)
                        
                   
                        if(aliens.indexOf(alien) <= 10){
                            _player.addScore(10);
                        }
                        else if(aliens.indexOf(alien) <= 32){
                            _player.addScore(20);
                        }
                        else{
                            _player.addScore(10);
                        }
                        
                    }
               
                }
            }
        }
        if(killcount == 55){
          _timer.stop();
          newWave();
      }
        for(Mothership ship: motherships){
            for(Projectile bullet: playerBullets){
                if(bullet.isVisible()){
                 if((ship.piece.intersects(bullet.piece)) && 
                    (ship.isVisible())){
                        _board[motherships.indexOf(ship)] = 0;
                        ship.setVisible(false);
                        
                        bullet.setVisible(false);
                        motherkillcount ++; //keeps track of mothership kills
                        
                        
                    }
                
                }
            }
        }
        for(Projectile bullet: alienBullets){
            if(bullet.isVisible()){
                 if((barrierOne.piece.intersects(bullet.piece)) &&
                 (barrierOne.isVisible())){
                            barrierOne.hit();
                            bullet.setVisible(false);  
                        }
                        if((barrierTwo.piece.intersects(bullet.piece)) &&
                        (barrierTwo.isVisible())){
                            barrierTwo.hit();
                            bullet.setVisible(false);  
                        }
                        if((barrierThree.piece.intersects(bullet.piece)) &&
                        (barrierThree.isVisible())){
                            barrierThree.hit();
                            bullet.setVisible(false);  
                        }
                 if((_player.piece.intersects(bullet.piece)) && 
                    (_player.isVisible())){ 
                        bullet.setVisible(false);                      
                        _player.hit();
                       
                       
                        if (_player.getLives() <= 0) {
                          JFrame f;
                          f = new JFrame();
                          JOptionPane.showMessageDialog(f,"GAME OVER SCORE: " + _player.getScore());
                          endGame = true;
                          
                         
                        }                      
                     }
                 }
         }
        
        for(Projectile bullet: alienBullets){
            bullet.move();
        }
        for(Projectile bullet: playerBullets){
            bullet.moveUp();
        }
        for(Mothership ship: motherships){
            ship.move();
        }
        goDown = false;
        if (count % 10 == 0) {
            canShoot= true;
        }
        count++;
        if (endGame == true) {
            _timer.stop();
        }
        
        repaint();
        
    }
    private class KeyPListener extends KeyInteractor 
    {
        public KeyPListener(JPanel p)
        {
            super(p,KeyEvent.VK_P);
        }
        
        public  void actionPerformed (ActionEvent e) {
            if(_timer.isRunning())
                _timer.stop();
            
            else
                _timer.start();
        }
    }
    private class KeyLeftListener extends KeyInteractor 
    {
        public KeyLeftListener(JPanel p)
        {
            super(p,KeyEvent.VK_LEFT);
        }
        
        public  void actionPerformed (ActionEvent e) {
            if(_timer.isRunning() && _player.isVisible() && goLeftPlayer)
                _player.moveLeft();
                goRightPlayer = true;
                
              
        }
    }
    private class KeyRightListener extends KeyInteractor 
    {
        public KeyRightListener(JPanel p)
        {
            super(p,KeyEvent.VK_RIGHT);
        }
        
        public  void actionPerformed (ActionEvent e) {
            if(_timer.isRunning() && _player.isVisible() &&  goRightPlayer)
                _player.moveRight();
                goLeftPlayer = true;
        }
    }
    private class KeySpaceListener extends KeyInteractor 
    {
        public KeySpaceListener(JPanel p)
        {
            super(p,KeyEvent.VK_SPACE);
        }
        
        public  void actionPerformed (ActionEvent e) {
            if(_timer.isRunning() && canShoot == true) {
                shoot();
                canShoot = false;  
                }
        }
    }
}
