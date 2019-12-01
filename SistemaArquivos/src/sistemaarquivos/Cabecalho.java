/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sistemaarquivos;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Pattern;

/**
 *
 * @author CEITELABINFO
 */
public class Cabecalho {

    private final String raiz;
    private final String nomeArquivo;
    public final String path;
    private int quantidadeArquivos;
    private File arquivo;
    //public List<Arquivo> arquivos;

    public Cabecalho(String root) {
        String local = "";
        try {
            local = new File(".").getCanonicalPath();
        } catch (IOException ex) {
            Logger.getLogger(Cabecalho.class.getName()).log(Level.SEVERE, null, ex);
        }

        this.raiz = local + "/" + root;
        this.nomeArquivo = root + ".header.txt";
        //this.arquivos = new ArrayList<>();
        this.path = this.raiz + "/" + this.nomeArquivo;
        this.arquivo = new File(this.path);

        if (!this.arquivo.exists()) {
            FileOutputStream fos;
            try {
                fos = new FileOutputStream(this.raiz + "/" + nomeArquivo);
                try {
                    fos.flush();
                    fos.close();
                } catch (IOException ex) {
                    Logger.getLogger(Cabecalho.class.getName()).log(Level.SEVERE, null, ex);
                }
                //this.arquivo.setReadOnly();
            } catch (FileNotFoundException ex) {
                Logger.getLogger(Cabecalho.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    public boolean AdicionarArquivo(Arquivo arquivo) {
        try {
            SimpleDateFormat formato = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
            //texto = NomeArquivo|TipoArquivo|Removido|DataCriacaoArquivo|PosicaoInicialArquivo|TamanhoDisco|TamanhoArquivo;
            String texto = arquivo.nome + '|' + arquivo.tipo + "|N|" + formato.format(arquivo.dataCriacao) + '|' + arquivo.posicaoInicial + '|' + arquivo.tamanhoDisco + '|' + arquivo.tamanho;

            FileWriter fw = new FileWriter(this.path, true);
            try (BufferedWriter conexao = new BufferedWriter(fw)) {
                conexao.write(texto);
                conexao.newLine();
                conexao.close();
            }
        } catch (Exception ex) {
            System.out.println("Falha ao salvar arquivo no cabeçalho. Detalhes: " + ex.getMessage());
        }

        return true;
    }

    public boolean RemoverArquivo(Arquivo arquivo) {

        //PARA QUE A REMOÇÃO EM DISCO FUNCIONE ISSO PRECISA SER IMPLEMENTADO
        return true;
    }

    public void Print() {
        //@1|VERSAO|DATACRIACAO|DATAMODIFICACAO|QTDEARUIVOS|TAMANHOCABECALHO	
        System.out.println("Quantidade de arquivos: " + this.quantidadeArquivos + ". Tamanho:" + this.arquivo.length() + " bytes. Data modificação: " + this.ObterDataAlteracaoFormatada(this.arquivo));
    }

    public void Atualizar(int quantidadeArq) {
        this.quantidadeArquivos = quantidadeArq;
    }

    public void Atualizar() {
        this.quantidadeArquivos = 0;
        //rever os tamanhos dos arquivos
        try {
            try (FileReader arq = new FileReader(this.path)) {
                BufferedReader lerArq = new BufferedReader(arq);
                String linha = lerArq.readLine(); // lê a primeira linha
                String rewrite = "";
                while (linha != null) {
                    //linha = NomeArquivo|TipoArquivo|Removido|DataCriacaoArquivo|PosicaoInicialArquivo|TamanhoDisco|TamanhoArquivo;
                    String[] infos = linha.split("[" + Pattern.quote("#|") + "]");

                    File file = new File(this.raiz + "/" + infos[0]);
                    if (!file.exists()) {
                        linha = linha.replace("|N|", "|S|");
                        rewrite = rewrite.concat(linha + "\n");
                    } else {
                        this.quantidadeArquivos++;
                        rewrite = rewrite.concat(linha + "\n");
                    }
                    linha = lerArq.readLine(); // lê da segunda até a última linha
                }

                FileWriter fw;
                fw = new FileWriter(this.path, false);
                BufferedWriter conexao = new BufferedWriter(fw);
                conexao.write(rewrite);
                conexao.close();
                
                lerArq.close();
            }
        } catch (IOException e) {
            System.err.printf("Erro na abertura do arquivo: %s.\n", e.getMessage());
        }
    }

    public Date ObterDataAlteracao(File arquivo) {
        Date retorno = new Date();

        SimpleDateFormat formato = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        String strDate = formato.format(arquivo.lastModified());
        try {
            retorno = formato.parse(strDate);
        } catch (ParseException ex) {
            Logger.getLogger(SistemaArquivos.class.getName()).log(Level.SEVERE, null, ex);
        }

        return retorno;
    }

    public String ObterDataAlteracaoFormatada(File arquivo) {
        SimpleDateFormat formato = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        String strDate = formato.format(arquivo.lastModified());

        return strDate;
    }
}
