public class LexicUnitMultieval extends LexicUnit {
    private String lexema;

    public LexicUnitMultieval(int fila, int columna, LexicClass clase, String lexema) {
        super(fila, columna, clase);
        this.lexema = lexema;
    }

    public String lexema() {
        return lexema;
    }

    public String toString() {
        return "[clase:" + clase() + ",fila:" + fila() + ",col:" + columna() + ",lexema:" + lexema() + "]";
    }
}
