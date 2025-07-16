// script del dashboard admin

// script 1 :
let citaActualId = null;

 // Filtrado de veiculos con datatables query:
$(document).ready(function () {
    $('#tabla-vehiculos').DataTable({
        "pageLength": 10,
        "language": {
            "lengthMenu": "Mostrar _MENU_ registros por página",
            "zeroRecords": "No se encontraron vehículos",
            "info": "Estas en la página _PAGE_ de _PAGES_",
            "infoEmpty": "No hay vehículos disponibles",
            "infoFiltered": "(filtrado de _MAX_ vehículos)",
            "search": "Buscar:",
            "paginate": {
                "first": "Primero",
                "last": "Último",
                "next": "Siguiente",
                "previous": "Anterior"
            }
        },
        // Evitar que la última columna (Acciones) sea ordenable
            "columnDefs": [
                { "orderable": false, "targets": -1 }
            ]
    });
});
//fin filtrado de vehiculos

//filtrado de la tabla de los anuncios de los vehiculos:
$(document).ready(function () {
    $('#tabla-anuncios').DataTable({
        "pageLength": 5,
        "language": {
            "lengthMenu": "Mostrar _MENU_ registros por página",
            "zeroRecords": "No se encontraron anuncios",
            "info": "Estas en la página _PAGE_ de _PAGES_",
            "infoEmpty": "No hay anuncios disponibles",
            "infoFiltered": "(filtrado de _MAX_ anuncios)",
            "search": "Buscar:",
            "paginate": {
                "first": "Primero",
                "last": "Último",
                "next": "Siguiente",
                "previous": "Anterior"
            }
        },
        // Evitar que la última columna (Acciones) sea ordenable
            "columnDefs": [
                { "orderable": false, "targets": -1 }
            ]
    });
});
//fin filtrado de anuncios

// filtrado de citas
$(document).ready(function (){
    $('#tabla-citas').DataTable({
        "pageLength": 10,
        "language": {
            "lengthMenu": "Mostrar _MENU_ registros por página",
            "zeroRecords": "No se encontraron citas",
            "info": "Estas en la página _PAGE_ de _PAGES_",
            "infoEmpty": "No hay citas disponibles",
            "infoFiltered": "(filtrado de _MAX_ citas)",
            "search": "Buscar:",
            "paginate": {
                "first": "Primero",
                "last": "Último",
                "next": "Siguiente",
                "previous": "Anterior"
            }
        },
        // Evitar que la última columna (Acciones) sea ordenable
            "columnDefs": [
                { "orderable": false, "targets": -1 }
            ]
    });
});

 // Delegación de eventos para los botones
 document.addEventListener('click', function(e) {
     // Aprobar cita
     if (e.target.classList.contains('btn-aprobar')) {
         cambiarEstadoCita(e.target.dataset.id, 'Aprobada');
     }
     // Rechazar cita
     else if (e.target.classList.contains('btn-rechazar')) {
         cambiarEstadoCita(e.target.dataset.id, 'Rechazada');
     }
     // Volver a pendiente
     else if (e.target.classList.contains('btn-volver')) {
         cambiarEstadoCita(e.target.dataset.id, 'Pendiente');
     }
     // Asignar fecha
     else if (e.target.classList.contains('btn-asignar')) {
         citaActualId = e.target.dataset.id;
         const fechaInput = document.getElementById('fecha-asignacion');

         // Configurar fecha mínima (hora actual)
         const now = new Date();
         const timezoneOffset = now.getTimezoneOffset() * 60000;
         const localISOTime = new Date(now - timezoneOffset).toISOString().slice(0, 16);
         fechaInput.min = localISOTime;

         // Abrir modal
         document.getElementById('modal-fecha').style.display = 'block';
     }
     // Notas
     else if (e.target.classList.contains('btn-notas')) {
         citaActualId = e.target.dataset.id;
         fetch(`/admin/citas/${citaActualId}/notas`)
             .then(response => response.text())
             .then(notas => {
                 document.getElementById('texto-notas').value = notas;
                 document.getElementById('modal-notas').style.display = 'block';
             });
     }
     // Cerrar modales
     else if (e.target.classList.contains('cerrar-modal')) {
         document.querySelectorAll('.modal-cita').forEach(modal => {
             modal.style.display = 'none';
         });
     }
 });

 // Confirmar fecha (con validación)
 document.getElementById('confirmar-fecha').addEventListener('click', function() {
     const fechaInput = document.getElementById('fecha-asignacion');
     const fechaSeleccionada = new Date(fechaInput.value);
     const ahora = new Date();

     if (!fechaInput.value) {
         alert('Por favor seleccione una fecha y hora');
         return;
     }

     if (fechaSeleccionada < ahora) {
         alert('No puede seleccionar una fecha y hora pasada');
         return;
     }

     fetch(`/admin/citas/${citaActualId}/asignar-fecha?fecha=${fechaInput.value}`, {
         method: 'POST'
     }).then(() => {
         document.getElementById('modal-fecha').style.display = 'none';
         actualizarFilaCita(citaActualId);
     });
 });

 // Guardar notas
 document.getElementById('guardar-notas').addEventListener('click', function() {
     const notas = document.getElementById('texto-notas').value;
     fetch(`/admin/citas/${citaActualId}/guardar-notas`, {
         method: 'POST',
         headers: {
             'Content-Type': 'application/x-www-form-urlencoded',
         },
         body: `notas=${encodeURIComponent(notas)}`
     }).then(() => {
         document.getElementById('modal-notas').style.display = 'none';
         actualizarFilaCita(citaActualId);
     });
 });

 // Función para cambiar estado
 function cambiarEstadoCita(id, estado) {
     fetch(`/admin/citas/${id}/cambiar-estado?estado=${estado}`, {
         method: 'POST'
     }).then(() => actualizarFilaCita(id));
 }

 // Función para actualizar solo la fila de la cita modificada
 function actualizarFilaCita(id) {
     fetch(`/admin/citas/${id}/datos`)
         .then(response => response.json())
         .then(cita => {
             const fila = document.querySelector(`tr[data-id="${id}"]`);
             if (fila) {
                 // Actualizar estado
                 fila.className = '';
                 fila.classList.add(cita.estado);
                 fila.querySelector('.estado-badge').textContent = cita.estado;

                 // Actualizar fecha asignada
                 const fechaCell = fila.querySelector('td:nth-child(7)');
                 if (cita.fechaAsignada) {
                     const fecha = new Date(cita.fechaAsignada);
                     fechaCell.textContent = fecha.toLocaleString('es-ES', {
                         day: '2-digit',
                         month: '2-digit',
                         year: 'numeric',
                         hour: '2-digit',
                         minute: '2-digit'
                     });
                 } else {
                     fechaCell.textContent = '-';
                 }

                 // Actualizar notas
                 fila.querySelector('td:nth-child(8)').textContent =
                     cita.notasAdmin || 'Sin notas';

                 // Actualizar botones según el nuevo estado
                 const accionesCell = fila.querySelector('.acciones');
                 accionesCell.innerHTML = generarBotonesAccion(cita.estado, id);
             }
         });
 }

 // Función para generar los botones de acción según el estado
 function generarBotonesAccion(estado, id) {
     if (estado === 'Pendiente') {
         return `
             <div class="acciones-pendiente">
                 <button class="btn-accion btn-aprobar" data-id="${id}">Aprobar</button>
                 <button class="btn-accion btn-rechazar" data-id="${id}">Rechazar</button>
                 <button class="btn-accion btn-notas" data-id="${id}">Notas</button>
             </div>
         `;
     } else if (estado === 'Aprobada') {
         return `
             <div class="acciones-aprobada">
                 <button class="btn-accion btn-asignar" data-id="${id}">Asignar Fecha</button>
                 <button class="btn-accion btn-volver" data-id="${id}">A Pendiente</button>
                 <button class="btn-accion btn-notas" data-id="${id}">Notas</button>
             </div>
         `;
     } else if (estado === 'Rechazada') {
         return `
             <div class="acciones-rechazada">
                 <button class="btn-accion btn-volver" data-id="${id}">A Pendiente</button>
                 <button class="btn-accion btn-notas" data-id="${id}">Notas</button>
             </div>
         `;
     }
     return '';
 }


// script 2:
document.addEventListener('DOMContentLoaded', function() {
     console.log("Script de notificaciones cargado");

     const bell = document.getElementById('notification-bell');
     const dropdown = document.getElementById('notification-dropdown');

     if (!bell || !dropdown) {
         console.error("Elementos de notificación no encontrados");
         return;
     }

     // Mostrar/ocultar dropdown
     bell.addEventListener('click', function(e) {
         e.preventDefault();
         e.stopPropagation();
         dropdown.style.display = dropdown.style.display === 'block' ? 'none' : 'block';
         if (dropdown.style.display === 'block') {
             loadNotifications();
         }
     });

     // Cerrar al hacer clic fuera
     document.addEventListener('click', function(e) {
         if (!bell.contains(e.target) && !dropdown.contains(e.target)) {
             dropdown.style.display = 'none';
         }
     });

     // Cargar notificaciones
     function loadNotifications() {
         console.log("Cargando notificaciones...");
         fetch('/admin/notificaciones')
             .then(response => {
                 if (!response.ok) throw new Error("Error en la respuesta");
                 return response.json();
             })
             .then(data => {
                 console.log("Notificaciones recibidas:", data);
                 const list = document.getElementById('notification-list');
                 const count = document.getElementById('notification-count');

                 if (!list || !count) {
                     console.error("Elementos de lista o contador no encontrados");
                     return;
                 }

                 count.textContent = data.length;
                 list.innerHTML = '';

                 if (data.length === 0) {
                     list.innerHTML = '<div class="notification-item empty">No hay notificaciones nuevas</div>';
                     return;
                 }

                 data.forEach(notif => {
                     const item = document.createElement('div');
                     item.className = 'notification-item';
                     item.innerHTML = `
                         <div class="content">
                             <strong>${notif.nombres} ${notif.apellidos}</strong>
                             <p>${notif.tipo} - ${formatDate(notif.fechaCreacion)}</p>
                             ${notif.comentario ? `<small>${notif.comentario}</small>` : ''}
                         </div>
                         <button class="mark-read" data-id="${notif.id}">×</button>
                     `;
                     list.appendChild(item);
                 });

                 // Marcar como leída
                 document.querySelectorAll('.mark-read').forEach(btn => {
                     btn.addEventListener('click', function(e) {
                         e.stopPropagation();
                         const id = this.getAttribute('data-id');
                         console.log("Marcando como leída:", id);
                         fetch(`/admin/marcar-leida/${id}`, {
                             method: 'POST',
                             headers: {
                                 'Content-Type': 'application/json'
                             }
                         })
                         .then(response => {
                             if (!response.ok) throw new Error("Error al marcar como leída");
                             loadNotifications();
                         })
                         .catch(error => console.error("Error:", error));
                     });
                 });
             })
             .catch(error => console.error("Error al cargar notificaciones:", error));
     }

     // Formatear fecha
     function formatDate(dateString) {
         if (!dateString) return '';
         const date = new Date(dateString);
         if (isNaN(date.getTime())) return '';

         const options = {
             year: 'numeric',
             month: 'short',
             day: 'numeric',
             hour: '2-digit',
             minute: '2-digit'
         };
         return date.toLocaleDateString('es-ES', options);
     }

     // Cargar inicialmente
     loadNotifications();

     // Actualizar cada 30 segundos
     setInterval(loadNotifications, 10000);
 });


// script 3:
 // Obtener el modal
    const modal = document.getElementById("modal-vehiculo");

    // Obtener el botón que abre el modal
    const btnAñadir = document.querySelector(".btn-añadir");

    // Obtener el elemento <span> que cierra el modal
    const spanClose = document.querySelector(".close");

    // Cuando el usuario hace clic en "Añadir Vehículo", mostrar el modal
    btnAñadir.addEventListener("click", function (event) {
        event.preventDefault(); // Evitar el comportamiento predeterminado del enlace
        modal.style.display = "block"; // Mostrar el modal
    });

    // Cuando el usuario hace clic en <span> (x), cerrar el modal
    spanClose.addEventListener("click", function () {
        modal.style.display = "none"; // Ocultar el modal
    });

    // Cuando el usuario hace clic fuera del modal, cerrarlo
    window.addEventListener("click", function (event) {
        if (event.target === modal) {
            modal.style.display = "none"; // Ocultar el modal
        }
    });




// script 5 :
document.addEventListener('DOMContentLoaded', function() {
    // Mostrar sección de citas 
    if (window.location.hash === '#mensaje') {
        document.querySelectorAll('main section').forEach(section => {
            section.style.display = 'none';
        });
        document.querySelector('#mensaje').style.display = 'block';
    }

    // Resaltar filtro activo
    const urlParams = new URLSearchParams(window.location.search);
    const tipo = urlParams.get('tipo');

    if (tipo) {
        document.querySelectorAll('.btn-filtro').forEach(btn => {
            if (btn.textContent === tipo ||
               (btn.textContent === 'Vehículos' && tipo === 'Interés en Vehículo')) {
                btn.style.backgroundColor = '#18bc9c';
            }
        });
    } else {
        document.querySelector('.btn-filtro[href="?"]').style.backgroundColor = '#18bc9c';
    }
});


    function mostrarModalEditar(vehiculoId) {
    // Hacer una petición AJAX para obtener los datos del vehículo editar
    fetch(`/admin/obtener-vehiculo/${vehiculoId}`)
        .then(response => response.json())
        .then(vehiculo => {
            // Llenar el formulario con los datos del vehículo
            document.getElementById('editar-id').value = vehiculo.id;
            document.getElementById('editar-marca').value = vehiculo.marca;
            document.getElementById('editar-modelo').value = vehiculo.modelo;
            document.getElementById('editar-año').value = vehiculo.año;
            document.getElementById('editar-precio').value = vehiculo.precio;
            document.getElementById('editar-categoria').value = vehiculo.categoria;
            document.getElementById('editar-motor').value = vehiculo.motor;
            document.getElementById('editar-transmision').value = vehiculo.transmision;
            document.getElementById('editar-combustible').value = vehiculo.combustible;
            document.getElementById('editar-pasajeros').value = vehiculo.pasajeros;
            document.getElementById('editar-descripcion').value = vehiculo.descripcion;
            document.getElementById('editar-colores').value =
                vehiculo.colores ? vehiculo.colores.join(', ') : '';

            // Mostrar la imagen actual si existe
            const imagenPreview = document.getElementById('editar-imagen-preview');
            if (vehiculo.imagenUrl) {
                imagenPreview.src = vehiculo.imagenUrl;
                imagenPreview.style.display = 'block';
            } else {
                imagenPreview.style.display = 'none';
            }

            // Configurar el action del formulario
            document.getElementById('form-editar-vehiculo').action = `/admin/editar/${vehiculo.id}`;

            // Mostrar el modal
            document.getElementById('modal-editar-vehiculo').style.display = 'block';
        })
        .catch(error => {
            console.error('Error al obtener los datos del vehículo:', error);
            alert('Error al cargar los datos del vehículo');
        });
}



// ---------------------

document.querySelectorAll('#sidebar .side-menu.top li a').forEach(link => {
    link.addEventListener('click', function(e) {
        // 1. Verifica si el elemento está oculto por Thymeleaf (rol no autorizado)
        if (this.offsetParent === null) return; // Si el elemento está oculto, no hagas nada
        
        // 2. Evita comportamiento predeterminado (excepto logout)
        if (this.classList.contains('logout')) return;
        e.preventDefault();

        // 3. Oculta secciones y muestra la target
        document.querySelectorAll('main section').forEach(section => {
            section.style.display = 'none';
        });
        const target = this.getAttribute('href');
        document.querySelector(target).style.display = 'block';

        // 4. Actualiza clase 'active' (reemplaza el segundo listener)
        document.querySelectorAll('#sidebar .side-menu.top li').forEach(li => {
            li.classList.remove('active');
        });
        this.parentElement.classList.add('active');
    });
});
// --------------------

// const allSideMenu = document.querySelectorAll('#sidebar .side-menu.top li a');

// allSideMenu.forEach(items=> {
//     const li = items.parentElement;

//     items.addEventListener('click', function (){
//         allSideMenu.forEach(i=>{
//             i.parentElement.classList.remove('active');
//         })
//         li.classList.add('active');
//     })
// })


// toggle sidebar
const menuBar = document.querySelector('#content nav .bx.bx-menu');
const sidebar = document.getElementById('sidebar');

menuBar.addEventListener('click', function(){
    sidebar.classList.toggle('hide');
})



const searchButton = document.querySelector('#content nav form .form-input button');
const searchButtonIcon = document.querySelector('#content nav form .form-input button .bx');
const searchForm = document.querySelector('#content nav form');

searchButton.addEventListener('click', function (e) {
    if (window.innerWidth < 576) {
        e.preventDefault(); 
        searchForm.classList.toggle('show');
        if(searchForm.classList.contains('show')) {
            searchButtonIcon.classList.replace('bx-search', 'bx-x');
        } else {
            searchButtonIcon.classList.replace('bx-x', 'bx-search');
        }
    }
})


if(window.innerWidth < 768) {
    sidebar.classList.add('hide');
} else if(window.innerWidth > 576) {
    searchButtonIcon.classList.replace('bx-x', 'bx-search');
    searchForm.classList.remove('show');
}

window.addEventListener('resize', function () {
    if(this.innerWidth > 576) {
        searchButtonIcon.classList.replace('bx-x', 'bx-search');
        searchForm.classList.remove('show');
    }
})

const switchMode = document.getElementById('switch-mode');

switchMode.addEventListener('change', function () {
	if(this.checked) {
		document.body.classList.add('dark');
	} else {
		document.body.classList.remove('dark');
	}
})