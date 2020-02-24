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
    public long tamanhoDisco;
    public Date dataCriacao;
    public int tipo;
    public String pathOrigem;
    public Date dataModificacao;
    public int posicaoInicial;
    public Map nomesTipos;
    public boolean removido;

    public Arquivo(String nome, long tamanho, Date dataCriacao, int tipo, String pathOrigem, Date dtModificacao, int posicaoInicial, int tamBloco, boolean removido) {
        this.nome = nome;
        this.tamanho = tamanho;

        long qtBlocosUtilizados = (this.tamanho / tamBloco);
        qtBlocosUtilizados = this.tamanho % tamBloco == 0 ? qtBlocosUtilizados : qtBlocosUtilizados + 1;
        this.tamanhoDisco = qtBlocosUtilizados * tamBloco;
        
        this.dataCriacao = dataCriacao;
        this.tipo = tipo;
        this.pathOrigem = pathOrigem;
        this.dataModificacao = dtModificacao;
        this.posicaoInicial = posicaoInicial;
        this.removido = removido;
        
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
        System.out.printf("%s:\n\tTipo: %s\n\tOrigem: %s\n\tPosição inicial: %d\n\tTamanho em disco: %d bytes.\n\tTamanho: %d bytes\n\tCriado em %s\n\tAlterado em %s\n\tRemovido: %s\n",
                this.nome, this.nomesTipos.get(this.tipo), this.pathOrigem, this.posicaoInicial, this.tamanhoDisco, this.tamanho, formato.format(this.dataCriacao), formato.format(this.dataModificacao), statusRemovido);
    }
    
    public void PrintSimples(){
        System.out.printf("\t%s:\n", this.nome);
    }
    
    public boolean Remover(){
        this.removido = true;        
        return this.removido;
    }
    
    public boolean Restaurar(){
        this.removido = false;
        return this.removido;
    }
}
