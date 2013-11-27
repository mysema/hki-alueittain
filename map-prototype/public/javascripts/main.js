var map = L.map('map').setView([60.16, 24.95], 13);

L.tileLayer('http://{s}.tile.cloudmade.com/c3f9f79471e64cd8b7de37c42b2aa752/98625/256/{z}/{x}/{y}.png', {
  attribution: 'Map data &copy; <a href="http://openstreetmap.org">OpenStreetMap</a> contributors, <a href="http://creativecommons.org/licenses/by-sa/2.0/">CC-BY-SA</a>, Imagery &copy; <a href="http://cloudmade.com">CloudMade</a>',
  maxZoom: 18
}).addTo(map);

var oldCircles = [
  L.circle([60.162, 24.945], 500, {
    color: '#f03',
    fillColor: '#f03',
    fillOpacity: 0.3
  }),
  L.circle([60.170, 24.92], 900, {
    color: '#f03',
    fillColor: '#f03',
    fillOpacity: 0.3
  }),
  L.circle([60.176, 24.97], 300, {
    color: '#f03',
    fillColor: '#f03',
    fillOpacity: 0.3
  }),
  L.circle([60.150, 24.98], 400, {
    color: '#f03',
    fillColor: '#f03',
    fillOpacity: 0.3
  })
];

var newCircles = [
  L.circle([60.162, 24.945], 400, {
    color: '#0f3',
    fillColor: '#0f3',
    fillOpacity: 0.3
  }),
  L.circle([60.170, 24.92], 700, {
    color: '#0f3',
    fillColor: '#0f3',
    fillOpacity: 0.3
  }),
  L.circle([60.176, 24.97], 450, {
    color: '#0f3',
    fillColor: '#0f3',
    fillOpacity: 0.3
  }),
  L.circle([60.150, 24.98], 600, {
    color: '#0f3',
    fillColor: '#0f3',
    fillOpacity: 0.3
  })
];

var oldCircleLayer = L.layerGroup(oldCircles);
var newCircleLayer = L.layerGroup(newCircles);

oldCircleLayer.addTo(map);
newCircleLayer.addTo(map);
