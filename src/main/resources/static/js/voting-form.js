// Добавление варианта ответа для конкретного вопроса
function addOption(button, questionIndex) {
    const optionsContainer = button.closest('.question').querySelector('.options-container');
    const optionIndex = optionsContainer.children.length;

    const newOption = document.createElement('div');
    newOption.className = 'option';
    newOption.innerHTML = `
        <input type="text" 
               name="questions[${questionIndex}].variants[${optionIndex}]" 
               placeholder="Вариант ответа" 
               required />
        <button type="button" onclick="removeOption(this)">-</button>
    `;
    optionsContainer.appendChild(newOption);
}

// Удаление варианта ответа с пересчетом индексов
function removeOption(button) {
    const optionDiv = button.closest('.option');
    const optionsContainer = optionDiv.parentElement;
    optionDiv.remove();

    // Пересчет индексов для оставшихся вариантов
    const options = optionsContainer.querySelectorAll('.option input');
    options.forEach((input, index) => {
        const questionIndex = input.name.match(/questions\[(\d+)\]/)[1];
        input.name = `questions[${questionIndex}].variants[${index}]`;
    });
}

// Добавление нового вопроса
function addQuestion() {
    const container = document.getElementById('questions-container');
    const questionIndex = container.children.length;

    const questionDiv = document.createElement('div');
    questionDiv.className = 'question';
    questionDiv.innerHTML = `
        <textarea name="questions[${questionIndex}].text" required></textarea>
        <select name="questions[${questionIndex}].type" 
                onchange="toggleOptions(this, ${questionIndex})">
            <option value="SINGLE_CHOICE">Один вариант</option>
            <option value="MULTIPLE_CHOICE">Несколько вариантов</option>
        </select>
        <label><input type="checkbox" name="questions[${questionIndex}].required}"> Обязательность</label>
        
        <div class="options-container"></div>
        <button type="button" onclick="addOption(this, ${questionIndex})">+</button>
        <button type="button" onclick="removeQuestion(this)">Удалить вопрос</button>
    `;
    container.appendChild(questionDiv);
}

// Удаление вопроса
function removeQuestion(button) {
    const questionDiv = button.closest('.question');
    questionDiv.remove();

    // Пересчет индексов всех вопросов
    const questions = document.querySelectorAll('#questions-container .question');
    questions.forEach((q, newIndex) => {
        const inputs = q.querySelectorAll('textarea, select, input');
        inputs.forEach(input => {
            input.name = input.name.replace(/\[\d+\]/, `[${newIndex}]`);
        });

        // Пересчет индексов вариантов ответов
        const options = q.querySelectorAll('.options-container input');
        options.forEach((o, oIndex) => {
            o.name = `questions[${newIndex}].options[${oIndex}]`;
        });
    });
}

// Скрытие/отображение вариантов при смене типа вопроса
function toggleOptions(select, questionIndex) {
    const optionsContainer = select.closest('.question').querySelector('.options-container');
    const addButton = select.closest('.question').querySelector('button[onclick^="addOption"]');

    if (select.value === 'SIMPLE_TEXT') {
        optionsContainer.style.display = 'none';
        addButton.style.display = 'none';
    } else {
        optionsContainer.style.display = 'block';
        addButton.style.display = 'inline-block';
    }
}

function togglePasswordField() {
    const closedCheckbox = document.querySelector("input[name='closed']");
    const passwordField = document.getElementById("passwordField");

    if (closedCheckbox.checked) {
        passwordField.classList.remove("hidden");
        passwordField.querySelector("input").setAttribute("required", true);
    } else {
        passwordField.classList.add("hidden");
        passwordField.querySelector("input").removeAttribute("required");
    }
}

document.addEventListener("DOMContentLoaded", function() {
    togglePasswordField(); // Применяем состояние при загрузке страницы
});

function setDefaultDateTime(inputId) {
    const now = new Date();
    const defaultEndDate = new Date(now.getTime() + 15 * 60 * 1000); // +15 минут

    const year = defaultEndDate.getFullYear();
    const month = String(defaultEndDate.getMonth() + 1).padStart(2, '0'); // Месяцы начинаются с 0
    const day = String(defaultEndDate.getDate()).padStart(2, '0');
    const hours = String(defaultEndDate.getHours()).padStart(2, '0');
    const minutes = String(defaultEndDate.getMinutes()).padStart(2, '0');

    document.getElementById(inputId).value = `${year}-${month}-${day}T${hours}:${minutes}`;
}

document.addEventListener("DOMContentLoaded", function () {
    setDefaultDateTime('endDate');
});