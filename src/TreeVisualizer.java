import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

/**
 * Navega pela árvore usando somente a API pública exposta em MorseBST.
 */
public class TreeVisualizer extends StackPane {

    private final Canvas canvas = new Canvas(800, 500);

    // Referência à árvore e handle da raiz
    private MorseBST tree;
    private Object rootHandle;

    public TreeVisualizer() {
        getChildren().add(canvas);

        // Redesenha ao redimensionar
        widthProperty().addListener((o, a, b) -> {
            canvas.setWidth(b.doubleValue());
            redraw();
        });
        heightProperty().addListener((o, a, b) -> {
            canvas.setHeight(b.doubleValue());
            redraw();
        });
    }

    /** Associa o visualizador a uma instância de MorseBST. */
    public void bind(MorseBST tree) {
        this.tree = tree;
        if (tree != null) {
            this.rootHandle = tree.getRootHandle();
        }
        redraw();
    }

    /** Atualiza a raiz (handle retornado por MorseBST.getRootHandle()). */
    public void setRootHandle(Object rootHandle) {
        this.rootHandle = rootHandle;
        redraw();
    }

    private void redraw() {
        GraphicsContext g = canvas.getGraphicsContext2D();
        double w = canvas.getWidth();
        double h = canvas.getHeight();

        // fundo
        g.setFill(Color.WHITE);
        g.fillRect(0, 0, w, h);

        if (tree == null || rootHandle == null) {
            g.setFill(Color.GRAY);
            g.setFont(Font.font(16));
            g.fillText("Árvore vazia. Insira elementos para visualizar.", 20, 40);
            return;
        }

        int height = heightOf(rootHandle);
        height = Math.max(height, 1);

        double topMargin = 40;
        double levelGap = Math.max(70, (h - topMargin - 40) / Math.max(1, height));
        double initialX = w / 2.0;
        double initialY = topMargin;
        double initialOffset = Math.max(40, w / 4.0);

        g.setStroke(Color.BLACK);
        g.setLineWidth(1.5);
        g.setFont(Font.font(14));

        drawNode(g, rootHandle, initialX, initialY, initialOffset, levelGap);
    }

    private void drawNode(GraphicsContext g, Object handle, double x, double y,
                          double xOffset, double levelGap) {
        if (handle == null) return;

        Object left = tree.leftOf(handle);
        Object right = tree.rightOf(handle);

        // arestas primeiro
        if (left != null) {
            double nx = x - xOffset;
            double ny = y + levelGap;
            g.strokeLine(x, y + 15, nx, ny - 15);
            drawNode(g, left, nx, ny, Math.max(18, xOffset / 2.0), levelGap);
        }
        if (right != null) {
            double nx = x + xOffset;
            double ny = y + levelGap;
            g.strokeLine(x, y + 15, nx, ny - 15);
            drawNode(g, right, nx, ny, Math.max(18, xOffset / 2.0), levelGap);
        }

        // nó
        double r = 18;
        g.setFill(Color.WHITE);
        g.fillOval(x - r, y - r, 2 * r, 2 * r);
        g.strokeOval(x - r, y - r, 2 * r, 2 * r);

        // valor (se tiver)
        if (tree.hasValue(handle)) {
            char ch = tree.valueOf(handle);
            if (ch != '\0' && ch != ' ') {
                String s = String.valueOf(ch);
                // medir com Text (sem com.sun.*)
                Text t = new Text(s);
                t.setFont(g.getFont());
                double tw = t.getLayoutBounds().getWidth();
                double th = t.getLayoutBounds().getHeight();
                g.setFill(Color.BLACK);
                g.fillText(s, x - tw / 2.0, y + th / 4.0);
            }
        }
    }

    // -------- utilitário de altura (navega via API pública do MorseBST) --------
    private int heightOf(Object handle) {
        if (handle == null) return 0;
        Object l = tree.leftOf(handle);
        Object r = tree.rightOf(handle);
        return 1 + Math.max(heightOf(l), heightOf(r));
    }
}
