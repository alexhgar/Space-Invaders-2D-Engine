package alexjulenerik.demo.model;
import java.util.List;

public class DisparoUnico implements EstrategiaDisparo{
    @Override
    public void realizarDisparo(int filaFondo, int col, List<Disparo> listaDisparos){
        // Como es 1 solo píxel, su fondo es directamente su centro
        Disparo d = new Disparo(filaFondo, col);
        d.getForma().add(new int[]{0, 0});
        listaDisparos.add(d);
    }
}