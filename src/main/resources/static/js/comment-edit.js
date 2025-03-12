// comment-edit.js

// Função para obter os cabeçalhos CSRF a partir das meta tags no HTML
const getCsrfHeaders = () => {
    const tokenMeta = document.querySelector('meta[name="_csrf"]');
    const headerMeta = document.querySelector('meta[name="_csrf_header"]');
    return tokenMeta && headerMeta ? { [headerMeta.content]: tokenMeta.content } : {};
};

// Função para enviar a requisição de like via AJAX com JSON
// Atualiza curtidas via AJAX
const likePhoto = (photoId) => {
    const csrfHeaders = getCsrfHeaders();
    fetch('/photo/like', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json',
            ...csrfHeaders
        },
        body: JSON.stringify({ photoId })
    })
        .then(response => response.json())
        .then(data => {
            if (data.success) {
                document.getElementById('likes-count').innerText = data.likesCount;
            } else {
                alert("Erro ao curtir: " + data.error);
            }
        })
        .catch(error => {
            console.error("Erro ao curtir foto:", error);
            alert("Erro ao curtir foto.");
        });
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
        if (!confirm("Comentário vazio será excluído. Confirmar?")) return;
    }
    const csrfHeaders = getCsrfHeaders();
    fetch(`/comment/update/${commentId}`, {
        method: 'POST',
        headers: { 'Content-Type': 'application/json', ...csrfHeaders },
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
    if (!confirm("Deseja realmente excluir este comentário?")) return;
    const csrfHeaders = getCsrfHeaders();
    fetch(`/comment/delete/${commentId}`, {
        method: 'POST',
        headers: { 'Content-Type': 'application/json', ...csrfHeaders }
    })
        .then(response => response.json())
        .then(data => {
            if (data.success) {
                const container = document.getElementById(`comment-container-${commentId}`);
                if (container) container.remove();
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
            event.preventDefault();
            const photoId = newCommentForm.querySelector("input[name='photoId']").value;
            const commentText = newCommentForm.querySelector("textarea[name='commentText']").value.trim();
            if (commentText === "") {
                alert("Comentário não pode estar vazio.");
                return;
            }
            const csrfHeaders = getCsrfHeaders();
            fetch('/comment/new', {
                method: 'POST',
                headers: { 'Content-Type': 'application/json', ...csrfHeaders },
                body: JSON.stringify({ photoId, commentText })
            })
                .then(response => response.json())
                .then(data => {
                    if (data.success) {
                        // Atualize a área de comentários conforme sua necessidade, por exemplo:
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

// Exibir campo para adicionar hashtag
const enableAddHashtag = () => {
    document.getElementById('add-hashtag-container').classList.remove('hidden');
    document.getElementById('add-hashtag-button').classList.add('hidden');
};

// Ocultar campo para adicionar hashtag
const disableAddHashtag = () => {
    document.getElementById('add-hashtag-container').classList.add('hidden');
    document.getElementById('add-hashtag-button').classList.remove('hidden');
};

// Função para adicionar hashtag via AJAX
const addHashtag = (photoId) => {
    const hashtagName = document.getElementById('new-hashtag-input').value.trim();
    if (hashtagName === "") {
        alert("Digite o nome da hashtag.");
        return;
    }
    const csrfHeaders = getCsrfHeaders();
    fetch('/tag/add', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json',
            ...csrfHeaders
        },
        body: JSON.stringify({ photoId, hashtagName })
    })
        .then(response => response.json())
        .then(data => {
            if (data.success) {
                location.reload(); // Atualiza para exibir a nova hashtag
            } else {
                alert("Erro ao adicionar hashtag: " + data.error);
            }
        })
        .catch(error => {
            console.error("Erro ao adicionar hashtag:", error);
            alert("Erro ao adicionar hashtag.");
        });
};

// Alterna visibilidade da caixa de busca das hashtags
const toggleDropdown = () => {
    const dropdownContainer = document.getElementById('dropdown-container');
    dropdownContainer.classList.toggle('hidden');
};

// Exibe/Oculta o input para adicionar hashtags
const toggleHashtagInput = () => {
    document.getElementById('hashtag-input-container').classList.toggle('hidden');
    document.getElementById('new-hashtag').value = '';
    document.getElementById('tag-suggestions').innerHTML = '';
};

// Busca hashtags existentes enquanto digita
const searchHashTags = (partialName) => {
    if (partialName.trim().length === 0) {
        document.getElementById('tag-suggestions').innerHTML = '';
        return;
    }

    fetch(`/tag/searchTags?name=${encodeURIComponent(partialName)}`)
        .then(response => response.json())
        .then(tags => {
            const suggestions = document.getElementById('tag-suggestions');
            suggestions.innerHTML = '';
            tags.forEach(tag => {
                const div = document.createElement('div');
                div.textContent = tag;
                div.className = 'p-2 cursor-pointer hover:bg-gray-200';
                div.onclick = () => {
                    document.getElementById('new-hashtag').value = tag;
                    suggestions.innerHTML = '';
                };
                suggestions.appendChild(div);
            });
        });
};

// Adiciona hashtag selecionada/criada via AJAX
const confirmAddHashtag = (photoId) => {
    const hashtagInput = document.getElementById('new-hashtag').value.trim();
    if (hashtagInput === "") {
        alert("Digite uma hashtag.");
        return;
    }

    const csrfHeaders = getCsrfHeaders();
    fetch('/tag/add', {
        method: 'POST',
        headers: {'Content-Type': 'application/json', ...csrfHeaders},
        body: JSON.stringify({ photoId, tag: hashtagInput })
    })
        .then(response => response.json())
        .then(data => {
            if (data.success) {
                location.reload();
            } else {
                alert("Erro ao adicionar hashtag: " + data.error);
            }
        })
        .catch(err => console.error("Erro ao adicionar hashtag:", err));
};





// Remove hashtag via AJAX
const removeHashtag = (photoId, tagId) => {
    const csrfHeaders = getCsrfHeaders();
    fetch('/tag/remove', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json',
            ...csrfHeaders
        },
        body: JSON.stringify({ photoId, tagId })
    })
        .then(response => {
            if (!response.ok) throw new Error(`Erro HTTP: ${response.status}`);
            return response.json();
        })
        .then(data => {
            if (data.success) {
                const tagElement = document.getElementById(`tag-container-${tagId}`);
                if (tagElement) {
                    tagElement.remove();
                } else {
                    console.warn(`Elemento com id tag-container-${tagId} não encontrado.`);
                }
            } else {
                alert("Erro ao remover hashtag: " + data.error);
            }
        })
        .catch(error => {
            console.error("Erro ao remover hashtag:", error);
            alert("Erro ao remover hashtag.");
        });
};

