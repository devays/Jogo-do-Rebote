import com.jogamp.opengl.*;
import com.jogamp.opengl.awt.GLJPanel;
import com.jogamp.opengl.util.FPSAnimator;
import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import com.jogamp.opengl.util.awt.TextRenderer;
import com.jogamp.opengl.glu.GLU;

public class RegrasDoJogo implements GLEventListener, KeyListener {
    private static final int LARGURA = 1920;
    private static final int ALTURA = 1080;
    private JFrame frame;

    public RegrasDoJogo() {
        GLProfile glProfile = GLProfile.get(GLProfile.GL2);
        GLCapabilities capabilities = new GLCapabilities(glProfile);

        GLJPanel panel = new GLJPanel(capabilities);
        panel.addGLEventListener(this);

        frame = new JFrame("Bem-Vindo ao Rebote 5.0");
        frame.setUndecorated(true);  // Tela cheia sem bordas
        frame.setSize(LARGURA, ALTURA);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(false);
        frame.addKeyListener(this);
        frame.setFocusable(true);
        frame.getContentPane().add(panel);

        FPSAnimator animator = new FPSAnimator(panel, 60);
        animator.start();

        frame.setVisible(true);
    }

    @Override
    public void init(GLAutoDrawable drawable) {

    }

    @Override
    public void display(GLAutoDrawable drawable) {
        GL2 gl = drawable.getGL().getGL2();
        gl.glClearColor(0.0f, 0.0f, 0.0f, 0);
        gl.glClear(GL2.GL_COLOR_BUFFER_BIT);

        // Desenha o título
        desenharTitulo(gl);

        // Desenha as regras do jogo
        desenharRegrasJogo(gl);

        // Desenha os comandos
        desenharComandos(gl);
    }

    private void desenharTitulo(GL2 gl) {
        gl.glColor3f(1, 1, 1); // Cor branca para o título

        // Configurar a fonte e o tamanho do texto
        Font font = new Font("SansSerif", Font.BOLD, 36);
        TextRenderer textRenderer = new TextRenderer(font);

        // Título do jogo
        String titulo = "Bem-Vindo ao Rebote 5.0";

        // Calcular a largura do título para centralizá-lo
        int larguraTitulo = (int) textRenderer.getBounds(titulo).getWidth();
        int xTitulo = (LARGURA - larguraTitulo) / 2;
        int yTitulo = ALTURA / 2 + 430; // Ajuste conforme necessário

        // Desenhar o título na tela
        textRenderer.beginRendering(LARGURA, ALTURA);
        textRenderer.setColor(Color.WHITE);
        textRenderer.draw(titulo, xTitulo, yTitulo);
        textRenderer.endRendering();
    }

    private void desenharRegrasJogo(GL2 gl) {
        gl.glColor3f(1, 1, 1); // Cor branca para as regras

        // Configurar a fonte e o tamanho do texto
        Font font = new Font("SansSerif", Font.PLAIN, 20);
        TextRenderer textRenderer = new TextRenderer(font);

        // Regras do jogo
        String[] regras = {
                "Regras do jogo:",
                "Faça a bolinha bater na raquete, a cada acerto você ganhará 35 pontos!",
                "Não deixe a bolinha cair, você tem somente 5 vidas.",
                "Após alcançar 200 pontos, a fase ficará mais difícil, terá um novo obstáculo."
        };

        // Calcular a largura das regras para centralizá-las
        int xRegras = 50;
        int yRegras = ALTURA / 2 + 150; // Ajuste conforme necessário

        // Desenhar as regras na tela
        textRenderer.beginRendering(LARGURA, ALTURA);
        textRenderer.setColor(Color.WHITE);
        for (String regra : regras) {
            textRenderer.draw(regra, xRegras, yRegras);
            yRegras -= 30; // Ajuste conforme necessário
        }
        textRenderer.endRendering();
    }


    private void desenharComandos(GL2 gl) {
        gl.glColor3f(1, 1, 1); // Cor branca para os comandos

        // Configurar a fonte e o tamanho do texto
        Font font = new Font("SansSerif", Font.PLAIN, 20);
        TextRenderer textRenderer = new TextRenderer(font);

        // Comandos do jogo
        String[] comandos = {
                "COMANDOS",
                "Utilize as SETAS do teclado para se mover",
                "ESC - Pausará o jogo",
                "Número 0 - Fechará o jogo",
                "ENTER - Começará o jogo",
                "APERTE ESPAÇO PARA IR PARA O JOGO!"
        };

        // Calcular a largura dos comandos para centralizá-los
        int xComandos = 50;
        int yComandos = ALTURA / 2 - 150; // Ajuste conforme necessário

        // Desenhar os comandos na tela
        textRenderer.beginRendering(LARGURA, ALTURA);
        textRenderer.setColor(Color.WHITE);
        for (String comando : comandos) {
            textRenderer.draw(comando, xComandos, yComandos);
            yComandos -= 30; // Ajuste conforme necessário
        }
        textRenderer.endRendering();
    }


    @Override
    public void reshape(GLAutoDrawable drawable, int x, int y, int width, int height) {
        GL2 gl = drawable.getGL().getGL2();
        GLU glu = new GLU();

        if (height <= 0) {
            height = 1;
        }

        float aspect = (float) width / height;

        gl.glViewport(0, 0, width, height);
        gl.glMatrixMode(GL2.GL_PROJECTION);
        gl.glLoadIdentity();

        glu.gluOrtho2D(0, LARGURA, 0, ALTURA);

        gl.glMatrixMode(GL2.GL_MODELVIEW);
        gl.glLoadIdentity();
    }


    @Override
    public void dispose(GLAutoDrawable drawable) {
    }

    @Override
    public void keyReleased(KeyEvent e) {
        // Implemente conforme necessário
    }

    @Override
    public void keyPressed(KeyEvent e) {
        int keyCode = e.getKeyCode();
         if (keyCode == KeyEvent.VK_SPACE) {
            fecharTela();
        }
    }

    private void fecharTela() {
        // Fecha a tela atual e abre a tela do jogo principal
        frame.dispose();
        new JogoRebote50();
    }

    @Override
    public void keyTyped(KeyEvent e) {
        // Adicione a lógica conforme necessário
    }

    public static void main(String[] args) {
        new RegrasDoJogo();
    }
}
