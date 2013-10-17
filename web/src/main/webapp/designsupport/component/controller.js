wdefine(function(){
	var okbt = $app.component("okbt");
	okbt.on('click', function(){
		var compInput = $app.component('componentinput');
		if(compInput.value() == null || $.trim(compInput.value()) == ""){
			alert("Please select a component.");
			return;
		}
		var compId = $app.component('componentid');
		if(compId.value() == null || $.trim(compId.value()) == ""){
			alert("Please input an id for the component.");
			return;
		}
		insertComponent(compId.value(), compInput.value());
		$app.close();
		FwBase.Wtf.Application.repaint(FwBase.Wtf.Design.DesignSupport.designable);
	});
	
	function insertComponent(id, type){
		var str = null;
		if(type == 'tab'){
			str = '<div id="' + id + '" wtftype="' + type + '">' +
					'<div id="item1"><div wtftype="container" style="min-height:150px"></div></div>' + 
					'<div id="item2"><div wtftype="container" style="min-height:150px"></div></div>' + 	
			      '</div>';
		}
		else
			str = '<div id="' + id + '" wtftype="' + type + '"></div>';
		FwBase.Wtf.Design.DesignSupport.currParent.html(str);
	}
});