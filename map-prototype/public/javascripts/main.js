var map = L.map('map').setView([60.16, 24.95], 13);

L.tileLayer('http://{s}.tile.cloudmade.com/c3f9f79471e64cd8b7de37c42b2aa752/98625/256/{z}/{x}/{y}.png', {
  attribution: 'Map data &copy; <a href="http://openstreetmap.org">OpenStreetMap</a> contributors, <a href="http://creativecommons.org/licenses/by-sa/2.0/">CC-BY-SA</a>, Imagery &copy; <a href="http://cloudmade.com">CloudMade</a>',
  maxZoom: 18
}).addTo(map);

var areas = [{
  type: 'Feature',
  properties: {title: 'sqm_per_person'},
  geometry: {
    type: 'Polygon',
    coordinates: [[
      [24.92, 60.18],
      [24.95, 60.17],
      [24.91, 60.15]
    ]]
  }
}];

L.geoJson(areas, {
  style: function(feature) {
    return {
      color: '#ff0000'
    }
  }
}).addTo(map);

console.log(map._layers);

map._layers[24]._container.style.clipPath = 'url(#clipLeft)';

L.geoJson(areas, {
  style: function(feature) {
    return {
      color: '#0000ff'
    }
  }
}).addTo(map);

map._layers[28]._container.style.clipPath = 'url(#clipRight)';

