// admin.js - Basic Admin Dashboard Functionality

document.addEventListener('DOMContentLoaded', function() {
    // Toggle book form visibility
    const addBookBtn = document.getElementById('addBookBtn');
    const bookFormContainer = document.querySelector('.book-form-container');
    const cancelBookBtn = document.getElementById('cancelBookBtn');

    if (addBookBtn && bookFormContainer) {
        addBookBtn.addEventListener('click', function() {
            bookFormContainer.style.display = 'block';
            document.getElementById('bookForm').reset();
            document.getElementById('bookId').value = '';
        });

        cancelBookBtn.addEventListener('click', function() {
            bookFormContainer.style.display = 'none';
        });
    }

    // Edit book buttons
    const editButtons = document.querySelectorAll('.edit-btn');
    editButtons.forEach(button => {
        button.addEventListener('click', function() {
            const row = this.closest('tr');
            // In a real app, you would fetch the book data and populate the form
            document.getElementById('bookId').value = row.cells[0].textContent;
            document.getElementById('bookTitle').value = row.cells[1].textContent;
            document.getElementById('bookAuthor').value = row.cells[2].textContent;
            // Set other fields similarly...

            bookFormContainer.style.display = 'block';
        });
    });

    // Form submission
    const bookForm = document.getElementById('bookForm');
    if (bookForm) {
        bookForm.addEventListener('submit', function(e) {
            e.preventDefault();
            // In a real app, you would send this data to the server
            alert('Book saved successfully!');
            bookFormContainer.style.display = 'none';
            // Refresh the book list
        });
    }

    // Return book buttons
    const returnButtons = document.querySelectorAll('.return-btn');
    returnButtons.forEach(button => {
        button.addEventListener('click', function() {
            if (confirm('Mark this book as returned?')) {
                // In a real app, you would send this to the server
                const row = this.closest('tr');
                row.cells[5].innerHTML = '<span class="status-returned">Returned</span>';
                this.disabled = true;
            }
        });
    });
});