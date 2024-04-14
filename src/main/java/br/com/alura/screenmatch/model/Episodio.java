package br.com.alura.screenmatch.model;

import com.fasterxml.jackson.annotation.JsonAlias;
import model.DadosEpisodio;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.List;

public class Episodio {
    private Integer temporada;
    private String tituloEpisodio;
    private Integer numeroEpisodio;
    private Double avaliacaoEpisodio;
    private LocalDate dataLancamentoEpisodio;

    public Episodio(Integer numeroTemporada, DadosEpisodio dadosEpisodios) {
        this.temporada = numeroTemporada;
        this.tituloEpisodio = dadosEpisodios.titulo();
        this.numeroEpisodio = dadosEpisodios.numero();
        try {
            this.avaliacaoEpisodio = Double.valueOf(dadosEpisodios.avaliacao());
        } catch (NumberFormatException ex) {
            this.avaliacaoEpisodio = 0.0;
        }
        try {
            this.dataLancamentoEpisodio = LocalDate.parse(dadosEpisodios.dataLancamento());
        } catch (DateTimeParseException ex) {
            this.dataLancamentoEpisodio = null;
        }
    }


    public Integer getTemporada() {
        return temporada;
    }

    public void setTemporada(Integer temporada) {
        this.temporada = temporada;
    }

    public String getTituloEpisodio() {
        return tituloEpisodio;
    }

    public void setTituloEpisodio(String tituloEpisodio) {
        this.tituloEpisodio = tituloEpisodio;
    }

    public Integer getNumeroEpisodio() {
        return numeroEpisodio;
    }

    public void setNumeroEpisodio(Integer numeroEpisodio) {
        this.numeroEpisodio = numeroEpisodio;
    }

    public Double getAvaliacaoEpisodio() {
        return avaliacaoEpisodio;
    }

    public void setAvaliacaoEpisodio(Double avaliacaoEpisodio) {
        this.avaliacaoEpisodio = avaliacaoEpisodio;
    }

    public LocalDate getDataLancamentoEpisodio() {
        return dataLancamentoEpisodio;
    }

    public void setDataLancamentoEpisodio(LocalDate dataLancamentoEpisodio) {
        this.dataLancamentoEpisodio = dataLancamentoEpisodio;
    }

    @Override
    public String toString() {
        return "Temporada=" + temporada +
                ", tituloEpisodio='" + tituloEpisodio + '\'' +
                ", numeroEpisodio=" + numeroEpisodio +
                ", avaliacaoEpisodio=" + avaliacaoEpisodio +
                ", dataLancamentoEpisodio=" + dataLancamentoEpisodio;
    }
}
