import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;

public class Main {
    public static void main(String arg[]) throws IOException {
        Reader input = new InputStreamReader(new FileInputStream("input.txt"));
        LexicAnalyzer_Tiny0 la = new LexicAnalyzer_Tiny0(input);
        LexicUnit unit;
        do {
            unit = la.sigToken();
            System.out.println(unit);
        }
        while (unit.clase() != LexicClass.EOF);
    }
}
