package com.comp301.a09akari.view;

import com.comp301.a09akari.controller.ControllerImpl;
import com.comp301.a09akari.model.*;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.layout.*;

public class PuzzleView implements FXComponent {
  private ControllerImpl controller;

  public PuzzleView(ControllerImpl controller) {
    this.controller = controller;
  }

  @Override
  public Parent render() {
    Pane stage = new VBox();
    stage.getStyleClass().add("stage");

    Pane controls = new HBox();
    stage.getStyleClass().add("stage");
    stage.getChildren().add(controls);

    Button prev = new Button();
    prev.setText("PREV");
    prev.setOnAction(actionEvent -> controller.clickPrevPuzzle());

    Button rand = new Button();
    rand.setText("RANDOM");
    rand.setOnAction(actionEvent -> controller.clickRandPuzzle());

    Button reset = new Button();
    reset.setText("RESET");
    reset.setOnAction(actionEvent -> controller.clickResetPuzzle());

    Pane solved = new HBox();
    Label solve = new Label("SOLVED");
    solve.getStyleClass().add("solved");
    solve.setVisible(false);

    Button next = new Button();
    next.setText("NEXT");
    next.setOnAction(actionEvent -> controller.clickNextPuzzle());

    controls.getChildren().add(prev);
    controls.getChildren().add(rand);
    controls.getChildren().add(next);

    solved.getChildren().add(solve);
    controls.getChildren().add(solved);
    controls.getChildren().add(reset);

    GridPane board = new GridPane();
    board.setHgap(1);
    board.setVgap(1);
    board.getStyleClass().add("board");
    stage.getChildren().add(board);

    Puzzle cur = controller.getModel().getActivePuzzle();
    for (int r = 0; r < cur.getHeight(); r++) {
      for (int c = 0; c < cur.getWidth(); c++) {
        switch (cur.getCellType(r, c)) {
          case CLUE:
            board.add(clue(r, c), c, r);
            break;

          case WALL:
            board.add(wall(r, c), c, r);
            break;

          case CORRIDOR:
            Button button = button(r, c);
            board.add(button, c, r);

            if (controller.getModel().isLit(r, c)) {
              if (controller.getModel().isLamp(r, c)) {
                if (controller.getModel().isLampIllegal(r, c)) {
                  button.getStyleClass().add("tile-4");
                } else {
                  button.getStyleClass().add("tile-5");
                }
              } else {
                button.getStyleClass().add("tile-6");
              }
            }

            if (controller.getModel().isSolved()) {
              solve.setVisible(true);
            }

            break;
          default:
            break;
        }
      }
    }

    return stage;
  }

  private Button button(int r, int c) {
    Button tile = new Button();
    tile.getStyleClass().add("tile");
    tile.getStyleClass().add("tile-3");
    tile.setOnAction(actionEvent -> controller.clickCell(r, c));

    return tile;
  }

  private Label clue(int r, int c) {
    int num = controller.getModel().getActivePuzzle().getClue(r, c);

    Label tile = new Label(String.valueOf(num));
    tile.getStyleClass().add("tile");
    if (controller.getModel().isClueSatisfied(r, c)) {
      tile.getStyleClass().add("tile-1");
    } else {
      tile.getStyleClass().add("tile-0");
    }

    return tile;
  }

  private Label wall(int r, int c) {
    Label tile = new Label();
    tile.getStyleClass().add("tile");
    tile.getStyleClass().add("tile-2");

    return tile;
  }
}
