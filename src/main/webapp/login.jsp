<%@ page contentType="text/html;charset=UTF-8" %>
<%
    String error = request.getParameter("error");
%>
<!DOCTYPE html>
<html>
<head>
    <title>Login - Ocean View Resort</title>
    <meta name="viewport" content="width=device-width, initial-scale=1.0">

    <style>
        body {
            margin: 0;
            font-family: Arial, Helvetica, sans-serif;
            background: #f4f6f9;
            display: flex;
            justify-content: center;
            align-items: center;
            height: 100vh;
        }

        .container {
            width: 900px;
            height: 520px;
            background: white;
            display: flex;
            border-radius: 15px;
            box-shadow: 0 10px 30px rgba(0,0,0,0.1);
            overflow: hidden;
        }

        /* LEFT IMAGE SECTION */
        .left{
            flex: 1;
            position: relative;
            overflow: hidden;
            display:flex;
            justify-content:center;
            align-items:center;
            border-top-left-radius: 15px;
            border-bottom-left-radius: 15px;
        }

        .bgimg{
            position:absolute;
            inset:0;
            width:100%;
            height:100%;
            object-fit:cover;
        }

        .overlay{
            position:relative;
            z-index:1;
            text-align:center;
            color:#fff;
            padding: 25px 35px;
            border-radius: 15px;
            background: linear-gradient(
                    180deg,
                    rgba(0,0,0,0.45),
                    rgba(0,0,0,0.15)
            );
            backdrop-filter: blur(2px);
        }

        .resort-name {
            margin: 0;
            font-size: 36px;
            font-weight: bold;
            letter-spacing: 1px;
        }

        .tagline {
            margin-top: 10px;
            font-size: 16px;
            font-weight: 300;
        }

        /* RIGHT FORM SECTION */
        .right {
            flex: 1;
            padding: 50px;
            display: flex;
            flex-direction: column;
            justify-content: center;
        }

        .right h1 {
            margin-bottom: 10px;
            color: #0077b6;
        }

        .right p {
            margin-bottom: 25px;
            color: gray;
        }

        label {
            font-size: 14px;
            font-weight: bold;
        }

        input {
            width: 100%;
            padding: 10px;
            margin-top: 5px;
            margin-bottom: 20px;
            border: 1px solid #ccc;
            border-radius: 8px;
        }

        input:focus {
            border-color: #0077b6;
            outline: none;
        }

        button {
            width: 100%;
            padding: 12px;
            background: #0077b6;
            color: white;
            border: none;
            border-radius: 10px;
            font-weight: bold;
            cursor: pointer;
        }

        button:hover {
            background: #005f8a;
        }

        .error-box {
            background: #ffe6e6;
            color: #cc0000;
            padding: 10px;
            border-radius: 8px;
            margin-bottom: 15px;
        }

        .footer {
            margin-top: 20px;
            font-size: 12px;
            color: gray;
        }

        /* Responsive */
        @media (max-width: 900px) {
            .container {
                width: 95%;
                height: auto;
                flex-direction: column;
            }

            .left {
                height: 250px;
                border-top-right-radius: 15px;
                border-bottom-left-radius: 0;
            }

            .right {
                padding: 30px;
            }
        }
    </style>
</head>

<body>

<div class="container">
    <div class="left">
        <img class="bgimg"
             src="<%= request.getContextPath() %>/assets/image.jpg"
             alt="Ocean View Resort">

        <div class="overlay">
            <h1 class="resort-name">Ocean View Resort</h1>
            <p class="tagline">Your Escape. Our Hospitality.</p>
        </div>
    </div>
    <div class="right">

        <h1>Welcome</h1>
        <p>Please login to continue</p>

        <% if (error != null) { %>
        <div class="error-box">
             Invalid username or password
        </div>
        <% } %>

        <form action="login" method="post">

            <label>Username</label>
            <input type="text" name="username" required>

            <label>Password</label>
            <input type="password" name="password" required>

            <button type="submit">LOGIN</button>

        </form>

        <div class="footer">
            © 2026 Ocean View Resort
        </div>

    </div>

</div>

</body>
</html>
