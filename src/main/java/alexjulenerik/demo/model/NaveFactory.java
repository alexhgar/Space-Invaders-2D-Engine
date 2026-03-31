package alexjulenerik.demo.model;

//JULEN
public class NaveFactory {

    public static Nave crearNave(String tipoNave, int filaCentral, int columnaCentral) {
        Nave nuevaNave = new Nave(filaCentral, columnaCentral);

        if (tipoNave.equals("GREEN")) {
            // Forma de T invertida (4 píxeles)
            nuevaNave.getForma().add(new int[]{0, 0});   // Centro
            nuevaNave.getForma().add(new int[]{0, -1});  // Izquierda
            nuevaNave.getForma().add(new int[]{0, 1});   // Derecha
            nuevaNave.getForma().add(new int[]{-1, 0});  // Arriba (punta)
        } else {
            if (tipoNave.equals("BLUE")) {
                // Forma de U (5 píxeles)
                nuevaNave.getForma().add(new int[]{0, 0});   // Centro (base)
                nuevaNave.getForma().add(new int[]{0, -1});  // Base izquierda
                nuevaNave.getForma().add(new int[]{0, 1});   // Base derecha
                nuevaNave.getForma().add(new int[]{-1, -1}); // Cuerno izquierdo
                nuevaNave.getForma().add(new int[]{-1, 1});  // Cuerno derecho
            } else {
                if (tipoNave.equals("RED")) {
                    // Forma de bloque ancho (6 píxeles)
                    nuevaNave.getForma().add(new int[]{0, 0});   // Centro
                    nuevaNave.getForma().add(new int[]{0, -1});  // Izquierda
                    nuevaNave.getForma().add(new int[]{0, 1});   // Derecha
                    nuevaNave.getForma().add(new int[]{-1, 0});  // Arriba centro
                    nuevaNave.getForma().add(new int[]{-1, -1}); // Arriba izquierda
                    nuevaNave.getForma().add(new int[]{-1, 1});  // Arriba derecha
                } else {
                    // Por si hay algún fallo, devolvemos un píxel simple
                    nuevaNave.getForma().add(new int[]{0, 0});
                }
            }
        }

        return nuevaNave;
    }
}