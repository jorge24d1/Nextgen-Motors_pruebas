document.addEventListener('DOMContentLoaded', function() {
        const avatars = document.querySelectorAll('.author-avatar');
        const colors = ['#3498db', '#e74c3c', '#2ecc71', '#f39c12', '#9b59b6'];

        avatars.forEach((avatar, index) => {
            const color = colors[index % colors.length];
            avatar.style.background = color;
            avatar.innerHTML = `<i class="fas fa-user" style="color: white; font-size: 1.5rem; line-height: 50px;"></i>`;
        });

        // Añadir funcionalidad para las preguntas frecuentes (si se agrega esa sección)
        document.querySelectorAll('.faq-question')?.forEach(question => {
            question.addEventListener('click', () => {
                const item = question.parentNode;
                item.classList.toggle('active');
                document.querySelectorAll('.faq-item').forEach(otherItem => {
                    if (otherItem !== item && otherItem.classList.contains('active')) {
                        otherItem.classList.remove('active');
                    }
                });
            });
        });

        // Smooth scrolling para enlaces internos
        document.querySelectorAll('a[href^="#"]').forEach(anchor => {
            anchor.addEventListener('click', function (e) {
                e.preventDefault();

                const targetId = this.getAttribute('href');
                if (targetId === '#') return;

                const targetElement = document.querySelector(targetId);
                if (targetElement) {
                    window.scrollTo({
                        top: targetElement.offsetTop - 100,
                        behavior: 'smooth'
                    });
                }
            });
        });
    });

    // Efecto de la barra de navegación
        window.addEventListener('scroll', function() {
            const nav = document.querySelector('nav');
            if (window.scrollY > 50) {
                nav.classList.add('scrolled');
            } else {
                nav.classList.remove('scrolled');
            }
        });

        // Asegurarse que el nav tenga la clase scrolled si no está en la parte superior
        document.addEventListener('DOMContentLoaded', function() {
            const nav = document.querySelector('nav');
            if (window.scrollY > 50) {
                nav.classList.add('scrolled');
            }
        });