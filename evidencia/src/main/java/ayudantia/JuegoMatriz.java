package  ayudantia;
import java.util.Random;
import java.util.Scanner;

public class JuegoMatriz {
    
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in); //Lee la entrada del usuario
        //Inicializar el mapa y los atributos del personaje
        String[][] mapa = inicializarMapa();
        int[] posicionPersonaje = {0, 0};  // Coordenadas iniciales
        int vidaPersonaje = 100;
        int ataquePersonaje = 15;
        int[][] enemigos = {{2, 2, 45, 10}};  // Coordenadas y atributos del enemigo {x, y, vida, ataque}

        iniciarJuego(mapa, posicionPersonaje, vidaPersonaje, ataquePersonaje, enemigos, scanner);
    }

    public static String[][] inicializarMapa() {
        String[][] mapa = new String[10][10];
        //Rellenar el mapa con puntos
        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 10; j++) {
                mapa[i][j] = ".";
            }
        }
        mapa[0][0] = "#";
        mapa[0][9] = "#";
        mapa[9][0] = "#";
        mapa[9][9] = "#";

        mapa[1][0] = "#"; // Colocar obstáculos adicionales
        mapa[0][1] = "#";
        mapa[1][1] = "#";
        mapa[8][9] = "#";
        mapa[9][8] = "#";
        mapa[8][8] = "#";

        mapa[0][0] = "P"; //Posicion inicial del personaje
        mapa[9][9] = "X"; //Destino
        mapa[3][3] = "#"; //Obstaculo
        mapa[2][2] = "E"; //Enemigo
        mapa[5][5] = "C"; //Cofre
        return mapa;

    }

    public static void iniciarJuego(String[][] mapa, int[] posicionPersonaje, int vidaPersonaje, int ataquePersonaje, int[][] enemigos, Scanner scanner) {
        boolean juegoActivo = true;
        boolean enemigoDerrotado = false;
        //buble principal del juego
        while(juegoActivo) {
            imprimirMapa(mapa, posicionPersonaje);
            System.out.println("Mover: w(arriba), a(izquierda), d(derecha), s(abajo)");
            String comando = scanner.nextLine();

            if (comando.equalsIgnoreCase("Salir")) {
                System.out.println("Juego terminado...");
                break;
            }

            int[] nuevaPosicion = moverPersonaje(mapa, posicionPersonaje, comando);
            if (nuevaPosicion != null) {
                posicionPersonaje = nuevaPosicion;
                vidaPersonaje = verificarEvento(mapa, posicionPersonaje, vidaPersonaje, ataquePersonaje, enemigos, scanner)
            }
            
            if (vidaPersonaje <= 0) {
                System.out.println("Has perdido toda tu vida. Fin del juego :()");
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
                System.out.println("Comando no valido");
                return null;
        }
        //Ve si la nueva posicion es valida
        if (esPosicionValida(mapa, nuevaX, nuevaY)) {
            mapa[posicionPersonaje[0]][posicionPersonaje[1]] = ".";
            posicionPersonaje[0] = nuevaX;
            posicionPersonaje[1] = nuevaY;
            mapa[nuevaX][nuevaY] = "P";
            return posicionPersonaje;
        } else {
            System.out.println("No puedes moverte a esa posicion");
            return  null;
        }
    }

    public static boolean esPosicionValida(String[][] mapa, int x, int y) {
        if (x < 0 || x >= 10 || y < 0 || y >= 10 || mapa[x][y].equals("#")) {
            return false;
        }
        return true;
    }

    public static int verificarEvento(String[][] mapa, int[] posicionPersonaje, int vidaPersonaje, int ataquePersonaje, int[][] enemigos, boolean enemigoDerrotado, Scanner scanner) {
        String evento = mapa[posicionPersonaje[0]][posicionPersonaje[1]];

        if (evento.equals("E")) {
            System.out.println("Oh, no, enemigo encontrado... Inicia el combate");
            vidaPersonaje = iniciarCombate(vidaPersonaje, ataquePersonaje, enemigos, scanner);
            mapa[posicionPersonaje[0]][posicionPersonaje[1]] = ".";
        } else if (evento.equals("C")) {
            System.out.println("Has encontrado un cofre");
            vidaPersonaje = abrirCofre(vidaPersonaje);
            mapa[posicionPersonaje[0]][posicionPersonaje[1]] = ".";
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
            if(vidaPersonaje <= 0) {
                System.out.println("Has perdido toda tu vida");
                return vidaPersonaje;
            }

            System.out.println("Quieres intentar huir? (si/no)");
            String decision = scanner.nextLine();
            if (decision.equals("si")) {
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