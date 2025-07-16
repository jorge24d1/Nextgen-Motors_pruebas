/<![CDATA[/
    document.addEventListener('DOMContentLoaded', function() {
        const vehiculoId = [[${vehiculo?.id}]];
        let colorSeleccionado = null;

        console.log("ID del vehículo:", vehiculoId); // Para depuración

        // Función para seleccionar color
        window.seleccionarColor = function(elemento) {
            document.querySelectorAll('.color-option').forEach(opt => {
                opt.classList.remove('selected');
            });
            elemento.classList.add('selected');
            colorSeleccionado = elemento.getAttribute('data-color');
            localStorage.setItem(colorVehiculo_${vehiculoId}, colorSeleccionado);
            console.log("Color seleccionado:", colorSeleccionado); // Para depuración
        };

         // Función para ir a cita
          window.irACita = function() {
              if (!vehiculoId) {
                  console.error("No hay ID de vehículo");
                  return;
              }

              let url = '/usuario/cita?vehiculoId=' + vehiculoId;

              if (colorSeleccionado) {
                  url += '&color=' + encodeURIComponent(colorSeleccionado);
              }

              console.log("Redirigiendo a:", url);
              window.location.href = url;
          };

        // Configurar el botón
        document.getElementById('cotizarBtn')?.addEventListener('click', function(e) {
            e.preventDefault();
            irACita();
        });

        // Cargar color seleccionado previamente
        if (vehiculoId) {
            const colorGuardado = localStorage.getItem(colorVehiculo_${vehiculoId});
            if (colorGuardado) {
                document.querySelectorAll('.color-option').forEach(opt => {
                    if (opt.getAttribute('data-color') === colorGuardado) {
                        opt.classList.add('selected');
                        colorSeleccionado = colorGuardado;
                    }
                });
            }
        }
    });
    /]]>/