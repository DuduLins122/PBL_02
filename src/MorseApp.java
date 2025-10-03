/*
 * Aplicação JavaFX simples com campos para INSERIR, CODIFICAR e DECODIFICAR
 * e um painel para visualizar a ABB do Código Morse.
 * Todas as operações da ABB são recursivas (ver MorseBST).
 */
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;

public class MorseApp extends Application {
    private final MorseBST tree = new MorseBST();
    private final TreeVisualizer visualizer = new TreeVisualizer();

    // UI
    private TextField tfChar;
    private TextField tfMorse;
    private TextField tfEncode;
    private TextField tfDecode;
    private TextArea taLog;

    @Override
    public void start(Stage stage) {
        // Visualizador
        visualizer.bind(tree); // <<< importante: associa o visualizador à árvore

        // Painel de controle (inserção de letra e código)
        tfChar = new TextField();
        tfChar.setPromptText("Letra (A-Z ou 0-9)");

        tfMorse = new TextField();
        tfMorse.setPromptText("Código Morse (., -)");

        Button btnInsert = new Button("Inserir (letra + morse)");
        btnInsert.setOnAction(e -> onInsert());

        // Encode: texto -> morse
        tfEncode = new TextField();
        tfEncode.setPromptText("Texto para ENCODE (A-Z, 0-9, espaços)");
        Button btnEncode = new Button("ENCODE");
        TextArea taEncode = new TextArea();
        taEncode.setEditable(false);
        btnEncode.setOnAction(e -> {
            String src = tfEncode.getText();
            String out = tree.encodeText(src);
            taEncode.setText(out);
            append("ENCODE \"" + src + "\" -> " + out);
        });

        // Decode: morse -> texto
        tfDecode = new TextField();
        tfDecode.setPromptText("Morse para DECODE (use . e -, tokens separados por espaço, / entre palavras)");
        Button btnDecode = new Button("DECODE");
        TextArea taDecode = new TextArea();
        taDecode.setEditable(false);
        btnDecode.setOnAction(e -> {
            String src = tfDecode.getText();
            String out = tree.decodeText(src);
            taDecode.setText(out);
            append("DECODE \"" + src + "\" -> " + out);
        });

        // Log
        taLog = new TextArea();
        taLog.setEditable(false);
        taLog.setPrefRowCount(6);

        // Layouts
        GridPane gpInsert = new GridPane();
        gpInsert.setHgap(8);
        gpInsert.setVgap(8);
        gpInsert.add(new Label("Letra:"), 0, 0);
        gpInsert.add(tfChar, 1, 0);
        gpInsert.add(new Label("Morse:"), 0, 1);
        gpInsert.add(tfMorse, 1, 1);
        gpInsert.add(btnInsert, 1, 2);

        VBox vbEncode = new VBox(8, tfEncode, btnEncode, taEncode);
        VBox vbDecode = new VBox(8, tfDecode, btnDecode, taDecode);

        HBox hbTop = new HBox(16, gpInsert, vbEncode, vbDecode);
        hbTop.setPadding(new Insets(10));

        BorderPane rootPane = new BorderPane();
        rootPane.setTop(hbTop);
        rootPane.setCenter(visualizer);
        rootPane.setBottom(new VBox(8, new Label("Log:"), taLog));
        BorderPane.setMargin(visualizer, new Insets(10));
        BorderPane.setMargin(taLog, new Insets(10));

        Scene scene = new Scene(rootPane, 1150, 560);
        stage.setScene(scene);
        stage.setTitle("Morse - ABB (Visão + Encode/Decode)");
        stage.show();

        // Desenha inicialmente
        refreshView();
    }

    private void onInsert() {
        String sChar = tfChar.getText() == null ? "" : tfChar.getText().trim();
        String sMorse = tfMorse.getText() == null ? "" : tfMorse.getText().trim();

        if (sChar.length() != 1) {
            append("Informe exatamente 1 caractere (A-Z ou 0-9).");
            return;
        }
        char c = Character.toUpperCase(sChar.charAt(0));
        if (!isLetterOrDigit(c)) {
            append("Somente letras A-Z ou dígitos 0-9.");
            return;
        }
        if (!isValidMorse(sMorse)) {
            append("Morse inválido: use apenas '.' e '-' (sem outros símbolos).");
            return;
        }

        tree.insert(sMorse, c);
        append("Inserido: " + c + " <- " + sMorse);
        refreshView();
    }

    private boolean isLetterOrDigit(char c) {
        return (c >= 'A' && c <= 'Z') || (c >= '0' && c <= '9');
    }

    private boolean isValidMorse(String s) {
        if (s == null || s.length() == 0) return false;
        int i = 0;
        while (i < s.length()) {
            char ch = s.charAt(i);
            if (ch != '.' && ch != '-') return false;
            i++;
        }
        return true;
    }

    private void refreshView() {
        // Atualiza a raiz do visualizador usando a API sem reflexão:
        visualizer.setRootHandle(tree.getRootHandle());
    }

    private void append(String msg) {
        taLog.appendText(msg + "\n");
    }

    public static void main(String[] args) {
        launch(args);
    }
}
