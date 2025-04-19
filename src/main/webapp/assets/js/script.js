// Common JavaScript functions for the library management system

/**
 * Toggle password visibility
 * @param {string} inputId - The ID of the password input field
 * @param {string} iconId - The ID of the toggle icon
 */
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

/**
 * Validate login form
 * @param {string} formId - The ID of the form to validate
 * @returns {boolean} - Returns true if form is valid
 */
function validateLoginForm(formId) {
    const form = document.getElementById(formId);
    if (!form) return true;

    let isValid = true;
    const email = form.user_email.value.trim();
    const password = form.password.value.trim();

    // Clear previous errors
    document.querySelectorAll('.error-field').forEach(el => el.classList.remove('error-field'));
    document.querySelectorAll('.error-message').forEach(el => el.remove());

    // Email validation
    if (!email) {
        showError('user_email', 'Email is required');
        isValid = false;
    } else if (!isValidEmail(email)) {
        showError('user_email', 'Please enter a valid email address');
        isValid = false;
    }

    // Password validation
    if (!password) {
        showError('password', 'Password is required');
        isValid = false;
    } else if (password.length < 6) {
        showError('password', 'Password must be at least 6 characters');
        isValid = false;
    }

    return isValid;
}

/**
 * Show error message for a field
 * @param {string} fieldId - The ID of the field with error
 * @param {string} message - The error message to display
 */
function showError(fieldId, message) {
    const field = document.getElementById(fieldId);
    if (!field) return;

    // Highlight field
    field.classList.add('error-field');

    // Create error message element
    const errorElement = document.createElement('div');
    errorElement.className = 'error-message';
    errorElement.textContent = message;
    errorElement.style.color = '#e74c3c';
    errorElement.style.fontSize = '0.8rem';
    errorElement.style.marginTop = '5px';

    // Insert after field
    field.parentNode.insertBefore(errorElement, field.nextSibling);
}

/**
 * Validate email format
 * @param {string} email - Email to validate
 * @returns {boolean} - Returns true if email is valid
 */
function isValidEmail(email) {
    const re = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
    return re.test(email);
}

// Initialize form validation when DOM is loaded
document.addEventListener('DOMContentLoaded', function() {
    // Login form validation
    const loginForm = document.getElementById('loginForm');
    if (loginForm) {
        loginForm.addEventListener('submit', function(event) {
            if (!validateLoginForm('loginForm')) {
                event.preventDefault();
            }
        });
    }

    // Add hover effects to cards
    const cards = document.querySelectorAll('.card');
    cards.forEach(card => {
        card.addEventListener('mouseenter', function() {
            this.style.transform = 'translateY(-5px)';
            this.style.transition = 'all 0.3s ease';
            this.style.boxShadow = '0 10px 20px rgba(0, 0, 0, 0.1)';
        });

        card.addEventListener('mouseleave', function() {
            this.style.transform = 'translateY(0)';
            this.style.boxShadow = '0 4px 8px rgba(0, 0, 0, 0.1)';
        });
    });

    // Add animation to buttons
    const buttons = document.querySelectorAll('.btn');
    buttons.forEach(button => {
        button.addEventListener('mousedown', function() {
            this.style.transform = 'scale(0.98)';
        });
        button.addEventListener('mouseup', function() {
            this.style.transform = 'scale(1)';
        });
    });
});