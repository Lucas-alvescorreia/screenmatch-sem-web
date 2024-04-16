package br.com.alura.screenmatch.principal;

import br.com.alura.screenmatch.model.Episodio;
import br.com.alura.screenmatch.service.ConsumoApi;
import br.com.alura.screenmatch.service.ConverteDados;
import model.DadosEpisodio;
import model.DadosSerie;
import model.DadosTemporada;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

public class Principal {
    private Scanner scan = new Scanner(System.in);
    private ConsumoApi consumoApi = new ConsumoApi();
    private ConverteDados conversor = new ConverteDados();
    private final String ENDERECO = "http://www.omdbapi.com/?t=";
    private final String API_KEY = "&apikey=e67ebfc3";

    public void exibeMenu() {
        System.out.println("Digite o nome da série para busca: ");
        var nomeSerie = scan.nextLine();
        var json = consumoApi.obterDados(ENDERECO + nomeSerie.replace(" ", "+") + API_KEY);

        DadosSerie dadosSerie = conversor.obterDados(json, DadosSerie.class);
        System.out.println(dadosSerie);

        List<DadosTemporada> temporadas = new ArrayList<>();

        for (int i = 1; i <= dadosSerie.totalTemporadas(); i++) {
            json = consumoApi.obterDados(ENDERECO + nomeSerie.replace(" ", "+") + "&season=" + i + API_KEY);
            DadosTemporada dadosTemporada = conversor.obterDados(json, DadosTemporada.class);
            temporadas.add(dadosTemporada);
        }
        temporadas.forEach(System.out::println);

        temporadas.forEach(t -> t.episodios().forEach(e -> System.out.println(e.titulo())));
        temporadas.forEach(t -> System.out.println());


        List<DadosEpisodio> dadosEpisodios = temporadas.stream()
                .flatMap(t -> t.episodios().stream())
                .collect(Collectors.toList());

        System.out.println("----------------TOP 5 EPISÓDIOS----------------");
        dadosEpisodios.stream()
                .filter(e -> !e.avaliacao().equalsIgnoreCase("N/A"))
                .sorted(Comparator.comparing(DadosEpisodio::avaliacao).reversed())
                .limit(5)
                .forEach(System.out::println);

        System.out.println("----------------Lista de EP com episodeos----------------");
        List<Episodio> episodios = temporadas.stream()
                .flatMap(t -> t.episodios().stream()
                        .map(d -> new Episodio(t.numero(), d)))
                .collect(Collectors.toList());

        episodios.forEach(System.out::println);

        System.out.println("-----------------Busca por nome do Episódio-----------------");
        System.out.println("Digite um trecho do titulo do episódio:");

        var trechoTitulo = scan.nextLine();
        Optional<Episodio> episodioBuscado = episodios.stream()
                .filter(e -> e.getTituloEpisodio().toUpperCase().contains(trechoTitulo.toUpperCase()))
                .findFirst();
        if (episodioBuscado.isPresent()) {
            System.out.println("Episódio encontrado");
            System.out.println("Temporada: " + episodioBuscado.get().getTemporada());
        } else {
            System.out.println("Episódio não encontado");
        }

        System.out.println("A partir de que ano você deseja ver os episódio?");
        var ano = scan.nextInt();
        scan.nextLine();

        LocalDate dataBusca = LocalDate.of(ano, 1, 1);

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        episodios.stream()
                .filter(e -> e.getDataLancamentoEpisodio() != null && e.getDataLancamentoEpisodio().isAfter(dataBusca))
                .forEach(e -> System.out.println(
                        "Temporada: " + e.getTemporada() +
                                "Episódio: " + e.getTituloEpisodio() +
                                "Data lançamento: " + e.getDataLancamentoEpisodio().format(formatter)
                ));

        System.out.println("----------------TOP 10 EPISÓDIOS----------------");
        dadosEpisodios.stream()
                .filter(e -> !e.avaliacao().equalsIgnoreCase("N/A"))
                .peek(e -> System.out.println("--Primeiro filtro(N/A) " + e))
                .sorted(Comparator.comparing(DadosEpisodio::avaliacao).reversed())
                .peek(e -> System.out.println("--Ordenação " + e))
                .limit(10)
                .peek(e -> System.out.println("--Limite " + e))
                .map(e -> e.titulo().toUpperCase())
                .peek(e -> System.out.println("--Mapeamento " + e))
                .forEach(System.out::println);

        System.out.println("--------------------Média de avaliações por temporada--------------------");
        Map<Integer, Double> avaliacoesPorTempoarada = episodios.stream()
                .filter(e -> e.getAvaliacaoEpisodio() > 0.0)
                .collect(Collectors.groupingBy(Episodio::getTemporada,
                        Collectors.averagingDouble(Episodio::getAvaliacaoEpisodio)));

        System.out.println(avaliacoesPorTempoarada);

        System.out.println("--------------------Média de avaliações pela série--------------------");
        DoubleSummaryStatistics est =episodios.stream()
                .filter(e -> e.getAvaliacaoEpisodio() > 0.0)
                .collect(Collectors.summarizingDouble(Episodio::getAvaliacaoEpisodio));
        System.out.println("Média: " + est.getAverage());
        System.out.println("Melhor Episódio: " + est.getMax());
        System.out.println("Pior Episódio: " + est.getMin());
        System.out.println("Quantidade de Episódios: " + est.getCount());
    }
}
