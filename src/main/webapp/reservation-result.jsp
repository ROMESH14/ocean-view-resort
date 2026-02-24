<%@ page contentType="text/html;charset=UTF-8" %>

<%
    Boolean success = (Boolean) request.getAttribute("success");
    com.oceanview.model.Reservation r =
            (com.oceanview.model.Reservation) request.getAttribute("reservation");

    Long nights = (Long) request.getAttribute("nights");
    Integer roomsNeeded = (Integer) request.getAttribute("roomsNeeded");

    java.math.BigDecimal ratePerRoom =
            (java.math.BigDecimal) request.getAttribute("ratePerRoom");

    Integer extraGuests =
            (Integer) request.getAttribute("extraGuests");

    java.math.BigDecimal extraFee =
            (java.math.BigDecimal) request.getAttribute("extraFee");

    java.math.BigDecimal roomCost =
            (java.math.BigDecimal) request.getAttribute("roomCost");

    java.math.BigDecimal extraCost =
            (java.math.BigDecimal) request.getAttribute("extraCost");

    java.math.BigDecimal total =
            (java.math.BigDecimal) request.getAttribute("total");

    java.text.NumberFormat formatter =
            java.text.NumberFormat.getCurrencyInstance(new java.util.Locale("en", "LK"));
%>

<!DOCTYPE html>
<html>
<head>
    <title>Reservation Status - Ocean View Resort</title>
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
            max-width: 920px;
            border-radius: 12px;
            padding: 12px 14px;
            font-size: 13px;
            border: 1px solid;
            margin-bottom: 14px;
        }

        .msg.success{
            background: #ecfdf3;
            color: #166534;
            border-color: #bbf7d0;
        }

        .msg.error{
            background: #fff1f2;
            color: #9f1239;
            border-color: #fecdd3;
        }

        .invoice-wrap{
            max-width: 920px;
        }

        .card{
            background:#fff;
            border-radius: 14px;
            box-shadow: 0 10px 25px rgba(0,0,0,0.06);
            padding: 20px;
            border: 1px solid rgba(0,0,0,0.04);
            margin-bottom: 14px;
        }

        .grid{
            display:grid;
            grid-template-columns: 1fr 1fr;
            gap: 12px;
            margin-top: 10px;
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

        h3{
            margin: 0 0 10px 0;
            font-size: 15px;
        }

        table{
            width:100%;
            border-collapse: collapse;
            font-size: 13px;
        }

        th, td{
            text-align:left;
            padding: 10px;
            border-bottom: 1px solid #e5e7eb;
        }

        th{
            color:#6b7280;
            font-size: 12px;
        }

        .total{
            display:flex;
            justify-content:space-between;
            align-items:center;
            margin-top: 12px;
            padding-top: 12px;
            border-top: 2px dashed #e5e7eb;
        }

        .total .amount{
            font-size: 18px;
            font-weight: 800;
        }

        .actions{
            margin-top: 12px;
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
        @media print {
            .topbar, .sidebar, .actions, .page-title, .page-subtitle, .msg { display:none !important; }
            .layout { display:block !important; }
            .content { padding:0 !important; }
            body { background:#fff !important; }
            .card { box-shadow:none !important; border:none !important; }
        }

        @media (max-width: 720px){
            .grid{ grid-template-columns: 1fr; }
            .invoice-wrap{ max-width: 100%; }
        }
    </style>
</head>

<body>

<%@ include file="header.jsp" %>

<h2 class="page-title">Reservation Status</h2>
<p class="page-subtitle">Confirmation details for the reservation you just created.</p>

<div class="invoice-wrap" id="printArea">

    <% if (success != null && success) { %>

        <div class="msg success">
             <b>Reservation Added Successfully!</b>
        </div>

        <div class="card">
            <h3>Reservation Details</h3>

            <div class="grid">
                <div class="row">
                    <div class="label">Reservation No</div>
                    <div class="value"><%= r.getReservationNo() %></div>
                </div>

                <div class="row">
                    <div class="label">Guest Name</div>
                    <div class="value"><%= r.getGuestName() %></div>
                </div>

                <div class="row" style="grid-column: 1 / -1;">
                    <div class="label">Address</div>
                    <div class="value"><%= r.getAddress() %></div>
                </div>

                <div class="row">
                    <div class="label">Contact No</div>
                    <div class="value"><%= r.getContactNo() %></div>
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
                    <div class="label">Dates</div>
                    <div class="value"><%= r.getCheckIn() %> → <%= r.getCheckOut() %></div>
                </div>
            </div>
        </div>

        <div class="card">
            <h3>Bill Summary</h3>

            <table>
                <thead>
                <tr>
                    <th>Item</th>
                    <th>Details</th>
                    <th>Amount</th>
                </tr>
                </thead>
                <tbody>
                <tr>
                    <td>Nights</td>
                    <td><%= nights %></td>
                    <td>-</td>
                </tr>
                <tr>
                    <td>Rooms Needed</td>
                    <td><%= roomsNeeded %></td>
                    <td>-</td>
                </tr>
                <tr>
                    <td>Rate per Room</td>
                    <td>Per night</td>
                    <td><%= formatter.format(ratePerRoom) %></td>
                </tr>
                <tr>
                    <td>Base Room Cost</td>
                    <td><%= roomsNeeded %> room(s) × <%= nights %> night(s)</td>
                    <td><%= formatter.format(roomCost) %></td>
                </tr>

                <tr>
                    <td>Extra Guests</td>
                    <td><%= extraGuests %></td>
                    <td>-</td>
                </tr>

                <% if (extraGuests != null && extraGuests > 0) { %>
                <tr>
                    <td>Extra Guest Fee</td>
                    <td>Per night</td>
                    <td><%= formatter.format(extraFee) %></td>
                </tr>
                <tr>
                    <td>Extra Guest Cost</td>
                    <td>Total extra charges</td>
                    <td><%= formatter.format(extraCost) %></td>
                </tr>
                <% } %>
                </tbody>
            </table>

            <div class="total">
                <div><b>Total Amount</b></div>
                <div class="amount"><%= formatter.format(total) %></div>
            </div>
        </div>

        <div class="actions">
            <button class="btn btn-primary" onclick="window.print()">🖨 Print</button>
            <a class="btn btn-secondary" href="reservation">Back to Form</a>
            <a class="btn btn-secondary" href="reservations">View All Reservations</a>
        </div>

    <% } else { %>

        <div class="msg error">
             <b>Insert Failed.</b> Please try again.
        </div>

        <div class="actions">
            <a class="btn btn-primary" href="reservation">Back to Form</a>
            <a class="btn btn-secondary" href="reservations">View All Reservations</a>
        </div>

    <% } %>

</div>

<%@ include file="footer.jsp" %>

</body>
</html>
