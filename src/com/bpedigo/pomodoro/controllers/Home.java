package com.bpedigo.pomodoro.controllers;

import com.bpedigo.pomodoro.model.Attempt;
import com.bpedigo.pomodoro.model.AttemptKind;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.VBox;
import javafx.scene.media.AudioClip;
import javafx.util.Duration;


public class Home {

    private final AudioClip mApplause;
    @FXML private VBox container;
    @FXML private Label title;
    @FXML private Label time;
    @FXML private TextArea message;


    private Attempt mCurrentAttempt;
    private Timeline mTimeLine;

    public Home(){
        mApplause = new AudioClip(getClass().getResource("/sounds/applause.mp3").toExternalForm());
    }

    public void clockFace(int num){
        int minutes = num / 60;
        int seconds = num % 60;
        time.setText(String.format("%02d:%02d",minutes,seconds));
    }

    private void prepareAttempt(AttemptKind kind){
        reset();
        mCurrentAttempt = new Attempt("",kind);
        addAttemptStyle(kind);
        title.setText(kind.getDisplayName());
        clockFace(mCurrentAttempt.getmRemainingSeconds());
        mTimeLine = new Timeline();
        mTimeLine.setCycleCount(kind.getmTotalSeconds());
        mTimeLine.getKeyFrames().add(new KeyFrame(Duration.seconds(1), e -> {
            mCurrentAttempt.tick();
            clockFace(mCurrentAttempt.getmRemainingSeconds());
        }));
        mTimeLine.setOnFinished( e -> {
            mApplause.play();
            saveCurrentAttempt();
            prepareAttempt(mCurrentAttempt.getmKind() == AttemptKind.FOCUS ?
                    AttemptKind.BREAK : AttemptKind.FOCUS);
        });
    }

    private void saveCurrentAttempt() {
        mCurrentAttempt.setmMessage(message.getText());
        mCurrentAttempt.save();
    }

    private void reset() {
        container.getStyleClass().remove("playing");
        if(mTimeLine != null && mTimeLine.getStatus() == Animation.Status.RUNNING){
            mTimeLine.stop();
        }
    }

    private void playTimer(){
        container.getStyleClass().add("playing");
        mTimeLine.play();
    }

    private void pauseTimer() {
        container.getStyleClass().remove("playing");
        mTimeLine.pause();
    }


    private void addAttemptStyle(AttemptKind kind) {
        if(mCurrentAttempt.getmKind()  != kind.FOCUS){
           setStyle("#3F8ABF"); // #3F8ABF
        }
        else {
            setStyle("#2D3339"); // #2D3339
        }
    }

    public void setStyle(String style){
        container.setStyle("-fx-background-color: " + style);
    }


    public void handleRestart(ActionEvent actionEvent) {
        prepareAttempt(AttemptKind.FOCUS);
        playTimer();
    }

    public void handlePlay(ActionEvent actionEvent) {
        if(mCurrentAttempt == null){
            handleRestart(actionEvent);
        } else {
            playTimer();
        }

    }

    public void handlePause(ActionEvent actionEvent) {
        pauseTimer();
    }
}
