package com.comp301.a09akari.model;

import java.util.ArrayList;

public class ModelImpl implements Model {
  private int active = 0;
  private final PuzzleLibrary library;
  private boolean[][] user;
  private Puzzle cur;
  private ArrayList<ModelObserver> observers = new ArrayList<>();

  public ModelImpl(PuzzleLibrary library) {
    this.library = library;
    cur = this.library.getPuzzle(active);
    user = new boolean[cur.getHeight()][cur.getWidth()];
  }

  @Override
  public void addLamp(int r, int c) {
    checkCell(r, c);
    user[r][c] = true;
    update();
  }

  @Override
  public void removeLamp(int r, int c) {
    checkCell(r, c);
    user[r][c] = false;
    update();
  }

  @Override
  public boolean isLit(int r, int c) {
    checkCell(r, c);
    if (user[r][c]) {
      return true;
    }
    for (int i = r; i < cur.getHeight(); i++) {
      if (user[i][c]) {
        return true;
      } else if (cur.getCellType(i, c) != CellType.CORRIDOR) {
        break;
      }
    }
    for (int i = r; i >= 0; i--) {
      if (user[i][c]) {
        return true;
      } else if (cur.getCellType(i, c) != CellType.CORRIDOR) {
        break;
      }
    }
    for (int j = c; j < cur.getWidth(); j++) {
      if (user[r][j]) {
        return true;
      } else if (cur.getCellType(r, j) != CellType.CORRIDOR) {
        break;
      }
    }
    for (int j = c; j >= 0; j--) {
      if (user[r][j]) {
        return true;
      } else if (cur.getCellType(r, j) != CellType.CORRIDOR) {
        break;
      }
    }
    return false;
  }

  @Override
  public boolean isLamp(int r, int c) {
    checkCell(r, c);
    return user[r][c];
  }

  @Override
  public boolean isLampIllegal(int r, int c) {
    if (r >= cur.getHeight() || r < 0 || c >= cur.getWidth() || c < 0) {
      throw new IndexOutOfBoundsException();
    }
    if (!isLamp(r, c)) {
      throw new IllegalArgumentException();
    }

    int temp;
    if (c > 0) {
      temp = c - 1;
      while (temp >= 0 && cur.getCellType(r, temp) == CellType.CORRIDOR) {
        if (user[r][temp]) {
          return true;
        }
        temp--;
      }
    }

    if (c < cur.getWidth() - 1) {
      temp = c + 1;
      while (temp < cur.getWidth() && cur.getCellType(r, temp) == CellType.CORRIDOR) {
        if (user[r][temp]) {
          return true;
        }
        temp++;
      }
    }

    if (r < cur.getHeight() - 1) {
      temp = r + 1;
      while (temp < cur.getHeight() && cur.getCellType(temp, c) == CellType.CORRIDOR) {
        if (user[temp][c]) {
          return true;
        }
        temp++;
      }
    }

    if (r > 0) {
      temp = r - 1;
      while (temp >= 0 && cur.getCellType(temp, c) == CellType.CORRIDOR) {
        if (user[temp][c]) {
          return true;
        }
        temp--;
      }
    }
    return false;
  }

  @Override
  public Puzzle getActivePuzzle() {
    return cur;
  }

  @Override
  public int getActivePuzzleIndex() {
    return active;
  }

  @Override
  public void setActivePuzzleIndex(int index) {
    if (index >= getPuzzleLibrarySize()) {
      throw new IndexOutOfBoundsException();
    }
    active = index;
    cur = library.getPuzzle(active);
    resetPuzzle();
    update();
  }

  @Override
  public int getPuzzleLibrarySize() {
    return library.size();
  }

  @Override
  public void resetPuzzle() {
    user = new boolean[cur.getHeight()][cur.getWidth()];
    //    for (int i = 0; i < user.length; i++) {
    //      for (int j = 0; j < user[0].length; j++) {
    //        user[i][j] = false;
    //      }
    //    }
    update();
  }

  @Override
  public boolean isSolved() {
    for (int i = 0; i < cur.getHeight(); i++) {
      for (int j = 0; j < cur.getWidth(); j++) {
        switch (cur.getCellType(i, j)) {
          case CLUE:
            if (!isClueSatisfied(i, j)) {
              return false;
            }
          case WALL:
            continue;
          case CORRIDOR:
            if (!isLit(i, j)) {
              return false;
            }
            if (isLamp(i, j) && isLampIllegal(i, j)) {
              return false;
            }
        }
      }
    }
    return true;
  }

  @Override
  public boolean isClueSatisfied(int r, int c) {
    if (r >= cur.getHeight() || r < 0 || c >= cur.getWidth() || c < 0) {
      throw new IndexOutOfBoundsException();
    }
    if (cur.getCellType(r, c) != CellType.CLUE) {
      throw new IllegalArgumentException();
    }
    int count = 0;
    if (r != cur.getHeight() - 1) {
      if (user[r + 1][c]) {
        count++;
      }
    }
    if (c != cur.getWidth() - 1) {
      if (user[r][c + 1]) {
        count++;
      }
    }
    if (r != 0) {
      if (user[r - 1][c]) {
        count++;
      }
    }
    if (c != 0) {
      if (user[r][c - 1]) {
        count++;
      }
    }
    return cur.getClue(r, c) == count;
  }

  @Override
  public void addObserver(ModelObserver observer) {
    observers.add(observer);
  }

  @Override
  public void removeObserver(ModelObserver observer) {
    observers.remove(observer);
  }

  private void checkCell(int r, int c) {
    if (r >= cur.getHeight() || r < 0 || c >= cur.getWidth() || c < 0) {
      throw new IndexOutOfBoundsException();
    }
    if (cur.getCellType(r, c) != CellType.CORRIDOR) {
      throw new IllegalArgumentException();
    }
  }

  private void update() {
    for (ModelObserver o : observers) {
      o.update(this);
    }
  }
}
