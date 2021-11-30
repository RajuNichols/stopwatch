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
public class AnalogModel extends AbstractModel {
    private Timeline analogClock;
    
    private double secondsElapsed=0;
    private double tickTimeInSeconds=0.01;
    private double angleDeltaPerSeconds=6;
    private double rotation=0;
    
    public AnalogModel(){
        
    }
    
    public void setupTimeline(){
        analogClock = new Timeline(new KeyFrame(Duration.millis(1000*tickTimeInSeconds), (ActionEvent event) ->{
            
            secondsElapsed+=tickTimeInSeconds;
            updateRotation();
        }));
        analogClock.setCycleCount(Animation.INDEFINITE);
    }
    
    public void updateRotation(){
        double lastAngle= rotation;
        rotation= secondsElapsed*angleDeltaPerSeconds;
        
        firePropertyChange("Analog", lastAngle, rotation);
    }
    
    public void play(){
        analogClock.play();
    }
    
    public void pause(){
        analogClock.pause();
    }
    
    public void reset(){
        secondsElapsed= 0;
    }
    
    public boolean isRunning(){
        if(analogClock!=null){
            if(analogClock.getStatus() == Animation.Status.RUNNING){
                return true;
            }
        }
      return false;  
    }
}
