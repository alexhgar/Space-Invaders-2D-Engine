package alexjulenerik.demo.model;

import java.util.List;

// Aplicamos patron Strategy. Permite añadir nuevos tipos de disparos sin modificar la lógica de GameModel

public interface EstrategiaDisparo {
    void realizarDisparo (int fila, int col, List<Disparo> listaDisparos);
}


    

        


