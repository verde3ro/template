var index = {
	frmLogin: null,
	lblMensaje: null,
	errorLogin: null,

	init: function () {
		//Inicilización de propiedades del objeto
		this.frmLogin = $('#frmLogin');
		this.lblMensaje = $('#lblMensaje');
		this.errorLogin  = general.obtenerParametro('error');
		
		//Funcionalidad
		setInterval(index.reload, (1 * 60 * 10000)); //Reinicia la pagina

		if (general.obtenerParametro('reload') !== null) {
			index.reload();
		}
		
		if (this.errorLogin !== null && this.lblMensaje.length > 0) {
			general.notify(this.lblMensaje.html(), 'warning');
		}
		
		FormValidation.formValidation(this.frmLogin[0], {
			fields: {
				txtUsuario: {
					validators: {
						notEmpty: {
							message: 'El usuario es requerido'
						}
					}
				},
				txtPassword: {
					validators: {
						notEmpty: {
							message: 'La contraseña es requerida'
						}
					}
				},
			},
			plugins: {
				declarative: new FormValidation.plugins.Declarative({html5Input: true,}),
				trigger: new FormValidation.plugins.Trigger(),
				bootstrap: new FormValidation.plugins.Bootstrap(),
				submitButton: new FormValidation.plugins.SubmitButton(),
				icon: new FormValidation.plugins.Icon({
					valid: 'fa fa-check',
					invalid: 'fa fa-times',
					validating: 'fa fa-refresh'
				}),
			},
		}).on('core.form.valid', function() {
			index.frmLogin.submit();
		});

		//Quita vertifica sessión
		clearInterval(general.validate_session);
	},
	reload: function () {
		location.href = general.base_url;
	}
};

index.init();