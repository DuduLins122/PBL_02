/*
 * Árvore Binária de Busca (ABB) para Código Morse.
 * Heurística: ponto vai para a esquerda (.) e traço vai para a direita (-).
 * Todas as operações (inserção, busca/encode, decode) são recursivas.
 * Não utiliza nenhuma estrutura pronta do Java (List, Map, etc.).
 * Não usa StringBuilder.
 */
public class MorseBST {

    // ----- Nó interno da ABB -----
    private static class Node {
        Node left;
        Node right;
        char value;
        boolean hasValue;
    }

    private final Node root = new Node();

    // ====== Construtor: popula a árvore Morse ======
    public MorseBST() {
        // Letras
        insert(".-",   'A');  insert("-...", 'B'); insert("-.-.", 'C'); insert("-..",  'D'); insert(".",    'E');
        insert("..-.", 'F');  insert("--.",  'G'); insert("....", 'H'); insert("..",   'I'); insert(".---", 'J');
        insert("-.-",  'K');  insert(".-..", 'L'); insert("--",   'M'); insert("-.",   'N'); insert("---",  'O');
        insert(".--.", 'P');  insert("--.-", 'Q'); insert(".-.",  'R'); insert("...",  'S'); insert("-",    'T');
        insert("..-",  'U');  insert("...-", 'V'); insert(".--",  'W'); insert("-..-", 'X'); insert("-.--", 'Y');
        insert("--..", 'Z');

        // Dígitos
        insert("-----", '0'); insert(".----", '1'); insert("..---", '2'); insert("...--", '3'); insert("....-", '4');
        insert(".....", '5'); insert("-....", '6'); insert("--...", '7'); insert("---..", '8'); insert("----.", '9');
    }

    // ====== Inserção recursiva: morse '.' vai para left, '-' vai para right ======
    public void insert(String morse, char c) {
        if (morse == null) return;
        insertRec(root, morse, 0, Character.toUpperCase(c));
    }

    private void insertRec(Node node, String morse, int idx, char c) {
        if (idx == morse.length()) {
            node.value = c;
            node.hasValue = true;
            return;
        }
        char ch = morse.charAt(idx);
        if (ch == '.') {
            if (node.left == null) node.left = new Node();
            insertRec(node.left, morse, idx + 1, c);
        } else if (ch == '-') {
            if (node.right == null) node.right = new Node();
            insertRec(node.right, morse, idx + 1, c);
        } // caracteres inválidos são ignorados
    }

    // ====== Encode (texto -> morse) ======
    public String encodeText(String text) {
        if (text == null) return "";
        String out = "";
        int i = 0;
        while (i < text.length()) {
            char ch = Character.toUpperCase(text.charAt(i));
            if (ch == ' ') {
                // separador de palavras: usa " / "
                out = out + "/ ";
                i++;
                continue;
            }
            String code = encodeChar(root, ch, "");
            if (code.length() > 0) {
                out = out + code + " ";
            }
            i++;
        }
        return out.trim();
    }

    // Busca recursiva do código de um caractere
    private String encodeChar(Node node, char target, String path) {
        if (node == null) return "";
        if (node.hasValue && node.value == target) return path;
        String left = encodeChar(node.left, target, path + ".");
        if (left.length() > 0) return left;
        String right = encodeChar(node.right, target, path + "-");
        if (right.length() > 0) return right;
        return "";
    }

    // ====== Decode (morse -> texto) ======
    public String decodeText(String morseLine) {
        if (morseLine == null) return "";
        String out = "";
        int i = 0;
        while (i < morseLine.length()) {
            // pula espaços extras
            while (i < morseLine.length() && morseLine.charAt(i) == ' ') i++;
            if (i >= morseLine.length()) break;

            // separador de palavras "/"
            if (morseLine.charAt(i) == '/') {
                out = out + " ";
                i++;
                continue;
            }

            // lê um token até espaço ou '/'
            String code = "";
            while (i < morseLine.length()) {
                char ch = morseLine.charAt(i);
                if (ch == '.' || ch == '-') {
                    code = code + ch;
                    i++;
                } else {
                    break;
                }
            }

            char decoded = decodeCode(root, code, 0);
            if (decoded != '\0') {
                out = out + decoded;
            }

            // consome espaços entre tokens
            while (i < morseLine.length() && morseLine.charAt(i) == ' ') i++;
        }
        return out;
    }

    // desce recursivamente pelo código morse
    private char decodeCode(Node node, String code, int idx) {
        if (node == null) return '\0';
        if (idx == code.length()) {
            return node.hasValue ? node.value : '\0';
        }
        char ch = code.charAt(idx);
        if (ch == '.') return decodeCode(node.left, code, idx + 1);
        if (ch == '-') return decodeCode(node.right, code, idx + 1);
        return '\0';
    }

    // ====== Suporte ao app/visualizador ======
    /** Mantido por compatibilidade (já existia no seu arquivo). */
    public Node getRoot() { return root; }

    /** API pública para o TreeVisualizer (sem reflexão): trabalha com "handles". */
    public Object getRootHandle() { return root; }

    public Object leftOf(Object handle) {
        if (handle == null) return null;
        return ((Node) handle).left;
    }

    public Object rightOf(Object handle) {
        if (handle == null) return null;
        return ((Node) handle).right;
    }

    public char valueOf(Object handle) {
        if (handle == null) return '\0';
        return ((Node) handle).value;
    }

    public boolean hasValue(Object handle) {
        if (handle == null) return false;
        return ((Node) handle).hasValue;
    }
}
