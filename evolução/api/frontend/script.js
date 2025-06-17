// Aguarda o documento HTML ser completamente carregado para executar o código.
document.addEventListener('DOMContentLoaded', () => {
    // Seleciona os elementos do formulário e da caixa de mensagem.
    const form = document.getElementById('pix-form');
    const messageBox = document.getElementById('message-box');

    // Adiciona um "ouvinte" ao evento de submissão do formulário.
    form.addEventListener('submit', async (event) => {
        // Previne o comportamento padrão do formulário, que é recarregar a página.
        event.preventDefault();

        // Oculta a caixa de mensagem de resultados anteriores.
        messageBox.style.display = 'none';
        messageBox.textContent = '';
        messageBox.className = 'message';

        // Pega os valores dos campos de input.
        const idContaOrigem = document.getElementById('idContaOrigem').value;
        const chavePixDestino = document.getElementById('chavePixDestino').value;
        const valor = document.getElementById('valor').value;

        // Cria o corpo da requisição (payload) em formato JSON.
        const requestBody = {
            idContaOrigem: parseInt(idContaOrigem),
            chavePixDestino: chavePixDestino,
            valor: parseFloat(valor)
        };

        try {
            // Usa a função fetch() para fazer a chamada à nossa API.
            const response = await fetch('http://localhost:8080/transacoes', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify(requestBody)
            });

            // Verifica se a resposta da API foi bem-sucedida.
            if (response.ok) {
                // Se sim, mostra uma mensagem de sucesso.
                showMessage('Transferência realizada com sucesso!', 'success');
                form.reset(); // Limpa o formulário.
            } else {
                // Se não, pega a mensagem de erro do cabeçalho e a exibe.
                const errorMessage = response.headers.get('X-Error-Message') || 'Ocorreu um erro na transação.';
                showMessage(errorMessage, 'error');
            }
        } catch (error) {
            // Se houver um erro de rede (API desligada, etc.), exibe uma mensagem genérica.
            console.error('Erro de rede:', error);
            showMessage('Não foi possível conectar ao servidor. Verifique se a API está a correr.', 'error');
        }
    });

    // Função auxiliar para exibir mensagens ao utilizador.
    function showMessage(text, type) {
        messageBox.textContent = text;
        messageBox.className = `message ${type}`; // Aplica a classe de estilo 'success' ou 'error'.
        messageBox.style.display = 'block'; // Torna a caixa de mensagem visível.
    }
});