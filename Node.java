/*
 * Nó simples da árvore do Código Morse.
 * Guarda um caractere opcional (quando aquele caminho representa um símbolo)
 * e dois filhos: esquerda (.) e direita (-).
 * Nenhuma estrutura pronta de dados é utilizada.
 */
public class Node {
    public char value;
    public boolean hasValue;
    public Node left;   // '.'
    public Node right;  // '-'

    public Node() {
        this.value = '\0';
        this.hasValue = false;
        this.left = null;
        this.right = null;
    }
}
