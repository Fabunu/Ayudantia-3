package ayudantia;
import java.util.Random;
import java.util.Scanner;

public class JuegoMatriz {
    
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String[][] mapa = inicializarMapa();

        int[] posicionPersonaje = {1, 1};
        int vidaPersonaje = 100;
        int ataquePersonaje = 15;
        int[][] enemigos = {{2, 2, 45, 10}}; // Enemigo en (2, 2)

        iniciarJuego(mapa, posicionPersonaje, vidaPersonaje, ataquePersonaje, enemigos, scanner);
    }

    public static String[][] inicializarMapa() {
        String[][] mapa = new String[10][10];
        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 10; j++) {
                mapa[i][j] = ".";
            }
        }

        for (int i = 0; i < 10; i++) {
            mapa[0][i] = "#";
            mapa[9][i] = "#";
            mapa[i][0] = "#";
            mapa[i][9] = "#";
        }
        
        colocarObstaculos(mapa);
        mapa[8][8] = "X"; // Destino
        mapa[2][2] = "E"; // Enemigo
        mapa[5][5] = "C"; // Cofre

        return mapa;
    }

    public static void colocarObstaculos(String[][] mapa) {
        mapa[1][3] = "#";
        mapa[1][1] = "#";
        mapa[5][4] = "#";
        mapa[2][1] = "#";
        mapa[8][1] = "#";
    }

    public static void iniciarJuego(String[][] mapa, int[] posicionPersonaje, int vidaPersonaje, int ataquePersonaje, int[][] enemigos, Scanner scanner) {
        boolean juegoActivo = true;

        while (juegoActivo) {
            imprimirMapa(mapa, posicionPersonaje);
            System.out.println("Mover: w(arriba), a(izquierda), d(derecha), s(abajo)");
            String comando = scanner.nextLine();

            if (comando.equalsIgnoreCase("salir")) {
                System.out.println("Juego terminado...");
                break;
            }

            int[] nuevaPosicion = moverPersonaje(mapa, posicionPersonaje, comando);
            if (nuevaPosicion != null) {
                posicionPersonaje = nuevaPosicion;
                vidaPersonaje = verificarEvento(mapa, posicionPersonaje, vidaPersonaje, ataquePersonaje, enemigos, scanner);
            }

            if (vidaPersonaje <= 0) {
                System.out.println("Has perdido toda tu vida. Fin del juego :(");
                break;
            }
        }
        scanner.close();
    }

    public static void imprimirMapa(String[][] mapa, int[] posicionPersonaje) {
        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 10; j++) {
                if (i == posicionPersonaje[0] && j == posicionPersonaje[1]) {
                    System.out.print("P ");
                } else {
                    System.out.print(mapa[i][j] + " ");
                }
            }
            System.out.println();
        }
    }

    public static int[] moverPersonaje(String[][] mapa, int[] posicionPersonaje, String direccion) {
        int nuevaX = posicionPersonaje[0];
        int nuevaY = posicionPersonaje[1];

        switch (direccion.toLowerCase()) {
            case "w": nuevaX--; break;
            case "s": nuevaX++; break;
            case "a": nuevaY--; break;
            case "d": nuevaY++; break;
            default:
                System.out.println("Comando no válido");
                return null;
        }

        if (esPosicionValida(mapa, nuevaX, nuevaY)) {
            mapa[posicionPersonaje[0]][posicionPersonaje[1]] = ".";
            return new int[] {nuevaX, nuevaY};
        } else {
            System.out.println("No puedes moverte a esa posición");
            return null;
        }
    }

    public static boolean esPosicionValida(String[][] mapa, int x, int y) {
        return !(x < 0 || x >= 10 || y < 0 || y >= 10 || mapa[x][y].equals("#"));
    }

    public static int verificarEvento(String[][] mapa, int[] posicionPersonaje, int vidaPersonaje, int ataquePersonaje, int[][] enemigos, Scanner scanner) {
        String evento = mapa[posicionPersonaje[0]][posicionPersonaje[1]];

        if (evento.equals("E")) {
            System.out.println("Oh no, enemigo encontrado... Inicia el combate");
            vidaPersonaje = iniciarCombate(vidaPersonaje, ataquePersonaje, enemigos, scanner);
            if (vidaPersonaje > 0) {
                mapa[posicionPersonaje[0]][posicionPersonaje[1]] = "."; // Eliminar enemigo
            }
        } else if (evento.equals("C")) {
            System.out.println("Has encontrado un cofre");
            vidaPersonaje = abrirCofre(vidaPersonaje);
            mapa[posicionPersonaje[0]][posicionPersonaje[1]] = "."; // Eliminar cofre
        } else if (evento.equals("X")) {
            System.out.println("Has llegado a tu destino, ganaste :]");
            System.exit(0);
        }
        return vidaPersonaje;
    }

    public static int iniciarCombate(int vidaPersonaje, int ataquePersonaje, int[][] enemigos, Scanner scanner) {
        int vidaEnemigo = enemigos[0][2];
        int ataqueEnemigo = enemigos[0][3];

        while (vidaPersonaje > 0 && vidaEnemigo > 0) {
            System.out.println("Atacar al enemigo");
            vidaEnemigo -= ataquePersonaje;
            if (vidaEnemigo <= 0) {
                System.out.println("Has derrotado al enemigo");
                return vidaPersonaje;
            }

            System.out.println("El enemigo ataca");
            vidaPersonaje -= ataqueEnemigo;
            if (vidaPersonaje <= 0) {
                System.out.println("Has perdido toda tu vida");
                return vidaPersonaje;
            }

            System.out.println("¿Quieres intentar huir? (si/no)");
            String decision = scanner.nextLine();
            if (decision.equalsIgnoreCase("si")) {
                System.out.println("Has huido del combate");
                return vidaPersonaje;
            }
        }
        return vidaPersonaje;
    }

    public static int abrirCofre(int vidaPersonaje) {
        Random random = new Random();
        if (random.nextBoolean()) {
            System.out.println("Recompensa. Has ganado 20 puntos de vida");
            vidaPersonaje += 20;
        } else {
            System.out.println("Trampa. Has perdido 20 puntos de vida");
            vidaPersonaje -= 20;
        }
        return vidaPersonaje;
    }
}
