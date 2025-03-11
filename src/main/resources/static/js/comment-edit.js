function enableEdit(commentId){
    document.getElementById("edit-container-" + commentId).classList.remove("hidden");
}

function disableEdit(event, commentId){
    event.preventDefault();
    document.getElementById("edit-container-" + commentId).classList.add(["hidden"]);
}

function confirmEmptyComment(form) {
    let textArea = form.querySelector("textarea[name='commentText']");
    let commentText = textArea.value.trim(); // Remove espaços extras

    if (commentText === "") {
        let confirmDelete = confirm("O comentário está vazio. Deseja excluir?");
        return confirmDelete; // Se o usuário confirmar, retorna true e o formulário será enviado
    }

    return true; // Continua normalmente se o comentário não estiver vazio
}

function confirmDelete(){
 return confirm("Deseja realmente excluir este comentário?");
}