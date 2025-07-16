 // Elementos del DOM
    const circle1 = document.getElementById('circle1');
    const circle2 = document.getElementById('circle2');
    const circle3 = document.getElementById('circle3');
    const emailInput = document.getElementById('email');
    const passwordInput = document.getElementById('password');
    const emailField = document.getElementById('email-field');
    const passwordField = document.getElementById('password-field');
    const nextButton = document.getElementById('next-button');
    const backButton = document.getElementById('back-button');
    const submitButton = document.getElementById('submit-button');
    const instructionText = document.getElementById('instruction-text');
    const errorMessage = document.getElementById('error-message');
    const personalDataSection = document.getElementById('personal-data-section');
    const hiddenEmail = document.getElementById('hidden-email');
    const hiddenPassword = document.getElementById('hidden-password');

    // Función para validar email
    function isValidEmail(email) {
        const regex = /^[^\s@]+@(gmail\.com|hotmail\.com)$/;
        return regex.test(email);
    }

    // Paso 1: Email
    nextButton.addEventListener('click', function() {
        if (emailField.style.display !== 'none') {
            const email = emailInput.value.trim();

            if (!email) {
                errorMessage.textContent = 'Por favor introduzca su email';
                errorMessage.style.display = 'block';
                return;
            }

            if (!isValidEmail(email)) {
                errorMessage.textContent = 'Por favor introduzca un email válido (@gmail.com o @hotmail.com)';
                errorMessage.style.display = 'block';
                return;
            }

            emailField.style.display = 'none';
            passwordField.style.display = 'block';
            circle2.classList.add('active');
            backButton.style.display = 'block';
            nextButton.textContent = 'Continuar';
            instructionText.textContent = 'Cree una contraseña segura';
            errorMessage.style.display = 'none';
        }
        // Paso 2: Contraseña
        else if (passwordField.style.display !== 'none') {
            const password = passwordInput.value.trim();

            if (!password) {
                errorMessage.textContent = 'Por favor introduzca una contraseña';
                errorMessage.style.display = 'block';
                return;
            }

            if (password.length < 8) {
                errorMessage.textContent = 'La contraseña debe tener al menos 6 caracteres';
                errorMessage.style.display = 'block';
                return;
            }

            passwordField.style.display = 'none';
            personalDataSection.style.display = 'block';
            circle3.classList.add('active');
            nextButton.style.display = 'none';
            submitButton.style.display = 'block';
            instructionText.textContent = 'Complete sus datos personales';
            errorMessage.style.display = 'none';
        }
    });

    // Botón Retroceder
    backButton.addEventListener('click', function() {
        if (personalDataSection.style.display !== 'none') {
            personalDataSection.style.display = 'none';
            passwordField.style.display = 'block';
            circle3.classList.remove('active');
            nextButton.style.display = 'block';
            submitButton.style.display = 'none';
            instructionText.textContent = 'Cree una contraseña segura';
            nextButton.textContent = 'Continuar';
        } else if (passwordField.style.display !== 'none') {
            passwordField.style.display = 'none';
            emailField.style.display = 'block';
            circle2.classList.remove('active');
            backButton.style.display = 'none';
            nextButton.textContent = 'Siguiente';
            instructionText.textContent = 'Introduzca su dirección de email';
        }
        errorMessage.style.display = 'none';
    });

    // Enviar formulario
    submitButton.addEventListener('click', function(e) {
        e.preventDefault();

        // Copiar datos a campos ocultos
        hiddenEmail.value = emailInput.value;
        hiddenPassword.value = passwordInput.value;

        // Validar campos restantes
        const nombre = document.getElementById('nombre').value.trim();
        const apellido = document.getElementById('apellido').value.trim();
        const identificacion = document.getElementById('identificacion').value.trim();

        if (!nombre || !apellido || !identificacion) {
            errorMessage.textContent = 'Por favor complete todos los campos';
            errorMessage.style.display = 'block';
            return;
        }

        document.getElementById('register-form').submit();
    });