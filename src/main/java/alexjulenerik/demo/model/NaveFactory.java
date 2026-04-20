package alexjulenerik.demo.model;

//JULEN
public class NaveFactory {

    public static Nave crearNave(String tipoNave, int filaCentral, int columnaCentral) {
        Nave nuevaNave = new Nave(filaCentral, columnaCentral);

        if (tipoNave.equals("GREEN")) {
            // Forma de T invertida (4 píxeles)
            nuevaNave.getForma().add(new int[]{0, 0});
            nuevaNave.getForma().add(new int[]{0, -1});
            nuevaNave.getForma().add(new int[]{0, 1});
            nuevaNave.getForma().add(new int[]{-1, 0});
        } else {
            if (tipoNave.equals("BLUE")) {
                // Forma de U (5 píxeles)
                nuevaNave.getForma().add(new int[]{0, 0});
                nuevaNave.getForma().add(new int[]{0, -1});
                nuevaNave.getForma().add(new int[]{0, 1});
                nuevaNave.getForma().add(new int[]{-1, -1});
                nuevaNave.getForma().add(new int[]{-1, 1});
            } else {
                if (tipoNave.equals("RED")) {
                    // Forma de bloque ancho (6 píxeles)
                    nuevaNave.getForma().add(new int[]{0, 0});
                    nuevaNave.getForma().add(new int[]{0, -1});
                    nuevaNave.getForma().add(new int[]{0, 1});
                    nuevaNave.getForma().add(new int[]{-1, 0});
                    nuevaNave.getForma().add(new int[]{-1, -1});
                    nuevaNave.getForma().add(new int[]{-1, 1});
                } else {
                    nuevaNave.getForma().add(new int[]{0, 0});
                }
            }
        }

        return nuevaNave;
    }
}