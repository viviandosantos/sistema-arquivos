/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sistemaarquivos;

import java.io.File;
import java.util.Scanner;

/**
 *
 * @author CEITELABINFO
 */
public class Main {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        String root = "Fs.me";
        int tamanhoBloco = 2;
        File dir = new File(root);
        Scanner s = new Scanner(System.in);
        String str;
        /*
        CRIAR:
        Diretório de lixeira.
        Quando o arquivo for eliminado do sistema de arquivos deverá ser enviado/criado na lixeira. 
        Quando o usuário quiser retomar o arquivo, ele deverá ser excluido da lixeira e trazido de volta a "vida"
        */        
        System.out.print("Informe o tamanho em bytes do bloco: ");
        tamanhoBloco = s.nextInt();
        
        if (!dir.exists()) {
            System.out.println("Criando diretório físico...");
            //criar pasta física principal para salvar os arquivos
            if (dir.mkdir()) {
                System.out.println("Diretório criado com sucesso!");
            } else {
                System.out.println("ERRO: Falha ao criar diretório!");
            }
        }
        
        SistemaArquivos sistemaArq = new SistemaArquivos(root, dir, tamanhoBloco);
        //ler arquivos criados numa execução anterior
        sistemaArq.ObterArquivos();

        System.out.println(""
                + "  <1> Criar Diretório\n" //estou com medo de criar esse role
                + "**<2> Inserir novos arquivos\n"
                + "  <3> Remover Arquivo\n" //li a parte de restaurar e estou em choque
                + "**<4> Exibir conteudo de arquivo txt\n"
                + "**<5> Listar arquivos\n"
                + "* <6> Atualizar arquivo\n" //seria uma boa ter interface pra trazer o que já tem no arquivo e a pessoa editar. No console vai sobreescrever.
                + "**<7> Listar info sistema de arquivos\n"
                + "<0> Sair do programa");
        int n = s.nextInt();

        // while (n != 0) {
        switch (n) {
            case 1:
                //dir.criarDiretorio();
                break;

            case 2:
                sistemaArq.InserirArquivo();
                break;

            case 3:
                sistemaArq.RemoverArquivo();
                break;

            case 4:
                System.out.print("Insira o nome do arquivo que deseja ler: ");
                str = s.next();
                sistemaArq.AbrirArquivo(str);
                break;
             
            case 5:
                sistemaArq.ListarArquivos();
                break;
                
             case 6:
                sistemaArq.AtualizarArquivo();
                break;
                 
             case 7:
                sistemaArq.Print();
                break;

            case 0:
                break;

            default:
                break;
        }
    }
}