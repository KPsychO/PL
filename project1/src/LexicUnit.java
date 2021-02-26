public abstract class LexicUnit {
    private LexicClass clase;
    private int fila;
    private int columna;

    public LexicUnit(int fila, int columna, LexicClass clase) {
        this.fila = fila;
        this.columna = columna;
        this.clase = clase;
    }

    public LexicClass clase() {
        return clase;
    }

    public abstract String lexema();

    public int fila() {
        return fila;
    }

    public int columna() {
        return columna;
    }
}
