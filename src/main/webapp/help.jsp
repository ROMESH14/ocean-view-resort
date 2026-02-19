<%@ page contentType="text/html;charset=UTF-8" %>
<!DOCTYPE html>
<html>
<head>
    <title>Help - Ocean View Resort</title>
    <meta name="viewport" content="width=device-width, initial-scale=1.0">

    <style>
        .page-title{
            margin: 0 0 6px 0;
            font-size: 22px;
        }

        .page-subtitle{
            margin: 0 0 18px 0;
            color: #6b7280;
            font-size: 13px;
        }

        .card{
            background:#fff;
            border-radius: 14px;
            box-shadow: 0 10px 25px rgba(0,0,0,0.06);
            padding: 20px;
            max-width: 920px;
            border: 1px solid rgba(0,0,0,0.04);
        }

        .section{
            margin-top: 16px;
            padding-top: 16px;
            border-top: 1px solid #e5e7eb;
        }

        .section:first-child{
            margin-top: 0;
            padding-top: 0;
            border-top: none;
        }

        h3{
            margin: 0 0 10px 0;
            font-size: 15px;
            color: #111827;
        }

        ul{
            margin: 0;
            padding-left: 18px;
            color: #374151;
            font-size: 13px;
            line-height: 1.6;
        }

        .tip{
            margin-top: 16px;
            background: #f0f9ff;
            border: 1px solid rgba(0,119,182,0.18);
            color: #0b4f6c;
            padding: 12px 14px;
            border-radius: 12px;
            font-size: 13px;
            max-width: 920px;
        }

        .note{
            color:#6b7280;
            font-size: 12px;
            margin-top: 8px;
        }
    </style>
</head>

<body>

<%@ include file="header.jsp" %>

<h2 class="page-title">Help</h2>
<p class="page-subtitle">User guide for Ocean View Resort Reservation System.</p>

<div class="card">

    <div class="section">
        <h3>1) Login / Logout</h3>
        <ul>
            <li>To use the system, you must login using your username and password.</li>
            <li>To exit safely, click <b>Logout</b>. This will close your session securely.</li>
        </ul>
    </div>

    <div class="section">
        <h3>2) Add New Reservation</h3>
        <ul>
            <li>Go to <b>Make Reservation</b> from the sidebar.</li>
            <li>Fill: Guest Name, Address, Contact No, Room Type, Number of Guests, Check-in, Check-out.</li>
            <li>Click <b>Book Now</b>.</li>
            <li>The system generates a unique <b>Reservation No</b> automatically.</li>
        </ul>
    </div>

    <div class="section">
        <h3>3) View All Reservations</h3>
        <ul>
            <li>Go to <b>All Reservations</b>.</li>
            <li>You can see every reservation with booking details.</li>
            <li>Use the search box to find reservations by guest name.</li>
        </ul>
    </div>

    <div class="section">
        <h3>4) Edit / Delete Reservation</h3>
        <ul>
            <li>In <b>All Reservations</b>, click <b>Edit</b> to update reservation details.</li>
            <li>Click <b>Delete</b> to remove a reservation (confirmation popup will appear).</li>
            <li>After update/delete, you will see a success popup message.</li>
        </ul>
    </div>

    <div class="section">
        <h3>5) Calculate and Print Bill</h3>
        <ul>
            <li>In <b>All Reservations</b>, click <b>View Bill</b>.</li>
            <li>The bill is calculated based on number of nights, room rate, and extra guest fees.</li>
            <li>Click <b>Print Bill</b> to print the invoice.</li>
        </ul>
    </div>

    <div class="section">
        <h3>6) Room Rules & Charges</h3>
        <ul>
            <li><b>Standard</b>: Rs. 13,000 per night (2 guests included). Extra guest: Rs. 2,000 per night. Max 3 guests per room.</li>
            <li><b>Deluxe</b>: (2 guests included). Extra guest: Rs. 5,000 per night. Max 3 guests per room.</li>
            <li><b>Suite</b>: Only 2 guests allowed (no extra guests).</li>
            <li>If guests exceed room capacity, the system calculates <b>rooms needed</b> automatically.</li>
        </ul>
        <div class="note">Note: Rates and rules can be updated based on resort policy.</div>
    </div>

</div>

<div class="tip">
    <b>Quick Tip:</b> Always double-check dates. Check-out must be after check-in.
</div>

<%@ include file="footer.jsp" %>

</body>
</html>
