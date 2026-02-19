<%@ page contentType="text/html;charset=UTF-8" %>
<%@ page import="com.oceanview.model.Reservation" %>

<%
    Reservation r = (Reservation) request.getAttribute("reservation");
    String error = (String) request.getAttribute("error");
%>

<!DOCTYPE html>
<html>
<head>
    <title>Edit Reservation - Ocean View Resort</title>
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

        .msg{
            max-width: 760px;
            border-radius: 12px;
            padding: 12px 14px;
            font-size: 13px;
            border: 1px solid;
            margin-bottom: 14px;
        }

        .msg.error{
            background: #fff1f2;
            color: #9f1239;
            border-color: #fecdd3;
        }

        .card{
            background: #fff;
            border-radius: 14px;
            box-shadow: 0 10px 25px rgba(0,0,0,0.06);
            padding: 20px;
            max-width: 760px;
            border: 1px solid rgba(0,0,0,0.04);
        }

        .badge{
            display:inline-block;
            padding: 6px 10px;
            border-radius: 999px;
            background: #eef6fb;
            color: #0077b6;
            font-weight: bold;
            font-size: 12px;
            border: 1px solid rgba(0,119,182,0.18);
            margin-bottom: 14px;
        }

        .grid{
            display:grid;
            grid-template-columns: 1fr 1fr;
            gap: 14px;
        }

        .full{
            grid-column: 1 / -1;
        }

        label{
            display:block;
            font-size: 13px;
            font-weight: bold;
            margin-bottom: 6px;
            color: #374151;
        }

        input, select{
            width: 100%;
            padding: 10px 5px;
            border: 1px solid #d1d5db;
            border-radius: 10px;
            font-size: 14px;
            outline: none;
            background: #fff;
        }

        input:focus, select:focus{
            border-color: #0077b6;
            box-shadow: 0 0 0 4px rgba(0,119,182,0.12);
        }

        .actions{
            display:flex;
            gap: 10px;
            margin-top: 16px;
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

        .notfound{
            max-width: 760px;
            background: #fff1f2;
            border: 1px solid #fecdd3;
            color: #9f1239;
            padding: 12px 14px;
            border-radius: 12px;
            font-size: 13px;
        }

        @media (max-width: 720px){
            .grid{ grid-template-columns: 1fr; }
            .card, .msg, .notfound{ max-width: 100%; }
        }
    </style>
</head>

<body>

<%@ include file="header.jsp" %>

<h2 class="page-title">Edit Reservation</h2>
<p class="page-subtitle">Update guest and booking details.</p>

<% if (r == null) { %>

    <div class="notfound">
        ❌ Reservation not found. Please go back to the reservations list.
    </div>

    <div class="actions">
        <a class="btn btn-secondary" href="reservations">Back to List</a>
    </div>

<% } else { %>

    <% if (error != null) { %>
        <div class="msg error">❌ <%= error %></div>
    <% } %>

    <div class="badge">Reservation # <%= r.getReservationNo() %></div>

    <div class="card">
        <form action="updateReservation" method="post">

            <input type="hidden" name="id" value="<%= r.getId() %>">

            <div class="grid">
                <div>
                    <label>Guest Name</label>
                    <input type="text" name="guestName" value="<%= r.getGuestName() %>" required>
                </div>

                <div>
                    <label>Contact No</label>
                    <input type="text" name="contactNo" value="<%= r.getContactNo() %>" required>
                </div>

                <div class="full">
                    <label>Address</label>
                    <input type="text" name="address" value="<%= r.getAddress() %>" required>
                </div>

                <div>
                    <label>Room Type</label>
                    <select name="roomType" required>
                        <option value="Standard" <%= "Standard".equals(r.getRoomType()) ? "selected" : "" %>>Standard</option>
                        <option value="Deluxe" <%= "Deluxe".equals(r.getRoomType()) ? "selected" : "" %>>Deluxe</option>
                        <option value="Suite" <%= "Suite".equals(r.getRoomType()) ? "selected" : "" %>>Suite</option>
                    </select>
                </div>

                <div>
                    <label>Number of Guests</label>
                    <input type="number" name="guestCount" min="1" required value="<%= r.getGuestCount() %>">
                </div>

                <div>
                    <label>Check-in</label>
                    <input type="date" name="checkIn" value="<%= r.getCheckIn() %>" required>
                </div>

                <div>
                    <label>Check-out</label>
                    <input type="date" name="checkOut" value="<%= r.getCheckOut() %>" required>
                </div>
            </div>

            <div class="actions">
                <button class="btn btn-primary" type="submit">Update</button>
                <a class="btn btn-secondary" href="reservations">Cancel</a>
            </div>

        </form>
    </div>

<% } %>

<%@ include file="footer.jsp" %>

</body>
</html>
