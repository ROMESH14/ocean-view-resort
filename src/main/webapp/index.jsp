<%@ page contentType="text/html;charset=UTF-8" %>
<%
    String error = (String) request.getAttribute("error");
%>
<!DOCTYPE html>
<html>
<head>
    <title>Ocean View Resort - Make Reservation</title>
    <meta name="viewport" content="width=device-width, initial-scale=1.0">

    <style>
        .page-title {
            margin: 0 0 6px 0;
            font-size: 22px;
        }

        .page-subtitle {
            margin: 0 0 18px 0;
            color: #6b7280;
            font-size: 13px;
        }
        .card {
            background: #fff;
            border-radius: 14px;
            box-shadow: 0 10px 25px rgba(0,0,0,0.06);
            padding: 28px;
            max-width: 1100px;
            border: 1px solid rgba(0,0,0,0.04);
        }

        .error-box {
            max-width: 1100px;
            background: #fff1f2;
            color: #9f1239;
            border: 1px solid #fecdd3;
            padding: 12px 14px;
            border-radius: 12px;
            margin-bottom: 14px;
            font-size: 13px;
        }
        .grid {
            display: grid;
            grid-template-columns: 1fr 1fr;
            gap: 20px;
        }

        label {
            display: block;
            font-size: 13px;
            font-weight: bold;
            margin-bottom: 8px;
            color: #374151;
        }
        input, select {
            width: 100%;
            padding: 12px 8px;
            border: 1px solid #d1d5db;
            border-radius: 12px;
            font-size: 14px;
            outline: none;
            background: #fff;
        }

        input:focus, select:focus {
            border-color: #0077b6;
            box-shadow: 0 0 0 4px rgba(0,119,182,0.12);
        }

        .full {
            grid-column: 1 / -1;
        }

        .actions {
            display: flex;
            gap: 10px;
            margin-top: 18px;
            flex-wrap: wrap;
        }

        .btn {
            border: none;
            padding: 12px 16px;
            border-radius: 10px;
            font-weight: bold;
            cursor: pointer;
            text-decoration: none;
            display: inline-block;
            font-size: 13px;
        }

        .btn-primary {
            background: #0077b6;
            color: #fff;
        }

        .btn-primary:hover {
            background: #005f8a;
        }

        .btn-secondary {
            background: #fff;
            color: #111827;
            border: 1px solid #d1d5db;
        }

        .btn-secondary:hover {
            background: #f9fafb;
        }
        @media (max-width: 900px) {
            .grid {
                grid-template-columns: 1fr;
            }
            .card, .error-box {
                max-width: 100%;
            }
        }
    </style>
</head>

<body>

<%@ include file="header.jsp" %>

<h2 class="page-title">Make a Reservation</h2>
<p class="page-subtitle">Enter guest details and select stay dates to book a room.</p>

<% if (error != null) { %>
<div class="error-box">
     <%= error %>
</div>
<% } %>

<div class="card">
    <form action="reservation" method="post">

        <div class="grid">

            <div>
                <label>Guest Name</label>
                <input type="text" name="guestName" required>
            </div>

            <div>
                <label>Contact No</label>
                <input type="text" name="contactNo" required>
            </div>

            <div class="full">
                <label>Address</label>
                <input type="text" name="address" required>
            </div>

            <div>
                <label>Room Type</label>
                <select name="roomType" required>
                    <option value="" selected disabled>-- Select Room Type --</option>
                    <option value="Standard">Standard</option>
                    <option value="Deluxe">Deluxe</option>
                    <option value="Suite">Suite</option>
                </select>
            </div>

            <div>
                <label>Number of Guests</label>
                <input type="number" name="guestCount" min="1" max="3" required>
            </div>

            <div>
                <label>Check-in</label>
                <input type="date" name="checkIn" required>
            </div>

            <div>
                <label>Check-out</label>
                <input type="date" name="checkOut" required>
            </div>

        </div>

        <div class="actions">
            <button class="btn btn-primary" type="submit">Book Now</button>
            <a class="btn btn-secondary" href="reservations">View All Reservations</a>
        </div>

    </form>
</div>

<%@ include file="footer.jsp" %>

</body>
</html>
