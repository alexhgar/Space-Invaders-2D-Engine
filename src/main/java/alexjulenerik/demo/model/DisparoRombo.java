package alexjulenerik.demo.model;
import java.util.List;

public class DisparoRombo implements EstrategiaDisparo{
    @Override
    public void realizarDisparo(int filaFondo, int col, List<Disparo> listaDisparos){
        // El punto más bajo del rombo está 2 píxeles por debajo de su centro (+2).
        // Restamos 2 a la fila de fondo para saber dónde colocar el centro del objeto.
        Disparo d = new Disparo(filaFondo - 2, col);

        // Primera fila (nada, nada, punto, nada, nada)
        d.getForma().add(new int[]{-2, 0});

        // Segunda fila (nada, punto, punto, punto, nada)
        d.getForma().add(new int[]{-1, -1});
        d.getForma().add(new int[]{-1, 0});
        d.getForma().add(new int[]{-1, 1});

        // Tercera fila - CENTRO (punto, punto, punto, punto, punto)
        d.getForma().add(new int[]{0, -2});
        d.getForma().add(new int[]{0, -1});
        d.getForma().add(new int[]{0, 0});
        d.getForma().add(new int[]{0, 1});
        d.getForma().add(new int[]{0, 2});

        // Cuarta fila (nada, punto, punto, punto, nada)
        d.getForma().add(new int[]{1, -1});
        d.getForma().add(new int[]{1, 0});
        d.getForma().add(new int[]{1, 1});

        // Quinta fila - FONDO (nada, nada, punto, nada, nada)
        d.getForma().add(new int[]{2, 0});

        listaDisparos.add(d);
    }
}