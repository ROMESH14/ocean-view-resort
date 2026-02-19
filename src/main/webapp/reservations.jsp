<%@ page contentType="text/html;charset=UTF-8" %>
<%@ page import="java.util.List" %>
<%@ page import="com.oceanview.model.Reservation" %>

<%
    List<Reservation> list = (List<Reservation>) request.getAttribute("reservations");
    String q = (String) request.getAttribute("q");
%>

<!DOCTYPE html>
<html>
<head>
    <title>All Reservations - Ocean View Resort</title>
    <meta name="viewport" content="width=device-width, initial-scale=1.0">

    <style>
        /* --- styles unchanged --- */
        .pagehead{display:flex;align-items:flex-end;justify-content:space-between;gap:12px;flex-wrap:wrap;margin-bottom:14px;}
        .page-title{margin:0 0 6px 0;font-size:22px;}
        .page-subtitle{margin:0;color:#6b7280;font-size:13px;}
        .btn{border:none;padding:10px 14px;border-radius:10px;font-weight:bold;cursor:pointer;text-decoration:none;display:inline-block;font-size:13px;}
        .btn-primary{background:#0077b6;color:#fff;} .btn-primary:hover{background:#005f8a;}
        .btn-secondary{background:#fff;color:#111827;border:1px solid #d1d5db;} .btn-secondary:hover{background:#f9fafb;}
        .btn-link{background:transparent;border:none;padding:0;color:#0077b6;font-weight:bold;cursor:pointer;text-decoration:none;font-size:13px;}
        .btn-link:hover{text-decoration:underline;}
        .toolbar{background:#fff;border-radius:14px;box-shadow:0 10px 25px rgba(0,0,0,0.06);padding:14px;border:1px solid rgba(0,0,0,0.04);display:flex;align-items:center;justify-content:space-between;gap:12px;flex-wrap:wrap;max-width:1100px;}
        .searchbox{display:flex;align-items:center;gap:10px;flex-wrap:wrap;}
        .searchbox label{font-size:13px;color:#374151;font-weight:bold;}
        .searchbox input{width:260px;max-width:70vw;padding:10px 12px;border:1px solid #d1d5db;border-radius:10px;font-size:13px;outline:none;}
        .searchbox input:focus{border-color:#0077b6;box-shadow:0 0 0 4px rgba(0,119,182,0.12);}
        .msg{max-width:1100px;margin-top:12px;border-radius:12px;padding:10px 12px;font-size:13px;border:1px solid;}
        .msg.success{background:#ecfdf3;color:#166534;border-color:#bbf7d0;}
        .tablecard{margin-top:14px;background:#fff;border-radius:14px;box-shadow:0 10px 25px rgba(0,0,0,0.06);border:1px solid rgba(0,0,0,0.04);overflow:hidden;max-width:1100px;}
        .tablewrap{overflow-x:auto;}
        table{width:100%;border-collapse:collapse;min-width:1100px;font-size:13px;}
        th, td{text-align:left;padding:12px 10px;border-bottom:1px solid #e5e7eb;vertical-align:top;}
        th{background:#f9fafb;color:#6b7280;font-size:12px;position:sticky;top:0;z-index:1;}
        tr:hover td{background:#f8fafc;}
        .actions{display:flex;gap:10px;flex-wrap:wrap;}
        .pill{display:inline-block;padding:4px 8px;border-radius:999px;border:1px solid #e5e7eb;background:#fff;color:#6b7280;font-size:12px;font-weight:bold;}
        .danger{color:#b91c1c;} .danger:hover{color:#7f1d1d;}
    </style>
</head>

<body>

<%@ include file="header.jsp" %>

<div class="pagehead">
    <div>
        <h2 class="page-title">All Reservations</h2>
        <p class="page-subtitle">Search, view, edit, delete reservations and generate bills.</p>
    </div>

    <a class="btn btn-primary" href="reservation">+ New Reservation</a>
</div>

<%
    String successParam = request.getParameter("success");
    if ("updated".equals(successParam)) {
%>
<div class="msg success">✅ Reservation updated successfully!</div>
<% } else if ("deleted".equals(successParam)) { %>
<div class="msg success">✅ Reservation deleted successfully!</div>
<% } %>

<div class="toolbar">
    <form class="searchbox" action="reservations" method="get">
        <label>Search Guest Name</label>
        <input type="text" name="q" value="<%= (q != null ? q : "") %>" placeholder="Type guest name...">
        <button class="btn btn-primary" type="submit">Search</button>
        <a class="btn btn-secondary" href="reservations">Clear</a>
    </form>

    <div class="pill">
        Total: <%= (list == null ? 0 : list.size()) %>
    </div>
</div>

<div class="tablecard">
    <div class="tablewrap">
        <table>
            <thead>
            <tr>
                <th>ID</th>
                <th>Reservation No</th>
                <th>Guest Name</th>
                <th>Address</th>
                <th>Contact No</th>
                <th>Room Type</th>
                <th>Room No</th>
                <th>Guests</th>
                <th>Check-in</th>
                <th>Check-out</th>
                <th>Actions</th>
            </tr>
            </thead>

            <tbody>
            <%
                if (list != null && !list.isEmpty()) {
                    for (Reservation r : list) {
            %>
            <tr>
                <td><%= r.getId() %></td>
                <td><%= r.getReservationNo() %></td>
                <td><b><%= r.getGuestName() %></b></td>
                <td><%= r.getAddress() %></td>
                <td><%= r.getContactNo() %></td>
                <td><%= r.getRoomType() %></td>

                <!-- ⭐ ROOM NUMBER DISPLAY -->
                <td><%= r.getRoomNumber() == null ? "-" : r.getRoomNumber() %></td>

                <td><%= r.getGuestCount() %></td>
                <td><%= r.getCheckIn() %></td>
                <td><%= r.getCheckOut() %></td>

                <td>
                    <div class="actions">
                        <a class="btn-link" href="details?id=<%= r.getId() %>">View</a>
                        <a class="btn-link" href="viewBill?id=<%= r.getId() %>">Bill</a>
                        <a class="btn-link" href="editReservation?id=<%= r.getId() %>">Edit</a>
                        <a class="btn-link danger"
                           href="deleteReservation?id=<%= r.getId() %>"
                           onclick="return confirm('Are you sure you want to delete this reservation?');">
                            Delete
                        </a>
                    </div>
                </td>
            </tr>
            <%
                    }
                } else {
            %>
            <tr>
                <td colspan="11" style="text-align:center; color:#6b7280; padding: 18px;">
                    No reservations found.
                </td>
            </tr>
            <%
                }
            %>
            </tbody>
        </table>
    </div>
</div>

<%@ include file="footer.jsp" %>

</body>
</html>
