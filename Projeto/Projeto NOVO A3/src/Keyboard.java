import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class Keyboard implements KeyListener {
    private JogoRebote50 jogo;
    private Raquete raquete;

    public Keyboard(JogoRebote50 jogo, Raquete raquete) {
        this.jogo = jogo;
        this.raquete = raquete;
    }

    @Override
    public void keyTyped(KeyEvent e) {
    }

    @Override
    public void keyPressed(KeyEvent e) {
        int keyCode = e.getKeyCode();

        if (keyCode == KeyEvent.VK_LEFT) {
            jogo.moverRaqueteEsquerda();
        } else if (keyCode == KeyEvent.VK_RIGHT) {
            jogo.moverRaqueteDireita();
        } else if (keyCode == KeyEvent.VK_ENTER) {
            if (!jogo.isJogoRodando()) {
                jogo.iniciarJogo();
            }
        } else if (keyCode == KeyEvent.VK_ESCAPE) {
            if (jogo.isJogoRodando()) {
                jogo.setJogoPausado(!jogo.isJogoPausado());
            }
        } else if (keyCode == KeyEvent.VK_0) {
            jogo.encerrarJogo();
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
    }
}