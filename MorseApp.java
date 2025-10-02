/*
 * Aplicação JavaFX com "menu" via botões para INSERIR, CODIFICAR, DECODIFICAR
 * e visualizar a ABB resultante. Impede operações com árvore vazia.
 * Todas as operações de árvore são recursivas (ver MorseBST).
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

    private TextField tfChar;
    private TextField tfMorse;
    private TextField tfEncode;
    private TextField tfDecode;
    private TextArea  taLog;

    @Override
    public void start(Stage stage) {
        stage.setTitle("TDE 2 - Código Morse (ABB Recursiva)");

        // Linha de inserção
        tfChar  = new TextField();
        tfChar.setPromptText("Caractere (A-Z ou 0-9)");
        tfMorse = new TextField();
        tfMorse.setPromptText("Código morse do caractere (ex: .-)");
        Button btInsert = new Button("INSERIR");
        btInsert.setOnAction(e -> onInsert());

        Button btLoad = new Button("CARREGAR A-Z/0-9");
        btLoad.setOnAction(e -> {
            tree.loadDefault();
            append("Tabela A-Z e 0-9 carregada.");
            refreshView();
        });

        HBox rowInsert = new HBox(8, new Label("Inserir:"), tfChar, tfMorse, btInsert, btLoad);

        // Linha de codificar
        tfEncode = new TextField();
        tfEncode.setPromptText("Texto para CODIFICAR");
        Button btEncode = new Button("CODIFICAR");
        btEncode.setOnAction(e -> onEncode());

        // Linha de decodificar
        tfDecode = new TextField();
        tfDecode.setPromptText("Morse p/ DECODIFICAR (ex: .... . .-.. .-.. --- / .-- --- .-. .-.. -..)");

        Button btDecode = new Button("DECODIFICAR");
        btDecode.setOnAction(e -> onDecode());

        Button btRefresh = new Button("Visualizar Árvore");
        btRefresh.setOnAction(e -> refreshView());

        HBox rowEncode = new HBox(8, new Label("Encode:"), tfEncode, btEncode);
        HBox rowDecode = new HBox(8, new Label("Decode:"), tfDecode, btDecode, btRefresh);

        taLog = new TextArea();
        taLog.setEditable(false);
        taLog.setPrefRowCount(8);

        VBox left = new VBox(10, rowInsert, rowEncode, rowDecode, new Label("Log:"), taLog);
        left.setPadding(new Insets(10));

        BorderPane rootPane = new BorderPane();
        rootPane.setLeft(left);
        rootPane.setCenter(visualizer);

        Scene scene = new Scene(rootPane, 1150, 560);
        stage.setScene(scene);
        stage.show();
    }

    private void onInsert() {
        String charStr = tfChar.getText();
        String morse   = tfMorse.getText();
        try {
            if (charStr == null || charStr.length() != 1)
                throw new IllegalArgumentException("Informe um único caractere (A-Z/0-9).");
            char c = charStr.charAt(0);
            if (morse == null || morse.isEmpty())
                throw new IllegalArgumentException("Informe o código morse do caractere.");
            tree.insert(morse, c);
            append("Inserido: '" + c + "' -> " + morse);
            refreshView();
        } catch (Exception ex) {
            append("ERRO inserir: " + ex.getMessage());
        }
    }

    private void onEncode() {
        if (tree.isEmpty()) { append("Árvore vazia: carregue/insira antes."); return; }
        String text = tfEncode.getText();
        try {
            String out = tree.encodeLine(text);
            append("ENCODE: \"" + text + "\" -> " + out);
        } catch (Exception ex) {
            append("ERRO encode: " + ex.getMessage());
        }
    }

    private void onDecode() {
        if (tree.isEmpty()) { append("Árvore vazia: carregue/insira antes."); return; }
        String line = tfDecode.getText();
        try {
            String out = tree.decodeLine(line);
            append("DECODE: \"" + line + "\" -> " + out);
        } catch (Exception ex) {
            append("ERRO decode: " + ex.getMessage());
        }
    }

    private void refreshView() {
        visualizer.setRoot(tree.getRoot());
    }

    private void append(String msg) {
        taLog.appendText(msg + "\n");
    }

    public static void main(String[] args) {
        launch(args);
    }
}
