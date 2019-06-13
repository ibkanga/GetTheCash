package getthecash;
// @author IgorBarašin2681

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;
import java.util.Random;
import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.stage.Stage;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.StageStyle;
import com.google.common.collect.Ordering;
import com.google.common.collect.TreeMultimap;
import java.util.Collection;
import javax.swing.JOptionPane;

public class Graphics extends Application{
        
        HighScore highScore;      
        TreeMultimap<Integer, String> scoreList;
        
        LongValue startTimeBullseye;
        LongValue startTimeMoneyBag;
        LongValue estimatedTimeBullseye;
        LongValue estimatedTimeMoneyBag;
        
        IntValue points;
        IntValue health;
        IntValue level;
        
        Random random;
        
        Group root = new Group();
        Scene scene = new Scene(root);        
        Canvas canvas = new Canvas(1000, 800);
        GraphicsContext gc = canvas.getGraphicsContext2D();
        
        Player briefcase;     
        Image bullseye;
        Circle bullseyeCircle;
       
        Image back = new Image("/resources/back.png");
        Image exit = new Image("/resources/moneyBagRed128.png");
                
        ArrayList<Sprite> moneybagList;
        
        ArrayList<String> input = new ArrayList<>();
        
        ArrayList<Sprite> listOfEnemies = new ArrayList<>();
        ArrayList<IntValue> listOfVel1 = new ArrayList<>();
        ArrayList<IntValue> listOfVel2 = new ArrayList<>();
        
        LongValue lastNanoTime = new LongValue(System.nanoTime());   
        
  
    @Override
    public void start(Stage stage) throws Exception {
        
        scoreList = TreeMultimap.create(Ordering.natural().reverse(), Ordering.natural());
        highScore = new HighScore();
           
        scoreList = highScore.getScores();

        if(scoreList.isEmpty())
            highScore.createBaseAndTable();
        scoreList = highScore.getScores();
        
        root.getChildren().add(canvas);
        
        random = new Random();
                        
        for(int i = 0; i < 10; i++)
            listOfVel1.add(new IntValue(random.nextInt(300) + 300));       

        for(int i = 0; i < 10; i++)
            listOfVel2.add(new IntValue(0));

        for(int i = 0; i < 5; i++)
            listOfEnemies.add(new Sprite());
                
        Iterator<Sprite> iteratorOfEnemies = listOfEnemies.iterator();
        while(iteratorOfEnemies.hasNext()) {
            Sprite temp = iteratorOfEnemies.next();
            temp.setImage("/resources/moneyBagRed.png");
            temp.setPosition(random.nextInt(500) + 300, random.nextInt(500) + 100);                      
        }

        
        menu();
      
        
        stage.setScene(scene);
        stage.setTitle("GET THE CASH!");
        stage.initStyle(StageStyle.UTILITY);

        stage.setMinHeight(800);
        stage.setMinWidth(1000);
        stage.setMaxHeight(800);
        stage.setMaxWidth(1000);
        
        stage.show();  
        
            
    }
    
    public void game() {

        briefcase = new Player();
        briefcase.setImage("/resources/briefcase.png");
        briefcase.setPosition(450, 350);
        
        bullseye = new Image("/resources/bullseye.png");
        bullseyeCircle = new Circle(100, 100, 32);
        
        points = new IntValue();    
        health  = new IntValue(100);  

        moneybagList = new ArrayList<>();  
            
        startTimeBullseye = new LongValue(System.currentTimeMillis());
        startTimeMoneyBag = new LongValue(System.currentTimeMillis());
        estimatedTimeBullseye = new LongValue();    
        estimatedTimeMoneyBag = new LongValue();  
        
        scene.setOnKeyPressed((KeyEvent e) -> {
            String code = e.getCode().toString();
            if (!input.contains(code)) 
                input.add(code);        
        });

        scene.setOnKeyReleased((KeyEvent e) -> {
            String code = e.getCode().toString();
            input.remove(code);
        });             
        
        scene.setOnMouseClicked((MouseEvent e) -> {
            if(bullseyeCircle.contains(e.getX(), e.getY())) {                        
                startTimeBullseye.setValue(System.currentTimeMillis()); 
                double x = 50 + 900 * Math.random();
                double y = 50 + 700 * Math.random();
                bullseyeCircle.setCenterX(x);
                bullseyeCircle.setCenterY(y);
                points.setValue(points.getValue() + 2);         
            } else 
                health.setValue(health.getValue() - 10);
        });
        
        level = new IntValue(1);
        
        new AnimationTimer() {
            
            @Override
            public void handle(long currentNanoTime) {
                                      
                double elapsedTime = (currentNanoTime - lastNanoTime.getValue()) / 1000000000.0;
                lastNanoTime.setValue(currentNanoTime);
                   
                briefcase.setVelocity(0,0);
                                
                if (input.contains("LEFT") || input.contains("A")) 
                    briefcase.addVelocity(-400,0);
                                   
                if (input.contains("RIGHT") || input.contains("D")) 
                    briefcase.addVelocity(400,0);
               
                if (input.contains("UP") || input.contains("W")) 
                    briefcase.addVelocity(0,-400);
           
                if (input.contains("DOWN") || input.contains("S")) 
                    briefcase.addVelocity(0,400);                
                               
                briefcase.update(elapsedTime);
                               
                for(int i = 0; i < 5; i++) {
                    if((int)listOfEnemies.get(i).getPositionX() <= 1 || (int)listOfEnemies.get(i).getPositionX() >= 930) 
                        listOfVel1.get(i*2).setValue(listOfVel2.get(i*2).getValue());
                    else 
                        listOfVel2.get(i*2).setValue(listOfVel1.get(i*2).getValue() * -1);
                
                    if((int)listOfEnemies.get(i).getPositionY() <= 1 || (int)listOfEnemies.get(i).getPositionY() >=710)
                        listOfVel1.get(i*2 + 1).setValue(listOfVel2.get(i*2 + 1).getValue());
                    else
                        listOfVel2.get(i*2 + 1).setValue(listOfVel1.get(i*2 + 1).getValue() * -1);
                }
                

                Iterator<Sprite> moneybagIter = moneybagList.iterator();
                while (moneybagIter.hasNext()) {
                
                    Sprite moneybag = moneybagIter.next();
                    if (briefcase.intersects(moneybag)) {                   
                        moneybagIter.remove();
                        points.incrementValue();
                    } 
                }


                estimatedTimeMoneyBag.setValue((System.currentTimeMillis() - startTimeMoneyBag.getValue()) / 1000); 
                String timeMoney = String.valueOf(estimatedTimeMoneyBag.getValue());
                
                int numOfBags = 2;
                if(estimatedTimeMoneyBag.getValue() >= 5) {
                    startTimeMoneyBag.setValue(System.currentTimeMillis()); 
                    health.setValue(health.getValue() - 20);
                }
                else {                                       
                    if(moneybagList.isEmpty()) {
                        if(points.getValue() >= 30)
                            numOfBags = 3;
                        if(points.getValue() >= 75) 
                            numOfBags = 4; 
                        for(int i = 0; i < numOfBags; i++) {
                            Sprite moneybag = new Sprite();
                            double px = 50 + 800 * Math.random();
                            double py = 50 + 600 * Math.random();
                            moneybag.setImage("/resources/moneybag.png");
                            moneybagList.add(moneybag);
                            moneybag.setPosition(px, py);
 
                        }
                        startTimeMoneyBag.setValue(System.currentTimeMillis()); 
                      
                    } 
                
                }
                                   
                clearSetFill(Color.IVORY);
                
                estimatedTimeBullseye.setValue((System.currentTimeMillis() - startTimeBullseye.getValue()) / 1000);
                    
                if(estimatedTimeBullseye.getValue() >= 3) {
                    startTimeBullseye.setValue(System.currentTimeMillis());
                    health.setValue(health.getValue() - 15);
                
                }
                else {               
                    String timeBullseye = String.valueOf(estimatedTimeBullseye.getValue());
                    gc.drawImage(bullseye, bullseyeCircle.getCenterX() - bullseyeCircle.getRadius(), bullseyeCircle.getCenterY() - bullseyeCircle.getRadius());  
                    gc.setFont(Font.font("Calibri"));
                    gc.strokeText(timeBullseye, bullseyeCircle.getCenterX() - bullseyeCircle.getRadius(), bullseyeCircle.getCenterY() - bullseyeCircle.getRadius());
                } 
                         

                briefcase.render(gc);    

                
                gc.setFont(fonting(20));
                gc.setFill(Color.BLACK);
                String pointsText = "Level: " + level.getValue();
                if(level.getValue() == 6)
                    pointsText = "Level: FINAL";

                gc.fillText( pointsText, 800, 104);
                gc.strokeText( pointsText, 800, 104);
                    
                                   
                if(points.getValue() >= 15) {
                    level.setValue(2);
                    Sprite tempy = listOfEnemies.get(0);
                    tempy.setVelocity(listOfVel1.get(0).getValue(), listOfVel1.get(1).getValue());
                    tempy.update(elapsedTime);
                    tempy.render(gc); 
                    
                    if (tempy.intersects(briefcase))
                        health.decrementValue();
                }
                
   
                if(points.getValue() >= 30) {
                    level.setValue(3);
                    Sprite tempy = listOfEnemies.get(1);
                    tempy.setVelocity(listOfVel1.get(2).getValue(), listOfVel1.get(3).getValue());
                    tempy.update(elapsedTime);
                    tempy.render(gc);
                    
                    if (tempy.intersects(briefcase))
                        health.decrementValue();
                }
                
                if(points.getValue() >= 50) {
                    level.setValue(4);
                    Sprite tempy = listOfEnemies.get(2);
                    tempy.setVelocity(listOfVel1.get(4).getValue(), listOfVel1.get(5).getValue());
                    tempy.update(elapsedTime);
                    tempy.render(gc);
                    
                    if (tempy.intersects(briefcase))
                        health.decrementValue();
                }
                
                if(points.getValue() >= 75) {
                    level.setValue(5);
                    Sprite tempy = listOfEnemies.get(3);
                    tempy.setVelocity(listOfVel1.get(6).getValue(), listOfVel1.get(7).getValue());
                    tempy.update(elapsedTime);
                    tempy.render(gc);
                    
                    if (tempy.intersects(briefcase))
                        health.decrementValue();
                }
                
                if(points.getValue() >= 100) {
                    level.setValue(6);
                    Sprite tempy = listOfEnemies.get(4);
                    tempy.setVelocity(listOfVel1.get(8).getValue(), listOfVel1.get(9).getValue());
                    tempy.update(elapsedTime);
                    tempy.render(gc);
                    
                    if (tempy.intersects(briefcase))
                       health.decrementValue();
                }
                
                                                                           
                for (Sprite moneybag : moneybagList ) {                   
                    moneybag.render(gc); 
                    gc.setFont(Font.font("Calibri"));
                    gc.fillText(timeMoney, moneybag.getPositionX(), moneybag.getPositionY());
                    gc.strokeText(timeMoney, moneybag.getPositionX(), moneybag.getPositionY());
                }

                gc.setFont(fonting(20));
                gc.setFill(Color.GREEN);
                String pointsTexty = "Cash: $" + (points.getValue());
                gc.fillText( pointsTexty, 800, 36);
                gc.strokeText( pointsTexty, 800, 36);
  
                gc.setFill(Color.RED);
                String healthP = "Health: " + (health.getValue());
                gc.fillText( healthP, 800, 70 );
                gc.strokeText( healthP, 800, 70 );
                
                if(health.getValue() <= 0) {
                    stop();
                    end();
                }
                
                
            }
        }.start();
        
    }
    
    
    public void end() {
        clearSetFill(Color.BLACK);
        
        gc.setFont(fonting(50));

        gc.setFill(Color.WHITE);
        gc.strokeText("GAME OVER", 330, 100);
        gc.fillText("GAME OVER", 330, 100);
        
        gc.setFill(Color.LAWNGREEN);
        gc.strokeText("SCORE: " + points.getValue(), 365, 300);
        gc.fillText("SCORE: " + points.getValue(), 365, 300);
               
        gc.setFont(fonting(25));
        
        gc.setFill(Color.BLUEVIOLET);
        gc.strokeText("BACK TO MENU", 200, 500);
        gc.fillText("BACK TO MENU", 200, 500);
        
        gc.setFill(Color.RED);
        gc.strokeText("EXIT", 670, 500);
        gc.fillText("EXIT", 670, 500);
        
        gc.drawImage(back, 245, 520);
        Circle backCircle = new Circle(245+64, 520+64, 64);       
        
        gc.drawImage(exit, 635, 515);
        Circle exitCircle = new Circle(635+64, 515+64, 64);          
        
        scene.setOnMouseClicked((MouseEvent e) -> {
            if(backCircle.contains(e.getX(), e.getY()))          
                menu();
            else if (exitCircle.contains(e.getX(), e.getY())) 
                System.exit(0);
        });  
        
        score();      
    }
     
    public void score() {
        Map<Integer, Collection<String>> map = scoreList.asMap();
        int save = 1;
            
        for(Map.Entry<Integer, Collection<String>> entry : map.entrySet()) {               
            Integer key = entry.getKey();
            for(String it : scoreList.get(key)) {
                save++;
            }
            if(points.getValue() > key) {
                String answer = JOptionPane.showInputDialog(null,"Enter your name: (Must be 4 characters long)", "NEW HIGHSCORE - " + points.getValue(), JOptionPane.INFORMATION_MESSAGE);
                while(answer == null || answer.trim().length() != 4)
                    answer = JOptionPane.showInputDialog(null,"Enter your name: (Must be 4 characters long)", "NEW HIGHSCORE - " + points.getValue(), JOptionPane.INFORMATION_MESSAGE);
                scoreList.put(points.getValue(), answer.trim().toUpperCase()); 
                highScore.writeScores(scoreList);
                break;
            } 
            else if(points.getValue() == key && save != 11) {
                String answer = JOptionPane.showInputDialog(null,"Enter your name: (Must be 4 characters long)", "NEW HIGHSCORE - " + points.getValue(), JOptionPane.INFORMATION_MESSAGE);
                for(String it : scoreList.get(key)) {
                while(answer == null || answer.trim().toUpperCase().equals(it) || answer.trim().length() != 4)
                    answer = JOptionPane.showInputDialog(null,"Enter your name: (Must be 4 characters long)", "NEW HIGHSCORE - " + points.getValue(), JOptionPane.INFORMATION_MESSAGE);
            }
            
                scoreList.put(points.getValue(), answer.trim().toUpperCase()); 
                highScore.writeScores(scoreList);
                break;             
            } 
               
        }
                                
        map = scoreList.asMap();
        int del = 0;
        for(Map.Entry<Integer, Collection<String>> entry : map.entrySet()) {               
            Integer key = entry.getKey();
            for(String delS : scoreList.get(key)) {
                del++;
                if(del > 10) {
                    scoreList.remove(key, delS);
                }
            }                                        
               
        }
    }
    
    public void menu() {
        clearSetFill(Color.BLACK);
               
        gc.setFont(fonting(50));
        
        gc.setFill(Color.WHITE);
        gc.strokeText("MENU", 415, 100);
        gc.fillText("MENU", 415, 100);
        
        gc.setFont(fonting(30));
        
        gc.setFill(Color.GREEN);
        gc.strokeText("NEW GAME", 100, 250);
        gc.fillText("NEW GAME", 100, 250);
        
        Image newGame = new Image("/resources/moneybag128.png");
        gc.drawImage(newGame, 125, 280);
        Circle newGameCircle = new Circle(125+64, 280+64, 64);  
        
        gc.setFill(Color.YELLOW);
        gc.strokeText("HELP", 445, 250);
        gc.fillText("HELP", 445, 250);
        
        Image help = new Image("/resources/help.png");
        gc.drawImage(help, 422, 285);
        Circle helpCircle = new Circle(422+64, 285+64, 64); 
        
        gc.setFill(Color.PURPLE);
        gc.strokeText("HIGHSCORES", 700, 250);
        gc.fillText("HIGHSCORES", 700, 250);
        
        Image highScores = new Image("/resources/highscores.png");
        gc.drawImage(highScores, 740, 285);
        Circle highScoresCircle = new Circle(740+64, 285+64, 64);
        
        gc.setFill(Color.RED);
        gc.strokeText("EXIT", 450, 550);
        gc.fillText("EXIT", 450, 550);
        
        gc.drawImage(exit, 422, 570);
        Circle exitCircle = new Circle(422+64, 570+64, 64);
               
        
        scene.setOnMouseClicked((MouseEvent e) -> {
            if(newGameCircle.contains(e.getX(), e.getY())) {          
                game();
            } else if(helpCircle.contains(e.getX(), e.getY())) {
                help();
            } else if(highScoresCircle.contains(e.getX(), e.getY())) {
                highScores();
            } else if(exitCircle.contains(e.getX(), e.getY())) {
                System.exit(0);
            } 
            
            
             
        });
    }
    
    public void help() {
        clearSetFill(Color.BLACK);
        
        gc.setFont(fonting(50));
        
        gc.setFill(Color.YELLOW);
        gc.strokeText("HELP", 415, 100);
        gc.fillText("HELP", 415, 100);
        
        gc.setFont(fonting(30));
        
        gc.setFill(Color.CHARTREUSE);
        gc.strokeText("COLLECT ", 100, 200);
        gc.fillText("COLLECT ", 100, 200);
        
        Image greenBag = new Image("/resources/moneybag.png");
        gc.drawImage(greenBag, 265, 165);
        
        gc.strokeText("BEFORE TIMER GOES TO 5!", 350, 200);
        gc.fillText("BEFORE TIMER GOES TO 5!", 350, 200);
        
        gc.setFill(Color.YELLOW);
        gc.strokeText("CLICK ON ", 100, 300);
        gc.fillText("CLICK ON ", 100, 300);
        
        Image target = new Image("/resources/bullseye.png");
        gc.drawImage(target, 290, 260);
        
        gc.strokeText("BEFORE TIMER GOES TO 3!", 380, 300);
        gc.fillText("BEFORE TIMER GOES TO 3!", 380, 300);
        
        gc.setFill(Color.RED);
        gc.strokeText("AVOID ", 100, 400);
        gc.fillText("AVOID ", 100, 400);
        
        Image redBag = new Image("/resources/moneyBagRed.png");
        gc.drawImage(redBag, 230, 350);
        
        gc.strokeText("!", 310, 400);
        gc.fillText("!", 310, 400);
        
        gc.setFill(Color.WHITE);
        gc.strokeText("WASD TO MOVE, LEFT MOUSE BUTTON TO CLICK!", 100, 500);
        gc.fillText("WASD TO MOVE, LEFT MOUSE BUTTON TO CLICK!", 100, 500);
        
        gc.setFill(Color.BLUEVIOLET);
        gc.strokeText("BACK TO MENU", 350, 600);
        gc.fillText("BACK TO MENU", 350, 600);
               
        gc.drawImage(back, 420, 620);
        Circle backCircle = new Circle(420+64, 620+64, 64); 
               
        scene.setOnMouseClicked((MouseEvent e) -> {
            if(backCircle.contains(e.getX(), e.getY()))        
                menu();
            
        });
    }
    
    public void highScores() {
        clearSetFill(Color.BLACK);
        
        gc.setFill(Color.PURPLE);
        gc.setFont(fonting(50));
        
        gc.strokeText("HIGHSCORES", 320, 100);
        gc.fillText("HIGHSCORES", 320, 100);
        
        gc.setFont(fonting(40));
        gc.setFill(Color.GREENYELLOW);
                 
        int i = 1;
        int j = 0;
        int k = 0;
        
        Map<Integer, Collection<String>> map = scoreList.asMap();
        for(Map.Entry<Integer, Collection<String>> entry: map.entrySet()) {
            Integer key = entry.getKey();
            Collection<String> values = scoreList.get(key);
            for(String nick : values) {
                if(k == 10)
                    break;
                if(i == 6) {
                    j = 580;
                    i = 1;
                }
                gc.fillText(String.valueOf(key), 100 + j, i * 90 + 100);
                gc.fillText(nick, 200 + j, i * 90 + 100);
                i++;
                k++;
            }
        }
            
            
        gc.setFont(fonting(30));
        gc.setFill(Color.BLUEVIOLET);
        gc.strokeText("BACK TO MENU", 350, 600);
        gc.fillText("BACK TO MENU", 350, 600);
        
        gc.drawImage(back, 420, 620);
        Circle backCircle = new Circle(420+64, 620+64, 64); 
              
        scene.setOnMouseClicked((MouseEvent e) -> {
            if(backCircle.contains(e.getX(), e.getY())) {          
                menu();
            }

        });
        
    }
    
    public void clearSetFill(Paint p) {
        gc.clearRect(0, 0,1000, 800);
        gc.setFill(p);
        gc.fillRect(0, 0, 1000, 800);
    }
    
    public Font fonting(int size) {
        return Font.font("Times New Roman", FontWeight.BOLD, size);
    }
           
}

