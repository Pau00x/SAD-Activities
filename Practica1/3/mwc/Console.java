import java.util.Observable;
import java.util.Observer;
/**
 *
 * @author isaac.grau laura.macia
 */
public class Console implements Observer { // VIEW
    @Override
    public void update(Observable observable, Object obj) {
        String[] action = (String[]) obj;
        switch (action[0]){
            case "backspace":
            case "delete":
            case "moveHome":
            case "moveLeft":
            case "moveRight":
            case "replaceChar":
            case "insertChar":
                System.out.print(action[1]);
                break;
            case "moveEnd":
                //System.out.print(action[1]);
                System.out.print("\033[" + action[1] + "C");
                break;
            default:
                System.err.println("Invalid input!!");
                break;
        }
    }
}
