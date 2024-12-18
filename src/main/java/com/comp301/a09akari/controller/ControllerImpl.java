package com.comp301.a09akari.controller;

import com.comp301.a09akari.model.Model;
import com.comp301.a09akari.model.Puzzle;

import java.util.Random;

public class ControllerImpl implements ClassicMvcController {
  private Model model;

  public ControllerImpl(Model model) {
    this.model = model;
  }

  @Override
  public void clickNextPuzzle() {
    if (model.getActivePuzzleIndex() == model.getPuzzleLibrarySize() - 1) {
      model.setActivePuzzleIndex(0);
      return;
    }
    model.setActivePuzzleIndex(model.getActivePuzzleIndex() + 1);
  }

  @Override
  public void clickPrevPuzzle() {
    if (model.getActivePuzzleIndex() == 0) {
      model.setActivePuzzleIndex(model.getPuzzleLibrarySize() - 1);
      return;
    }
    model.setActivePuzzleIndex(model.getActivePuzzleIndex() - 1);
  }

  @Override
  public void clickRandPuzzle() {
    int cur = model.getActivePuzzleIndex();
    Random rand = new Random();
    int next = rand.nextInt(model.getPuzzleLibrarySize());
    if (next == cur) {
      if (next == 0) {
        model.setActivePuzzleIndex(next + 1);
      } else {
        model.setActivePuzzleIndex(next - 1);
      }
    } else {
      model.setActivePuzzleIndex(next);
    }
  }

  @Override
  public void clickResetPuzzle() {
    model.resetPuzzle();
  }

  @Override
  public void clickCell(int r, int c) {
    if (model.isLamp(r, c)) {
      model.removeLamp(r, c);
    } else {
      model.addLamp(r, c);
    }
  }

  public Model getModel() {
    return model;
  }
}
