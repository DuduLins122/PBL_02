/*
 * Componente JavaFX para desenhar a ABB do Morse.
 * Usa um Canvas e recursão; não utiliza coleções.
 */
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

public class TreeVisualizer extends Pane {
    private final Canvas canvas = new Canvas(900, 520);
    private Node root;

    public TreeVisualizer() {
        getChildren().add(canvas);
        setPrefSize(canvas.getWidth(), canvas.getHeight());
    }

    public void setRoot(Node r) {
        this.root = r;
        draw();
    }

    private double treeHeight(Node n) {
        if (n == null) return 0;
        double lh = treeHeight(n.left);
        double rh = treeHeight(n.right);
        return 1 + (lh > rh ? lh : rh);
    }

    private void drawEdge(GraphicsContext g, double x1, double y1, double x2, double y2) {
        g.strokeLine(x1, y1, x2, y2);
    }

    private void drawNode(GraphicsContext g, double x, double y, String text) {
        double r = 16;
        g.setFill(Color.LIGHTGRAY);
        g.fillOval(x - r, y - r, 2*r, 2*r);
        g.setStroke(Color.BLACK);
        g.strokeOval(x - r, y - r, 2*r, 2*r);
        g.setFill(Color.BLACK);
        g.setFont(Font.font(14));
        g.fillText(text, x - 5, y + 5);
    }

    private void drawRecursive(GraphicsContext g, Node n, double x, double y, double dx, double dy) {
        if (n == null) return;
        String label = n.hasValue ? String.valueOf(n.value) : "·";
        drawNode(g, x, y, label);

        if (n.left != null) {
            double nx = x - dx, ny = y + dy;
            drawEdge(g, x, y, nx, ny);
            drawRecursive(g, n.left, nx, ny, dx/1.6, dy);
        }
        if (n.right != null) {
            double nx = x + dx, ny = y + dy;
            drawEdge(g, x, y, nx, ny);
            drawRecursive(g, n.right, nx, ny, dx/1.6, dy);
        }
    }

    private void draw() {
        GraphicsContext g = canvas.getGraphicsContext2D();
        g.setFill(Color.WHITE);
        g.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());

        if (root == null) return;

        double h = treeHeight(root);
        double dy = 70;
        double dx = Math.pow(2, h) * 5; // espaçamento horizontal aproximado

        drawRecursive(g, root, canvas.getWidth()/2, 40, dx, dy);
    }
}
