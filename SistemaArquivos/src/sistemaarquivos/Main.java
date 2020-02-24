/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sistemaarquivos;

import java.io.File;
import java.util.Scanner;

/**
 * @MausElementos <3 
 * @author Leonardo Perales, Pablo Alcantara e Vivian Santos
 * @Curso: Ciência da Computação - Sistemas Operacionais II 
 * @Prof Márcio Piva
 */
public class Main {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        try {

            // TODO code application logic here
            String root = "Fs.me";
            String lixeira = "Fs.me.RecycleBin";
            int tamanhoBloco = 30;
            File dir = new File(root);
            File dirLixeira = new File(lixeira);
            Scanner s = new Scanner(System.in);
            String str;

            System.out.println("Tamanho da unidade de bloco: " + tamanhoBloco + " bytes.\n");

            //System.out.print("Informe o tamanho em bytes do bloco: ");
            //tamanhoBloco = s.nextInt();
            if (!dir.exists()) {
                System.out.println("Criando diretório físico...");
                //criar pasta física principal para salvar os arquivos
                if (dir.mkdir()) {
                    System.out.println("Diretório criado com sucesso!");
                } else {
                    System.out.println("ERRO: Falha ao criar diretório!");
                }
            }
            System.out.println("\n");
            if (!dirLixeira.exists()) {
                dirLixeira.mkdir();
            }

            SistemaArquivos sistemaArq = new SistemaArquivos(root, dir, tamanhoBloco, lixeira, dirLixeira);
            //ler arquivos criados numa execução anterior
            sistemaArq.ObterArquivos();

            System.out.print(""
                    + "<1> Exibir info sistema de arquivos\n"
                    + "<2> Inserir novos arquivos\n"
                    + "<3> Listar arquivos\n"
                    + "<4> Exibir conteudo de arquivo de texto\n"
                    + "<5> Remover Arquivo\n"
                    + "<6> Restaurar arquivo\n"
                    //+ "<7> Atualizar arquivo\n"
                    + "<0> Sair do programa\n"
                    + "Digite a opção desejada: ");
            int n = s.nextInt();
            System.out.println("\n");

            while (n != 0) {
                switch (n) {
                    case 1:
                        sistemaArq.Print();
                        break;

                    case 2:
                        sistemaArq.InserirArquivo();
                        break;

                    case 3:
                        sistemaArq.ListarArquivos();
                        break;

                    case 4:
                        System.out.println("Arquivos: ");
                        sistemaArq.ListarArquivosSimplificados();
                        System.out.print("Insira o nome do arquivo que deseja ler: ");
                        str = s.next();
                        sistemaArq.AbrirArquivo(str);
                        break;

                    case 5:
                        sistemaArq.RemoverArquivo();
                        break;

                    case 6:
                        sistemaArq.RestaurarArquivo();
                        break;

//                case 7:
//                    sistemaArq.AtualizarArquivo();
//                    break;
                    case 0:
                        break;

                    default:
                        System.out.println("Opção inválida!");
                        break;
                }
                System.out.print("\n"
                        + "<1> Exibir info sistema de arquivos\n"
                        + "<2> Inserir novos arquivos\n"
                        + "<3> Listar arquivos\n"
                        + "<4> Exibir conteudo de arquivo de texto\n"
                        + "<5> Remover Arquivo\n"
                        + "<6> Restaurar arquivo\n"
                        //+ "<7> Atualizar arquivo\n"
                        + "<0> Sair do programa\n"
                        + "Digite a opção desejada: ");
                if (s.hasNext()) {
                    n = s.nextInt();
                }
                System.out.println("\n");
            }
            s.close();
        } catch (Exception e) {
            System.out.println("Erro durante a execução.");
        }
    }
}
