package com.comp301.a09akari.view;

import com.comp301.a09akari.controller.ControllerImpl;
import javafx.scene.Parent;
import javafx.scene.layout.*;
import javafx.scene.control.*;

public class ControlView implements FXComponent {
  ControllerImpl controller;

  public ControlView(ControllerImpl controller) {
    this.controller = controller;
  }

  @Override
  public Parent render() {
    Pane stage = new HBox();
    stage.getStyleClass().add("stage");

    Pane top = new HBox();
    top.getStyleClass().add("top");
    stage.getChildren().add(top);

    Pane logoContainer = new HBox();
    Label logo = new Label("AKARI");
    logo.getStyleClass().add("logo");
    logoContainer.getChildren().add(logo);
    HBox.setHgrow(logoContainer, Priority.ALWAYS);
    top.getChildren().add(logoContainer);

    Pane counter = new VBox();
    counter.getStyleClass().add("number");
    int count = this.controller.getModel().getActivePuzzleIndex() + 1;
    Label puzzleCount = new Label("Puzzle Number: " + count);
    puzzleCount.getStyleClass().add("number-label");
    counter.getChildren().add(puzzleCount);

    Label puzzleTotal = new Label("out of 5");
    puzzleTotal.getStyleClass().add("number-value");
    counter.getChildren().add(puzzleTotal);
    top.getChildren().add(counter);

    return stage;
  }
}
