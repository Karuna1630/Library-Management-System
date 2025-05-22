function togglePassword(inputId, iconId) {
    const passwordInput = document.getElementById(inputId);
    const icon = document.getElementById(iconId);
    if (passwordInput.type === 'password') {
        passwordInput.type = 'text';
        icon.classList.replace('fa-eye', 'fa-eye-slash');
    } else {
        passwordInput.type = 'password';
        icon.classList.replace('fa-eye-slash', 'fa-eye');
    }
}

function showToast(message, type) {
    console.log('Showing toast: ' + message + ' (type: ' + type + ')');
    const toast = document.createElement('div');
    toast.className = 'toast ' + type;
    toast.innerHTML = message + '<span class="close-btn" onclick="this.parentElement.remove()">×</span>';
    document.body.appendChild(toast);

    setTimeout(() => toast.classList.add('show'), 100);
    setTimeout(() => {
        toast.classList.remove('show');
        setTimeout(() => toast.remove(), 300);
    }, 4000);
}

function showFallbackMessage(message, type) {
    // No longer used since messages are handled in JSP
}

document.addEventListener('DOMContentLoaded', function() {
    console.log('Login page loaded');
    const successMessage = document.querySelector('script[data-messages]')?.getAttribute('data-success');
    const errorMessage = document.querySelector('script[data-messages]')?.getAttribute('data-error');
    console.log('Success message: ' + (successMessage || 'null'));
    console.log('Error message: ' + (errorMessage || 'null'));

    // Removed message display logic; handled in JSP
});