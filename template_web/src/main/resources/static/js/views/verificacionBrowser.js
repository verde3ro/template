verBrowser = {
		txtUsuario:null,
		txtPassword: null,
		btnEntrar: null,

		init: function () {
			this.txtUsuario = $('#txtUsuario');
			this.txtPassword = $('#txtPassword');
			this.btnEntrar = $('#btnEntrar');

			this.cargaMensajeBrowser();
		},
		cargaMensajeBrowser: function () {
			var exploradorBrowser = false;
			var es_explorer = navigator.userAgent.toLowerCase().indexOf('trident');
			var explorer = navigator.userAgent.toLowerCase().indexOf('msie');
			var html = '';

			html +='<div class="row">';
				html +='<class="text-center" style="padding-left: 5px; padding-right: 5px;">';
				html +='<center>';
				html +='<img src="http://10.16.8.94/resources/img/browsers.png" alt="logo" style="width: 50%; padding-left: 0px; padding-right: 0px;" />';
				html +='</center>';
				html +='</div>';
				html +='</div>';
			if (es_explorer >= 0 || explorer >= 0) {
				exploradorBrowser = true;
			}

			if (exploradorBrowser) {
				bootbox.dialog({
					title: 'Favor de solo utilizar los siguientes navegadores',
					onEscape: true,
					animate: true,
					message: html,
					centerVertical: true,
					buttons: {
						save: {
							label: 'Aceptar',
							className: 'btn-success',
							callback: function () {
								
							}
						}
					}
				});
				verBrowser.txtUsuario.prop("disabled", true);
				verBrowser.txtPassword.prop("disabled", true);
				verBrowser.btnEntrar.hide();
			}
		}
};

verBrowser.init();