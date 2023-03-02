var form = document.forms.namedItem("fileinfo");
console.log(form);
form.addEventListener('submit', function(ev) {

  var oOutput = document.getElementById("output"),
  oData = new FormData(document.forms.namedItem("fileinfo"));

  oData.append("ubicacion", "img");
  oData.append("fecha", fecha());
  console.log(fecha());

  var oReq = new XMLHttpRequest();
  oReq.open("POST", "http://localhost:8081/api/files", true);
  oReq.onload = function(oEvent) {
    if (oReq.status == 200) {
      oOutput.innerHTML = "cargo con exito";
    } else {
      oOutput.innerHTML = "Error " + oReq.status + " occurred uploading your file.<br />";
    }
  };

  oReq.send(oData);
  ev.preventDefault();
}, false);

function fecha() {
    var f = new Date();
    let dia=f.getDate();
    let mes=(f.getMonth() +1);
    if (dia<10) {
      dia="0"+dia;
    }
    if (mes<10) {
      mes="0"+mes;
    }
    return  f.getFullYear()+ "-"+mes+ "-"+dia ;
}