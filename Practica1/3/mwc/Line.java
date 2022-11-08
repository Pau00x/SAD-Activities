import java.util.Observable;
public class Line extends Observable {    // MODEL
  /**
   *
   * @author isaac.grau laura.macia
   */
    private int posCursor;
    private StringBuilder line;
    private Boolean modeInsert;
    private String[] action;

    public Line() {
        this.line = new StringBuilder();
        this.posCursor = 0;
        this.modeInsert = true;
        this.action = new String[2];
    }

    public StringBuilder getLine() {
        return this.line;
    }

    public int getPosCursor() {
        return this.posCursor;
    }

    public int getLength() {
        return this.line.length();
    }

    public Boolean getMode() {
        return this.modeInsert;
    }

    public void insertMode() {
        this.modeInsert = !this.modeInsert;
    }

    public void insertChar(char newChar) { // If modeInsert == True
        this.line = this.line.insert(this.posCursor, newChar);
        action[0] = "insertChar";
        action[1] = ANSI.INSERT + newChar;
        this.posCursor++;
        this.setChanged();
        this.notifyObservers(action);
    }

    public void replaceChar(char newChar) { // If modeInsert == False
        this.line.replace(this.posCursor, this.posCursor + 1, "" + newChar);
        this.posCursor++;
        action[0] = "replaceChar";
        action[1] = "" + newChar;
        this.setChanged();
        this.notifyObservers(action);
    }

    public void backspace() {
        int borrar = this.posCursor - 1;
        this.line.deleteCharAt(borrar);
        this.posCursor--;
        action[0] = "backspace";
        action[1] = ANSI.BS;
        this.setChanged();
        this.notifyObservers(action);
    }

    public void delete() {
        if (this.posCursor < this.line.length()) {
            this.line.deleteCharAt(this.posCursor);
            action[0] = "delete";
            action[1] = ANSI.DEL;
            this.setChanged();
            this.notifyObservers(action);
        }
    }

    public void moveRight() {
        if (this.posCursor < line.length()) {
            this.posCursor++;
            action[0] = "moveRight";
            action[1] = ANSI.RIGHT;
            this.setChanged();
            this.notifyObservers(action);
        }
    }

    public void moveLeft() {
        if (this.posCursor != 0) {
            this.posCursor--;
            action[0] = "moveLeft";
            action[1] = ANSI.LEFT;
            this.setChanged();
            this.notifyObservers(action);
        }
    }

    public void moveHome() {
        this.posCursor = 0;
        action[0] = "moveHome";
        action[1] = ANSI.HOME;
        this.setChanged();
        this.notifyObservers(action);
    }

    public void moveEnd() {
        int moure = this.getLength() - this.posCursor;
        this.posCursor = this.getLength();
        action[0] = "moveEnd";
        action[1] = "" + moure; // In this case we pass the number to move
        this.setChanged();
        this.notifyObservers(action);
    }
}
