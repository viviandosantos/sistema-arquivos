/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sistemaarquivos;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author CEITELABINFO
 */
public class Arquivo {

    public String nome;
    public long tamanho;
    private long tamanhoBloco;
    public Date dataCriacao;
    public int tipo;
    public String pathOrigem;
    public Date dataModificacao;
    public int posicaoInicial;
    public Map nomesTipos;
    public boolean removido;

    public Arquivo(String nome, long tamanho, Date dataCriacao, int tipo, String pathOrigem, Date dtModificacao, int posicaoInicial, int tamBloco) {
        this.nome = nome;
        this.tamanho = tamanho;
        long tam = (this.tamanho / this.tamanhoBloco);
        this.tamanhoBloco = this.tamanho % this.tamanhoBloco == 0 ? tam : tam + 1;
        this.dataCriacao = dataCriacao;
        this.tipo = tipo;
        this.pathOrigem = pathOrigem;
        this.dataModificacao = dtModificacao;
        this.posicaoInicial = posicaoInicial;
        this.removido = false;
        
        this.nomesTipos = new HashMap<>();
        this.nomesTipos.put(1, "Texto");
        this.nomesTipos.put(2, "Word");
        this.nomesTipos.put(3, "Excel");
        this.nomesTipos.put(4, "Executável");
        this.nomesTipos.put(0, "Arquivo");
    }

    public Arquivo() {
        this.nomesTipos = new HashMap<>();
        this.removido = false;
        this.nomesTipos.put(1, "Texto");
        this.nomesTipos.put(2, "Word");
        this.nomesTipos.put(3, "Excel");
        this.nomesTipos.put(4, "Executável");   
    }

    public void Print() {
        String statusRemovido = this.removido ? "Sim" : "Não";
        SimpleDateFormat formato = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        System.out.printf("%s:\n\tTipo: %s\n\tOrigem: %s\n\tPosição inicial: %d\n\tTamanho: %d bytes\n\tCriado em %s\n\tAlterado em %s\n\tRemovido: %s\n",
                this.nome, this.nomesTipos.get(this.tipo), this.pathOrigem, this.posicaoInicial, this.tamanho, formato.format(this.dataCriacao), formato.format(this.dataModificacao), statusRemovido);
    }
}
