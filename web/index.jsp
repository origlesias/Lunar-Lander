<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="en">
	<head>
		<meta charset="UTF-8">
		<meta name="viewport" content="width=device-width; initial-scale=1.0; maximum-scale=1.0; user-scalable=0;">
		<title>Lunar Landing in HTML5</title>
		<link rel="stylesheet" href="css/style.css">
		<script src="js/jquery-3.1.1.min.js"></script>
                <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.1.0/jquery.min.js"></script>
		<script src="js/lunar.js"></script>
	</head>
	<body>
            <h2 id="nick"></h2>
            <div id="sesion">
			<div class="container">
                            <a href="#" onclick="login()">Conectarse</a>
                            <a href="#" onclick="register()">Registrarse</a>
			</div>
		</div>
            <div id="login">
			<div class="container">
                            <form method="POST" action="Cuenta">
                                <h2>User</h2>
                                <input type="text" name="nick" id="user" required>
				<h2>Password</h2> 
                                <input type="password" name="password" id="password" required>
                                <br>
                                <input type="submit" value="Conectarse" id="submit">
                                <br>
                                <a href="#" onclick="back()">Atras</a>
                            </form>
			</div>
		</div>
                                
                        <div id="reg">
			<div class="container">
                            <form method="POST" action="Registro">
                                <h2>User</h2>
                                <input type="text" name="nick" id="user2" required>
				<h2>Password</h2> 
                                <input type="password" name="password" id="password2" required>
                                <br>
                                <input type="submit" value="Registrarse" id="submit2">
                                <br>
                                <a href="#" onclick="back()">Atras</a>
                            </form>
			</div>
		</div>
                <div id="save">
			<div class="container">
                                <h1></h1>
				<h2></h2>
                              <a href="#" onclick="menu()">Menu</a>
			</div>
		</div>
            
            <div id="scoreTable">
			<div class="container">
                            <center>
                            <div class="scroll">
                            <table id="table">
                                <tr>
                                <td>Usuario</td>
                                <td>Puntuación</td>
                                </tr>
                            </table>
                            </div>
                            <a href="#" onclick="backScore();">Atras</a>
                            </center>
			</div>
		</div>
            
            
            <!-- Original -->
		<div id="state">
			<div class="container">
				<h1></h1>
				<h2></h2>
                                <a href="#" onclick="reset();">Jugar</a>
                                <a href="#" onclick="showScore();">Puntuaciones</a>
                                <a href="#" onclick="logout();">Logout</a>
			</div>
		</div>
		<div id="game">
			<div id="gauge"><div></div></div>
			<div id="ship"></div>
			<div id="explode"></div>
			<div id="moon">
				<div id="landing-pad"><div id="ms">-</div></div>
			</div>
		</div>
		<script>
			(function(i,s,o,g,r,a,m){i['GoogleAnalyticsObject']=r;i[r]=i[r]||function(){
			(i[r].q=i[r].q||[]).push(arguments)},i[r].l=1*new Date();a=s.createElement(o),
			m=s.getElementsByTagName(o)[0];a.async=1;a.src=g;m.parentNode.insertBefore(a,m)
			})(window,document,'script','https://www.google-analytics.com/analytics.js','ga');
			ga('create', 'UA-41665373-8', 'auto');
			ga('send', 'pageview');
		</script>
	</body>
</html>