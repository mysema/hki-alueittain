var map = L.map('map').setView([60.16, 24.95], 13);

L.tileLayer('http://{s}.tile.cloudmade.com/c3f9f79471e64cd8b7de37c42b2aa752/98625/256/{z}/{x}/{y}.png', {
    attribution: 'Map data &copy; <a href="http://openstreetmap.org">OpenStreetMap</a> contributors, <a href="http://creativecommons.org/licenses/by-sa/2.0/">CC-BY-SA</a>, Imagery &copy; <a href="http://cloudmade.com">CloudMade</a>',
    maxZoom: 18
}).addTo(map);
