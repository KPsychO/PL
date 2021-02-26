import java.io.Reader;
import java.io.IOException;

public class LexicAnalyzer_Tiny0 {

    private Reader input;
    private StringBuffer lex;
    private int nextCh;
    private int iniRow;
    private int iniCol;
    private int curRow;
    private int curCol;
    private static String NL = System.getProperty("line.separator");

    private static enum State {
        INIT, O_MUL, O_DIV, C_LPRN, C_RPRN, C_COMA, O_EQ,
        O_ADD, O_SUBS, REC_ID, T_INT, REC_0, REC_IDEC, REC_DEC, REC_COM, REC_EOF
    }

    private State state;

    public LexicAnalyzer_Tiny0(Reader input) throws IOException {
        this.input = input;
        lex = new StringBuffer();
        nextCh = input.read();
        curRow = 1;
        curCol = 1;
    }

    public LexicUnit sigToken() throws IOException {
        state = State.INIT;
        iniRow = curRow;
        iniCol = curCol;
        lex.delete(0, lex.length());
        while (true) {
            switch (state) {
                case INIT:
                    if (is_lett()) transit(State.REC_ID);
                    else if (is_pos_dig()) transit(State.T_INT);
                    else if (is_zero()) transit(State.REC_0);
                    else if (is_add()) transit(State.O_ADD);
                    else if (is_sub()) transit(State.O_SUBS);
                    else if (is_mul()) transit(State.O_MUL);
                    else if (is_div()) transit(State.O_DIV);
                    else if (is_lprn()) transit(State.C_LPRN);
                    else if (is_rprn()) transit(State.C_RPRN);
                    else if (is_eq()) transit(State.O_EQ);
                    else if (is_col()) transit(State.C_COMA);
                    else if (is_hash()) transitaIgnorando(State.REC_COM);
                    else if (is_space()) transitaIgnorando(State.INIT);
                    else if (is_EOF()) transit(State.REC_EOF);
                    else error();
                    break;
                case REC_ID:
                    if (is_lett() || is_dig()) transit(State.REC_ID);
                    else return unidadId();
                    break;
                case T_INT:
                    if (is_dig()) transit(State.T_INT);
                    else if (is_dot()) transit(State.REC_IDEC);
                    else return unidadEnt();
                    break;
                case REC_0:
                    if (is_dot()) transit(State.REC_IDEC);
                    else return unidadEnt();
                    break;
                case O_ADD:
                    if (is_pos_dig()) transit(State.T_INT);
                    else if (is_zero()) transit(State.REC_0);
                    else return unit_add();
                    break;
                case O_SUBS:
                    if (is_pos_dig()) transit(State.T_INT);
                    else if (is_zero()) transit(State.REC_0);
                    else return unit_subs();
                    break;
                case O_MUL:
                    return unit_mul();
                case O_DIV:
                    return unit_div();
                case C_LPRN:
                    return unit_lprn();
                case C_RPRN:
                    return unit_rprn();
                case O_EQ:
                    return unit_eq();
                case C_COMA:
                    return unit_coma();
                case REC_COM:
                    if (hayNL()) transitaIgnorando(State.INIT);
                    else if (is_EOF()) transit(State.REC_EOF);
                    else transitaIgnorando(State.REC_COM);
                    break;
                case REC_EOF:
                    return unidadEof();
                case REC_IDEC:
                    if (is_pos_dig()) transit(State.REC_DEC);
                    else if (is_zero()) transit(State.REC_IDEC);
                    else error();
                    break;
                case REC_DEC:
                    if (is_pos_dig()) transit(State.REC_DEC);
                    else if (is_zero()) transit(State.REC_IDEC);
                    else return unidadReal();
                    break;
            }
        }
    }

    private void transit(State sig) throws IOException {
        lex.append((char) nextCh);
        sigCar();
        state = sig;
    }

    private void transitaIgnorando(State sig) throws IOException {
        sigCar();
        iniRow = curRow;
        iniCol = curCol;
        state = sig;
    }

    private void sigCar() throws IOException {
        nextCh = input.read();
        if (nextCh == NL.charAt(0)) saltaFinDeLinea();
        if (nextCh == '\n') {
            curRow++;
            curCol = 0;
        } else {
            curCol++;
        }
    }

    private void saltaFinDeLinea() throws IOException {
        for (int i = 1; i < NL.length(); i++) {
            nextCh = input.read();
            if (nextCh != NL.charAt(i)) error();
        }
        nextCh = '\n';
    }

    private boolean is_lett() {
        return nextCh >= 'a' && nextCh <= 'z' ||
                nextCh >= 'A' && nextCh <= 'z';
    }

    private boolean is_pos_dig() {
        return nextCh >= '1' && nextCh <= '9';
    }

    private boolean is_zero() {
        return nextCh == '0';
    }

    private boolean is_dig() {
        return is_pos_dig() || is_zero();
    }

    private boolean is_add() {
        return nextCh == '+';
    }

    private boolean is_sub() {
        return nextCh == '-';
    }

    private boolean is_mul() {
        return nextCh == '*';
    }

    private boolean is_div() {
        return nextCh == '/';
    }

    private boolean is_lprn() {
        return nextCh == '(';
    }

    private boolean is_rprn() {
        return nextCh == ')';
    }

    private boolean is_eq() {
        return nextCh == '=';
    }

    private boolean is_col() {
        return nextCh == ',';
    }

    private boolean is_dot() {
        return nextCh == '.';
    }

    private boolean is_hash() {
        return nextCh == '#';
    }

    private boolean is_space() {
        return nextCh == ' ' || nextCh == '\t' || nextCh == '\n';
    }

    private boolean hayNL() {
        return nextCh == '\r' || nextCh == '\b' || nextCh == '\n';
    }

    private boolean is_EOF() {
        return nextCh == -1;
    }

    private LexicUnit unidadId() {
        switch (lex.toString()) {
            case "evalua":
                return new LexicUnitUnieval(iniRow, iniCol, LexicClass.EVALUA);
            case "donde":
                return new LexicUnitUnieval(iniRow, iniCol, LexicClass.DONDE);
            default:
                return new LexicUnitMultieval(iniRow, iniCol, LexicClass.IDEN, lex.toString());
        }
    }

    private LexicUnit unidadEnt() {
        return new LexicUnitMultieval(iniRow, iniCol, LexicClass.ENT, lex.toString());
    }

    private LexicUnit unidadReal() {
        return new LexicUnitMultieval(iniRow, iniCol, LexicClass.REAL, lex.toString());
    }

    private LexicUnit unit_add() {
        return new LexicUnitUnieval(iniRow, iniCol, LexicClass.MAS);
    }

    private LexicUnit unit_subs() {
        return new LexicUnitUnieval(iniRow, iniCol, LexicClass.MENOS);
    }

    private LexicUnit unit_mul() {
        return new LexicUnitUnieval(iniRow, iniCol, LexicClass.POR);
    }

    private LexicUnit unit_div() {
        return new LexicUnitUnieval(iniRow, iniCol, LexicClass.DIV);
    }

    private LexicUnit unit_lprn() {
        return new LexicUnitUnieval(iniRow, iniCol, LexicClass.PAP);
    }

    private LexicUnit unit_rprn() {
        return new LexicUnitUnieval(iniRow, iniCol, LexicClass.PCIERRE);
    }

    private LexicUnit unit_eq() {
        return new LexicUnitUnieval(iniRow, iniCol, LexicClass.IGUAL);
    }

    private LexicUnit unit_coma() {
        return new LexicUnitUnieval(iniRow, iniCol, LexicClass.COMA);
    }

    private LexicUnit unidadEof() {
        return new LexicUnitUnieval(iniRow, iniCol, LexicClass.EOF);
    }

    private void error() {
        System.err.println("(" + curRow + ',' + curCol + "):Unexpected character");
        System.exit(1);
    }

}