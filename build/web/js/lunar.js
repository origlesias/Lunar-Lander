// Moon
var moon = {};
moon.mtop = 50;
moon.g = 0.04 ; // Gravity

// Ship
var ship = {};
ship.y = 0; // Position
ship.v = 0; // Speed
ship.thrust = -moon.g; // Thrust

ship.maxSpeed = 20;
ship.safeSpeed = 1.4; // Maximum speed 'til it explodes

ship.maxFuel = 400; // Fuel/FPS
ship.fuel = ship.maxFuel; // Restore fuel

var pause = true;
var spacePressed = 0;

// Sound efects
var audio = {};
audio.puh = document.createElement("AUDIO");
audio.puh.src = './sound/puh.mp3';
$(audio.puh).prop('volume', 0);

function map(x,  in_min,  in_max,  out_min,  out_max) {
	return (x - in_min) * (out_max - out_min) / (in_max - in_min) + out_min;
}

function reset() {
	$('#state').hide();
	$('#state h1').removeClass();
	ship.fuel = ship.maxFuel;
	ship.y = 0;
	ship.v = 0;
	spacePressed = false;
	$('#ms').css('color', '#000');
	$('#explode').removeClass();
	$('#explode').css('background', 'transparent');
	pause = false;
}

function getSpeed() {
	return parseFloat(Math.round(-ship.v * 100) / 100).toFixed(2);
}

function resize() {
	moon.base = $('#landing-pad').height() + -($('#landing-pad').height()-150)*0.4 + 30;
}

function doEvent(kind, ms) {
	pause = true;
	$('#save h1').addClass(kind);
	$('#save h1').html((kind == 'win') ? 'YOU WIN!' : 'YOU LOSE');
	$('#save h2').html((kind == 'win') ? 'Speed: ' + ms + ' m/s': 'Maybe next time...');
	$('#ms').css('color', (kind == 'win') ? '#0a0' : '#f00');
	if(kind == 'lose')
	{
		$(audio.puh).prop('volume', 1);
		audio.puh.play();
		$('#explode').addClass('exploded');
		$('#explode').css('background', 'url(\'./img/explosion.gif?p=' + new Date().getTime() + '\')');
	}
	$('#save').delay((kind == 'win') ? 0 : 1000).show(0);
         $.post("Moon",
                {
                 score: ms
             });
}

// Modified js
function login(){
    $('#sesion').hide();
    $('#login').show();
}

function register(){
   $('#sesion').hide();
    $('#reg').show(); 
}

function showScore(){
    $('#state').hide();
    $('#scoreTable').show();
}

function backScore(){
    $('#state').show();
    $('#scoreTable').hide();
}

function back(){
    $('#reg').hide();
    $('#login').hide();
    $('#sesion').show();
}

function onSubmit(cname){
    $('#login').hide();
    $('#menu').show();
    $('#nick').text(cname);
    $('#state').show();
}

function menu(){
    $('#save').hide();
    $('#state').show();
}


function logout(){
    delete_cookie("nick");
    window.location= "/LunarLander";
}

//Cookies
function getCookie(cname) {
    var name = cname + "=";
    var ca = document.cookie.split(';');
    for(var i = 0; i < ca.length; i++) {
        var c = ca[i];
        while (c.charAt(0) == ' ') {
            c = c.substring(1);
        }
        if (c.indexOf(name) == 0) {
            return c.substring(name.length, c.length);
        }
    }
    return "";
}

function delete_cookie( name ) {
  document.cookie = name + '=; expires=Thu, 01 Jan 1970 00:00:01 GMT;';
}

$(document).ready(function() {
        var user=getCookie("error");
        var log= getCookie("nick");
        if(user == "passwrong"){
            alert("Contraseña incorrecta");
            delete_cookie("error");
            $('#login').show();
        }else if(user == "existsuser"){
            alert("Este usuario ya esta registrado");
            delete_cookie("error");
            $('#reg').show();
        }else if(log.length>0){
            onSubmit(log);
        }else{
            $('#sesion').show();
        }
        
        $.ajax({
            type: "GET",
            url: "Score",
            dataType: "json",
            success: function (responseJson) {
                        if (responseJson !== null) {
                            $.each(responseJson, function (key, value) {
                                $("#table").append("<tr><td>" +value['userId'] + "</td><td>"+value['score']+"</td></tr>");
                            });
                        }
                    }
        });
        
        
        
	$('body').keydown(function(e) {
		if(e.keyCode == 32) spacePressed = 1;
	});
	$('body').keyup(function(e) {
		if(e.keyCode == 32) spacePressed = 0;
	});
	
	$('#game').bind('touchstart', function(e){
		audio.puh.play();
		spacePressed = 1;
	}).bind('touchend', function(e){
		spacePressed = 0;
	});
		
	// Update
	window.setInterval(function() {
		if(!pause) {
			$(audio.puh).delay(1000).prop('volume', 0);
			ship.v += (spacePressed) ? ((ship.fuel > 0) ? ship.thrust : moon.g) : moon.g; // Aceleración
			ship.v = (ship.v > ship.maxSpeed) ? ship.maxSpeed : ((ship.v < -ship.maxSpeed) ? -ship.maxSpeed : ship.v); // Velocidad
			
			if((ship.v > 0 && ship.y < 500) || (ship.v < 0 && ship.y > 0))
				ship.y += ship.v;
			else
			{
				if(ship.y >= 500)
				{
					ship.y = 500;
					$('#ms').html(getSpeed() + ' m/s');
					doEvent((ship.v > ship.safeSpeed) ? 'lose' : 'win', getSpeed());
				}
				if(ship.y < 0)
					ship.y = 0;
				if(!pause) ship.v = 0;
			}
			
			// Quitamos fuel
			if(ship.fuel > 0 && spacePressed)
				ship.fuel--;
			
			// Dibujamos el juego
			resize();
			$('#gauge div').css('width', ship.fuel/ship.maxFuel*100 + '%');
			$('#ship').css('top', map(ship.y, 0, 600, moon.mtop, $('body').height() - moon.base));
			$('#explode').css('top', map(ship.y, 0, 500, moon.mtop, $('body').height() - moon.base) - 100);
			$('#ship').css('background', (spacePressed & ship.fuel > 0) ? 'url(\'./img/ship.png\')' : 'url(\'./img/shipOff.png\')');
			$('#ms').html(getSpeed() + ' m/s');
		}
	}, 16.6666667);
});