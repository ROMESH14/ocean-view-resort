<%@ page contentType="text/html;charset=UTF-8" %>
<%@ page import="com.oceanview.model.Reservation" %>

<%
    Reservation r = (Reservation) request.getAttribute("reservation");
%>

<!DOCTYPE html>
<html>
<head>
    <title>Reservation Details - Ocean View Resort</title>
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

        .grid{
            display:grid;
            grid-template-columns: 1fr 1fr;
            gap: 12px;
            margin-top: 12px;
        }

        .row{
            background: #f9fafb;
            border: 1px solid #e5e7eb;
            border-radius: 12px;
            padding: 10px 12px;
        }

        .label{
            font-size: 12px;
            color: #6b7280;
            margin-bottom: 4px;
        }

        .value{
            font-size: 14px;
            font-weight: bold;
            color: #111827;
        }

        .actions{
            margin-top: 16px;
            display:flex;
            gap: 10px;
            flex-wrap: wrap;
        }

        .btn{
            border: none;
            padding: 11px 14px;
            border-radius: 10px;
            font-weight: bold;
            cursor: pointer;
            text-decoration: none;
            display: inline-block;
            font-size: 13px;
        }

        .btn-primary{
            background: #0077b6;
            color: #fff;
        }
        .btn-primary:hover{ background:#005f8a; }

        .btn-secondary{
            background: #fff;
            color: #111827;
            border: 1px solid #d1d5db;
        }
        .btn-secondary:hover{ background:#f9fafb; }

        .btn-warn{
            background: #fff7ed;
            color: #9a3412;
            border: 1px solid #fed7aa;
        }
        .btn-warn:hover{ background:#ffedd5; }

        .notfound{
            max-width: 920px;
            background: #fff1f2;
            border: 1px solid #fecdd3;
            color: #9f1239;
            padding: 12px 14px;
            border-radius: 12px;
            font-size: 13px;
        }

        @media (max-width: 720px){
            .grid{ grid-template-columns: 1fr; }
            .card, .notfound{ max-width: 100%; }
        }
    </style>
</head>

<body>

<%@ include file="header.jsp" %>

<h2 class="page-title">Reservation Details</h2>
<p class="page-subtitle">View guest and booking information.</p>

<% if (r == null) { %>

    <div class="notfound">
        ❌ Reservation not found. Please go back to the reservations list.
    </div>

    <div class="actions">
        <a class="btn btn-secondary" href="reservations">Back to List</a>
    </div>

<% } else { %>

    <div class="card">

        <div class="grid">
            <div class="row">
                <div class="label">ID</div>
                <div class="value"><%= r.getId() %></div>
            </div>

            <div class="row">
                <div class="label">Reservation No</div>
                <div class="value"><%= r.getReservationNo() %></div>
            </div>

            <div class="row">
                <div class="label">Guest Name</div>
                <div class="value"><%= r.getGuestName() %></div>
            </div>

            <div class="row">
                <div class="label">Contact No</div>
                <div class="value"><%= r.getContactNo() %></div>
            </div>

            <div class="row" style="grid-column: 1 / -1;">
                <div class="label">Address</div>
                <div class="value"><%= r.getAddress() %></div>
            </div>

            <div class="row">
                <div class="label">Room Type</div>
                <div class="value"><%= r.getRoomType() %></div>
            </div>

            <div class="row">
                <div class="label">Guests</div>
                <div class="value"><%= r.getGuestCount() %></div>
            </div>

            <div class="row">
                <div class="label">Check-in</div>
                <div class="value"><%= r.getCheckIn() %></div>
            </div>

            <div class="row">
                <div class="label">Check-out</div>
                <div class="value"><%= r.getCheckOut() %></div>
            </div>
        </div>

        <div class="actions">
            <a class="btn btn-primary" href="viewBill?id=<%= r.getId() %>">View Bill</a>
            <a class="btn btn-warn" href="editReservation?id=<%= r.getId() %>">Edit</a>
            <a class="btn btn-secondary" href="reservations">Back</a>
        </div>

    </div>

<% } %>

<%@ include file="footer.jsp" %>

</body>
</html>
