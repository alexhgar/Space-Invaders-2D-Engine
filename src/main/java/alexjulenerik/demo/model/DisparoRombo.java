package alexjulenerik.demo.model;
import java.util.List;

//Implementamos disparo en bloque. Posicion se calcula mediante deltas en relacion con el pixel central
public class DisparoRombo implements EstrategiaDisparo{
    @Override
    public void realizarDisparo(int fila, int col, List<Disparo> listaDisparos){
        Disparo d = new Disparo(fila, col);
        d.getForma().add(new int[]{0,0});
        d.getForma().add(new int[]{1,-1});
        d.getForma().add(new int[]{1,1});
        d.getForma().add(new int[]{2,0});
        listaDisparos.add(d);
    }
}
