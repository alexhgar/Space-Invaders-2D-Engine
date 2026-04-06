package alexjulenerik.demo.model;
import java.util.List;

public class DisparoFlecha implements EstrategiaDisparo{
    @Override
    public void realizarDisparo(int filaFondo, int col, List<Disparo> listaDisparos){
        // El punto más bajo de la flecha está 1 píxel por debajo de su centro (+1).
        Disparo d = new Disparo(filaFondo - 1, col);

        d.getForma().add(new int[]{0,0});
        d.getForma().add(new int[]{1,-1});
        d.getForma().add(new int[]{1,1});

        listaDisparos.add(d);
    }
}