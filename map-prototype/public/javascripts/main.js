var map = L.map('map').setView([60.16, 24.95], 13);

L.tileLayer('http://{s}.tile.cloudmade.com/c3f9f79471e64cd8b7de37c42b2aa752/98625/256/{z}/{x}/{y}.png', {
  attribution: 'Map data &copy; <a href="http://openstreetmap.org">OpenStreetMap</a> contributors, <a href="http://creativecommons.org/licenses/by-sa/2.0/">CC-BY-SA</a>, Imagery &copy; <a href="http://cloudmade.com">CloudMade</a>',
  maxZoom: 18
}).addTo(map);

// Polygons representing the areas we want to compare
var areas = [{
  type: 'Feature',
  properties: {
    name: 'area1',
    // old and new classes for comparison
    classification: [0, 1]
  },
  geometry: {
    type: 'Polygon',
    coordinates: [[
      [24.92, 60.18],
      [24.95, 60.17],
      [24.91, 60.16]
    ]]
  }
}, {
  type: 'Feature',
  properties: {
    name: 'area2',
    classification: [1, 0]
  },
  geometry: {
    type: 'Polygon',
    coordinates: [[
      [24.95, 60.179],
      [24.97, 60.182],
      [24.96, 60.17]
    ]]
  }
}];

// What color to assign a given class
var classToColor = ['#FF0000', '#0000FF'];

L.geoJson(areas, {
  style: function(feature) {
    // Color using the first (old) class
    return {color: classToColor[feature.properties.classification[0]]};
  },
  // Left region of map
  clipPath: 'url(#clipLeft)'
}).addTo(map);

L.geoJson(areas, {
  style: function(feature) {
    // Color using the second (new) class
    return {color: classToColor[feature.properties.classification[1]]};
  },
  // Right region of map
  clipPath: 'url(#clipRight)'
}).addTo(map);

// Assign clip paths to all appropriate Leaflet layers
for (var i in map._layers) {
  if (map._layers[i]._container) {
    map._layers[i]._container.style.clipPath = 
      map._layers[i].options.clipPath;
  }
}

function moveSwipe(map, swipe, x) {
  x = Math.max(0, Math.min(x, map.getSize()['x']));
  var handle = swipe.getElementsByTagName('div')[0];
  swipe.style.width = x + 'px';
  handle.style.left = (x - handle.getBoundingClientRect().width/2) + 'px';
};

function setDivide(map, clipLeft, clipRight, x) {  
  x = Math.max(0, Math.min(x, map.getSize()['x']));
  var layerX = map.containerPointToLayerPoint(x, 0).x,
    clipLeftRect = clipLeft.getElementsByTagName('rect')[0],
    clipRightRect = clipRight.getElementsByTagName('rect')[0];

  clipLeftRect.setAttribute('width', layerX);
  clipRightRect.setAttribute('x', layerX);
};

var swipe = document.getElementById('swipe'),
  handle = swipe.getElementsByTagName('div')[0];
  dragging = false;

setDivide(map, 
          document.getElementById('clipLeft'), 
          document.getElementById('clipRight'), 
          handle.getBoundingClientRect().left);

swipe.getElementsByTagName('div')[0].onmousedown = function() {
  dragging = true;
}

document.onmouseup = function() {
  dragging = false;
}

document.onmousemove = function(e) {
  if (dragging) {
    // TODO: The divide "jumps" a bit if you grab the handle off-center
    moveSwipe(map, swipe, e.x || e.pageX);
    setDivide(map, 
              document.getElementById('clipLeft'), 
              document.getElementById('clipRight'),
              handle.getBoundingClientRect().left);
  }
}






