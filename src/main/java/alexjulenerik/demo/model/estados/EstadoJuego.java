package alexjulenerik.demo.model.estados;

import alexjulenerik.demo.model.GameModel;

public interface EstadoJuego {
    void actualizarLogica(GameModel contexto);
    String getNombre();
}