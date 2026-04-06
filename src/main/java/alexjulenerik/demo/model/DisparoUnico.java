package alexjulenerik.demo.model;
import java.util.List;

//Implementamos disparo en bloque. Posicion se calcula mediante deltas en relacion con el pixel central
public class DisparoUnico implements EstrategiaDisparo{
    @Override

    public void realizarDisparo(int fila, int col, List<Disparo> listaDisparos){
        Disparo d = new Disparo(fila, col);
        d.getForma().add(new int[]{0, 0});
        listaDisparos.add(d);
    }
}
