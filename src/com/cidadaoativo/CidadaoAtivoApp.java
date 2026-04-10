package com.cidadaoativo;

import com.cidadaoativo.domain.*;
import com.cidadaoativo.service.ServicoSolicitacoes;
import com.cidadaoativo.service.ServicoUsuarios;
import com.cidadaoativo.repository.RepositorioSolicitacoes;
import com.cidadaoativo.repository.RepositorioUsuarios;
import java.util.*;

public class CidadaoAtivoApp {
    private final ServicoSolicitacoes servicoSolicitacoes;
    private final ServicoUsuarios servicoUsuarios;
    private final Scanner scanner;
    private Usuario usuarioLogado;

    public CidadaoAtivoApp() {
        RepositorioSolicitacoes repositorioSolicitacoes = new RepositorioSolicitacoes();
        RepositorioUsuarios repositorioUsuarios = new RepositorioUsuarios();

        this.servicoSolicitacoes = new ServicoSolicitacoes(repositorioSolicitacoes);
        this.servicoUsuarios = new ServicoUsuarios(repositorioUsuarios);
        this.scanner = new Scanner(System.in);

        inicializarDadosTeste();
    }

    private void inicializarDadosTeste() {
        servicoUsuarios.criarUsuario("João Silva", "joao@email.com", "11999999999",
                "Rua A, 100", "Centro", TipoUsuario.CIDADAO);
        servicoUsuarios.criarUsuario("Maria Santos", "maria@email.com", "11988888888",
                "Rua B, 200", "Vila Nova", TipoUsuario.CIDADAO);
        servicoUsuarios.criarUsuario("Pedro Atendente", "pedro@prefeitura.com", "11977777777",
                "Prefeitura", "Centro", TipoUsuario.ATENDENTE);
        servicoUsuarios.criarUsuario("Ana Gestora", "ana@prefeitura.com", "11966666666",
                "Prefeitura", "Centro", TipoUsuario.GESTOR);
    }

    public void iniciar() {
        System.out.println("\n╔════════════════════════════════════════╗");
        System.out.println("║      BEM-VINDO AO CIDADÃO ATIVO       ║");
        System.out.println("║   Sistema de Solicitações Públicas    ║");
        System.out.println("╚════════════════════════════════════════╝\n");

        boolean executando = true;
        while (executando) {
            if (usuarioLogado == null) {
                exibirMenuAutenticacao();
            } else {
                exibirMenuPrincipal();
            }
            executando = processarComando();
        }

        System.out.println("\n✓ Obrigado por usar CidadãoAtivo! Até logo!");
        scanner.close();
    }

    private void exibirMenuAutenticacao() {
        System.out.println("\n┌─ AUTENTICAÇÃO ─────────────────────┐");
        System.out.println("│ 1. Login                            │");
        System.out.println("│ 2. Registrar novo cidadão           │");
        System.out.println("│ 3. Sair                             │");
        System.out.println("└─────────────────────────────────────┘");
    }

    private void exibirMenuPrincipal() {
        System.out.println("\n┌─ MENU PRINCIPAL ────────────────────┐");
        System.out.println("│ Usuário: " + usuarioLogado.getNome());
        System.out.println("│ Tipo: " + usuarioLogado.getTipoUsuario().getDescricao());
        System.out.println("├─────────────────────────────────────┤");

        switch (usuarioLogado.getTipoUsuario()) {
            case CIDADAO:
                exibirMenuCidadao();
                break;
            case ATENDENTE:
                exibirMenuAtendente();
                break;
            case GESTOR:
                exibirMenuGestor();
                break;
        }

        System.out.println("│ 0. Logout                           │");
        System.out.println("└─────────────────────────────────────┘");
    }

    private void exibirMenuCidadao() {
        System.out.println("│ 1. Criar solicitação                │");
        System.out.println("│ 2. Acompanhar solicitação           │");
        System.out.println("│ 3. Listar minhas solicitações       │");
    }

    private void exibirMenuAtendente() {
        System.out.println("│ 1. Listar solicitações em triagem   │");
        System.out.println("│ 2. Listar solicitações abertas      │");
        System.out.println("│ 3. Atualizar status de solicitação  │");
        System.out.println("│ 4. Buscar solicitação por protocolo │");
        System.out.println("│ 5. Listar solicitações atrasadas    │");
    }

    private void exibirMenuGestor() {
        System.out.println("│ 1. Dashboard                        │");
        System.out.println("│ 2. Listar por categoria             │");
        System.out.println("│ 3. Listar por bairro                │");
        System.out.println("│ 4. Listar por prioridade            │");
        System.out.println("│ 5. Relatório de desempenho          │");
    }

    private boolean processarComando() {
        System.out.print("\n> Digite sua opção: ");
        String opcao = scanner.nextLine().trim();

        try {
            if (usuarioLogado == null) {
                return processarComandoAutenticacao(opcao);
            } else {
                switch (usuarioLogado.getTipoUsuario()) {
                    case CIDADAO:
                        return processarComandoCidadao(opcao);
                    case ATENDENTE:
                        return processarComandoAtendente(opcao);
                    case GESTOR:
                        return processarComandoGestor(opcao);
                    default:
                        System.out.println("✗ Tipo de usuário desconhecido");
                        return true;
                }
            }
        } catch (Exception e) {
            System.out.println("✗ Erro: " + e.getMessage());
            return true;
        }
    }

    private boolean processarComandoAutenticacao(String opcao) {
        switch (opcao) {
            case "1":
                realizarLogin();
                return true;
            case "2":
                registrarCidadao();
                return true;
            case "3":
                return false;
            default:
                System.out.println("✗ Opção inválida");
                return true;
        }
    }

    private boolean processarComandoCidadao(String opcao) {
        switch (opcao) {
            case "1":
                criarSolicitacao();
                return true;
            case "2":
                acompanharSolicitacao();
                return true;
            case "3":
                listarMinhasSolicitacoes();
                return true;
            case "0":
                usuarioLogado = null;
                System.out.println("✓ Logout realizado com sucesso");
                return true;
            default:
                System.out.println("✗ Opção inválida");
                return true;
        }
    }

    private boolean processarComandoAtendente(String opcao) {
        switch (opcao) {
            case "1":
                listarTriagem();
                return true;
            case "2":
                listarAbertas();
                return true;
            case "3":
                atualizarStatus();
                return true;
            case "4":
                buscarSolicitacao();
                return true;
            case "5":
                listarAtrasadas();
                return true;
            case "0":
                usuarioLogado = null;
                System.out.println("✓ Logout realizado com sucesso");
                return true;
            default:
                System.out.println("✗ Opção inválida");
                return true;
        }
    }

    private boolean processarComandoGestor(String opcao) {
        switch (opcao) {
            case "1":
                exibirDashboard();
                return true;
            case "2":
                listarPorCategoria();
                return true;
            case "3":
                listarPorBairro();
                return true;
            case "4":
                listarPorPrioridade();
                return true;
            case "5":
                exibirRelatorioDesempenho();
                return true;
            case "0":
                usuarioLogado = null;
                System.out.println("✓ Logout realizado com sucesso");
                return true;
            default:
                System.out.println("✗ Opção inválida");
                return true;
        }
    }



    private void realizarLogin() {
        System.out.print("\n- Email: ");
        String email = scanner.nextLine().trim();

        List<Usuario> usuarios = servicoUsuarios.listarTodos();
        for (Usuario u : usuarios) {
            if (u.getEmail().equalsIgnoreCase(email)) {
                usuarioLogado = u;
                System.out.println("✓ Login realizado com sucesso!");
                return;
            }
        }
        System.out.println("✗ Usuário não encontrado");
    }

    private void registrarCidadao() {
        System.out.print("\n- Nome completo: ");
        String nome = scanner.nextLine().trim();

        System.out.print("- Email: ");
        String email = scanner.nextLine().trim();

        System.out.print("- Telefone: ");
        String telefone = scanner.nextLine().trim();

        System.out.print("- Endereço: ");
        String endereco = scanner.nextLine().trim();

        System.out.print("- Bairro: ");
        String bairro = scanner.nextLine().trim();

        try {
            Usuario usuario = servicoUsuarios.criarUsuario(nome, email, telefone,
                    endereco, bairro, TipoUsuario.CIDADAO);
            usuarioLogado = usuario;
            System.out.println("✓ Cadastro realizado com sucesso!");
        } catch (Exception e) {
            System.out.println("✗ Erro ao cadastrar: " + e.getMessage());
        }
    }


    private void criarSolicitacao() {
        System.out.println("\n─── CRIAR SOLICITAÇÃO ───\n");

        System.out.println("Categorias disponíveis:");
        Categoria[] categorias = Categoria.values();
        for (int i = 0; i < categorias.length; i++) {
            System.out.println((i + 1) + ". " + categorias[i].getDescricao());
        }

        System.out.print("- Escolha a categoria (número): ");
        int escolhaCategoria = Integer.parseInt(scanner.nextLine().trim());
        if (escolhaCategoria < 1 || escolhaCategoria > categorias.length) {
            System.out.println("✗ Categoria inválida");
            return;
        }
        Categoria categoria = categorias[escolhaCategoria - 1];

        System.out.print("- Descrição detalhada (mínimo 10 caracteres): ");
        String descricao = scanner.nextLine().trim();

        System.out.print("- Localização (bairro/endereço): ");
        String localizacao = scanner.nextLine().trim();

        System.out.println("\nNíveis de Prioridade:");
        NivelPrioridade[] prioridades = NivelPrioridade.values();
        for (int i = 0; i < prioridades.length; i++) {
            System.out.println((i + 1) + ". " + prioridades[i].getDescricao() +
                    " (" + prioridades[i].getDiasSLA() + " dias SLA)");
        }

        System.out.print("- Escolha a prioridade (número): ");
        int escolhaPrioridade = Integer.parseInt(scanner.nextLine().trim());
        if (escolhaPrioridade < 1 || escolhaPrioridade > prioridades.length) {
            System.out.println("✗ Prioridade inválida");
            return;
        }
        NivelPrioridade prioridade = prioridades[escolhaPrioridade - 1];

        System.out.print("- Solicitar anonimamente? (s/n): ");
        boolean anonimo = scanner.nextLine().trim().equalsIgnoreCase("s");

        try {
            Solicitacao solicitacao = servicoSolicitacoes.criarSolicitacao(
                    usuarioLogado.getId(), categoria, descricao, localizacao, anonimo, prioridade
            );
            System.out.println("\n✓ Solicitação criada com sucesso!");
            System.out.println("✓ Protocolo: " + solicitacao.getProtocolo());
            System.out.println("✓ Guarde este protocolo para acompanhar sua solicitação");
        } catch (Exception e) {
            System.out.println("✗ Erro ao criar solicitação: " + e.getMessage());
        }
    }

    private void acompanharSolicitacao() {
        System.out.print("\n- Digite o protocolo: ");
        String protocolo = scanner.nextLine().trim();

        Solicitacao solicitacao = servicoSolicitacoes.buscarPorProtocolo(protocolo);
        if (solicitacao == null) {
            System.out.println("✗ Solicitação não encontrada");
            return;
        }

        if (!solicitacao.getIdCidadao().equals(usuarioLogado.getId()) && !solicitacao.isAnonimo()) {
            System.out.println("✗ Você não tem permissão para visualizar esta solicitação");
            return;
        }

        exibirDetalhesSolicitacao(solicitacao);
    }

    private void listarMinhasSolicitacoes() {
        List<Solicitacao> solicitacoes = servicoSolicitacoes.listarSolicitacoesCidadao(usuarioLogado.getId());

        if (solicitacoes.isEmpty()) {
            System.out.println("\n✓ Você não possui solicitações");
            return;
        }

        System.out.println("\n─── MINHAS SOLICITAÇÕES (" + solicitacoes.size() + ") ───\n");
        for (Solicitacao s : solicitacoes) {
            System.out.println(s);
            System.out.println();
        }
    }


    private void listarTriagem() {
        List<Solicitacao> solicitacoes = servicoSolicitacoes.listarFilaTriagem();
        exibirListaSolicitacoes("FILA DE TRIAGEM", solicitacoes);
    }

    private void listarAbertas() {
        List<Solicitacao> solicitacoes = servicoSolicitacoes.listarPorStatus(StatusSolicitacao.ABERTO);
        exibirListaSolicitacoes("SOLICITAÇÕES ABERTAS", solicitacoes);
    }

    private void atualizarStatus() {
        System.out.print("\n- Digite o protocolo: ");
        String protocolo = scanner.nextLine().trim();

        Solicitacao solicitacao = servicoSolicitacoes.buscarPorProtocolo(protocolo);
        if (solicitacao == null) {
            System.out.println("✗ Solicitação não encontrada");
            return;
        }

        System.out.println("\nStatus atual: " + solicitacao.getStatus().getDescricao());
        System.out.println("\nNovos status disponíveis:");
        StatusSolicitacao[] status = StatusSolicitacao.values();
        for (int i = 0; i < status.length; i++) {
            if (status[i] != solicitacao.getStatus()) {
                System.out.println((i + 1) + ". " + status[i].getDescricao());
            }
        }

        System.out.print("- Escolha o novo status (número): ");
        int escolha = Integer.parseInt(scanner.nextLine().trim());
        if (escolha < 1 || escolha > status.length) {
            System.out.println("✗ Status inválido");
            return;
        }

        StatusSolicitacao novoStatus = status[escolha - 1];
        System.out.print("- Comentário obrigatório: ");
        String comentario = scanner.nextLine().trim();

        try {
            servicoSolicitacoes.atualizarStatus(protocolo, novoStatus,
                    usuarioLogado.getId(), comentario);
            System.out.println("✓ Status atualizado com sucesso!");
        } catch (Exception e) {
            System.out.println("✗ Erro: " + e.getMessage());
        }
    }

    private void buscarSolicitacao() {
        System.out.print("\n- Digite o protocolo: ");
        String protocolo = scanner.nextLine().trim();

        Solicitacao solicitacao = servicoSolicitacoes.buscarPorProtocolo(protocolo);
        if (solicitacao == null) {
            System.out.println("✗ Solicitação não encontrada");
            return;
        }

        exibirDetalhesSolicitacao(solicitacao);
    }

    private void listarAtrasadas() {
        List<Solicitacao> solicitacoes = servicoSolicitacoes.listarAtrasadas();
        exibirListaSolicitacoes("SOLICITAÇÕES ATRASADAS", solicitacoes);
    }

        private void exibirDashboard() {
        System.out.println("\n╔════════════════════════════════════╗");
        System.out.println("║         DASHBOARD GERENCIAL        ║");
        System.out.println("╚════════════════════════════════════╝");

        int total = servicoSolicitacoes.listarTodas().size();
        int abertas = servicoSolicitacoes.contarSolicitacoesAbertas();
        int resolvidas = servicoSolicitacoes.contarSolicitacoesResolvidas();
        int atrasadas = servicoSolicitacoes.listarAtrasadas().size();
        int atendentes = servicoUsuarios.contarAtendentes();

        System.out.println("\n- Total de solicitações: " + total);
        System.out.println("- Solicitações abertas: " + abertas);
        System.out.println("- Solicitações resolvidas: " + resolvidas);
        System.out.println("- Solicitações atrasadas: " + atrasadas);
        System.out.println("- Atendentes cadastrados: " + atendentes);

        if (total > 0) {
            double taxaResolucao = (double) resolvidas / total * 100;
            System.out.println("\n- Taxa de resolução: " + String.format("%.2f%%", taxaResolucao));
        }
    }

    private void listarPorCategoria() {
        System.out.println("\nCategorias:");
        Categoria[] categorias = Categoria.values();
        for (int i = 0; i < categorias.length; i++) {
            System.out.println((i + 1) + ". " + categorias[i].getDescricao());
        }

        System.out.print("- Escolha: ");
        int escolha = Integer.parseInt(scanner.nextLine().trim());
        if (escolha < 1 || escolha > categorias.length) {
            System.out.println("✗ Categoria inválida");
            return;
        }

        List<Solicitacao> solicitacoes = servicoSolicitacoes.listarPorCategoria(categorias[escolha - 1]);
        exibirListaSolicitacoes("SOLICITAÇÕES - " + categorias[escolha - 1].getDescricao(), solicitacoes);
    }

    private void listarPorBairro() {
        System.out.print("\n- Digite o bairro: ");
        String bairro = scanner.nextLine().trim();

        List<Solicitacao> solicitacoes = servicoSolicitacoes.listarPorBairro(bairro);
        exibirListaSolicitacoes("SOLICITAÇÕES - " + bairro, solicitacoes);
    }

    private void listarPorPrioridade() {
        System.out.println("\nNíveis de prioridade:");
        NivelPrioridade[] prioridades = NivelPrioridade.values();
        for (int i = 0; i < prioridades.length; i++) {
            System.out.println((i + 1) + ". " + prioridades[i].getDescricao());
        }

        System.out.print("- Escolha: ");
        int escolha = Integer.parseInt(scanner.nextLine().trim());
        if (escolha < 1 || escolha > prioridades.length) {
            System.out.println("✗ Prioridade inválida");
            return;
        }

        List<Solicitacao> solicitacoes = servicoSolicitacoes.listarPorPrioridade(prioridades[escolha - 1]);
        exibirListaSolicitacoes("SOLICITAÇÕES - " + prioridades[escolha - 1].getDescricao(), solicitacoes);
    }

    private void exibirRelatorioDesempenho() {
        System.out.println("\n╔════════════════════════════════════╗");
        System.out.println("║    RELATÓRIO DE DESEMPENHO        ║");
        System.out.println("╚════════════════════════════════════╝");

        exibirDashboard();
    }



    private void exibirListaSolicitacoes(String titulo, List<Solicitacao> solicitacoes) {
        System.out.println("\n┌─ " + titulo + " (" + solicitacoes.size() + ") ─┐");

        if (solicitacoes.isEmpty()) {
            System.out.println("│ Nenhuma solicitação encontrada");
            System.out.println("└────────────────────────┘");
            return;
        }

        for (Solicitacao s : solicitacoes) {
            System.out.println("├─ " + s);
        }
        System.out.println("└────────────────────────┘");
    }

    private void exibirDetalhesSolicitacao(Solicitacao solicitacao) {
        System.out.println("\n╔════════════════════════════════════╗");
        System.out.println("║    DETALHES DA SOLICITAÇÃO        ║");
        System.out.println("╚════════════════════════════════════╝");
        System.out.println("\nProtocolo: " + solicitacao.getProtocolo());
        System.out.println("Categoria: " + solicitacao.getCategoria().getDescricao());
        System.out.println("Status: " + solicitacao.getStatus().getDescricao());
        System.out.println("Prioridade: " + solicitacao.getPrioridade().getDescricao());
        System.out.println("Localização: " + solicitacao.getLocalizacao());
        System.out.println("Descrição: " + solicitacao.getDescricao());
        System.out.println("Criada em: " + solicitacao.getDataCriacao());
        System.out.println("Prazo SLA: " + solicitacao.getDataPrazo());

        if (solicitacao.estaAtrasada()) {
            System.out.println("⚠ ATENÇÃO: Solicitação ATRASADA");
        }

        System.out.println("\n─── HISTÓRICO ───");
        List<HistoricoStatus> historico = solicitacao.getHistorico();
        if (historico.isEmpty()) {
            System.out.println("Nenhuma alteração registrada");
        } else {
            for (HistoricoStatus h : historico) {
                System.out.println(h);
            }
        }
    }

    public static void main(String[] args) {
        CidadaoAtivoApp app = new CidadaoAtivoApp();
        app.iniciar();
    }
}