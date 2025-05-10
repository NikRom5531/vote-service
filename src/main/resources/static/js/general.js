// function copyToClipboard(text) {
//     navigator.clipboard.writeText(text)
//         .then(() => alert("Скопировано: " + text))
//         .catch(err => console.error("Ошибка копирования", err));
// }

function copyToClipboard(button) {
    const id = button.getAttribute('data-id');
    const password = button.getAttribute('data-password');
    const textToCopy = id || password;

    navigator.clipboard.writeText(textToCopy)
        // .then(() => alert(`Скопировано: ${textToCopy}`))
        .catch(err => console.error("Ошибка копирования", err));
}