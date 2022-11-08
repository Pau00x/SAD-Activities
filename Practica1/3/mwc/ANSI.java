/**
 *
 * @author isaac.grau laura.macia
 */
//ANSI escape sequences used on Console to print
interface ANSI {

    public final static String BS = "\033[1D" + "\033[1P"; // Move cursor left the indicated # of columns. + Delete the
                                                           // indicated # of characters on current line.
    public final static String DEL = "\033[1P"; // Delete the indicated # of characters on current line.
    public final static String HOME = "\033[1G"; // Move cursor to indicated column in current row.
    public final static String LEFT = "\033[1D";
    public final static String RIGHT = "\033[1C";
    public final static String INSERT = "\033[1@"; // Insert the indicated # of blank characters and print the char
}
