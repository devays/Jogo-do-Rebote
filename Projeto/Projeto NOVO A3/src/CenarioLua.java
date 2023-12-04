import com.jogamp.opengl.GL2;
import java.util.Random;

public class CenarioLua {

    private static final int NUM_ESTRELAS = 50;
    private static final int NUM_NUVENS = 10; // Número de nuvens

    private float luaX;
    private float luaY;
    private float raioLua;

    private float[] estrelasX;
    private float[] estrelasY;
    private int largura; // Adicionando uma largura
    private int altura;  // Adicionando uma altura

    private float[][] nuvensX;
    private float[][] nuvensY;
    private float[] nuvensTamanho;

    public CenarioLua(int largura, int altura) {
        this.largura = largura;  // Definindo a largura
        this.altura = altura;    // Definindo a altura
        Random random = new Random();

        // Posição e raio da lua
        luaX = largura / 4.0f;
        luaY = altura - altura / 4.0f;
        raioLua = 50.0f;

        // Posições aleatórias das estrelas
        estrelasX = new float[NUM_ESTRELAS];
        estrelasY = new float[NUM_ESTRELAS];
        for (int i = 0; i < NUM_ESTRELAS; i++) {
            estrelasX[i] = random.nextFloat() * largura;
            estrelasY[i] = random.nextFloat() * altura;
        }

        // Inicialização das nuvens
        nuvensX = new float[NUM_NUVENS][];
        nuvensY = new float[NUM_NUVENS][];
        nuvensTamanho = new float[NUM_NUVENS];
        for (int i = 0; i < NUM_NUVENS; i++) {
            int numBolas = 3; // Cada nuvem tem 3 bolas
            nuvensX[i] = new float[numBolas];
            nuvensY[i] = new float[numBolas];
            nuvensTamanho[i] = random.nextInt(20) + 10; // Tamanho entre 10 e 30

            float nuvemCenterX = random.nextFloat() * largura;
            float nuvemCenterY = random.nextFloat() * (altura / 2.0f); // Ajustando a altura para espalhar as nuvens

            // Distribui as bolas da nuvem com um espaçamento maior
            for (int j = 0; j < numBolas; j++) {
                nuvensX[i][j] = nuvemCenterX - nuvensTamanho[i] + j * nuvensTamanho[i] * 1.2f;
                nuvensY[i][j] = nuvemCenterY;
            }
        }
    }

    public void desenhar(GL2 gl) {
        desenharEstrelas(gl);
        desenharLua(gl);
        desenharNuvens(gl);
    }

    private void desenharEstrelas(GL2 gl) {
        // Cor mais quente para as estrelas
        float corEstrelas[] = {1.0f, 1.0f, 0.9f};

        gl.glPointSize(5.0f);

        gl.glBegin(GL2.GL_POINTS);
        for (int i = 0; i < NUM_ESTRELAS; i++) {
            float distanciaX = estrelasX[i] - luaX;
            float distanciaY = estrelasY[i] - luaY;
            float distancia = (float) Math.sqrt(distanciaX * distanciaX + distanciaY * distanciaY);

            float intensidadeLuz = Math.max(0, 1 - distancia / largura); // Intensidade da luz decresce com a distância

            float cor[] = {
                    corEstrelas[0] * intensidadeLuz,
                    corEstrelas[1] * intensidadeLuz,
                    corEstrelas[2] * intensidadeLuz
            };

            gl.glColor3fv(cor, 0);
            gl.glVertex2f(estrelasX[i], estrelasY[i]);
        }
        gl.glEnd();
    }

    private void desenharLua(GL2 gl) {
        // Cor amarela para o centro da lua
        float corCentro[] = {1.0f, 0.95f, 0.8f};

        // Cor mais escura para a borda da lua
        float corBorda[] = {0.9f, 0.85f, 0.7f};

        gl.glBegin(GL2.GL_TRIANGLE_FAN);
        gl.glColor3fv(corCentro, 0);
        gl.glVertex2f(luaX, luaY); // Centro da lua

        for (int i = 0; i <= 360; i += 5) {
            float radianos = (float) Math.toRadians(i);
            float x = luaX + raioLua * (float) Math.cos(radianos);
            float y = luaY + raioLua * (float) Math.sin(radianos);

            float t = i / 360.0f; // Normalização para obter um valor entre 0 e 1

            // Interpolação linear entre as cores do centro e da borda
            float cor[] = {
                    (1 - t) * corCentro[0] + t * corBorda[0],
                    (1 - t) * corCentro[1] + t * corBorda[1],
                    (1 - t) * corCentro[2] + t * corBorda[2]
            };

            gl.glColor3fv(cor, 0);
            gl.glVertex2f(x, y);
        }
        gl.glEnd();
    }

    private void desenharNuvens(GL2 gl) {
        // Cor das nuvens
        float corNuvens[] = {0.0f, 0.0f, 0.05f};

        for (int i = 0; i < NUM_NUVENS; i++) {
            gl.glColor3fv(corNuvens, 0);
            for (int j = 0; j < nuvensX[i].length; j++) {
                gl.glBegin(GL2.GL_TRIANGLE_FAN);
                for (int k = 0; k <= 360; k += 5) {
                    float radianos = (float) Math.toRadians(k);
                    float x = nuvensX[i][j] + nuvensTamanho[i] * (float) Math.cos(radianos);
                    float y = nuvensY[i][j] + nuvensTamanho[i] * (float) Math.sin(radianos);
                    gl.glVertex2f(x, y);
                }
                gl.glEnd();
            }
        }
    }
}
