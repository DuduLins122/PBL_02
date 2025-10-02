/*
 * Árvore Binária de Busca (ABB) para Código Morse.
 * Heurística: ponto vai para a esquerda (.) e traço vai para a direita (-).
 * Todas as operações (inserção, busca/encode, decode) são recursivas.
 * Não utiliza nenhuma estrutura pronta do Java (List, Map, etc.).
 */
public class MorseBST {
    private final Node root = new Node();

    public boolean isEmpty() {
        return !root.hasValue && root.left == null && root.right == null;
    }

    // ---------- INSERÇÃO (recursiva) ----------
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
        } else {
            // caractere inválido na descrição morse
            throw new IllegalArgumentException("Código morse inválido para inserção: '" + ch + "'");
        }
    }

    // ---------- DECODE de UM símbolo (recursivo) ----------
    public char decodeChar(String morse) {
        if (morse == null || morse.isEmpty()) return 0;
        return decodeCharRec(root, morse, 0);
    }

    private char decodeCharRec(Node node, String morse, int idx) {
        if (node == null) return 0;
        if (idx == morse.length()) {
            return node.hasValue ? node.value : 0;
        }
        char ch = morse.charAt(idx);
        if (ch == '.') return decodeCharRec(node.left, morse, idx + 1);
        if (ch == '-') return decodeCharRec(node.right, morse, idx + 1);
        return 0;
    }

    // ---------- ENCODE de UM caractere (recursivo DFS) ----------
    public String encodeChar(char c) {
        c = Character.toUpperCase(c);
        return encodeFindRec(root, c, "");
    }

    private String encodeFindRec(Node node, char target, String path) {
        if (node == null) return null;
        if (node.hasValue && node.value == target) return path;

        String leftTry = encodeFindRec(node.left, target, path + ".");
        if (leftTry != null) return leftTry;
        return encodeFindRec(node.right, target, path + "-");
    }

    // ---------- ENCODE de linha inteira (recursivo) ----------
    // Entre letras: espaço simples. Entre palavras: " / " (barra).
    public String encodeLine(String text) {
        if (text == null) return "";
        return encodeLineRec(text, 0, true);
    }

    private String encodeLineRec(String text, int idx, boolean first) {
        if (idx >= text.length()) return "";
        char ch = text.charAt(idx);

        // espaço em branco serve como separador de palavras
        if (Character.isWhitespace(ch)) {
            // pula sequência de espaços
            int j = idx + 1;
            while (j < text.length() && Character.isWhitespace(text.charAt(j))) j++;
            String sep = first ? "" : " / ";
            return sep + encodeLineRec(text, j, false);
        }

        String morse = encodeChar(ch);
        if (morse == null) {
            throw new IllegalArgumentException("Caractere não suportado para ENCODE: '" + ch + "'");
        }
        String prefix = first ? "" : " ";
        return prefix + morse + encodeLineRec(text, idx + 1, false);
    }

    // ---------- DECODE de linha inteira (recursivo) ----------
    // Aceita '.', '-', espaço e '/'; separa palavras por '/'.
    

    private void validateDecodeCharacters(String s, int idx) {
        if (idx >= s.length()) return;
        char ch = s.charAt(idx);
        if (ch != '.' && ch != '-' && ch != ' ' && ch != '/') {
            throw new IllegalArgumentException("Entrada de DECODE inválida: contém '" + ch + "'");
        }
        validateDecodeCharacters(s, idx + 1);
    }

    private int skipSpaces(String s, int idx) {
        if (idx >= s.length()) return idx;
        if (s.charAt(idx) == ' ') return skipSpaces(s, idx + 1);
        return idx;
    }

    // devolve próximo índice após consumir um token (até espaço ou '/')
    private int tokenEnd(String s, int idx) {
        if (idx >= s.length()) return idx;
        char ch = s.charAt(idx);
        if (ch == ' ' || ch == '/') return idx;
        return tokenEnd(s, idx + 1);
    }

    private void decodeLineRec(String s, int idx, StringBuilder out) {
        if (idx >= s.length()) return;

        // pular espaços
        int i = skipSpaces(s, idx);
        if (i >= s.length()) return;

        // separador de palavras
        if (s.charAt(i) == '/') {
            out = out + (' ');
            decodeLineRec(s, i + 1, out);
            return;
        }

        // ler token morse até espaço ou '/'
        int j = tokenEnd(s, i);
        String token = s.substring(i, j);
        char decoded = decodeChar(token);
        if (decoded == 0) {
            throw new IllegalArgumentException("Token morse inválido para DECODE: \"" + token + "\"");
        }
        out = out + (decoded);

        decodeLineRec(s, j, out);
    }

    // ---------- CARGA PADRÃO (A-Z e 0-9) ----------
    public void loadDefault() {
        // Letras
        insert(".-",   'A');
        insert("-...", 'B');
        insert("-.-.", 'C');
        insert("-..",  'D');
        insert(".",    'E');
        insert("..-.", 'F');
        insert("--.",  'G');
        insert("....", 'H');
        insert("..",   'I');
        insert(".---", 'J');
        insert("-.-",  'K');
        insert(".-..", 'L');
        insert("--",   'M');
        insert("-.",   'N');
        insert("---",  'O');
        insert(".--.", 'P');
        insert("--.-", 'Q');
        insert(".-.",  'R');
        insert("...",  'S');
        insert("-",    'T');
        insert("..-",  'U');
        insert("...-", 'V');
        insert(".--",  'W');
        insert("-..-", 'X');
        insert("-.--", 'Y');
        insert("--..", 'Z');

        // Dígitos
        insert("-----", '0');
        insert(".----", '1');
        insert("..---", '2');
        insert("...--", '3');
        insert("....-", '4');
        insert(".....", '5');
        insert("-....", '6');
        insert("--...", '7');
        insert("---..", '8');
        insert("----.", '9');
    }

    public Node getRoot() { return root; }


// ---------- DECODE de linha inteira (recursivo) ----------
// Aceita '.', '-', espaço e '/'; separa palavras por '/'.
// Versão sem StringBuilder.
public String decodeLine(String line) {
    if (line == null) return "";
    validateDecodeCharacters(line, 0);
    return decodeLineRecNoSB(line);
}

private String decodeLineRecNoSB(String s) {
    if (s == null || s.isEmpty()) return "";
    // pular espaços no começo
    int i = 0;
    while (i < s.length() && s.charAt(i) == ' ') i++;
    if (i >= s.length()) return "";

    char ch = s.charAt(i);

    // separador de palavra
    if (ch == '/') {
        int j = i + 1;
        while (j < s.length() && s.charAt(j) == ' ') j++;
        return " " + decodeLineRecNoSB(s.substring(j));
    }

    // ler token morse [.,-] até espaço ou '/'
    int j = i;
    while (j < s.length()) {
        char c = s.charAt(j);
        if (c == ' ' || c == '/') break;
        j++;
    }
    String token = s.substring(i, j);
    char decoded = decodeChar(token);
    if (decoded == 0) {
        throw new IllegalArgumentException("Token morse inválido para DECODE: \"" + token + "\"");
    }

    int k = j;
    while (k < s.length() && s.charAt(k) == ' ') k++;
    return decoded + decodeLineRecNoSB(s.substring(k));
}
}
