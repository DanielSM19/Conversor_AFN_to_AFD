import java.util.*;

public class ConversorAFNtoAFD {

    public static void main(String[] args) {
        // Definir as informações do AFND de entrada
        Scanner scanner = new Scanner(System.in);
        System.out.println("*****************Conversor AFN para AFD******************");
        System.out.println("- Autor: Daniel Monteiro \n");

        System.out.println("Informe os estados do autômato:");
        String estadosInput = scanner.nextLine();
        String[] estados = estadosInput.split(",");
        if (estados.length > 4) {
            System.out.println("Número de estados excede 4.");
            return;
        }

        System.out.println("Informe o estado inicial:");
        int estadoInicial = Integer.parseInt(scanner.nextLine());

        System.out.println("Informe a função programa:");
        String funcoesInput = scanner.nextLine();
        String[] funcoes = funcoesInput.split(", ");
        if (funcoes.length > 8) {
            System.out.println("O número de transições excede 8");
            return;
        }

        System.out.println("Informe os estados finais:");
        String estadosFinaisInput = scanner.nextLine();
        String[] estadosFinais = estadosFinaisInput.split(",");

        int numEstados = estados.length;
        int numTransicoes = funcoes.length;
        int numSimbolos = 0;

        for (String funcao : funcoes) {
            String[] partes = funcao.split(" ");
         //   int simbolo = Integer.parseInt(partes[1]);
            int simbolo = partes[1].charAt(0) - 'a';

            if (simbolo > numSimbolos) {
                numSimbolos = simbolo;
            }
        }
        numSimbolos++;

        // Definir as transições do AFND
        int[][][] transicoesAFND = new int[numEstados][numSimbolos][];

        for (int i = 0; i < numEstados; i++) {
            for (int j = 0; j < numSimbolos; j++) {
                transicoesAFND[i][j] = new int[0];
            }
        }

        for (String funcao : funcoes) {
            String[] partes = funcao.split(" ");
            int estadoOrigem = Integer.parseInt(partes[0]);
          //  int simbolo = Integer.parseInt(partes[1]);
            int simbolo = partes[1].charAt(0) - 'a';
            int estadoDestino = Integer.parseInt(partes[2]);

            int[] transicoes = transicoesAFND[estadoOrigem][simbolo];
            transicoes = Arrays.copyOf(transicoes, transicoes.length + 1);
            transicoes[transicoes.length - 1] = estadoDestino;
            transicoesAFND[estadoOrigem][simbolo] = transicoes;
        }

        // Criar conjunto vazio para estados do AFD
        Set<Set<Integer>> estadosAFD = new HashSet<>();

        // Criar fila para processamento dos estados do AFD
        Queue<Set<Integer>> fila = new LinkedList<>();

        // Criar estado inicial do AFD a partir do estado inicial do AFND
        Set<Integer> estadoInicialAFD = new HashSet<>();
        estadoInicialAFD.add(estadoInicial);
        estadosAFD.add(estadoInicialAFD);
        fila.add(estadoInicialAFD);

        // Criar mapa para mapear transições do AFD
        Map<Set<Integer>, Map<Integer, Set<Integer>>> transicoesAFD = new HashMap<>();

        // Processar os estados do AFD
        while (!fila.isEmpty()) {
            Set<Integer> estadoAtualAFD = fila.poll();

            // Criar mapa para as transições a partir do estado atual do AFD
            Map<Integer, Set<Integer>> transicoesEstadoAFD = new HashMap<>();

            // Verificar para cada símbolo do alfabeto
            for (int simbolo = 0; simbolo < numSimbolos; simbolo++) {
                // Criar conjunto para armazenar estados alcançáveis a partir do estado atual do AFD
                Set<Integer> estadosAlcancaveisAFD = new HashSet<>();

                // Percorrer os estados do AFND contidos no estado atual do AFD
                for (int estadoAFND : estadoAtualAFD) {
                    // Obter os estados alcançáveis a partir do estadoAFND e simbolo
                    int[] estadosAlcancaveis = transicoesAFND[estadoAFND][simbolo];

                    // Adicionar os estados alcançáveis no conjunto estadosAlcancaveisAFD
                    for (int estadoAlcancavel : estadosAlcancaveis) {
                        estadosAlcancaveisAFD.add(estadoAlcancavel);
                    }
                }

                // Verificar se o conjunto estadosAlcancaveisAFD já foi processado
                if (!estadosAFD.contains(estadosAlcancaveisAFD)) {
                    estadosAFD.add(estadosAlcancaveisAFD);
                    fila.add(estadosAlcancaveisAFD);
                }

                // Adicionar a transição no mapa de transições do estado atual do AFD
                transicoesEstadoAFD.put(simbolo, estadosAlcancaveisAFD);
            }

            // Adicionar as transições do estado atual do AFD no mapa de transições do AFD
            transicoesAFD.put(estadoAtualAFD, transicoesEstadoAFD);
        }

        // Imprimir o resultado do AFD convertido
        System.out.println("Estados do AFD:");
        for (Set<Integer> estado : estadosAFD) {
            System.out.println(estado);
        }

        System.out.println("Transições do AFD:");
        for (Map.Entry<Set<Integer>, Map<Integer, Set<Integer>>> entrada : transicoesAFD.entrySet()) {
            Set<Integer> estadoOrigem = entrada.getKey();
            Map<Integer, Set<Integer>> transicoes = entrada.getValue();

            for (Map.Entry<Integer, Set<Integer>> transicao : transicoes.entrySet()) {
                int simbolo = transicao.getKey();
                Set<Integer> estadoDestino = transicao.getValue();

                System.out.println(estadoOrigem + " -- " + (char) ('a' + simbolo) + " --> " + estadoDestino);
            }
        }
    }
}
