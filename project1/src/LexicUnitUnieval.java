public class LexicUnitUnieval extends LexicUnit {
    public String lexema() {
        throw new UnsupportedOperationException();
    }

    public LexicUnitUnieval(int fila, int columna, LexicClass clase) {
        super(fila, columna, clase);
    }

    public String toString() {
        return "[clase:" + clase() + ",fila:" + fila() + ",col:" + columna() + "]";
    }
}