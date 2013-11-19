wdefine(function(){
	DesignSupport.eventControllerWrapper();
	
	$app.component("entitybutton").on("click", function(){
		var event = {action: 'entity'};
		FwBase.Wtf.Design.DesignSupport.interactWithEclipse(event, callbackForEntity);
	});
	
	$app.component("generatebutton").on("click", function(){
		var event = {action: 'generatecode', selectedClass: this.data().selectedClass};
		DesignSupport.interactWithEclipse(event, callbackForGenerate);
	});
	function callbackForGenerate(infos) {
		if(infos.errormsg){
			alert(infos.errormsg);
			return;
		}
		else{
			//infos.generatedList
			//show msg
		}
	}
	function callbackForEntity(infos) {
		if(infos.errormsg){
			alert(infos.errormsg);
			return;
		}
		if(infos.selectedClass){
			$app.component("generatebutton").enable(true);
			$app.component("entityattr").value(infos.selectedClass);
			$app.component("generatebutton").data({restapiExist: infos.restapiExist, selectedClass: infos.selectedClass});
			$app.data("moName", infos.moName);
		}
		else{
			$app.component("generatebutton").enable(false);
			$app.component("entityattr").value("");
			$app.component("generatebutton").data({restapiExist: false});
		}
		
		$app.component("idattr").value(infos.modelId);
		$app.component("urlattr").value(infos.serviceName);
		$app.component("autoload").value(true);
	}
	
	var okbt = $app.component("okbt").on('click', function(){
		var app = this.ctx;
		var id = app.component('idattr').value();
		var autoload = app.component("autoload").value();
		var url = app.component("urlattr").value();
		var entity = app.component("entityattr").value();
		var model = {autoload: autoload, url: url, entityName: entity, moName: app.data('moName')};
		DesignSupport.getDesignApp().model(id, model);
		model.id = id;
		FwBase.Wtf.Design.DesignSupport.syncModel(id, model);
		if(app.parent.component('modeldropdown')){
			var models = DesignSupport.getDesignApp().models();
			var options = [];
			for(var i = 0; i < models.length; i ++){
				options.push({text: models[i].id, value: models[i].id});
			}
			app.parent.component('modeldropdown').datas(options);
		}
			
		DesignSupport.closeForEvent();
		app.close();
	});
	
	$app.on('loaded', function(){
		var navid = this.reqData('navid');
		if(navid){
			var model = DesignSupport.getDesignApp().model(navid);
			this.component("entityattr").value(model.metadata.entityName);
			if(model.metadata.entityName != null && model.metadata.entityName != "")
				this.component("generatebutton").enable(false);
			this.component("idattr").value(model.id);
			this.component("urlattr").value(model.metadata.url);
			this.component("autoload").value(model.metadata.autoload);
			this.data("moName", model.metadata.moName);
		}
	});
});