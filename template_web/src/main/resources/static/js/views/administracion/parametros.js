/* global general, bootbox, e, Cookies */

var parametros = {
	tblParametros: null,
	btnAgregarParametro: null,
	modalRecurso: null,
	frmParametro: null,
	fvParametro: null,
	idParametro: null,

	// metodos
	init: function () {
		// Inicialización de propiedades
		this.tblParametros = $('#tblParametros');
		this.btnAgregarParametro = $('#btnAgregarParametro');
	
		// Funcionalidad
		this.tblParametros.bootstrapTable({
			url: general.base_url + '/administracion/parametros/obtenerTodos'
		});
	
		this.tblParametros.on('load-success.bs.table', function (e, data) {
			if (!parametros.tblParametros.is(':visible')) {
				parametros.tblParametros.show();
				parametros.tblParametros.bootstrapTable('resetView');
			}
			general.unblock();
		});
	
		this.tblParametros.on('sort.bs.table', function (e, row) {
			if (parametros.tblParametros.bootstrapTable('getOptions').totalRows > 0) {
				general.block();
			}
		});
	
		this.tblParametros.on('page-change.bs.table', function (e, row) {
			if (parametros.tblParametros.bootstrapTable('getOptions').totalRows > 0) {
				general.block();
			}
		});
	
		this.tblParametros.on('search.bs.table', function (e, row) {
			if (parametros.tblParametros.bootstrapTable('getOptions').totalRows > 0) {
				general.block();
			}
		});
	
		this.tblParametros.bootstrapTable({}).on('refresh.bs.table', function (e, row) {
			if (parametros.tblParametros.bootstrapTable('getOptions').totalRows > 0) {
				general.block();
			}
		});
	
		this.tblParametros.on('load-error.bs.table', function (e, status) {
			general.unblock();
			general.notify('Ocurrió un error al recuperar la información de los parametros ' + status, 'error');
		});
	
		this.btnAgregarParametro.click(function () {
			parametros.cargaFrmParametro();
		});
	
	},
	cargaFrmParametro: function (datosIn) {
		var datos = (datosIn !== undefined) ? $.parseJSON($.base64.decode(datosIn)) : null;
		parametros.idParametro = (datos !== null) ? parseInt($.trim(datos.id)) : 0;

		$.ajax({
			type: 'GET',
			url: general.base_url + '/administracion/parametros/parametro',
			data: datos,
			contentType: 'application/html; charset=utf-8',
			beforeSend: function (xhr) {
				general.block();
			},
			success: function (resultado) {
				try {
					parametros.modalRecurso = bootbox.dialog({
						title: 'Guardar Parámetro',
						onEscape: true,
						animate: true,
						size: 'large',
						message: resultado,
						buttons: {
							cancel: {
								label: 'Cancelar',
								className: 'btn-default'
							},
							save: {
								label: 'Guardar',
								className: 'btn-success btn-dialog-success',
								callback: function () {
									parametros.fvParametro.validate().then(function(status) {
										parametros.guardarParametro();
										parametros.modalRecurso.modal('hide');
									});
									return false;
								}
							}
						}
					});
		
					parametros.modalRecurso.on('shown.bs.modal', function () {
						$('.bootbox-close-button').focus();
							setTimeout(function () {
							$('.bootbox-close-button').focusout();
						}, 100);
							
						if(parametros.idParametro !== 0){
							$('#txtNombre').prop('readonly', true);
						}
	
						// Inicialización
						parametros.frmParametro= $("#frmParametro");
					
						// Funcionalidad
						parametros.fvParametro = FormValidation.formValidation(document.getElementById('frmParametro'),
							{
								locale: 'es_ES',
								localization: FormValidation.locales.es_ES,
								plugins: {
									declarative: new FormValidation.plugins.Declarative({html5Input: true,}),
									excluded: new FormValidation.plugins.Excluded(),
									fieldStatus: new FormValidation.plugins.FieldStatus({
										onStatusChanged: function(areFieldsValid) {
											($('#txtNombre').val().length > 0 && $('#txtValor').val().length > 0) ? $('.btn-dialog-success').prop('disabled', false) : $('.btn-dialog-success').prop('disabled', true);
										}
									}),
								},
							},
						);
					});
				} catch (e) {
					setTimeout(function () {
						general.notify('Ocurrió un error al cargar la página de parámetro: ' + e + '.', 'error');
					}, 500);
				}
			},
			error: function () {
				general.unblock();
				setTimeout(function () {
					general.notify('Ocurrió un error en la petición al servidor al cargar la página de parámetro.', 'error');
				}, 500);
			},
			complete: function () {
				general.unblock();
			}
		});
	},
	guardarParametro: function () { 
		var form = parametros.frmParametro.serializeArray();
		var data = {
			id: form[0]['value'],
			nombre: form[1]['value'],
			valor: form[2]['value']
		};

		$.ajax({
			type: 'POST',
			url: general.base_url + '/administracion/parametros/parametro',
			data: JSON.stringify(data),
			contentType: 'application/json; charset=utf-8',
			dataType: 'json',
			beforeSend: function (xhr) {
				general.block();
				xhr.setRequestHeader('X-XSRF-TOKEN', general.obtenerCookie('XSRF-TOKEN'));
			},
			success: function (resultado) {
				try {
					if (resultado.estatus === 'success') {
						parametros.tblParametros.bootstrapTable('refresh');
					}
					general.notify(resultado.mensaje, resultado.estatus);
				} catch (e) {
					setTimeout(function () {
						general.notify('Ocurrió un error al guardar el parámetro: ' + e + '.', 'error');
					}, 500);
				}
			},
			error: function () {
				general.unblock();
				setTimeout(function () {
					general.notify('Ocurrió un error en la petición al servidor al guardar el parámetro.', 'error');
				}, 500);
			},
			complete: function () {
				general.unblock();
			}
		});
	},
	borrarParametro: function (idIn) {
		var id = (idIn !== undefined) ? $.base64.decode(idIn) : null;
		bootbox.confirm({
			message: "¿Deseas borrar el parámetro?",
			buttons: {
				confirm: {
					label: 'Sí',
					className: 'btn-success'
				},
				cancel: {
					label: 'No',
					className: 'btn-danger'
				}
			},
			callback: function (result) {
				if (result) {
					$.ajax({
						type: 'PUT',
						url: general.base_url + '/administracion/parametros/parametro/' + id,
						contentType: 'application/json; charset=utf-8',
						beforeSend: function (xhr) {
							general.block();
							xhr.setRequestHeader('X-XSRF-TOKEN', general.obtenerCookie('XSRF-TOKEN'));
						},
						success: function (resultado) {
							try {
								if (resultado.estatus === 'success') {
									parametros.tblParametros.bootstrapTable('refresh');
								}
								general.notify(resultado.mensaje, resultado.estatus);
							} catch (e) {
								setTimeout(function () {
									general.notify('Ocurrió un error al borrar el parámetro: ' + e + '.', 'error');
								}, 500);
							}
						},
						error: function () {
							general.unblock();
							setTimeout(function () {
								general.notify('Ocurrió un error en la petición al servidor al borrar el parámetro.', 'error');
							}, 500);
						},
						complete: function () {
							general.unblock();
						}
					});
				}
			}
		});
	},
	estatusFormatter: function (value, row, index) {
		return (value === 'AC') ? 'Activo' : 'Inactivo';
	},
	actionFormatter: function (value, row, index) {
		return [
			'&nbsp;<a class="btn btn-warning btn-sm text-white" role="button" href="javascript:parametros.cargaFrmParametro(\'' + $.base64.encode(JSON.stringify(row)) + '\');" data-toggle="tooltip" data-placement="top" title="Editar Parámetro"><i class="far fa-edit"></i></a>',
			'&nbsp;<a class="btn btn-danger btn-sm" role="button" href="javascript:parametros.borrarParametro(\'' + $.base64.encode(row.id) + '\');" data-toggle="tooltip" data-placement="top" title="Borrar Parámetro"><i class="far fa-trash-alt"></i></a>'
			].join('');
	}
};

parametros.init();