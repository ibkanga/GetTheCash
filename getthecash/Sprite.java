package getthecash;
// @author IgorBarašin2681

import javafx.geometry.Rectangle2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

public class Sprite {
    protected Image image;
    protected double positionX;
    protected double positionY;
    protected double velocityX;
    protected double velocityY;
    protected double width;
    protected double height;
    
    public Sprite() {
        positionX = 0;
        positionY = 0;
        velocityX = 0;
        velocityY = 0;
        width = 0;
        height = 0;
    }
        
    public double getPositionX() {
        return positionX;
    }
    
    public double getPositionY() {
        return positionY;
    }
    
    public void setImage(Image image) {
        this.image = image;
        width = image.getWidth();
        height = image.getHeight();
    }
    
    public void setImage(String name) {
        Image img = new Image(name);
        setImage(img);
    }
    
    public void setPosition(double positionX, double positionY) {     
        this.positionX = positionX;
        this.positionY = positionY;
    }
    
    public void setVelocity(double velocityX, double velocityY) {
        this.velocityX = velocityX;
        this.velocityY = velocityY;
    }
    
    public void addVelocity(double x, double y) {  
        velocityX += x;
        velocityY += y;
    }
    
    public void update(double time) {
        positionX += velocityX * time; 
        positionY += velocityY * time;          
    }
    
    public void render(GraphicsContext gc) {
        gc.drawImage(image, positionX, positionY);         
    }
    
    public Rectangle2D getBoundary() {
        return new Rectangle2D(positionX, positionY, width, height);
    }
    
    public boolean intersects(Sprite s) {
        return s.getBoundary().intersects(this.getBoundary());
    }  
    
}

