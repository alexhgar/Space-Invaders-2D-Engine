package alexjulenerik.demo.model.estados;

import alexjulenerik.demo.model.GameModel;

public class EstadoDerrota implements EstadoJuego {
    @Override
    public void actualizarLogica(GameModel contexto) {
        // En derrota, el juego se congela
    }

    @Override
    public String getNombre() {
        return "DERROTA";
    }
}