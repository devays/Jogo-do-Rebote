import com.jogamp.opengl.GL2;

public class Raquete {
    private float x;
    private static final float LARGURA = 100.0f;

    private float raqueteX = LARGURA / 2.0f - 50.0f;
    private static final float RAQUETE_Y = 10.0f;
    private static final float RAQUETE_LARGURA = 140.0f;
    private static final float RAQUETE_ALTURA = 20.0f;

    public Raquete(float xInicial) {
        this.x = xInicial;
    }

    public float getRaqueteX() {
        return raqueteX;
    }

    public float setRaqueteX(float raqueteX) {
        return this.raqueteX = raqueteX;
    }

    public float getRaqueteY() {
        return RAQUETE_Y;
    }

    public float getRaqueteLargura() {
        return RAQUETE_LARGURA;
    }
    public float getRaqueteAltura() {
        return RAQUETE_ALTURA;
    }

    public void desenhar(GL2 gl) {
        gl.glColor3f(0.5f, 0.5f, 0.5f);
        gl.glBegin(GL2.GL_QUADS);
        gl.glVertex2f(raqueteX, RAQUETE_Y);
        gl.glVertex2f(raqueteX + RAQUETE_LARGURA, RAQUETE_Y);
        gl.glVertex2f(raqueteX + RAQUETE_LARGURA, RAQUETE_Y + RAQUETE_ALTURA);
        gl.glVertex2f(raqueteX, RAQUETE_Y + RAQUETE_ALTURA);
        gl.glEnd();
    }
}
