package alexjulenerik.demo.model;

//JULEN
public class EnemigoFactory {

    public static Enemigo crearEnemigo(String tipoEnemigo, int filaCentral, int columnaCentral) {
        Enemigo nuevoEnemigo = new Enemigo(filaCentral, columnaCentral);

        if (tipoEnemigo.equals("BASICO")) {
            // Primera fila (arriba): dos sí, uno no, dos sí
            nuevoEnemigo.getForma().add(new int[]{-1, -2});
            nuevoEnemigo.getForma().add(new int[]{-1, -1});
            nuevoEnemigo.getForma().add(new int[]{-1, 1});
            nuevoEnemigo.getForma().add(new int[]{-1, 2});

            // Segunda fila (centro): uno no, tres sí, uno no
            nuevoEnemigo.getForma().add(new int[]{0, -1});
            nuevoEnemigo.getForma().add(new int[]{0, 0});  // Píxel central
            nuevoEnemigo.getForma().add(new int[]{0, 1});

            // Tercera fila (abajo): dos no, uno sí, dos no
            nuevoEnemigo.getForma().add(new int[]{1, 0});

        } else {
            // Forma de emergencia por defecto (un solo píxel) si se pasa un tipo incorrecto
            nuevoEnemigo.getForma().add(new int[]{0, 0});
        }

        return nuevoEnemigo;
    }
}