$(document).ready(function() {

  var setupFileupload = function(part) {
    var btn = $('#' + part),
      fileInput = $('#' + part + '-upload'),
      textInput = $('#' + part + '-input');

    fileInput.fileupload({
      dataType: 'json',
      maxNumberOfFiles: 1,
      add: function(e, data) {
        var file = data.files[0];
        textInput.attr('value', file.name);
      },
      done: function (e, data) {
      }
    });

    btn.click(function(e) {
      e.preventDefault();
      fileInput.click();   
    });
  };

  setupFileupload('mapping');
  setupFileupload('config');
  setupFileupload('excel');

  $('input[type="text"]').keydown(function (e) {
    e.preventDefault();
  });

});
