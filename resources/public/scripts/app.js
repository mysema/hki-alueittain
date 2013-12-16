$(document).ready(function() {

  // --------------------------------------
  // Area page
  // --------------------------------------
  
  $('select#area').change(function(e) {
    window.location = '/?area=' + $(this).val();
  });

  // --------------------------------------
  // Config and Excel upload 
  // --------------------------------------
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
        data.process().done(function () {
            data.submit();
        });
      }
    });

    btn.click(function(e) {
      e.preventDefault();
      fileInput.click();   
    });
  };

  setupFileupload('mapping');
  setupFileupload('ui-config');
  setupFileupload('excel');

  $('input[type="text"]').keydown(function (e) {
    e.preventDefault();
  });

  $('#publish').click(function(e) {
    var mappingFilename = $('#mapping-input').val(),
      uiConfigFilename = $('#ui-config-input').val(),
      excelFilename = $('#excel-input').val();
      
    e.preventDefault();

    $.post('/publish', 
           {
             mapping: mappingFilename,
             uiConfig: uiConfigFilename,
             excel: excelFilename
           },
           function(data) {
             window.location = "/";
           });

  });


});
