import java.util.Scanner;
import java.text.NumberFormat;
import java.util.Locale;

public class Main {

    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);
        NumberFormat formatoBR = NumberFormat.getCurrencyInstance(new Locale("pt", "BR"));

        boolean continuar = true;

        String nome = cliente(scanner);
        String historico = "";


        double totalGeral = 0;
        double totalTaxas = 0;
        double totalBandeiras = 0;

        int totalPropriedades = 0;

        while (continuar) {

            String local = localDoCliente(scanner);
            double consumoKwh = lerConsumo(scanner);

            double valorBase = calcularValor(consumoKwh);

            // 🔹 Taxa da propriedade
            double taxa = taxaPorPropriedade(local);
            double valorTaxa = valorBase * taxa;

            // 🔹 Bandeira
            String corDaBandeira = bandeiraTarifa(scanner);

            double taxaDaBandeira = valorDaTaxaPelaBandeira(corDaBandeira);

            double valorBandeira = valorComTaxaDaBandeira(valorBase, taxaDaBandeira);

            // 🔹 Valor final
            double valorFinal = valorBase + valorTaxa + valorBandeira;
 
            // 🔹 Acumuladores
            totalGeral += valorFinal;
            totalTaxas += valorTaxa;
            totalBandeiras += valorBandeira;
            historico += local + " - " + valorBase + ", ";


            totalPropriedades++;

            // 🔹 Conta individual
            System.out.println(
                    conta(
                            nome,
                            local,
                            consumoKwh,
                            valorBase,
                            valorTaxa,
                            corDaBandeira,
                            valorBandeira,
                            valorFinal,
                            formatoBR,
                            historico
                    )
            );

            // 🔹 Continuar?
            while (true) {

                System.out.println("\nDeseja inserir mais uma propriedade?");
                System.out.println("1 - Sim | 2 - Não");

                if (!scanner.hasNextInt()) {
                    System.out.println("Valor inválido!");
                    scanner.next();
                    continue;
                }

                int escolha = scanner.nextInt();

                if (escolha == 1) {
                    break;
                }

                if (escolha == 2) {
                    continuar = false;
                    break;
                }

                System.out.println("Opção inválida!");
            }
        }

        // 🔹 Resumo final
        System.out.println("\n=== RESUMO FINAL ===");
        System.out.println("Cliente: " + nome);
        System.out.println("Total de propriedades: " + totalPropriedades);
        System.out.println("Total em taxas: "
                + formatoBR.format(totalTaxas));
        System.out.println("Total bandeiras: "
                + formatoBR.format(totalBandeiras));
        System.out.println("Valor total: "
                + formatoBR.format(totalGeral));
        System.out.println("Historico: "
                + historico);

        scanner.close();
    }

    // 🔹 Cliente
    public static String cliente(Scanner scanner) {

        while (true) {

            System.out.print("Qual seu nome: ");

            String nome = scanner.nextLine();

            if (nome.matches("[a-zA-Z ]+")) {
                return nome;
            }

            System.out.println("Nome inválido!");
        }
    }

    // 🔹 Tipo da propriedade
    public static String localDoCliente(Scanner scanner) {

        while (true) {

            System.out.println("\nTipo de propriedade:");
            System.out.println("1 - Residencial");
            System.out.println("2 - Comercial");
            System.out.println("3 - Industrial");

            if (!scanner.hasNextInt()) {
                System.out.println("Valor inválido!");
                scanner.next();
                continue;
            }

            int tipo = scanner.nextInt();

            switch (tipo) {

                case 1:
                    return "Residencial";

                case 2:
                    return "Comercial";

                case 3:
                    return "Industrial";

                default:
                    System.out.println("Opção inválida!");
            }
        }
    }

    // 🔹 Consumo
    public static double lerConsumo(Scanner scanner) {

        while (true) {

            System.out.print("Informe o consumo (kWh): ");

            if (!scanner.hasNextDouble()) {
                System.out.println("Valor inválido!");
                scanner.next();
                continue;
            }

            double consumo = scanner.nextDouble();

            if (consumo <= 0) {
                System.out.println("Consumo inválido!");
                continue;
            }

            return consumo;
        }
    }

    // 🔹 Valor base
    public static double calcularValor(double consumo) {

        if (consumo <= 100) {
            return consumo * 0.50;

        } else if (consumo <= 200) {
            return consumo * 0.75;

        } else {
            return consumo * 1.00;
        }
    }

    // 🔹 Taxa por tipo
    public static double taxaPorPropriedade(String local) {

        if (local.equals("Residencial")) return 0.05;

        if (local.equals("Comercial")) return 0.10;

        if (local.equals("Industrial")) return 0.15;

        return 0;
    }

    // 🔹 Bandeira
    public static String bandeiraTarifa(Scanner scanner) {

        while (true) {

            System.out.println("\nQual a bandeira?");
            System.out.println("1 - Verde");
            System.out.println("2 - Amarela");
            System.out.println("3 - Vermelha");

            if (!scanner.hasNextInt()) {
                System.out.println("Valor inválido!");
                scanner.next();
                continue;
            }

            int opcao = scanner.nextInt();

            switch (opcao) {

                case 1:
                    return "Verde";

                case 2:
                    return "Amarela";

                case 3:
                    return "Vermelha";

                default:
                    System.out.println("Opção inválida!");
            }
        }
    }

    // 🔹 Taxa da bandeira
    public static double valorDaTaxaPelaBandeira(String bandeira) {

        if (bandeira.equals("Verde")) return 0.00;

        if (bandeira.equals("Amarela")) return 0.05;

        if (bandeira.equals("Vermelha")) return 0.10;

        return 0;
    }

    // 🔹 Valor da bandeira
    public static double valorComTaxaDaBandeira(double valorBase, double taxaDaBandeira) {
        return valorBase * taxaDaBandeira;
    }

    // 🔹 Conta
    public static String conta(
            String nome,
            String local,
            double consumo,
            double valorBase,
            double valorTaxa,
            String bandeira,
            double valorBandeira,
            double valorFinal,
            NumberFormat formato,
            String historico
    ) {

        return "\n--- CONTA ---\n" +
                "Cliente: " + nome + "\n" +
                "Propriedade: " + local + "\n" +
                "Consumo: " + consumo + " kWh\n" +
                "Valor base: " + formato.format(valorBase) + "\n" +
                "Taxa propriedade: "
                + formato.format(valorTaxa) + "\n" +
                "Bandeira: " + bandeira + "\n" +
                "Valor bandeira: "
                + formato.format(valorBandeira) + "\n" +
                "Valor final: "
                + formato.format(valorFinal) + "\n" +
                "Historico: "
                + historico;
    }
}