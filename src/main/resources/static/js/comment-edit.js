// comment-edit.js

// Função para obter os cabeçalhos CSRF a partir das meta tags no HTML
const getCsrfHeaders = () => {
    const tokenMeta = document.querySelector('meta[name="_csrf"]');
    const headerMeta = document.querySelector('meta[name="_csrf_header"]');
    return tokenMeta && headerMeta ? { [headerMeta.content]: tokenMeta.content } : {};
};

// Exibe o formulário de edição removendo a classe "hidden"
const enableEdit = (commentId) => {
    console.log("enableEdit chamado para commentId:", commentId);
    const container = document.getElementById(`edit-container-${commentId}`);
    if (container) {
        container.classList.remove("hidden");
        console.log("Formulário de edição exibido para commentId:", commentId);
    } else {
        console.error("Container de edição não encontrado para commentId:", commentId);
    }
};

// Oculta o formulário de edição adicionando a classe "hidden"
const disableEdit = (commentId) => {
    console.log("disableEdit chamado para commentId:", commentId);
    const container = document.getElementById(`edit-container-${commentId}`);
    if (container) {
        container.classList.add("hidden");
        console.log("Formulário de edição ocultado para commentId:", commentId);
    } else {
        console.error("Container de edição não encontrado para commentId:", commentId);
    }
};

// Envia a atualização do comentário via AJAX usando fetch
const saveComment = (commentId) => {
    const textarea = document.getElementById(`edit-textarea-${commentId}`);
    const newText = textarea.value.trim();
    if (newText === "") {
        if (!confirm("Comentário vazio será excluído. Confirmar?")) {
            return;
        }
    }
    const csrfHeaders = getCsrfHeaders();
    fetch(`/comment/update/${commentId}`, {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json',
            ...csrfHeaders
        },
        body: JSON.stringify({ commentText: newText })
    })
        .then(response => response.json())
        .then(data => {
            if (data.success) {
                const commentTextElement = document.getElementById(`comment-text-${commentId}`);
                if (commentTextElement) {
                    commentTextElement.innerText = data.commentText;
                }
                disableEdit(commentId);
            } else {
                alert("Erro ao salvar comentário: " + data.error);
            }
        })
        .catch(error => {
            console.error("Erro ao salvar comentário:", error);
            alert("Erro ao salvar comentário.");
        });
};

// Envia a requisição de exclusão via AJAX usando fetch
const deleteComment = (commentId) => {
    if (!confirm("Deseja realmente excluir este comentário?")) {
        return;
    }
    const csrfHeaders = getCsrfHeaders();
    fetch(`/comment/delete/${commentId}`, {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json',
            ...csrfHeaders
        }
    })
        .then(response => response.json())
        .then(data => {
            if (data.success) {
                const container = document.getElementById(`comment-container-${commentId}`);
                if (container) {
                    container.remove();
                }
            } else {
                alert("Erro ao excluir comentário: " + data.error);
            }
        })
        .catch(error => {
            console.error("Erro ao excluir comentário:", error);
            alert("Erro ao excluir comentário.");
        });
};

// Intercepta o envio do formulário de novo comentário via AJAX
document.addEventListener("DOMContentLoaded", () => {
    const newCommentForm = document.getElementById("new-comment-form");
    if (newCommentForm) {
        newCommentForm.addEventListener("submit", (event) => {
            event.preventDefault(); // Previne o envio padrão
            const photoId = newCommentForm.querySelector("input[name='photoId']").value;
            const commentText = newCommentForm.querySelector("textarea[name='commentText']").value.trim();
            if (commentText === "") {
                alert("Comentário não pode estar vazio.");
                return;
            }
            const csrfHeaders = getCsrfHeaders();
            fetch('/comment/new', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                    ...csrfHeaders
                },
                body: JSON.stringify({ photoId: photoId, commentText: commentText })
            })
                .then(response => response.json())
                .then(data => {
                    if (data.success) {
                        // Aqui você pode atualizar o DOM dinamicamente ou recarregar a página
                        location.reload();
                    } else {
                        alert("Erro ao enviar comentário: " + data.error);
                    }
                })
                .catch(error => {
                    console.error("Erro ao enviar comentário:", error);
                    alert("Erro ao enviar comentário.");
                });
        });
    }
});
