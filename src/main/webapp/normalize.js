/**
 * 
 */
(function()
{
  if( window.localStorage )
  {
    if( !localStorage.getItem( 'firstLoad' ) )
    {
      localStorage[ 'firstLoad' ] = true;
      window.location.reload();
    }  
    else
      localStorage.removeItem( 'firstLoad' );
  }
})();

function normalizer(index, university){
	var all_raw_mark = document.getElementsByClassName("raw_mark_" + index);
	var n = all_raw_mark.length;
	var s = "&jury=" + index;
	s += "&jugename=" + document.getElementById('jugename' + index).value 
	for (var i = 0; i < n; i++){
		s += "&" + all_raw_mark[i].id + "=" + all_raw_mark[i].value;
	}

	document.getElementById("confirm_link_" + index).href = "Edit?action=NORMALIZE&university=" + university + s;
	document.getElementById("confirm_link_" + index).innerHTML = "Xác nhận đã nhập điểm chính xác."
}


function update_selection_results(university){
	var all_results = document.getElementsByClassName("selected");
	var n = all_results.length;
	
	var s = "";
	for (var i = 0; i < n; i++){
		s += "&" + all_results[i].id + "=" + ((all_results[i].checked)?"1":"0");
	}
	
	document.getElementById("confirm_link_final").href = "Edit?action=UPDATE_FINAL_RESULTS&university=" + university + s;
	document.getElementById("confirm_link_final").innerHTML = "Xác nhận duyệt."
}

function set_params(key){
	var v = document.getElementById("input_" + key).value;
	document.getElementById("confirm_" + key).href = "Parameters?action=SET_PARAM&key=" + key + "&value=" + v;
	document.getElementById("confirm_" + key).innerHTML = "Xác nhận cập nhật";
}

function set_university_params(key){
	var v1 = document.getElementById("UniversityName_" + key).value;
	var v2 = document.getElementById("FoundationName_" + key).value;
	var v3 = document.getElementById("StudentClass_" + key).value;
	var v4 = document.getElementById("EvaluatedBy_" + key).value;
	var v5 = document.getElementById("Logo_" + key).value;
	var v6 = document.getElementById("VnCoefs_" + key).value;
	var v7 = document.getElementById("NbJugesByCopy_" + key).value;
	var v8 = document.getElementById("MaxDocs_" + key).value;
	document.getElementById("confirm_university_" + key).href = "Parameters?action=SET_UNIVERSITY_PARAMS&key=" + key + "&values=" + v1 + "XXX" + v2 + "XXX" + v3 + "XXX" + v4 + "XXX" + v5 + "XXX" + v6 + "XXX" + v7 + "XXX" + v8;
	document.getElementById("confirm_university_" + key).innerHTML = "Xác nhận cập nhật";
}

function jury_normalizer(index, university){
	var all_raw_mark = document.getElementsByClassName("raw_mark_" + index);
	var n = all_raw_mark.length;
	var s = "&jury=" + index;
	s += "&jugename=" + document.getElementById('jugename' + index).value 
	for (var i = 0; i < n; i++){
		s += "&" + all_raw_mark[i].id + "=" + all_raw_mark[i].value;
	}

	document.getElementById("confirm_link_" + index).href = "Jury?action=JURY_NORMALIZE&university=" + university + s;
	document.getElementById("confirm_link_" + index).innerHTML = "Xác nhận đã nhập điểm chính xác."
}