define(["base/base"], function(base){	
	FwBase.Wtf.View.Controls.Menu = function(){
		this.menuitems = [];
		FwBase.Wtf.View.Controls.BaseControl.apply(this, arguments);
	};
	$.extend(FwBase.Wtf.View.Controls.Menu.prototype, FwBase.Wtf.View.Controls.BaseControl.prototype, 
		{		
			template: _.template($('#sys_atom_controls_menu').html()),
			postInit : function() {
				//just for 3 levels
				for(var i = 0; i < this.metadata.groups.length; i ++){
					var group = this.metadata.groups[i];
					for(var j = 0; j < group.menus.length; j ++){
						var pre = "";
						var menu = group.menus[j];
						this.createMenuItem(pre, menu);
						if(menu.menus){
							pre = menu.id + "_";
							for(var k = 0; k < menu.menus.length; k ++){
								var cmenu = menu.menus[k];
								this.createMenuItem(pre, cmenu);
								if(cmenu.menus){
									pre += cmenu.id + "_";
									for(var m = 0; m < cmenu.menus.length; m ++){
										var ccmenu = cmenu.menus[m];
										this.createMenuItem(pre, ccmenu);
									}
								}
							}
						}
					};
				};
			},
			mockMetadata : function() {
				var groups = [
								{menus : [{id:'add',name:'Add', icon:'dd'}, {id:'edit',name:'Edit'}, {id:'del',name:'Delete'}]},
								{menus : [{id:'save',name:'Save'}, {id:'disable',name:'Disable'}]},
								{menus : [{id:'help',name:'Help', menus:[{id:'samplea', name:'Samplea'}, {id:'sampleb', name:'Sampleb', disabled:true}, {divider:true}, {id:'more', name:'More', menus:[{id:'linka', name:'Linka'}, {id:'linkb', name:'Linkb'}]}]}]}
							 ];
				this.metadata.groups = groups;
			},
			makeDefault : function(){
				this.setDefault({style: "primary"});
				this.metadata.cssclass = "btn btn-" + this.metadata.style;
			},
			createMenuItem : function(pre, itemmetadata) {
				var menuitem = new FwBase.Wtf.View.Controls.MenuItem(this, itemmetadata, this.el, pre);
				this.menuitems.push(menuitem);
			},
			getMenuItem : function(itemid){
				for(var i = 0; i < this.menuitems.length; i ++){
					if(this.menuitems[i].id == itemid)
						return this.menuitems[i];
				}
				return null;
			},
			itemClicked : function(item) {
				var source = {source : this, trigger : item};
				this.trigger("click", source);
			}
		}
	);
	
	FwBase.Wtf.View.Controls.MenuItem = function(menubar, metadata, parentEl, pre){
		this.menubar = menubar;
		this.metadata = metadata;
		this.id = metadata.id;
		this.parentEl = parentEl;
		this.pre = pre;
		this.create();
	};
	
	$.extend(FwBase.Wtf.View.Controls.MenuItem.prototype, FwBase.Wtf.View.Controls.Listener.prototype, {
		create : function() {
			this.el = this.parentEl.find("#" + this.pre + this.id);
			var oThis = this;
			this.el.click(function(){
				oThis.menubar.itemClicked(oThis);
				return false;
			});
		}
	});
	return FwBase.Wtf.View.Controls.Menu;
});
