/* global general, bootbox, e, Cookies */

var roles = {
        tblRoles: null,
        btnAgregarRol: null,
        modalRol: null,
        frmRol: null,
        fvRol: null,
        idRol: null,
        tblRecursos: null,
        frmRecurso: null,
        fvRecurso: null,
        cmbTxtRecurso: null,

        // metodos
        init: function () {
            // Inicialización de propiedades
            this.tblRoles = $('#tblRoles');
            this.btnAgregarRol = $('#btnAgregarRol');

            // Funcionalidad
            this.tblRoles.bootstrapTable({
                url: general.base_url + '/administracion/roles/obtenerTodos'
            });

            this.tblRoles.on('load-success.bs.table', function (e, data) {
                if (!roles.tblRoles.is(':visible')) {
                    roles.tblRoles.show();
                    roles.tblRoles.bootstrapTable('resetView');
                }

                general.unblock();
            });

            this.tblRoles.on('sort.bs.table', function (e, row) {
                if (roles.tblRoles.bootstrapTable('getOptions').totalRows > 0) {
                    general.block();
                }
            });

            this.tblRoles.on('page-change.bs.table', function (e, row) {
                if (roles.tblRoles.bootstrapTable('getOptions').totalRows > 0) {
                    general.block();
                }
            });

            this.tblRoles.on('search.bs.table', function (e, row) {
                if (roles.tblRoles.bootstrapTable('getOptions').totalRows > 0) {
                    general.block();
                }
            });

            this.tblRoles.bootstrapTable({}).on('refresh.bs.table', function (e, row) {
                if (roles.tblRoles.bootstrapTable('getOptions').totalRows > 0) {
                    general.block();
                }
            });

            this.tblRoles.on('load-error.bs.table', function (e, status) {
                general.unblock();
                general.notify('Ocurrió un error al recuperar la información de los roles ' + status, 'error');
            });

            this.btnAgregarRol.click(function () {
                roles.cargaFrmRol();
            });
        },
        cargaFrmRol: function (datosIn) {
            var datos = (datosIn !== undefined) ? $.parseJSON($.base64.decode(datosIn)) : null;
            roles.idRol = (datos !== null) ? parseInt($.trim(datos.id)) : 0;
            $.ajax({
                type: 'GET',
                url: general.base_url + '/administracion/roles/rol',
                data: datos,
                contentType: 'application/html; charset=utf-8',
                beforeSend: function (xhr) {
                    general.block();
                },
                success: function (resultado) {
                    try {
                        roles.modalRol = bootbox.dialog({
                            title: 'Guardar Rol',
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
                                        // roles.frmRol.submit();
                                        roles.fvRol.validate().then(function(status) {
                                            if (roles.tblRecursos.bootstrapTable('getOptions').totalRows > 0) {
                                              roles.guardarRol();
                                              roles.modalRol.modal('hide');
                                              } else {

                                              general.notify('Debes seleccionar los recursos del rol.', 'warning');
                                              }
                                        });
                                        return false;
                                    }
                                }
                            }
                        });

                        roles.modalRol.on('shown.bs.modal', function () {
                            $('.bootbox-close-button').focus();

                            setTimeout(function () {
                                $('.bootbox-close-button').focusout();
                            }, 100);

                            if(roles.idRol!==0){
                                $('#txtRol').prop('readonly', true);

                            }
                            // Inicialización
                            roles.frmRol = $('#frmRol');
                            roles.tblRecursos = $('#tblRecursos');
                            roles.frmRecurso = $('#frmRecurso');
                            roles.cmbTxtRecurso = $('#cmbTxtRecurso');

                            // Funcionalidad
// roles.frmRol.formValidation({excluded: [':disabled', ':hidden',
// ':not(:visible)'], live: 'enabled', locale: 'es_ES'})
// .on('success.form.fv', function (e) {
// e.preventDefault();
// if (roles.tblRecursos.bootstrapTable('getOptions').totalRows > 0) {
// roles.guardarRol();
// roles.modalRol.modal('hide');
// } else {
// general.notify('<strong>Alerta</strong><br />', 'Debes seleccionar los
// recursos del rol.', 'warning', true);
// }
// });

                            roles.fvRol = FormValidation.formValidation(document.getElementById('frmRol'),
                                    {
                                locale: 'es_ES',
                                localization: FormValidation.locales.es_ES,
                                plugins: {
                                    declarative: new FormValidation.plugins.Declarative({html5Input: true,}),
                                    excluded: new FormValidation.plugins.Excluded(),
                                    fieldStatus: new FormValidation.plugins.FieldStatus({
                                        onStatusChanged: function(areFieldsValid) {
                                            ($('#txtRol').val().length > 0 && $('#txtDescripcion').val().length > 0)  ? $('.btn-dialog-success').prop('disabled', false) : $('.btn-dialog-success').prop('disabled', true);
                                        }
                                    }),
                                },
                                    },

                            );


                            roles.cargaRecursos();

                            roles.tblRecursos.bootstrapTable({
                                uniqueId: 'id'
                            });
                            roles.tblRecursos.bootstrapTable('resetView');

// roles.frmRecurso.formValidation({excluded: [':disabled'], live: 'enabled',
// locale: 'es_ES'})
// .on('success.form.fv', function (e) {
// e.preventDefault();

// roles.agregarRecurso($.trim(roles.cmbTxtRecurso.val()));
// });
                            roles.fvRecurso = FormValidation.formValidation(document.getElementById('frmRecurso'),
                                    {
                                locale: 'es_ES',
                                localization: FormValidation.locales.es_ES,
                                plugins: {
                                    declarative: new FormValidation.plugins.Declarative({html5Input: true,}),
                                    excluded: new FormValidation.plugins.Excluded(),
                                    submitButton: new FormValidation.plugins.SubmitButton(),
                                    fieldStatus: new FormValidation.plugins.FieldStatus({
                                        onStatusChanged: function(areFieldsValid) {
                                            (roles.tblRecursos.bootstrapTable('getOptions').totalRows > 0) 
                                            ? $('.btn-dialog-success').prop('disabled', false) 
                                                    : $('.btn-dialog-success').prop('disabled', true);
                                        }
                                    }),
                                },
                                    },

                            ).on('core.form.valid', function() {
                                roles.agregarRecurso($.trim(roles.cmbTxtRecurso.val()));
                            });

                            if (roles.idRol > 0) {
                                roles.cargaTablaRecursos();
                            }
                        });
                    } catch (e) {
                        setTimeout(function () {
                            general.notify('Ocurrió un error al cargar la página de rol: ' + e + '.', 'error');
                        }, 500);
                    }
                },
                error: function () {
                    general.unblock();
                    setTimeout(function () {
                        general.notify('Ocurrió un error en la petición al servidor al cargar la página de rol.', 'error');
                    }, 500);
                },
                complete: function () {
                    if (roles.idRol === 0) {
                        general.unblock();
                    }
                }
            });
        },
        cargaRecursos: function () {
            roles.cmbTxtRecurso.select2({
                theme: 'bootstrap4',
                placeholder: 'Selecciona una opción',
                language: 'es',
                dropdownParent: roles.modalRol, // solo modal
                ajax: {
                    url: general.base_url + '/administracion/recursos/obtenerPorRecurso',
                    type: 'GET',
                    contentType: 'application/json; charset=utf-8',
                    delay: 25,
                    data: function (params) {
                        return {
                            txtRecurso: params.term,
                        };
                    },
                    processResults: function (data, params) {
                        return {
                            results: $.map(data.datos, function (item) {
                                return {
                                    id: item.id + '|' + item.recurso + '|' + item.descripcion,
                                    text: item.recurso
                                };
                            })
                        };
                    },
                    cache: true
                },
                escapeMarkup: function (markup) {
                    return markup;
                },
                minimumInputLength: 2
            });
        },
        cargaTablaRecursos: function () {
            $.ajax({
                type: 'GET',
                url: general.base_url + '/administracion/roles/obtenerRecursos',
                data: {txtIdRol: roles.idRol},
                contentType: 'application/json; charset=utf-8',
                beforeSend: function (xhr) {
                    general.block();
                },
                success: function (resultado) {
                    try {
                        if (resultado.estatus === 'success') {
                            roles.tblRecursos.bootstrapTable({});
                            roles.tblRecursos.bootstrapTable('load', resultado.datos);
                            roles.tblRecursos.bootstrapTable('selectPage', 1);
                        } else {
                            roles.tblRecursos.bootstrapTable({});
                            general.notify(resultado.mensaje, resultado.estatus);
                        }

                        roles.tblRecursos.bootstrapTable('resetView');
                    } catch (e) {
                        setTimeout(function () {
                            general.notify('Ocurrió un error carga de los recursos: ' + e + '.', 'error');
                        }, 500);
                    }
                },
                error: function () {
                    general.unblock();
                    setTimeout(function () {
                        general.notify('Ocurrió un error en la petición al servidor al cargar los recursos.', 'error');
                    }, 500);
                },
                complete: function () {
                    general.unblock();
                }
            });
        },
        guardarRol: function () {
            var form = roles.frmRol.serializeArray();

            var data = {
                    id: form[0]['value'],
                    rol: form[1]['value'],
                    descripcion: form[2]['value'],
                    recursos: roles.tblRecursos.bootstrapTable('getData')
            };

            $.ajax({
                type: 'POST',
                url: general.base_url + '/administracion/roles/rol',
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
                            roles.tblRoles.bootstrapTable('refresh');
                        }

                        general.notify(resultado.mensaje, resultado.estatus);
                    } catch (e) {
                        setTimeout(function () {
                            general.notify('Ocurrió un error al guardar el rol: ' + e + '.', 'error');
                        }, 500);
                    }
                },
                error: function () {
                    general.unblock();
                    setTimeout(function () {
                        general.notify('Ocurrió un error en la petición al servidor al guardar el rol.', 'error');
                    }, 500);
                },
                complete: function () {
                    general.unblock();
                }
            });
        },
        borrarRol: function (idIn) {
            var id = (idIn !== undefined) ? $.base64.decode(idIn) : null;
            bootbox.confirm({
                message: "¿Deseas borrar el rol?",
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
                            url: general.base_url + '/administracion/roles/rol/' + id,
                            contentType: 'application/json; charset=utf-8',
                            beforeSend: function (xhr) {
                                general.block();
                                xhr.setRequestHeader('X-XSRF-TOKEN', general.obtenerCookie('XSRF-TOKEN'));
                            },
                            success: function (resultado) {
                                try {
                                    if (resultado.estatus === 'success') {
                                        roles.tblRoles.bootstrapTable('refresh');
                                    }

                                    general.notify(resultado.mensaje, resultado.estatus);
                                } catch (e) {
                                    setTimeout(function () {
                                        general.notify('Ocurrió un error al borrar el rol: ' + e + '.', 'error');
                                    }, 500);
                                }
                            },
                            error: function () {
                                general.unblock();
                                setTimeout(function () {
                                    general.notify('Ocurrió un error en la petición al servidor al borrar el rol.', 'error');
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
        agregarRecurso: function (datos) {
            var datosRecurso = datos.split("|");

            if (roles.tblRecursos.bootstrapTable('getRowByUniqueId', datosRecurso[0]) === null) {
                roles.tblRecursos.bootstrapTable('insertRow', {
                    index: datosRecurso[0],
                    row: {
                        id: datosRecurso[0],
                        recurso: datosRecurso[1],
                        descripcion: datosRecurso[2]
                    }
                });

                roles.cmbTxtRecurso.val('').trigger('change');
                roles.fvRecurso.resetField(roles.cmbTxtRecurso.attr('name'), true);
            } else {
                general.notify('El recurso seleccionado ya se ha agregado previamente.', 'warning');
            }

        },
        quitarRecurso: function (id) {
            id = $.base64.decode(id);

            if (roles.tblRecursos.bootstrapTable('getRowByUniqueId', id) !== null) {
                roles.tblRecursos.bootstrapTable('removeByUniqueId', id);
            } else {
                general.notify('El recurso seleccionado no se ha agregado previamente.', 'warning');
            }
        },
        estatusFormatter: function (value, row, index) {
            return (value === 'AC') ? 'Activo' : 'Inactivo';
        },
        actionFormatter: function (value, row, index) {
            return [
                '&nbsp;<a class="btn btn-warning btn-sm text-white" role="button" href="javascript:roles.cargaFrmRol(\'' + $.base64.encode(JSON.stringify(row)) + '\');" data-toggle="tooltip" data-placement="top" title="Editar Rol"><i class="far fa-edit"></i></a>',
                '&nbsp;<a class="btn btn-danger btn-sm" role="button" href="javascript:roles.borrarRol(\'' + $.base64.encode(row.id) + '\');" data-toggle="tooltip" data-placement="top" title="Borrar Rol"><i class="far fa-trash-alt"></i></a>'
                ].join('');
        },
        recursosFormatter: function (value, row, index) {
            return [
                '&nbsp;<a class="btn btn-danger btn-lg btn-xs" role="button" href="javascript:roles.quitarRecurso(\'' + $.base64.encode(row.id) + '\');" data-toggle="tooltip" data-placement="top" title="Borrar Recurso"><i class="far fa-trash-alt"></i</a>'
                ].join('');
        }
};

roles.init();