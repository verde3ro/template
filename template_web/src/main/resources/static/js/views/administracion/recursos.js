/* global general, bootbox, e, Cookies */

var recursos = {
    tblRecursos: null,
    btnAgregarRecurso: null,
    modalRecurso: null,
    frmRecurso: null,
    fvRecurso: null,
    idRecurso : null,

    // metodos
    init: function () {
        // Inicialización de propiedades
        this.tblRecursos = $('#tblRecursos');
        this.btnAgregarRecurso = $('#btnAgregarRecurso');

        // Funcionalidad
        this.tblRecursos.bootstrapTable({
            url: general.base_url + '/administracion/recursos/obtenerTodos'
        });

        this.tblRecursos.on('load-success.bs.table', function (e, data) {
            if (!recursos.tblRecursos.is(':visible')) {
                recursos.tblRecursos.show();
                recursos.tblRecursos.bootstrapTable('resetView');
            }

            general.unblock();
        });

        this.tblRecursos.on('sort.bs.table', function (e, row) {
            if (recursos.tblRecursos.bootstrapTable('getOptions').totalRows > 0) {
                general.block();
            }
        });

        this.tblRecursos.on('page-change.bs.table', function (e, row) {
            if (recursos.tblRecursos.bootstrapTable('getOptions').totalRows > 0) {
                general.block();
            }
        });

        this.tblRecursos.on('search.bs.table', function (e, row) {
            if (recursos.tblRecursos.bootstrapTable('getOptions').totalRows > 0) {
                general.block();
            }
        });

        this.tblRecursos.bootstrapTable({}).on('refresh.bs.table', function (e, row) {
            if (recursos.tblRecursos.bootstrapTable('getOptions').totalRows > 0) {
                general.block();
            }
        });

        this.tblRecursos.on('load-error.bs.table', function (e, status) {
            general.unblock();
            general.notify('Ocurrió un error al recuperar la información de los recursos ' + status, 'error');
        });

        this.btnAgregarRecurso.click(function () {
            recursos.cargaFrmRecurso();
        });
    },
    cargaFrmRecurso: function (datosIn) {
        var datos = (datosIn !== undefined) ? $.parseJSON($.base64.decode(datosIn)) : null;
        recursos.idRecurso = (datos !== null) ? parseInt($.trim(datos.id)) : 0;

        $.ajax({
            type: 'GET',
            url: general.base_url + '/administracion/recursos/recurso',
            data: datos,
            contentType: 'application/html; charset=utf-8',
            beforeSend: function (xhr) {
                general.block();
            },
            success: function (resultado) {
                try {
                    recursos.modalRecurso = bootbox.dialog({
                        title: 'Guardar Recurso',
                        onEscape: true,
                        animate: true,
                        size: 'smmall',
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
                                    recursos.fvRecurso.validate().then(function(status) {
                                        if(status === 'Valid'){
                                            recursos.guardarRecurso();
                                            recursos.modalRecurso.modal('hide');
                                        }
                                    });
                                    return false;
                                }
                            }
                        }
                    });

                    recursos.modalRecurso.on('shown.bs.modal', function () {
                        $('.bootbox-close-button').focus();

                        setTimeout(function () {
                            $('.bootbox-close-button').focusout();
                        }, 100);


            			if(recursos.idRecurso!==0){
            				 $('#txtRecurso').prop('readonly', true);
            			}
                        // Inicialización
                        recursos.frmRecurso = $('#frmRecurso');

                        // Funcionalidad
// recursos.frmRecurso.formValidation({excluded: [':disabled', ':hidden',
// ':not(:visible)'], live: 'enabled', locale: 'es_ES'})
// .on('success.form.fv', function (e) {
// e.preventDefault();
//
// recursos.guardarRecurso();
// recursos.modalRecurso.modal('hide');
// });
                        recursos.fvRecurso = FormValidation.formValidation(document.getElementById('frmRecurso'),
                                {
                            locale: 'es_ES',
                            localization: FormValidation.locales.es_ES,
                            plugins: {
                                declarative: new FormValidation.plugins.Declarative({html5Input: true,}),
                                excluded: new FormValidation.plugins.Excluded(),
                                submitButton: new FormValidation.plugins.SubmitButton(),
                                fieldStatus: new FormValidation.plugins.FieldStatus({
                                    onStatusChanged: function(areFieldsValid) {
                                        ($('#txtRecurso').val().length > 0 && $('#txtDescripcion').val().length > 0)
                                        ? $('.btn-dialog-success').prop('disabled', false) 
                                                : $('.btn-dialog-success').prop('disabled', true);
                                    }
                                }),
                            },
                                },
                        );
                        
                    });
                } catch (e) {
                    setTimeout(function () {
                        general.notify('Ocurrió un error al cargar la página de recurso: ' + e + '.', 'error');
                    }, 500);
                }
            },
            error: function () {
                general.unblock();
                setTimeout(function () {
                    general.notify('Ocurrió un error en la petición al servidor al cargar la página de recurso.', 'error');
                }, 500);
            },
            complete: function () {
                general.unblock();
            }
        });
    },
    guardarRecurso: function () {
        var form = recursos.frmRecurso.serializeArray();
        var data = {
            id: form[0]['value'],
            recurso: form[1]['value'],
            descripcion: form[2]['value'],
            sesion: form[3]['value']
        };

        $.ajax({
            type: 'POST',
            url: general.base_url + '/administracion/recursos/recurso',
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
                        recursos.tblRecursos.bootstrapTable('refresh');
                    }

                    general.notify(resultado.mensaje, resultado.estatus);
                } catch (e) {
                    setTimeout(function () {
                        general.notify('Ocurrió un error al guardar el recurso: ' + e + '.', 'error');
                    }, 500);
                }
            },
            error: function () {
                general.unblock();
                setTimeout(function () {
                    general.notify('Ocurrió un error en la petición al servidor al guardar el recurso.', 'error');
                }, 500);
            },
            complete: function () {
                general.unblock();
            }
        });
    },
    borrarRecurso: function (idIn) {
        var id = (idIn !== undefined) ? $.base64.decode(idIn) : null;
        bootbox.confirm({
            message: "¿Deseas borrar el recurso?",
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
                        url: general.base_url + '/administracion/recursos/recurso/' + id,
                        contentType: 'application/json; charset=utf-8',
                        beforeSend: function (xhr) {
                            general.block();
                            xhr.setRequestHeader('X-XSRF-TOKEN', general.obtenerCookie('XSRF-TOKEN'));
                        },
                        success: function (resultado) {
                            try {
                                if (resultado.estatus === 'success') {
                                    recursos.tblRecursos.bootstrapTable('refresh');
                                }

                                general.notify(resultado.mensaje, resultado.estatus);
                            } catch (e) {
                                setTimeout(function () {
                                    general.notify('Ocurrió un error al borrar el recurso: ' + e + '.', 'error');
                                }, 500);
                            }
                        },
                        error: function () {
                            general.unblock();
                            setTimeout(function () {
                                general.notify('Ocurrió un error en la petición al servidor al borrar el recurso.', 'error');
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
    sesionFormatter: function (value, row, index) {
        return (parseInt(value) === 1) ? 'Con sesión' : 'Sin sesión';
    },
    actionFormatter: function (value, row, index) {
        return [
            '&nbsp;<a class="btn btn-warning btn-sm text-white" role="button" href="javascript:recursos.cargaFrmRecurso(\'' + $.base64.encode(JSON.stringify(row)) + '\');" data-toggle="tooltip" data-placement="top" title="Editar Recurso"><i class="far fa-edit"></i></a>',
            '&nbsp;<a class="btn btn-danger btn-sm" role="button" href="javascript:recursos.borrarRecurso(\'' + $.base64.encode(row.id) + '\');" data-toggle="tooltip" data-placement="top" title="Borrar Recurso"><i class="far fa-trash-alt"></i></a>'
        ].join('');
    }
};

recursos.init();