document.addEventListener('DOMContentLoaded', function() {
     // Carrusel dinámico
     const carousel = document.querySelector('.carousel');
     const inner = document.querySelector('.carousel-inner');
     const items = document.querySelectorAll('.carousel-item');
     const totalItems = items.length;

     if (totalItems > 0) {
         let currentIndex = 0;

         // Función para mover el carrusel
         function goToSlide(index) {
             if (index < 0) index = totalItems - 1;
             else if (index >= totalItems) index = 0;

             currentIndex = index;
             inner.style.transform = `translateX(-${currentIndex * 100}%)`;

             // Actualizar indicadores
             updateIndicators();
         }

         // Crear indicadores dinámicos
         function createIndicators() {
             const nav = document.createElement('div');
             nav.className = 'carousel-nav';

             items.forEach((_, i) => {
                 const btn = document.createElement('button');
                 if (i === 0) btn.classList.add('active');
                 btn.addEventListener('click', () => goToSlide(i));
                 nav.appendChild(btn);
             });

             carousel.appendChild(nav);
         }

         // Actualizar indicadores
         function updateIndicators() {
             const indicators = document.querySelectorAll('.carousel-nav button');
             indicators.forEach((ind, i) => {
                 ind.classList.toggle('active', i === currentIndex);
             });
         }

         // Navegación automática
         let interval = setInterval(() => goToSlide(currentIndex + 1), 5000);

         // Pausar al interactuar
         carousel.addEventListener('mouseenter', () => clearInterval(interval));
         carousel.addEventListener('mouseleave', () => {
             interval = setInterval(() => goToSlide(currentIndex + 1), 5000);
         });

         // Crear controles
         createIndicators();

         // Flechas de navegación (opcional)
         const prevBtn = document.createElement('button');
         prevBtn.className = 'carousel-arrow prev';
         prevBtn.innerHTML = '❮';
         prevBtn.addEventListener('click', () => goToSlide(currentIndex - 1));

         const nextBtn = document.createElement('button');
         nextBtn.className = 'carousel-arrow next';
         nextBtn.innerHTML = '❯';
         nextBtn.addEventListener('click', () => goToSlide(currentIndex + 1));

         carousel.appendChild(prevBtn);
         carousel.appendChild(nextBtn);
     }

     // Resto de tu código (barra de navegación, avatares, etc.)
     window.addEventListener('scroll', function() {
         const nav = document.getElementById('mainNav');
         if (window.scrollY > 50) {
             nav.classList.add('scrolled');
         } else {
             nav.classList.remove('scrolled');
         }
     });

     // Avatares de testimonios
     const avatars = document.querySelectorAll('.author-avatar');
     const colors = ['#3498db', '#e74c3c', '#2ecc71', '#f39c12', '#9b59b6'];
     avatars.forEach((avatar, index) => {
         const color = colors[index % colors.length];
         avatar.style.background = color;
         avatar.innerHTML = `<i class="fas fa-user" style="color: white; font-size: 1.5rem; line-height: 50px;"></i>`;
     });
 });