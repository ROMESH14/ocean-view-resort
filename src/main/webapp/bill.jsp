<%@ page contentType="text/html;charset=UTF-8" %>

<%
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
    <title>Reservation Bill - Ocean View Resort</title>
    <meta name="viewport" content="width=device-width, initial-scale=1.0">

    <style>
        /* Page-only styles (layout comes from header.jsp) */

        .invoice-wrap{
            max-width: 820px;
        }

        .invoice{
            background: #fff;
            border-radius: 14px;
            box-shadow: 0 10px 25px rgba(0,0,0,0.06);
            padding: 22px;
            border: 1px solid rgba(0,0,0,0.04);
        }

        .inv-head{
            display:flex;
            align-items:flex-start;
            justify-content:space-between;
            gap: 12px;
            margin-bottom: 14px;
        }

        .inv-title{
            margin: 0;
            font-size: 20px;
        }

        .inv-sub{
            margin: 6px 0 0;
            color: #6b7280;
            font-size: 12px;
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
            white-space: nowrap;
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

        .section-title{
            margin: 18px 0 10px;
            font-size: 14px;
            color: #111827;
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

        .total h3{
            margin:0;
            font-size: 16px;
        }

        .total .amount{
            font-size: 18px;
            font-weight: 800;
            color:#0f172a;
        }

        .actions{
            margin-top: 14px;
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

        /* NEW: friendly error box if opened directly */
        .error-box{
            max-width: 820px;
            background: #fff1f2;
            border: 1px solid #fecdd3;
            color: #9f1239;
            padding: 12px 14px;
            border-radius: 12px;
            font-size: 13px;
            margin-bottom: 12px;
        }

        /* PRINT MODE: hide sidebar/topbar and print only invoice */
        @media print {
            .topbar, .sidebar, .actions { display: none !important; }
            .layout { display: block !important; }
            .content { padding: 0 !important; }
            body { background: #fff !important; }
            .invoice { box-shadow: none !important; border: none !important; }
        }

        @media (max-width: 720px){
            .grid{ grid-template-columns: 1fr; }
        }
    </style>
</head>

<body>

<%@ include file="header.jsp" %>

<%-- ✅ NEW: stop crash if servlet data is missing --%>
<% if (r == null) { %>
    <div class="invoice-wrap">
        <div class="error-box">
            ❌ Bill data not found. Please open the bill from <b>All Reservations</b> by clicking <b>Bill</b>.
        </div>
        <a class="btn btn-secondary" href="reservations">Back to Reservations</a>
    </div>

    <%@ include file="footer.jsp" %>
</body>
</html>
<% return; } %>

<div class="invoice-wrap">

    <div class="invoice" id="invoice">

        <div class="inv-head">
            <div>
                <h2 class="inv-title">Reservation Bill</h2>
                <p class="inv-sub">Ocean View Resort • Your Escape. Our Hospitality.</p>
            </div>
            <div class="badge">Reservation # <%= r.getReservationNo() %></div>
        </div>

        <div class="grid">
            <div class="row">
                <div class="label">Guest Name</div>
                <div class="value"><%= r.getGuestName() %></div>
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
                <div class="label">Stay Dates</div>
                <div class="value"><%= r.getCheckIn() %> → <%= r.getCheckOut() %></div>
            </div>
        </div>

        <h3 class="section-title">Bill Summary</h3>

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

            <% if (extraGuests != null && extraGuests > 0) { %>
            <tr>
                <td>Extra Guest Fee</td>
                <td><%= extraGuests %> extra guest(s) • per night</td>
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
            <h3>Total</h3>
            <div class="amount"><%= formatter.format(total) %></div>
        </div>

    </div>

    <div class="actions">
        <button class="btn btn-primary" onclick="window.print()">🖨 Print Bill</button>
        <a class="btn btn-secondary" href="reservations">Back to List</a>
    </div>

</div>

<%@ include file="footer.jsp" %>

</body>
</html>
