document.addEventListener('DOMContentLoaded', function() {
    // Elementos del DOM
    const chatContainer = document.getElementById('chat-container');
    const chatToggle = document.getElementById('chat-toggle');
    const closeChatBtn = document.getElementById('close-chat');
    const chatMessages = document.getElementById('chat-messages');
    const userInput = document.getElementById('user-input');
    const sendBtn = document.getElementById('send-btn');

    // Crear botón de reset
    const resetBtn = document.createElement('button');
    resetBtn.id = 'reset-chat';
    resetBtn.textContent = 'Restablecer chat';
    resetBtn.className = 'reset-button';
    chatContainer.querySelector('.chat-header').appendChild(resetBtn);

    // Variables para el timeout de inactividad
    let inactivityTimer;
    const INACTIVITY_TIMEOUT = 180000; // 3 minutos en milisegundos
    const RESET_AFTER_PROMPT = 60000; // 1 minuto después del mensaje de inactividad

    // Función para restablecer el chat completamente
    function resetChat() {
        chatMessages.innerHTML = '';
        sessionStorage.removeItem('chatHistory');
        addBotMessage('¡Hola! Bienvenido al concesionario . ¿En qué puedo ayudarte hoy?', [
            { text: 'Vehículos disponibles', value: 'vehiculos' },
            { text: 'Agendar cita', value: 'agendar' },

            { text: 'Contactar asesor', value: 'asesor' }
        ]);
        resetInactivityTimer();
    }

    // Función para manejar el timeout de inactividad
    function resetInactivityTimer() {
        clearTimeout(inactivityTimer);

        inactivityTimer = setTimeout(() => {
            addBotMessage('¿Sigues ahí? ¿En qué más puedo ayudarte?', [
                { text: 'Sí, quiero continuar', value: 'continuar' },
                { text: 'No, gracias', value: 'salir' }
            ]);

            inactivityTimer = setTimeout(() => {
                resetChat();
            }, RESET_AFTER_PROMPT);
        }, INACTIVITY_TIMEOUT);
    }

    // Evento para el botón de reset
    resetBtn.addEventListener('click', resetChat);

    // Alternar visibilidad del chat
    chatToggle.addEventListener('click', function() {
        chatContainer.classList.toggle('hidden');
        if (!chatContainer.classList.contains('hidden')) {
            resetInactivityTimer();
        }
    });

    // Cerrar el chat
    closeChatBtn.addEventListener('click', function() {
        chatContainer.classList.add('hidden');
    });

    // Cargar historial del chat al iniciar
    loadChatHistory();

    // Mostrar mensaje inicial si no hay historial
    if (!sessionStorage.getItem('chatHistory')) {
        addBotMessage('¡Hola! Bienvenido al concesionario. ¿En qué puedo ayudarte hoy?', [
            { text: 'Vehículos disponibles', value: 'vehiculos' },
            { text: 'Agendar cita', value: 'agendar' },
            { text: 'Contactar asesor', value: 'asesor' }
        ]);
    }

    // Eventos que indican actividad del usuario
    const activityEvents = ['mousedown', 'keypress', 'scroll', 'touchstart'];
    activityEvents.forEach(event => {
        document.addEventListener(event, resetInactivityTimer, false);
    });

    // Enviar mensaje al presionar Enter o el botón
    userInput.addEventListener('keypress', function(e) {
        if (e.key === 'Enter') {
            sendMessage();
        }
        resetInactivityTimer();
    });

    sendBtn.addEventListener('click', function() {
        sendMessage();
        resetInactivityTimer();
    });

    function sendMessage() {
        const message = userInput.value.trim();
        if (message) {
            addUserMessage(message);
            userInput.value = '';
            resetInactivityTimer();

            setTimeout(() => {
                processUserInput(message.toLowerCase());
            }, 500);
        }
    }

    // Guardar historial del chat
    function saveChatHistory() {
        sessionStorage.setItem('chatHistory', chatMessages.innerHTML);
    }

    // Cargar historial del chat
    function loadChatHistory() {
        const savedHistory = sessionStorage.getItem('chatHistory');
        if (savedHistory) {
            chatMessages.innerHTML = savedHistory;
            restoreOptionButtons();
        }
    }

    // Restaurar eventos de los botones de opciones
    function restoreOptionButtons() {
        document.querySelectorAll('.option-button').forEach(button => {
            button.addEventListener('click', function() {
                const text = this.textContent;
                const value = this.getAttribute('data-value') || text.toLowerCase();
                addUserMessage(text);
                processUserInput(value);
            });
        });
    }

    function processUserInput(input) {
        if (input === 'continuar') {
            addBotMessage('Perfecto, ¿en qué más puedo ayudarte?');
            showMainMenu();
            resetInactivityTimer();
            return;
        }
        else if (input === 'salir') {
            resetChat();
            return;
        }
        else if (input === 'hola' || input === 'hi' || input === 'buenos días') {
            showMainMenu();
        }
        else if (input.includes('vehículo') || input === 'vehiculos' || input === '1') {
            showVehicleCategories();
        }
        else if (input.includes('agendar') || input.includes('cita') || input === '2') {
            showAppointmentOptions();
        }

        else if (input.includes('asesor') || input === '4') {
            showAdvisorOptions();
        }
        else if (input === 'menu' || input === 'volver' || input === 'inicio') {
            showMainMenu();
        }
        else if (input === 'información taller') {
            window.location.href = '/usuario/cita?tipo=Información Taller';
        }
        else if (input === 'mantenimiento') {
            window.location.href = '/usuario/cita?tipo=Mantenimiento';
        }
        else if (input === 'garantías') {
            window.location.href = '/usuario/cita?tipo=Garantías';
        }
        else if (input === 'otros') {
            window.location.href = '/usuario/cita?tipo=Otros';
        }
        else if (input === 'pick-ups' || input === 'pick ups') {
            window.location.href = '/vehiculos#categoria-pick-ups';
        }
        else if (input === 'camionetas') {
            window.location.href = '/vehiculos#categoria-camionetas';
        }
        else if (input === 'automóviles' || input === 'automoviles') {
            window.location.href = '/vehiculos#categoria-automóviles';
        }
        else if (input === 'llamar') {
            addBotMessage('Puedes llamar al concesionario al número: <a href="tel:3054424835" style="color: #0066cc; text-decoration: underline;">305-442-4835</a>');
        }
        else if (input === 'whatsapp') {
            addBotMessage('Puedes contactarnos por WhatsApp: <a href="https://wa.me/3234615898" target="_blank" style="color: #0066cc; text-decoration: underline;">323-461-5898</a>');
        }
        else if (input === 'email') {
            addBotMessage('Puedes escribirnos al correo electrónico: <a href="mailto:Nexgenmotors@gmail.com" style="color: #0066cc; text-decoration: underline;">Nexgenmotors@gmail.com</a>');
        }

        else if (input === 'cotizar') {
            window.location.href = '/usuario/cita';
        }
        else {
            addBotMessage('No entendí tu solicitud. Por favor selecciona una opción del menú:');
            showMainMenu();
        }

        resetInactivityTimer();
    }

    function showVehicleCategories() {
        addBotMessage('Tenemos estas categorías de vehículos:', [
            { text: 'Pick-Ups', value: 'pick-ups' },
            { text: 'Camionetas', value: 'camionetas' },
            { text: 'Automóviles', value: 'automóviles' },
            { text: 'Volver al menú', value: 'menu' }
        ]);
    }

    function showAppointmentOptions() {
        addBotMessage('¿Qué tipo de cita deseas agendar?', [
            { text: 'Información Taller', value: 'información taller' },
            { text: 'Mantenimiento', value: 'mantenimiento' },
            { text: 'Garantías', value: 'garantías' },
            { text: 'Otros servicios', value: 'otros' },
            { text: 'Volver al menú', value: 'menu' }
        ]);
    }



    function showAdvisorOptions() {
        addBotMessage('Puedes contactar a un asesor:', [
            { text: 'Llamar al concesionario: 305-442-4835', value: 'llamar' },
            { text: 'WhatsApp: 323-461-5898', value: 'whatsapp' },
            { text: 'Correo electrónico: Nexgenmotors@gmail.com', value: 'email' },
            { text: 'Volver al menú', value: 'menu' }
        ]);
    }

    function showMainMenu() {
        addBotMessage('¿Cómo puedo ayudarte hoy?', [
            { text: 'Vehículos disponibles', value: 'vehiculos' },
            { text: 'Agendar cita', value: 'agendar' },

            { text: 'Contactar asesor', value: 'asesor' }
        ]);
    }

    function addUserMessage(text) {
        const messageDiv = document.createElement('div');
        messageDiv.className = 'message user-message';
        messageDiv.textContent = text;

        const timestamp = document.createElement('div');
        timestamp.className = 'timestamp';
        timestamp.textContent = getCurrentTime();

        const container = document.createElement('div');
        container.className = 'message-container';
        container.appendChild(messageDiv);
        container.appendChild(timestamp);

        chatMessages.appendChild(container);
        chatMessages.scrollTop = chatMessages.scrollHeight;
        saveChatHistory();
    }

    function addBotMessage(text, options = null) {
        const messageDiv = document.createElement('div');
        messageDiv.className = 'message bot-message';
        messageDiv.innerHTML = text;

        const timestamp = document.createElement('div');
        timestamp.className = 'timestamp';
        timestamp.textContent = getCurrentTime();

        const container = document.createElement('div');
        container.className = 'message-container';
        container.appendChild(messageDiv);
        container.appendChild(timestamp);

        if (options) {
            const optionsContainer = document.createElement('div');
            optionsContainer.className = 'options-container';

            options.forEach(option => {
                const button = document.createElement('button');
                button.className = 'option-button';
                button.textContent = option.text;
                button.setAttribute('data-value', option.value);
                button.addEventListener('click', () => {
                    addUserMessage(option.text);
                    processUserInput(option.value);
                });
                optionsContainer.appendChild(button);
            });

            container.appendChild(optionsContainer);
        }

        chatMessages.appendChild(container);
        chatMessages.scrollTop = chatMessages.scrollHeight;
        saveChatHistory();
    }

    function getCurrentTime() {
        const now = new Date();
        return now.toLocaleTimeString([], { hour: '2-digit', minute: '2-digit' });
    }

    // Iniciar el temporizador de inactividad al cargar
    resetInactivityTimer();
});