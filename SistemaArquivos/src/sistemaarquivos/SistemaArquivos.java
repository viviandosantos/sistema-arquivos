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
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.function.Predicate;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 *
 * @author CEITELABINFO
 */
public class SistemaArquivos {

    private final String raiz;
    private final String lixeira;
    private final File arquivo;
    private final File arquivoLixeira;
    private int ultimaPosicao;
    private final int tamanhoBloco;
    public Cabecalho header;
    public List<Arquivo> arquivos;
    public Map tiposArquivos;

    public SistemaArquivos(String root, File arq, int tamBloco, String lixeira, File dirLixeira) {
        this.raiz = root;
        this.header = new Cabecalho(root);
        this.ultimaPosicao = 0;
        this.arquivo = arq;
        this.lixeira = lixeira;
        this.arquivoLixeira = dirLixeira;
        this.arquivos = new ArrayList<>();
        this.tamanhoBloco = tamBloco;

        this.tiposArquivos = new HashMap<>();
        this.tiposArquivos.put("txt", 1);
        this.tiposArquivos.put("doc", 2);
        this.tiposArquivos.put("docx", 2);
        this.tiposArquivos.put("csv", 3);
        this.tiposArquivos.put("xls", 3);
        this.tiposArquivos.put("xlsx", 3);
        this.tiposArquivos.put("exe", 4);
    }

    public boolean InserirArquivo() {
        try {
            Scanner st = new Scanner(System.in);
            System.out.print("Insira o nome do novo arquivo: ");
            String nomeArquivo = st.nextLine();

            File arquivoCriado = new File(this.raiz + "/" + nomeArquivo);

            while (!nomeArquivo.contains(".")) {
                System.out.print("Insira a extensão do arquivo! Insira o nome do novo arquivo: ");
                nomeArquivo = st.nextLine();
            }

            while (arquivoCriado.exists()) {
                System.out.print("Já existe um arquivo com esse nome! Insira outro nome para o novo arquivo: ");
                nomeArquivo = st.nextLine();
                arquivoCriado = new File(this.raiz + "/" + nomeArquivo);
            }

            int tipoArquivo = this.ObterTipoExtensao(nomeArquivo);

            System.out.print("Insira um texto para seu arquivo: ");
            String data = st.next();

            try (FileOutputStream fos = new FileOutputStream(this.raiz + "/" + nomeArquivo)) {
                fos.write(data.getBytes());
                fos.flush();
            }

            Date agora = new Date();
            Arquivo novoArquivo = new Arquivo(nomeArquivo, arquivoCriado.length(), agora, tipoArquivo, this.raiz, agora, this.ultimaPosicao, this.tamanhoBloco, false);

            this.ultimaPosicao += novoArquivo.tamanho;
            this.header.AdicionarArquivo(novoArquivo);
            this.arquivos.add(novoArquivo);

            //atualizar cabeçalho
            this.header.Atualizar();

            System.out.println("Arquivo salvo com sucesso!");
            novoArquivo.Print();
            return true;

        } catch (Exception ex) {

            System.out.println("Erro ao criar arquivo. Detalhes: " + ex.getMessage());
            return false;
        }
    }

    public void ObterArquivos() {
        //obter ultima posição disponível do header + quantidade de arquivos 
        int quantidadeArquivos = 0;
        SimpleDateFormat formato = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        try {
            try (FileReader arq = new FileReader(this.header.path)) {
                BufferedReader lerArq = new BufferedReader(arq);
                String linha = lerArq.readLine(); // lê a primeira linha

                while (linha != null) {
                    quantidadeArquivos++;
                    String[] infos = linha.split("[" + Pattern.quote("#|") + "]");

                    //linha = NomeArquivo|TipoArquivo|Removido|DataCriacaoArquivo|PosicaoInicialArquivo|TamanhoDisco|TamanhoArquivo;
                    File file = new File(this.raiz + "/" + infos[0]);
                    try {
                        boolean foiRemovido = infos[2].equals("|S|");
                        Arquivo arqLinha = new Arquivo(infos[0], file.length(), formato.parse(infos[3]), Integer.parseInt(infos[1]),
                                file.getCanonicalPath(), this.header.ObterDataAlteracao(file), Integer.parseInt(infos[4]), this.tamanhoBloco, foiRemovido);
                        //arqLinha.Print();
                        this.arquivos.add(arqLinha);
                    } catch (ParseException ex) {
                        Logger.getLogger(SistemaArquivos.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    //ultimo bloco utilizado 
                    int tamanhoArquivo = Integer.parseInt(infos[infos.length - 1]);
                    int blocosUtilizados = tamanhoArquivo / this.tamanhoBloco;
                    blocosUtilizados = tamanhoArquivo % this.tamanhoBloco == 0 ? blocosUtilizados : blocosUtilizados + 1;

                    this.ultimaPosicao = Integer.parseInt(infos[infos.length - 3]) + blocosUtilizados;
                    linha = lerArq.readLine(); // lê da segunda até a última linha
                }
                this.header.Atualizar(quantidadeArquivos);
            }
        } catch (IOException e) {
            System.err.printf("Erro na abertura do arquivo: %s.\n", e.getMessage());
        }
    }

    public void ListarArquivos() {
        if (this.arquivos.isEmpty()) {
            System.out.println("Não há arquivos nesse diretório!");
        } else {
            for (Arquivo arq : this.arquivos) {
                arq.Print();
            }
        }
    }

    public void ListarArquivosSimplificados() {
        if (this.arquivos.isEmpty()) {
            System.out.println("Não há arquivos nesse diretório!");
        } else {
            for (Arquivo arq : this.arquivos) {
                arq.PrintSimples();
            }
        }
    }

    public void ListarArquivosRemovidos() {
        int count = 0;
        System.out.println("Arquivos na lixeira: ");
        for (Arquivo arq : this.arquivos) {
            if (arq.removido) {
                arq.PrintSimples();
                count++;
            }
        }
        if (count == 0) {
            System.out.println("Lixeira vazia!");
        }
    }

    public void AbrirArquivo(String nomeArquivo) {
        File f = new File(this.raiz + "/" + nomeArquivo);
        if (f.exists()) {
            FileReader arq;
            try {
                arq = new FileReader(this.raiz + "/" + nomeArquivo);
                BufferedReader lerArq = new BufferedReader(arq);
                String linha = lerArq.readLine();
                while (linha != null) {
                    System.out.print(linha);
                    linha = lerArq.readLine();
                }
                System.out.println("\n");
                arq.close();
            } catch (FileNotFoundException ex) {
                Logger.getLogger(SistemaArquivos.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IOException ex) {
                Logger.getLogger(SistemaArquivos.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else {
            System.out.println("Arquivo inexistente!");
        }
    }

    public void Print() {
        System.out.println("File System " + this.raiz);
        this.header.Print();
        this.ListarArquivos();
    }

    public void AtualizarArquivo() {
        System.out.println("Arquivos: ");
        this.ListarArquivosSimplificados();

        String nomeArquivo;
        String novoConteudo;
        Scanner st = new Scanner(System.in);

        System.out.print("Insira o nome do arquivo que deseja atualizar: ");
        nomeArquivo = st.next();

        File arquivoExistente = new File(this.raiz + "/" + nomeArquivo);

        while (!arquivoExistente.exists()) {
            System.out.print("Esse arquivo não existe! Insira o nome de um arquivo válido: ");
            nomeArquivo = st.nextLine();
            arquivoExistente = new File(this.raiz + "/" + nomeArquivo);
        }

        if (arquivoExistente.exists()) {
            System.out.println("Conteúdo atual do arquivo: ");
            this.AbrirArquivo(nomeArquivo);

            System.out.println("Digite o novo conteúdo:");
            novoConteudo = st.next();

            FileWriter fw;
            try {
                fw = new FileWriter(this.raiz + "/" + nomeArquivo, false);
                try (BufferedWriter conexao = new BufferedWriter(fw)) {
                    conexao.write(novoConteudo);
                    conexao.newLine();
                    conexao.close();
                }

                this.header.Atualizar();

                System.out.println("Arquivo salvo com sucesso!");
            } catch (IOException ex) {
                System.out.println("Erro ao salvar arquivo. Detalhes: " + ex.getMessage());
            }
        }
    }

    private int ObterTipoExtensao(String nomeArquivo) {

        String[] auxExtensao = nomeArquivo.split("[" + Pattern.quote("#.") + "]");
        String extensao = auxExtensao[auxExtensao.length - 1];
        int tipoArquivo;

        if (this.tiposArquivos.keySet().contains(extensao)) {
            tipoArquivo = Integer.parseInt(this.tiposArquivos.get(extensao).toString());
        } else {
            tipoArquivo = 0;
        }

        return tipoArquivo;
    }

    public boolean RemoverArquivo() {
        System.out.println("Arquivos: ");
        this.ListarArquivosSimplificados();

        String nomeArquivo;
        Scanner st = new Scanner(System.in);
        System.out.print("Insira o nome do arquivo a ser excluído: ");
        nomeArquivo = st.nextLine();

        File arquivoExistente = new File(this.raiz + "/" + nomeArquivo);
        boolean sucesso = false;

        if (arquivoExistente.exists()) {
            Predicate<Arquivo> porNome = arq -> arq.nome.equals(nomeArquivo);
            List<Arquivo> arquivosExcluir = this.arquivos.stream().filter(porNome).collect(Collectors.toList());

            if (arquivosExcluir.isEmpty()) {
                System.out.println("Arquivo não encontrado pelo sistema de arquivos!");
            } else {
                Arquivo arq = arquivosExcluir.get(0);
                System.out.println("Removendo arquivo " + arq.nome);
                try {
                    Files.move(Paths.get(arquivoExistente.getAbsolutePath()), Paths.get(this.arquivoLixeira.getCanonicalPath() + "\\" + arq.nome));
                } catch (IOException ex) {
                    System.out.println("Falha ao apagar arquivo! Detalhes: " + ex.getMessage());
                }

                if (!arquivoExistente.exists()) {
                    System.out.println("Arquivo removido com sucesso!");
                    arq.Remover();
                    this.header.Atualizar();
                } else {
                    System.out.println("Falha ao apagar arquivo!");
                }
                sucesso = true;
            }
        } else {
            System.out.println("Arquivo não encontrado pelo sistema de arquivos!");
            sucesso = false;
        }
        return sucesso;
    }

    public void RestaurarArquivo() {
        this.ListarArquivosRemovidos();
        String nomeArquivo;
        Scanner st = new Scanner(System.in);
        System.out.print("Insira o nome do arquivo a ser restaurado: ");
        nomeArquivo = st.nextLine();

        try {
            File arquivoExistente = new File(this.arquivoLixeira.getCanonicalPath() + "\\" + nomeArquivo);
            if (!arquivoExistente.exists()) {
                System.out.println("Arquivo não localizado na lixeira!");
            } else {
                File arquivoRestaurado = new File(this.arquivo.getAbsolutePath() + "\\" + nomeArquivo);
                //verificar se npa ha arquivo com o mesmo nome
                if (!arquivoRestaurado.exists()) {
                    Files.move(Paths.get(this.arquivoLixeira.getCanonicalPath() + "\\" + nomeArquivo), Paths.get(this.arquivo.getAbsolutePath() + "\\" + nomeArquivo));
                    this.header.Atualizar();
                    System.out.println("Arquivo restaurado com sucesso!");
                } else {
                    System.out.printf("Já existe um arquivo com o nome %s no diretório %s.\n", nomeArquivo, this.arquivo.getAbsolutePath());
                    //Files.move(Paths.get(this.arquivoLixeira.getCanonicalPath() + "\\" + nomeArquivo), Paths.get(this.arquivo.getAbsolutePath() + "\\" + nomeArquivo + "(1)"));
                }
            }
        } catch (IOException ex) {
            Logger.getLogger(SistemaArquivos.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
