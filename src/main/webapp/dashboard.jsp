<%@ page contentType="text/html;charset=UTF-8" %>
<!DOCTYPE html>
<html>
<head>
    <title>Dashboard - Ocean View Resort</title>
    <meta name="viewport" content="width=device-width, initial-scale=1.0">

    <style>
        /* Dashboard-only styles (header.jsp already handles layout) */

        .container {
            max-width: 1100px;
            margin: 10px auto;
            padding: 0 10px;
        }

        .welcome {
            margin-bottom: 20px;
        }

        .welcome h1 {
            margin: 0;
        }

        .welcome p {
            color: gray;
            margin-top: 5px;
        }

        /* KPI Cards */
        .card-grid {
            display: grid;
            grid-template-columns: repeat(4, 1fr);
            gap: 15px;
            margin-top: 20px;
        }

        .card {
            background: white;
            padding: 20px;
            border-radius: 12px;
            box-shadow: 0 10px 25px rgba(0,0,0,0.05);
        }

        .card h3 {
            margin: 0;
            font-size: 13px;
            color: gray;
        }

        .card p {
            font-size: 26px;
            font-weight: bold;
            margin: 10px 0 0;
        }

        /* Quick Actions */
        .actions {
            margin-top: 30px;
            display: grid;
            grid-template-columns: repeat(3, 1fr);
            gap: 15px;
            max-width: 1100px;
        }

        .action-btn {
            text-decoration: none;
            background: #0077b6;
            color: white;
            padding: 15px;
            text-align: center;
            border-radius: 12px;
            font-weight: bold;
            transition: 0.3s;
            display: inline-block;
        }

        .action-btn:hover {
            background: #005f8a;
        }

        /* Responsive */
        @media (max-width: 900px) {
            .card-grid,
            .actions {
                grid-template-columns: repeat(2, 1fr);
            }
        }

        @media (max-width: 500px) {
            .card-grid,
            .actions {
                grid-template-columns: 1fr;
            }
        }
    </style>
</head>

<body>

<%@ include file="header.jsp" %>

<%
    Integer totalReservations = (Integer) request.getAttribute("totalReservations");
    Integer bookingsToday = (Integer) request.getAttribute("bookingsToday");
    Integer todayCheckIns = (Integer) request.getAttribute("todayCheckIns");
    Integer todayCheckOuts = (Integer) request.getAttribute("todayCheckOuts");

    java.math.BigDecimal monthlyRevenue = (java.math.BigDecimal) request.getAttribute("monthlyRevenue");
    if (monthlyRevenue == null) monthlyRevenue = java.math.BigDecimal.ZERO;

    if (totalReservations == null) totalReservations = 0;
    if (bookingsToday == null) bookingsToday = 0;
    if (todayCheckIns == null) todayCheckIns = 0;
    if (todayCheckOuts == null) todayCheckOuts = 0;

    java.text.NumberFormat formatter =
            java.text.NumberFormat.getCurrencyInstance(new java.util.Locale("en", "LK"));
%>

<div class="container">

    <!-- Welcome -->
    <div class="welcome">
        <h1>Welcome to Dashboard</h1>
        <p>Manage reservations, billing and guest services easily.</p>
    </div>

    <!-- KPI Cards -->
    <div class="card-grid">
        <div class="card">
            <h3>Total Reservations</h3>
            <p><%= totalReservations %></p>
        </div>

        <div class="card">
            <h3>Bookings Made Today</h3>
            <p><%= bookingsToday %></p>
        </div>

        <div class="card">
            <h3>Today Check-ins</h3>
            <p><%= todayCheckIns %></p>
        </div>

        <div class="card">
            <h3>Monthly Revenue</h3>
            <p><%= formatter.format(monthlyRevenue) %></p>
        </div>
    </div>

    <!-- Quick Actions -->
    <div class="actions">
        <a class="action-btn" href="index.jsp">Make Reservation</a>
        <a class="action-btn" href="reservations">All Reservations</a>
        <a class="action-btn" href="help.jsp">Help</a>
    </div>

</div>

<%@ include file="footer.jsp" %>

</body>
</html>
