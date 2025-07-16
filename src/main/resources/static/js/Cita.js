 //    funcion para select2
    $(document).ready(function() {
        $('#vehiculo').select2({
            placeholder: "-- Selecciona un vehículo --",
            allowClear: true,
            width: '100%'
        });

        // Llamar también a actualizarNombreVehiculo al iniciar, por si ya hay valor
        actualizarNombreVehiculo();
    });
    // fin select2


        // Efecto de la barra de navegación
        window.addEventListener('scroll', function() {
            const nav = document.getElementById('mainNav');
            if (window.scrollY > 50) {
                nav.classList.add('scrolled');
            } else {
                nav.classList.remove('scrolled');
            }
        });

        function seleccionarCategoria(categoria, elemento) {
           // Limpiar selecciones previas
           document.querySelectorAll('.categoria-item').forEach(item => {
               item.classList.remove('selected');
           });

           // Seleccionar nueva categoría
           elemento.classList.add('selected');
           document.getElementById('tipo').value = categoria;

           // Elementos del formulario
           const vehiculoContainer = document.getElementById('vehiculoContainer');
        //    const modeloContainer = document.getElementById('modeloContainer');
           const comentarioContainer = document.getElementById('comentarioContainer');
           const vehiculoSelect = document.getElementById('vehiculo');
           const nombreVehiculoInput = document.getElementById('nombreVehiculo');
           const colorSelect = document.getElementById('colorVehiculo');
           const colorContainer = document.getElementById('colorContainer');

           if (categoria === 'Otros') {
               vehiculoContainer.style.display = 'block';
            //    modeloContainer.style.display = 'none';
               comentarioContainer.style.display = 'Block';
           } else {
               // Limpiar selección de vehículo
               vehiculoSelect.selectedIndex = 0;
               nombreVehiculoInput.value = '';

               vehiculoContainer.style.display = 'block';
               // modeloContainer.style.display = 'none';
               comentarioContainer.style.display = 'none';

               colorSelect.innerHTML = '<option value="">-- Selecciona un color --</option>';
               colorContainer.style.display = 'none';

               vehiculoContainer.style.display = 'block';
               comentarioContainer.style.display = 'none';
           }
       }



       function actualizarNombreVehiculo() {
        const vehiculoSelect = document.getElementById('vehiculo');
        const nombreVehiculoInput = document.getElementById('nombreVehiculo');
        const colorSelect = document.getElementById('colorVehiculo');
        const selectedOption = vehiculoSelect.options[vehiculoSelect.selectedIndex];

        // Actualizar nombre del vehículo
        nombreVehiculoInput.value = selectedOption.getAttribute('data-nombre') || '';

        // Limpiar y cargar colores
        colorSelect.innerHTML = '<option value="">-- Selecciona un color --</option>';

        if (selectedOption.value) {
            const colores = selectedOption.getAttribute('data-colores')?.split(',') || [];

            colores.forEach(color => {
                if (color.trim()) {
                    const option = new Option(color.trim(), color.trim());
                    colorSelect.add(option);
                }
            });

            // Mostrar selector si hay colores
            document.getElementById('colorContainer').style.display =
                colores.length > 0 ? 'block' : 'none';
        } else {
            document.getElementById('colorContainer').style.display = 'none';
        }
    }

       // Función para mostrar el snackbar
       function showSnackbar(message) {
           const snackbar = document.getElementById("snackbar");
           snackbar.textContent = message;
           snackbar.className = "show";
           setTimeout(function(){ snackbar.className = snackbar.className.replace("show", ""); }, 3000);
       }