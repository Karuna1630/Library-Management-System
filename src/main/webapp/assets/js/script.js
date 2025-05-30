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
 * Show toast notification
 * @param {string} message - The message to display
 * @param {string} type - The type of toast ('success' or 'error')
 */
function showToast(message, type) {
    const toast = document.createElement('div');
    toast.className = `toast ${type}`;
    toast.textContent = message;
    document.body.appendChild(toast);

    // Show toast
    setTimeout(() => {
        toast.classList.add('show');
    }, 100);

    // Hide and remove toast after 4 seconds
    setTimeout(() => {
        toast.classList.remove('show');
        setTimeout(() => {
            toast.remove();
        }, 300);
    }, 4000);
}

/**
 * Validate registration form
 * @param {string} formId - The ID of the form to validate
 * @returns {boolean} - Returns true if form is valid
 */
function validateRegisterForm(formId) {
    const form = document.getElementById(formId);
    if (!form) return true;

    let isValid = true;
    const fullName = form.full_name.value.trim();
    const email = form.user_email.value.trim();
    const password = form.password.value.trim();
    const confirmPassword = form.confirmPassword.value.trim();
    const role = form.role.value;
    const imageInput = form.image;

    // Clear previous errors
    document.querySelectorAll('.error-field').forEach(el => el.classList.remove('error-field'));
    document.querySelectorAll('.error-message').forEach(el => el.remove());

    // Full name validation
    if (!fullName) {
        showError('full_name', 'Full name is required');
        isValid = false;
    } else if (fullName.length < 3) {
        showError('full_name', 'Full name must be at least 3 characters');
        isValid = false;
    } else if (!/^[a-zA-Z\s]+$/.test(fullName)) {
        showError('full_name', 'Full name can only contain letters and spaces');
        isValid = false;
    }

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
    } else if (password.length < 8) {
        showError('password', 'Password must be at least 8 characters');
        isValid = false;
    } else if (!isStrongPassword(password)) {
        showError('password', 'Password must contain at least one uppercase letter, one lowercase letter, one number, and one special character');
        isValid = false;
    }

    // Confirm password validation
    if (!confirmPassword) {
        showError('confirmPassword', 'Please confirm your password');
        isValid = false;
    } else if (password !== confirmPassword) {
        showError('confirmPassword', 'Passwords do not match');
        isValid = false;
    }

    // Role validation
    if (!role) {
        showError('role', 'Please select a role');
        isValid = false;
    }

    // Image validation (optional)
    if (imageInput.files.length > 0) {
        const file = imageInput.files[0];
        const validImageTypes = ['image/jpeg', 'image/png', 'image/gif', 'image/webp'];
        const maxSizeInBytes = 5 * 1024 * 1024; // 5MB

        if (!validImageTypes.includes(file.type)) {
            showError('image', 'Please select a valid image (JPEG, PNG, GIF, or WEBP)');
            isValid = false;
        } else if (file.size > maxSizeInBytes) {
            showError('image', 'Image size must be less than 5MB');
            isValid = false;
        }
    }

    return isValid;
}

/**
 * Check if password is strong
 * @param {string} password - Password to validate
 * @returns {boolean} - Returns true if password is strong
 */
function isStrongPassword(password) {
    const hasUpperCase = /[A-Z]/.test(password);
    const hasLowerCase = /[a-z]/.test(password);
    const hasNumber = /[0-9]/.test(password);
    const hasSpecialChar = /[!@#$%^&*()_+\-=\[\]{};':"\\|,.<>\/?]/.test(password);
    return hasUpperCase && hasLowerCase && hasNumber && hasSpecialChar;
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

// Initialize form validation and effects
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

    // Register form validation
    const registerForm = document.getElementById('registerForm');
    if (registerForm) {
        registerForm.addEventListener('submit', function(event) {
            if (!validateRegisterForm('registerForm')) {
                event.preventDefault();
            }
        });
    }

    // Card hover effects
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

    // Button click effects
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