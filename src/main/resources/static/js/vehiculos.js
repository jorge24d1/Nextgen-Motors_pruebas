 // Efecto de la barra de navegación
    window.addEventListener('scroll', function() {
        const nav = document.getElementById('mainNav');
        if (window.scrollY > 50) {
            nav.classList.add('scrolled');
        } else {
            nav.classList.remove('scrolled');
        }
    });


    document.addEventListener('DOMContentLoaded', function() {
    // Si hay un hash en la URL, hacer scroll a esa sección
    if (window.location.hash) {
        const targetSection = document.querySelector(window.location.hash);
        if (targetSection) {
            setTimeout(() => {
                targetSection.scrollIntoView({ behavior: 'smooth' });

                // Opcional: resaltar la sección
                targetSection.style.transition = 'background-color 0.5s';
                targetSection.style.backgroundColor = '#f0f8ff';
                setTimeout(() => {
                    targetSection.style.backgroundColor = '';
                }, 2000);
            }, 300); // Pequeño retardo para asegurar que la página esté cargada
        }
    }
});