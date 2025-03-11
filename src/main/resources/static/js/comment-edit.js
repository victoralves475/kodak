function enableEdit(commentId){
    document.getElementById("edit-container-" + commentId).classList.remove("hidden");
}

function disableEdit(event, commentId){
    event.preventDefault();
    document.getElementById("edit-container-" + commentId).classList.add(["hidden"]);
}