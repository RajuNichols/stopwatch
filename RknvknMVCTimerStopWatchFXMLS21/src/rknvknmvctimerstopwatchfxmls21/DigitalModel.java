/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rknvknmvctimerstopwatchfxmls21;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.util.Duration;

/**
 *
 * @author nicho
 */
public class DigitalModel extends AbstractModel{
    private Timeline digitalClock;
    private Timeline timer;
    
    private int secondTimer= 60;
    private int hundrethSecondTimer= 0;
    private int tempSecondTimer;
    private String timerString="Timer: 00:00";
    
    private int minute;
    private int second;
    private int hundrethSecond;
    private double tickTimeHundrethSecond= 0.01;
    private String digitalString= "00:00.00";
    
    private int count= 0;
    private int tempMinute=0;
    private int tempSecond=0;
    private int tempHundrethSecond=0;
    private double graphLapInput;
    private String lapString= "Lap: 00:00.00";
    
    private int averageHundrethSecond;
    private int averageSecond;
    private int averageMinute;
    private double graphAverageInput;
    private String averageString= "Average Lap Time: 00:00.00";
    
    public void DigitalMethod(){
        
    }
    
    public void setupTimelines(){
        digitalClock = new Timeline(new KeyFrame(Duration.millis(1000*tickTimeHundrethSecond), (ActionEvent event) -> {
            
            hundrethSecond++;
            
            if(hundrethSecond== 100){
                second++;
                hundrethSecond= 0;
            }
            
            if(second== 60){
                minute++;
                second= 0;
            }
            
            updateDigital();
        }));
        
        digitalClock.setCycleCount(Animation.INDEFINITE);
        
        timer = new Timeline(new KeyFrame(Duration.millis(1000*tickTimeHundrethSecond), (ActionEvent event) -> {
            
            if(hundrethSecondTimer==0){
                hundrethSecondTimer=100;
                secondTimer--;
            }
            
            hundrethSecondTimer--;
            
            updateTimer();
        }));
        
        timer.setCycleCount(Animation.INDEFINITE);
    }
    
    public void updateDigital(){
        String lastString= digitalString;
        digitalString= minute + ":" + second + "." + hundrethSecond;
        
        firePropertyChange("Digital", lastString, digitalString);
    }
    
    public void updateTimer(){
        String lastString= timerString;
        timerString= "Timer: " + secondTimer + ":" + hundrethSecondTimer;
        
        if(secondTimer<= 0 && hundrethSecondTimer== 0){
                timer.pause();
                timerString= "Times up!";
            }
        
        firePropertyChange("Timer", lastString, timerString);
    }
    
    public double getLapTime(){
            tempMinute=minute-tempMinute;
            tempSecond=second-tempSecond;
            tempHundrethSecond=hundrethSecond-tempHundrethSecond;
                    
            if(tempHundrethSecond<0){
                tempHundrethSecond=100+tempHundrethSecond;
                tempSecond--;
            }
            
            graphLapInput= tempSecond + (tempHundrethSecond/100);
            
            return graphLapInput;
    }
    
    public double getAverageLapTime(){
        averageHundrethSecond*=count;
        averageSecond*=count;
                    
        averageHundrethSecond+= tempHundrethSecond;
        averageSecond+= tempSecond;
                    
        count++;
                    
        averageHundrethSecond= averageHundrethSecond/count;
        averageSecond= averageSecond/count;
        
        graphAverageInput= averageSecond+(averageHundrethSecond/100);
        
        return graphAverageInput;
    }
    
    public void setLapText(){
        String lastString= lapString;
        lapString= "Lap " + count + ": " + tempMinute + ":" + tempSecond + "." + tempHundrethSecond;
        
        firePropertyChange("Lap", lastString, lapString);
    }
    
    public void setAverageText(){
        String lastString= averageString;
        averageString= "Average Lap Time: " + averageMinute + ":" + averageSecond + "." + averageHundrethSecond;
        
        firePropertyChange("Average", lastString, averageString);
    }
    
    public int getCount(){
        return count;
    }
    
    public void moveTemps(){
        tempMinute=minute;
        tempSecond=second;
        tempHundrethSecond=hundrethSecond;
    }
    
    public void play(){
        digitalClock.play();
        timer.play();
    }
    
    public void pause(){
        digitalClock.pause();
        timer.pause();
    }
    
    public void reset(){
        minute= 0;
        second= 0;
        hundrethSecond= 0;
        secondTimer= tempSecondTimer;
        hundrethSecondTimer=0;
        tempMinute=0;
        tempSecond=0;
        tempHundrethSecond=0;
        averageHundrethSecond=0;
        averageSecond=0;
        averageMinute=0;
        graphLapInput=0;
        graphAverageInput=0;
        count= 0;
    }
    
    public void setTempSecondTimer(int tempSecondTimer){
        this.tempSecondTimer=tempSecondTimer;
        secondTimer=tempSecondTimer;
    }
    
    public int getSecond(){
        return second;
    }
    
    public int getMinute(){
        return minute;
    }
    
    public int getHundrethSecond(){
        return hundrethSecond;
    }
    
    public int getSecondTimer(){
        return secondTimer;
    }
    
    public int getHundrethSecondTimer(){
        return hundrethSecondTimer;
    }
}
