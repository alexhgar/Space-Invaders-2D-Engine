package alexjulenerik.demo.model.estados;

import alexjulenerik.demo.model.GameModel;

public class EstadoVictoria implements EstadoJuego {
    @Override
    public void actualizarLogica(GameModel contexto) {
        // En victoria, el juego se congela
    }

    @Override
    public String getNombre() {
        return "VICTORIA";
    }
}