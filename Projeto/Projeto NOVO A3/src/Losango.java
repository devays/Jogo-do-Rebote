import com.jogamp.opengl.GL2;
import com.jogamp.opengl.glu.GLU;

public class Losango {
    private float x;
    private float y;
    private float width;
    private float height;

    public Losango(float x, float y, float width, float height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    // Métodos getter e setter podem ser úteis se você quiser alterar o tamanho do losango durante a execução

    public void setWidth(float width) {
        this.width = width;
    }

    public void setHeight(float height) {
        this.height = height;
    }

    public float getWidth() {
        return width;
    }

    public float getHeight() {
        return height;
    }

    public void desenhar(GL2 gl) {
        gl.glBegin(GL2.GL_POLYGON);

        gl.glVertex2f(x - width / 2, y);
        gl.glVertex2f(x, y + height / 2);
        gl.glVertex2f(x + width / 2, y);
        gl.glVertex2f(x, y - height / 2);

        gl.glEnd();
    }

    public boolean verificaColisaoBola(float bolaX, float bolaY, float raioBola) {
        float deltaX = bolaX - x;
        float deltaY = bolaY - y;

        // Coordenadas relativas ao sistema de coordenadas local do losango (rotacionado)
        float localX = (float) (deltaX * Math.cos(Math.toRadians(-45))) - (float) (deltaY * Math.sin(Math.toRadians(-45)));
        float localY = (float) (deltaX * Math.sin(Math.toRadians(-45))) + (float) (deltaY * Math.cos(Math.toRadians(-45)));

        // Verifica se a bola está dentro do retângulo delimitado pelo losango
        if (Math.abs(localX) > (width / 2 + raioBola) || Math.abs(localY) > (height / 2 + raioBola)) {
            return false;
        }

        // Verifica se a bola está dentro do losango
        return (Math.abs(localX) / (width / 2) + Math.abs(localY) / (height / 2)) <= 1;
    }
}