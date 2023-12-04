import com.jogamp.opengl.*;
import com.jogamp.opengl.awt.GLJPanel;
import com.jogamp.opengl.util.FPSAnimator;
import com.jogamp.opengl.glu.GLU;
import com.jogamp.opengl.util.awt.TextRenderer;

import java.awt.Font;
import java.awt.Color;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.JFrame;

public class JogoRebote50 implements GLEventListener, KeyListener {
    public static final int LARGURA = 1920;
    private static final int ALTURA = 1080;
    private static final int TAMANHO_BOLA = 12;
    private Losango losango;
    private TextRenderer textRenderer;
    private float bolaX = LARGURA / 2.0f;
    private float bolaY = ALTURA / 2.0f;

    private CenarioLua cenarioLua;
    private Raquete raquete;

    private float velocidadeInicialBolaX = 5.0f;
    private float velocidadeInicialBolaY = 3.0f;
    private float velocidadeBolaX = velocidadeInicialBolaX;
    private float velocidadeBolaY = velocidadeInicialBolaY;

    private boolean jogoRodando = false;
    private boolean jogoPausado = false;
    private boolean mostrarMensagemInicial = true;
    private String mensagemPausa = "Jogo Pausado, Aperte ESC para voltar ao jogo.";

    private int pontuacao = 0;
    private static final int VIDAS_INICIAIS = 5;
    private int vidas = VIDAS_INICIAIS;
    private static final int TAMANHO_vidas = 20;

    private Keyboard keyboard;

    public JogoRebote50() {
        GLProfile glProfile = GLProfile.get(GLProfile.GL2);
        GLCapabilities capabilities = new GLCapabilities(glProfile);

        GLJPanel panel = new GLJPanel(capabilities);
        panel.addGLEventListener(this);

        JFrame frame = new JFrame("JOGO DO REBOTE");
        frame.setSize(LARGURA, ALTURA);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(false);

        // Adicione o ouvinte de teclado à instância correta do JFrame
        frame.addKeyListener(new Keyboard(this, raquete));
        frame.setFocusable(true);

        frame.setUndecorated(true);

        frame.getContentPane().add(panel);

        FPSAnimator animator = new FPSAnimator(panel, 60);
        animator.start();

        frame.setVisible(true);
        raquete = new Raquete(LARGURA / 2.0f - 50.0f);
    }

    @Override
    public void init(GLAutoDrawable drawable) {
        textRenderer = new TextRenderer(new Font("SansSerif", Font.PLAIN, 16));
    }

    @Override
    public void display(GLAutoDrawable drawable) {
        GL2 gl = drawable.getGL().getGL2();
        gl.glClearColor(0.0f, 0.0f, 0.1f, 1.0f);
        gl.glClear(GL2.GL_COLOR_BUFFER_BIT);

        // Desenha o cenário
        if (cenarioLua == null) {
            cenarioLua = new CenarioLua(LARGURA, ALTURA);
        }
        cenarioLua.desenhar(gl);

        if (jogoRodando && !jogoPausado) {
            moverBola();
            verificarColisaoRaquete();

            // Colisão com o losango
            if (losango != null && losango.verificaColisaoBola(bolaX, bolaY, TAMANHO_BOLA / 2)) {
                // Bola nao ultrapassar o losango
                ajustarPosicaoBolaAposColisaoLosango();
            }
            gl.glMatrixMode(GL2.GL_MODELVIEW);
            gl.glLoadIdentity();
            desenharTextoPontuacao(gl);

            // Desenha as vidas
            desenharVidas(gl);

            // Desenha a bola
            desenharBola(gl);

            // Desenha a raquete
            raquete.desenhar(gl);

            // Desenha o losango centralizado
            if (pontuacao >= 200) {
                desenharLosangoCentralizado(gl);
            }
            gl.glFlush();
        }

        // mensagem de pausa e inicial
        if (jogoPausado) {
            desenharMensagemPausa(gl);
        } else if (mostrarMensagemInicial && !jogoRodando) {
            desenharMensagemInicial(gl);
        }

        gl.glFlush();
    }

    private void desenharVidas(GL2 gl) {
        gl.glColor3f(1, 0, 0);

        for (int i = 0; i < vidas; i++) {
            float vidasX = LARGURA - (i + 1) * (TAMANHO_vidas + 5); // Espaçamento de 5 entre os quadrados
            float vidasY = ALTURA - TAMANHO_vidas - 5; // Distância de 5 do topo

            gl.glBegin(GL2.GL_QUADS);

            gl.glVertex2f(vidasX, vidasY);
            gl.glVertex2f(vidasX + TAMANHO_vidas, vidasY);
            gl.glVertex2f(vidasX + TAMANHO_vidas, vidasY - TAMANHO_vidas);
            gl.glVertex2f(vidasX, vidasY - TAMANHO_vidas);

            gl.glEnd();
        }
    }
    private void ajustarPosicaoBolaAposColisaoLosango() {
        // Ajustar a posição da bola após a colisão com o losango
        velocidadeBolaX = -velocidadeBolaX;
        velocidadeBolaY = -velocidadeBolaY;
    }
    private void desenharLosangoCentralizado(GL2 gl) {
        // Criar a instância de Losango se ainda não foi criada
        if (losango == null) {
            losango = new Losango(LARGURA / 2.0f, ALTURA / 2.0f, 50.0f, 50.0f);
        }

        // Desenha o losango
        losango.desenhar(gl);
    }
    private void desenharTextoPontuacao(GL2 gl) {
        if (textRenderer == null) {
            return;
        }

        textRenderer.beginRendering(LARGURA, ALTURA);
        textRenderer.setColor(Color.WHITE);
        textRenderer.draw("PONTUAÇÃO: " + pontuacao, LARGURA - 150, ALTURA - 20);
        textRenderer.endRendering();
    }
    private void desenharMensagemPausa(GL2 gl) {
        // Fonte e o tamanho do texto
        Font font = new Font("SansSerif", Font.PLAIN, 16);
        textRenderer = new TextRenderer(font);

        // Largura da mensagem de pausa
        int larguraTexto = (int) textRenderer.getBounds(mensagemPausa).getWidth();

        // Cor branca para o texto
        gl.glColor3f(1, 1, 1);

        // Posiciona o texto no centro da tela
        int xTexto = (LARGURA - larguraTexto) / 2;
        int yTexto = ALTURA / 2;

        // Desenha a mensagem de pausa
        textRenderer.beginRendering(LARGURA, ALTURA);
        textRenderer.draw(mensagemPausa, xTexto, yTexto);
        textRenderer.endRendering();
    }

    private void desenharMensagemInicial(GL2 gl) {
        gl.glColor3f(1, 1, 1); // Cor branca

        Font font = new Font("SansSerif", Font.PLAIN, 24);
        TextRenderer textRenderer = new TextRenderer(font);

        // Mensagem inicial
        String mensagem = "Pressione ENTER para iniciar o jogo";

        // Largura da mensagem para centralizá-la
        int larguraTexto = (int) textRenderer.getBounds(mensagem).getWidth();
        int xTexto = (LARGURA - larguraTexto) / 2;
        int yTexto = ALTURA / 2;

        // Desenhar a mensagem na tela
        textRenderer.beginRendering(LARGURA, ALTURA);
        textRenderer.setColor(Color.WHITE);
        textRenderer.draw(mensagem, xTexto, yTexto);
        textRenderer.endRendering();
    }

    private void desenharBola(GL2 gl) {
        gl.glColor3f(1, 1, 1); // Cor branca
        gl.glBegin(GL2.GL_POLYGON);
        for (int i = 0; i < 360; i += 5) {
            float radianos = (float) Math.toRadians(i);
            float x = bolaX + TAMANHO_BOLA * (float) Math.cos(radianos);
            float y = bolaY + TAMANHO_BOLA * (float) Math.sin(radianos);
            gl.glVertex2f(x, y);
        }
        gl.glEnd();
    }


    private void verificarColisaoRaquete() {
        // Colisão com a raquete
        if (bolaY - TAMANHO_BOLA / 2 <= raquete.getRaqueteY() + raquete.getRaqueteAltura() &&
                bolaY + TAMANHO_BOLA / 2 >= raquete.getRaqueteY() &&
                bolaX + TAMANHO_BOLA / 2 >= raquete.getRaqueteX() &&
                bolaX - TAMANHO_BOLA / 2 <= raquete.getRaqueteX() + raquete.getRaqueteLargura()) {

            // Verificar a direção da bola
            if (velocidadeBolaY > 0) {
                // Mexer na posição da bola para garantir que ela não ultrapasse a raquete por baixo
                bolaY = raquete.getRaqueteY() - TAMANHO_BOLA / 2;
            } else {
                // Mexer na posição da bola para garantir que ela não ultrapasse a raquete por cima
                bolaY = raquete.getRaqueteY() + raquete.getRaqueteAltura() + TAMANHO_BOLA / 2;
            }

            velocidadeBolaY = -velocidadeBolaY;  // Bola quica na raquete

            // Adicionar pontos
            pontuacao += 35;

            // Verificar se a pontuação atingiu 200
            if (pontuacao >= 200) {
                // Aumentar a velocidade gradativamente
                velocidadeBolaX *= 1.1;  // Aumenta em 10%
                velocidadeBolaY *= 1.1;

                // Configurar um losango maior
                if (losango == null) {
                    losango = new Losango(LARGURA / 2.0f, ALTURA / 2.0f, 160.0f, 160.0f);
                } else {
                    losango.setWidth(160.0f);
                    losango.setHeight(160.0f);
                }
            }
        }

        // Colisão com a parte superior
        if (bolaY + TAMANHO_BOLA / 2 >= ALTURA) {
            velocidadeBolaY = -velocidadeBolaY;  // Bola quica na parte superior
            bolaY = ALTURA - TAMANHO_BOLA / 2;    // Garante que a bola não ultrapasse a parte superior
        }
    }
    private void moverBola() {
        bolaX += velocidadeBolaX;
        bolaY += velocidadeBolaY;

        // Lógica de colisão da bola com as bordas
        if (bolaX - TAMANHO_BOLA / 2 < 0 || bolaX + TAMANHO_BOLA / 2 > LARGURA) {
            velocidadeBolaX = -velocidadeBolaX;
        }
        // Se a bola atingiu a parte inferior
        if (bolaY - TAMANHO_BOLA / 2 < 0) {
            perderVida();
        }
    }
    private void perderVida() {
        vidas--;

        // Verifica se ainda restam vidas
        if (vidas > 0) {
            reiniciarNivel(); // Reiniciar o nível
        } else {
            // Pressionar ENTER reinicia o jogo
            jogoRodando = false;
            mostrarMensagemInicial = true;
        }
    }

    private void reiniciarNivel() {
        bolaX = LARGURA / 2.0f;
        bolaY = raquete.getRaqueteY() - TAMANHO_BOLA - -220.0f;
        velocidadeBolaX = velocidadeInicialBolaX;
        velocidadeBolaY = velocidadeInicialBolaY;
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


    // Adicione os métodos necessários para permitir que a classe Keyboard atualize a posição da raquete
    public void moverRaqueteEsquerda() {
        if (raquete.getRaqueteX() - 10 >= 0) {
            raquete.setRaqueteX(raquete.getRaqueteX() - 10);
        }
    }

    public void moverRaqueteDireita() {
        if (raquete.getRaqueteX() + raquete.getRaqueteLargura() + 10 <= LARGURA) {
            raquete.setRaqueteX(raquete.getRaqueteX() + 10);
        }
    }

    public void encerrarJogo() {
        System.exit(0);  // Encerra o programa
    }

    public void iniciarJogo() {
        jogoRodando = true;
        mostrarMensagemInicial = false;
        bolaX = LARGURA / 2.0f;
        bolaY = raquete.getRaqueteY() - TAMANHO_BOLA - -220.0f;
        velocidadeBolaX = velocidadeInicialBolaX;
        velocidadeBolaY = velocidadeInicialBolaY;
        vidas = VIDAS_INICIAIS;
        pontuacao = 0;
    }


    @Override
    public void keyTyped(KeyEvent e) {
    }

    @Override
    public void keyPressed(KeyEvent e) {

    }

    @Override
    public void keyReleased(KeyEvent e) {

    }

    public boolean isJogoRodando() {
        return jogoRodando;
    }

    public boolean isJogoPausado() {
        return jogoPausado;
    }


    public static void main(String[] args) {
        new JogoRebote50();
    }

    public void setJogoPausado(boolean pausado) {
        this.jogoPausado = pausado;
    }
}