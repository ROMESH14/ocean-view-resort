<%@ page contentType="text/html;charset=UTF-8" %>

<style>
    body {
        margin: 0;
        font-family: Arial, Helvetica, sans-serif;
        background: #f4f6f9;
    }

    .topbar {
        height: 60px;
        background: #0077b6;
        color: white;
        display: flex;
        align-items: center;
        justify-content: space-between;
        padding: 0 25px;
        font-weight: bold;
    }

    .topbar span {
        font-size: 18px;
    }

    .layout {
        display: flex;
        min-height: calc(100vh - 60px);
    }

    .sidebar {
        width: 220px;
        background: white;
        padding-top: 20px;
        box-shadow: 2px 0 10px rgba(0,0,0,0.05);
    }

    .sidebar a {
        display: block;
        padding: 12px 20px;
        text-decoration: none;
        color: #333;
        font-weight: 500;
        transition: 0.3s;
    }

    .sidebar a:hover {
        background: #0077b6;
        color: white;
    }

    .content {
        flex: 1;
        padding: 25px;
    }
</style>

<!-- Top Navigation -->
<div class="topbar">
    <span>Ocean View Resort</span>
    <a href="logout" style="color:white; text-decoration:none;">Logout</a>
</div>

<div class="layout">
    <div class="sidebar">
        <a href="dashboard">Dashboard</a>
        <a href="index.jsp">Make Reservation</a>
        <a href="reservations">All Reservations</a>
        <a href="help.jsp">Help</a>
    </div>

    <!-- Page Content Starts -->
    <div class="content">
