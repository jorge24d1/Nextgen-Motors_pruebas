
$(document).ready(function () {
    $('#tabla-miscitas').DataTable({
        "pageLength": 10,
        "language": {
            "lengthMenu": "Mostrar MENU registros por página",
            "zeroRecords": "No se encontraron vehículos",
            "info": "Estas en la página PAGE de PAGES",
            "infoEmpty": "No hay vehículos disponibles",
            "infoFiltered": "(filtrado de MAX vehículos)",
            "search": "Buscar:",
            "paginate": {
                "first": "Primero",
                "last": "Último",
                "next": "Siguiente",
                "previous": "Anterior"
            }
        }
    });
});
    document.querySelectorAll('#sidebar a').forEach(link => {
    link.addEventListener('click', function (e) {
        if (this.classList.contains('logout')) return;
        e.preventDefault(); // Evita el comportamiento predeterminado del enlace, excepto el de logout

        // Oculta todas las secciones
        document.querySelectorAll('main section').forEach(section => {
            section.style.display = 'none';
        });

        // Muestra la sección correspondiente
        const target = this.getAttribute('href');
        document.querySelector(target).style.display = 'block';
    });
 });
