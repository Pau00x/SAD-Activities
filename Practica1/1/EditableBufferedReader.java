/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sad.practica1;

/**
 *
 * @author Isaac
 */
import java.io.*;
import java.util.logging.Level;
import java.util.logging.Logger;


public class EditableBufferedReader extends BufferedReader {

    static final int ESCAPE = 27;
    static final int CARRIAGE_RETURN = 13;
    static final int BACKSPACE = 127;
    static final int FLETXA_DRETA = 300;
    static final int FLETXA_ESQUERRA = 301;
    static final int CLIC_DRET = 302;
    static final int CLIC_ESQUERRA = 303;
    static final int RODA_AMUNT = 304;
    static final int RODA_AVALL = 305;
    static final int INSERTAR = 306;
    static final int FIN = 307;
    static final int INICI = 308;
    static final int SUPRIMIR = 309;
    private final Line linia;
    private final Console consola;
    private int[] car;

    public EditableBufferedReader(Reader in) {
        super(in);
        this.consola = new Console();
        this.linia = new Line(this.consola);
    }

    private void setRaw() throws IOException, InterruptedException {
        String[] cmd = {"/bin/sh", "-c", "stty raw -echo </dev/tty"};
        Runtime.getRuntime().exec(cmd).waitFor();
    }

    private void unsetRaw() throws IOException, InterruptedException {
        String[] cmd = {"/bin/sh", "-c", "stty -raw echo </dev/tty"};
        Runtime.getRuntime().exec(cmd).waitFor();
    }

    public int read(int[] pos) throws IOException {
        try {
            car = new int[10];
            if (car[0] == '\r') {
                return CARRIAGE_RETURN;
            }
            if (match("\033[D")) {
                return FLETXA_ESQUERRA;
            }
            if (match("\033[C")) {
                return FLETXA_DRETA;
            }
            if (match("\033[1;5A")) {
                return INSERTAR;
            }
            if (match("\033[1;5B")) {
                return SUPRIMIR;
            }
            if (match("\033[1;5C")) {
                return FIN;
            }
            if (match("\033[1;5D")) {
                return INICI;
            }
            if (match("\033[M ")) {
                pos[0] = super.read();
                pos[1] = super.read();
                return CLIC_DRET;
            }
            if (match("\033[M`")) {
                pos[0] = super.read();
                pos[1] = super.read();
                return RODA_AVALL;
            }
            if (match("\033[Ma")) {
                pos[0] = super.read();
                pos[1] = super.read();
                return RODA_AMUNT;
            }
            if (match("\033[M\"")) {
                pos[0] = super.read();
                pos[1] = super.read();
                return CLIC_ESQUERRA;
            }
            return car[0];
        } catch (IOException e) {
            System.out.println("Exception happened - here's what I know: ");
            e.printStackTrace();
            return -1;
        }
    }

    @Override
    public String readLine() throws IOException {
        try {
            setRaw();
            int[] pos = new int[2];
            boolean loop = false;
            while (!loop) {
                int i = this.read(pos);
                char car_aux;
                switch (i) {
                    case CARRIAGE_RETURN:
                        unsetRaw();
                        loop = true;
                        linia.carReturn();
                        break;
                    case BACKSPACE:
                        linia.deleteChar();
                        break;
                    case FLETXA_DRETA:
                        linia.moveCursorRight();
                        break;
                    case FLETXA_ESQUERRA:
                        linia.moveCursorLeft();
                        break;
                    case RODA_AMUNT:
                        linia.moveCursorRight();
                        break;
                    case RODA_AVALL:
                        linia.moveCursorLeft();
                        break;
                    case CLIC_DRET:
                        int posicio = (char) pos[0] - 33;
                        linia.clicDret(posicio);
                        break;
                    case CLIC_ESQUERRA:
                        break;
                    case INSERTAR:
                        linia.setMode();
                        break;
                    case INICI:
                        linia.home();
                        break;
                    case FIN:
                        linia.end();
                        break;
                    case SUPRIMIR:
                        linia.suprimirChar();
                        break;
                    default:
                        if (!linia.getMode()) {
                            car_aux = (char) i;
                            linia.addChar(car_aux);
                            break;
                        } else {
                            car_aux = (char) i;
                            linia.addChar(car_aux);
                            break;
                        }
                }
            }
            return linia.toString();
        } catch (InterruptedException ex) {
            Logger.getLogger(EditableBufferedReader.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }


    public boolean match(String str) throws IOException {
        int i = 0;
        while (i < str.length()) {
            if (car[i] == 0) {
                car[i] = super.read();
            }
            if (!(car[i] == str.charAt(i))) {
                return false;
            }
            i++;
        }
        return true;
    }
}
