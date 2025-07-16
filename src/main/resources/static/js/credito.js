document.getElementById('calculate-button').addEventListener('click', function() {
        const price = parseFloat(document.getElementById('vehicle-price').value) || 0;
        const downPayment = parseFloat(document.getElementById('down-payment').value) || 0;
        const term = parseInt(document.getElementById('loan-term').value);
        const rate = parseFloat(document.getElementById('interest-rate').value) / 100 / 12;

        const financedAmount = price - downPayment;
        const monthlyPayment = (financedAmount * rate) / (1 - Math.pow(1 + rate, -term));
        const totalInterest = (monthlyPayment * term) - financedAmount;
        const totalCost = financedAmount + totalInterest;

        document.getElementById('financed-amount').textContent = '$' + financedAmount.toLocaleString('es-MX', {minimumFractionDigits: 2, maximumFractionDigits: 2});
        document.getElementById('monthly-payment').textContent = '$' + monthlyPayment.toLocaleString('es-MX', {minimumFractionDigits: 2, maximumFractionDigits: 2});
        document.getElementById('total-interest').textContent = '$' + totalInterest.toLocaleString('es-MX', {minimumFractionDigits: 2, maximumFractionDigits: 2});
        document.getElementById('total-cost').textContent = '$' + totalCost.toLocaleString('es-MX', {minimumFractionDigits: 2, maximumFractionDigits: 2});
    });

    document.addEventListener('DOMContentLoaded', function() {
        const avatars = document.querySelectorAll('.author-avatar');
        const colors = ['#3498db', '#e74c3c', '#2ecc71', '#f39c12', '#9b59b6'];

        avatars.forEach((avatar, index) => {
            const color = colors[index % colors.length];
            avatar.style.background = color;
            avatar.innerHTML = `<i class="fas fa-user" style="color: white; font-size: 1.5rem; line-height: 50px;"></i>`;
        });

        document.getElementById('vehicle-price').value = '450000';
        document.getElementById('down-payment').value = '90000';
        document.getElementById('calculate-button').click();
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