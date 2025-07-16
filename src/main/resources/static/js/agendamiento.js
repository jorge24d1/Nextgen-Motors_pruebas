 // Efecto de la barra de navegación
  window.addEventListener('scroll', function() {
      const nav = document.getElementById('mainNav');
      if (window.scrollY > 50) {
          nav.classList.add('scrolled');
      } else {
          nav.classList.remove('scrolled');
      }
  });