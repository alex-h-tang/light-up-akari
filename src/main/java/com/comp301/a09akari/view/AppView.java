package com.comp301.a09akari.view;

import com.comp301.a09akari.controller.ControllerImpl;
import com.comp301.a09akari.model.Model;
import javafx.scene.*;
import javafx.scene.layout.*;

public class AppView implements FXComponent {
  private Model model;
  private ControllerImpl controller;

  public AppView(ControllerImpl controller, Model model) {
    this.controller = controller;
    this.model = model;
  }

  @Override
  public Parent render() {
    Pane stage = new VBox();

    FXComponent control = new ControlView(controller);
    FXComponent puzzle = new PuzzleView(controller);

    stage.getChildren().add(control.render());
    stage.getChildren().add(puzzle.render());

    return stage;
  }
}
