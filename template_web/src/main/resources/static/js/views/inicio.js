/* global general, bootbox, e, Cookies */

var inicio = {
	// metodos
	init : function() {
		// Inicialización de propiedades

		//Funcionalidad
		inicio.cargaAvisoPrivacidad();
	},
	cargaAvisoPrivacidad : function() {
		$.ajax({
			type : 'GET',
			url : general.base_url + '/inicio/obtenerAvisoPrivacidad',
			contentType : 'application/json; charset=utf-8',
			dataType : 'json',
			beforeSend : function(xhr) {
				general.block();
			},
			success : function(resultado) {
				if (resultado.estatus === "success") {
					bootbox.dialog({
						title : 'AVISO DE PRIVACIDAD',
						message : resultado.datos,
						size: 'large',
						buttons: {
							ok: {
								label: "Aceptar",
								className: 'btn-info'
							}
						}
					}).on('shown.bs.modal', function() {
						$('.btn-info:first').focus();
					});
				}
			},
			error : function() {
				general.unblock();

				setTimeout(function() {
					general.notify('<strong>Ocurrió un error</strong><br />', 'Ocurrió un error en la petición al servidor al obtener Aviso de Privacidad.', 'danger', true);
				}, 500);
			},
			complete : function() {
				general.unblock();
			}
		});
	}
};
inicio.init();