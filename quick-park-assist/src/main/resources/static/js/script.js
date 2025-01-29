function updatePrice() {
    var spotSelect = document.getElementById("parkingSpotId");
    var priceField = document.getElementById("price");
    var duration = document.getElementById("duration").value;

    // Get the price per hour from the selected parking spot option
    var selectedOption = spotSelect.options[spotSelect.selectedIndex];
    var pricePerHour = parseFloat(selectedOption.getAttribute("data-price"));

    // Calculate the total price
    var totalPrice = pricePerHour * duration;

    // Update the price field with the calculated price
    priceField.value = totalPrice.toFixed(2);  // Format the price to two decimal places
}

function showConfirmationPage(event) {
    // event.preventDefault(); // Prevent the form from submitting
    // Hide the booking form
    document.querySelector('.booking-form').style.display = 'none';

    const confirmationPage = document.getElementById('confirmationPage');
    const bookingForm = document.querySelector('.booking-form');

    // Get the form values
    const fullName = document.getElementById('fullName').value;
    const parkingSpot = document.getElementById('parkingSpotId').options[document.getElementById('parkingSpotId').selectedIndex].text;
    const duration = document.getElementById('duration').value;
    const startTime = document.getElementById('startTime').value;
    const price = document.getElementById('price').value;
    const paymentMethod = document.getElementById('paymentMethod').value;

    // Fill the confirmation page with the form values
    const bookingSummary = document.getElementById('bookingSummary');
    bookingSummary.innerHTML = `
        <li><strong>Full Name:</strong> ${fullName}</li>
        <li><strong>Parking Spot:</strong> ${parkingSpot}</li>
        <li><strong>Duration:</strong> ${duration} hours</li>
        <li><strong>Start Time:</strong> ${startTime}</li>
        <li><strong>Total Price:</strong> $${price}</li>
    `;
    document.getElementById('paymentMethodSummary').textContent = paymentMethod;

    // Hide the booking form and show the confirmation page
    bookingForm.style.display = 'none';
    confirmationPage.style.display = 'block';
}

//Function to make success message after booking cancelled disappear

    // Function to hide alert message after 3 seconds
    window.onload = function() {
    const alert = document.querySelector('.alert');
    if (alert) {
    setTimeout(function() {
    alert.style.opacity = 0; // Fade out the alert
    setTimeout(function() {
    alert.style.display = 'none'; // Completely remove it from view after fade
}, 500); // Wait for the fade effect to complete
}, 3000); // Wait for 3 seconds before starting to fade out
}
};

