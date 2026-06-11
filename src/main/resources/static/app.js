const API_URL = '/api';
let usuarioLogado = null;

// FUNÇÕES DE AUTENTICAÇÃO
function showTab(tabName) {
    document.querySelectorAll('.tab-content').forEach(el => el.classList.remove('active'));
    document.querySelectorAll('.tab-btn').forEach(el => el.classList.remove('active'));
    
    document.getElementById(tabName + '-form').classList.add('active');
    event.target.classList.add('active');
}

async function fazerLogin() {
    const email = document.getElementById('login-email').value;
    const senha = document.getElementById('login-senha').value;
    
    try {
        const response = await fetch(`${API_URL}/usuarios/login`, {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({ email, senha })
        });
        
        const data = await response.json();
        if (response.ok) {
            usuarioLogado = data.usuario;
            localStorage.setItem('usuario', JSON.stringify(data.usuario));
            localStorage.setItem('token', data.token);
            mostrarMainContent();
            carregarPerfil();
            loadSolicitacoes();
        } else {
            alert(data.erro || 'Erro ao fazer login');
        }
    } catch (error) {
        console.error('Erro:', error);
        alert('Erro ao conectar com o servidor');
    }
}

async function fazerRegistro() {
    const nome = document.getElementById('reg-nome').value;
    const email = document.getElementById('reg-email').value;
    const senha = document.getElementById('reg-senha').value;
    const cpf = document.getElementById('reg-cpf').value;
    const tipo = document.getElementById('reg-tipo').value;
    
    try {
        const response = await fetch(`${API_URL}/usuarios/registro`, {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({ nome, email, senha, cpf, tipo })
        });
        
        const data = await response.json();
        if (response.ok) {
            alert('Usuário registrado com sucesso! Faça login.');
            document.getElementById('reg-nome').value = '';
            document.getElementById('reg-email').value = '';
            document.getElementById('reg-senha').value = '';
            document.getElementById('reg-cpf').value = '';
            showTab('login');
        } else {
            alert(data.erro || 'Erro ao registrar');
        }
    } catch (error) {
        console.error('Erro:', error);
        alert('Erro ao conectar com o servidor');
    }
}

function logout() {
    usuarioLogado = null;
    localStorage.removeItem('usuario');
    localStorage.removeItem('token');
    document.getElementById('auth-section').classList.remove('hidden');
    document.getElementById('main-content').classList.add('hidden');
}

// FUNÇÕES DE NAVEGAÇÃO
function mostrarMainContent() {
    document.getElementById('auth-section').classList.add('hidden');
    document.getElementById('main-content').classList.remove('hidden');
    showSection('home');
}

function showSection(sectionId) {
    document.querySelectorAll('.section').forEach(el => el.classList.remove('active'));
    document.getElementById(sectionId).classList.add('active');
}

function carregarPerfil() {
    if (usuarioLogado) {
        document.getElementById('perfil-nome').textContent = usuarioLogado.nome;
        document.getElementById('perfil-email').textContent = usuarioLogado.email;
        document.getElementById('perfil-cpf').textContent = usuarioLogado.cpf;
        document.getElementById('perfil-tipo').textContent = usuarioLogado.tipo;
    }
}

// FUNÇÕES DE SOLICITAÇÃO
function toggleAnonimo() {
    const anonimo = document.getElementById('sol-anonimo').checked;
    document.getElementById('anonimo-fields').classList.toggle('hidden', !anonimo);
}

async function criarSolicitacao() {
    const categoria = document.getElementById('sol-categoria').value;
    const descricao = document.getElementById('sol-descricao').value;
    const localizacao = document.getElementById('sol-localizacao').value;
    const bairro = document.getElementById('sol-bairro').value;
    const prioridade = document.getElementById('sol-prioridade').value;
    const anonimo = document.getElementById('sol-anonimo').checked;
    const nomeAnonimo = document.getElementById('sol-nome-anonimo').value;
    const telefoneAnonimo = document.getElementById('sol-telefone-anonimo').value;
    
    try {
        const response = await fetch(`${API_URL}/solicitacoes`, {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({
                categoria,
                descricao,
                localizacao,
                bairro,
                prioridade,
                anonimo,
                nomeAnonimo: anonimo ? nomeAnonimo : null,
                telefoneAnonimo: anonimo ? telefoneAnonimo : null,
                cidadaoId: !anonimo ? usuarioLogado.id : null
            })
        });
        
        const data = await response.json();
        if (response.ok) {
            alert('Solicitação criada com sucesso! Protocolo: ' + data.protocolo);
            document.getElementById('form-solicitacao').reset();
            loadSolicitacoes();
        } else {
            alert(data.erro || 'Erro ao criar solicitação');
        }
    } catch (error) {
        console.error('Erro:', error);
        alert('Erro ao conectar com o servidor');
    }
}

async function loadSolicitacoes() {
    if (!usuarioLogado) return;
    
    try {
        const response = await fetch(`${API_URL}/solicitacoes/bairro/geral`);
        const solicitacoes = await response.json();
        
        const lista = document.getElementById('lista-solicitacoes');
        lista.innerHTML = '';
        
        solicitacoes.forEach(sol => {
            const card = criarCartaoSolicitacao(sol);
            lista.appendChild(card);
        });
    } catch (error) {
        console.error('Erro:', error);
    }
}

function criarCartaoSolicitacao(sol) {
    const card = document.createElement('div');
    card.className = 'solicitacao-card';
    card.onclick = () => mostrarDetalhes(sol);
    
    const statusClass = 'status-' + sol.status.toLowerCase().replace('_', '');
    
    card.innerHTML = `
        <div class="solicitacao-header">
            <div>
                <div class="solicitacao-protocolo">${sol.protocolo}</div>
                <div class="solicitacao-descricao">${sol.categoria}</div>
            </div>
            <span class="status-badge ${statusClass}">${sol.status}</span>
        </div>
        <p>${sol.descricao.substring(0, 100)}...</p>
        <div class="solicitacao-info">
            <span>📍 ${sol.localizacao}</span>
            <span>⚡ ${sol.prioridade}</span>
            <span>📅 ${new Date(sol.dataCriacao).toLocaleDateString('pt-BR')}</span>
        </div>
    `;
    
    return card;
}

function mostrarDetalhes(sol) {
    const modal = document.getElementById('modal-detalhes');
    const body = document.getElementById('modal-body');
    
    body.innerHTML = `
        <p><strong>Protocolo:</strong> ${sol.protocolo}</p>
        <p><strong>Categoria:</strong> ${sol.categoria}</p>
        <p><strong>Descrição:</strong> ${sol.descricao}</p>
        <p><strong>Localização:</strong> ${sol.localizacao}</p>
        <p><strong>Bairro:</strong> ${sol.bairro}</p>
        <p><strong>Prioridade:</strong> ${sol.prioridade}</p>
        <p><strong>Status:</strong> ${sol.status}</p>
        <p><strong>Data de Criação:</strong> ${new Date(sol.dataCriacao).toLocaleDateString('pt-BR')}</p>
        <p><strong>Prazo SLA:</strong> ${new Date(sol.prazoSLA).toLocaleDateString('pt-BR')}</p>
    `;
    
    modal.classList.remove('hidden');
}

function fecharModal() {
    document.getElementById('modal-detalhes').classList.add('hidden');
}

function filtrarSolicitacoes() {
    // Implementar filtro de solicitações no dashboard
}

// Verificar se usuário está logado ao carregar
window.addEventListener('load', () => {
    const usuarioSalvo = localStorage.getItem('usuario');
    if (usuarioSalvo) {
        usuarioLogado = JSON.parse(usuarioSalvo);
        mostrarMainContent();
        carregarPerfil();
        loadSolicitacoes();
    }
});