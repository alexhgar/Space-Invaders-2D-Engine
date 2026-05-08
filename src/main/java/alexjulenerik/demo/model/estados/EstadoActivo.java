package alexjulenerik.demo.model.estados;

import alexjulenerik.demo.model.GameModel;

public class EstadoActivo implements EstadoJuego {
    @Override
    public void actualizarLogica(GameModel contexto) {
        // Mientras el juego esté activo, delegamos en el modelo para que mueva todo
        contexto.logicaJuegoActivo();
    }

    @Override
    public String getNombre() {
        return "ACTIVO";
    }
}