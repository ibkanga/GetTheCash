package getthecash;
// @author IgorBarašin2681

public class Player extends Sprite{
    
    @Override
    public void update(double time) {
        
        if((int)positionX <= 1) 
            positionX = 1;
        
        if((int)positionX >= 930)
            positionX = 930;
                                           
        if((int)positionY <= 1) {
            positionY = 1;
        } 
        
        if((int)positionY >= 710) {
            positionY = 710;
        }
                               
        positionX += velocityX * time;        
        positionY += velocityY * time;

    }

}
