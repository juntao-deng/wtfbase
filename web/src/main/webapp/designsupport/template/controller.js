wdefine(function(){
	var okbt = $app.component("okbt");
	okbt.on('click', function(){
		var templateinput = $app.component('templateinput');
		insertTemplate(templateinput.value());
		$app.close();
		FwBase.Wtf.Application.repaint(FwBase.Wtf.Design.DesignSupport.designable);
	});
	
	function insertTemplate(tplId){
		var str = '<div wtftype="template" wtfmetadata="' + tplId + '"></div>';
		FwBase.Wtf.Design.DesignSupport.currParent.html(str);
	}
});