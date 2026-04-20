package alexjulenerik.demo.model;

//JULEN
public class EnemigoFactory {

    public static Enemigo crearEnemigo(String tipoEnemigo, int filaCentral, int columnaCentral) {
        Enemigo nuevoEnemigo = new Enemigo(filaCentral, columnaCentral);

        if (tipoEnemigo.equals("BASICO")) {
            // Primera fila
            nuevoEnemigo.getForma().add(new int[]{-1, -2});
            nuevoEnemigo.getForma().add(new int[]{-1, -1});
            nuevoEnemigo.getForma().add(new int[]{-1, 1});
            nuevoEnemigo.getForma().add(new int[]{-1, 2});

            // Segunda fila
            nuevoEnemigo.getForma().add(new int[]{0, -1});
            nuevoEnemigo.getForma().add(new int[]{0, 0});  // Píxel central
            nuevoEnemigo.getForma().add(new int[]{0, 1});

            // Tercera fila
            nuevoEnemigo.getForma().add(new int[]{1, 0});

        } else {
            // Por si acaso
            nuevoEnemigo.getForma().add(new int[]{0, 0});
        }

        return nuevoEnemigo;
    }
}