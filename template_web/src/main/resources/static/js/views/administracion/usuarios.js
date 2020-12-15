/* global general, bootbox, e, Cookies */

var usuarios = {
        btnAgregarUsuario: null,
        tblUsuarios: null,
        modalUsuario: null,
        frmUsuario: null,
        fvUsuario: null,
        txtUsuario: null,
        txtNombre: null,
        txtCorreo: null,
        idUsuario: null,
        cmbUsuario: null,
        tblRoles: null,
        frmRol: null,
        fvRol: null,
        cmbRol: null,

        // metodos
        init: function () {
            // Inicialización de propiedades
            this.tblUsuarios = $('#tblUsuarios');
            this.btnAgregarUsuario = $('#btnAgregarUsuario');

            // Funcionalidad
            this.tblUsuarios.bootstrapTable({
                url: general.base_url + '/administracion/usuarios/obtenerTodos'
            });

            this.tblUsuarios.on('load-success.bs.table', function (e, data) {
                if (!usuarios.tblUsuarios.is(':visible')) {
                    usuarios.tblUsuarios.show();
                    usuarios.tblUsuarios.bootstrapTable('resetView');
                }
                general.unblock();
            });

            this.tblUsuarios.on('sort.bs.table', function (e, row) {
                if (usuarios.tblUsuarios.bootstrapTable('getOptions').totalRows > 0) {
                    general.block();
                }
            });

            this.tblUsuarios.on('page-change.bs.table', function (e, row) {
                if (usuarios.tblUsuarios.bootstrapTable('getOptions').totalRows > 0) {
                    general.block();
                }
            });

            this.tblUsuarios.on('search.bs.table', function (e, row) {
                if (usuarios.tblUsuarios.bootstrapTable('getOptions').totalRows > 0) {
                    general.block();
                }
            });

            this.tblUsuarios.bootstrapTable({}).on('refresh.bs.table', function (e, row) {
                if (usuarios.tblUsuarios.bootstrapTable('getOptions').totalRows > 0) {
                    general.block();
                }
            });

            this.tblUsuarios.on('load-error.bs.table', function (e, status) {
                general.unblock();
                general.notify('Ocurrió un error al recuperar la información de los usuarios ' + status, 'error');
            });

            this.btnAgregarUsuario.click(function () {
                usuarios.cargaFrmUsuario();
            });
        },
        cargaFrmUsuario: function (datosIn) {
            var datos = (datosIn !== undefined) ? $.parseJSON($.base64.decode(datosIn)) : null;
            usuarios.idUsuario = (datos !== null) ? parseInt($.trim(datos.id)) : 0;
            $.ajax({
                type: 'GET',
                url: general.base_url + '/administracion/usuarios/usuario',
                data: datos,
                contentType: 'application/html; charset=utf-8',
                beforeSend: function (xhr) {
                    general.block();
                },
                success: function (resultado) {
                    try {
                        usuarios.modalUsuario = bootbox.dialog({
                            title: 'Guardar Usuario',
                            onEscape: true,
                            animate: true,
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
                                        usuarios.fvUsuario.validate().then(function(status) {
                                            if(status === 'Valid'){
                                                if (usuarios.txtCorreo.val().includes("@geq.net")) {
                                                    general.notify('Direccion de correo invalida.', 'warning');
                                                    usuarios.txtCorreo.val('');
                                                    usuarios.frmUsuario.formValidation('resetField', 'txtCorreo');
                                                } else {
                                                    if (usuarios.tblRoles.bootstrapTable('getOptions').totalRows > 0) {
                                                        usuarios.guardarUsuario();
                                                        usuarios.modalUsuario.modal('hide');
                                                    } else {
                                                        general.notify('Debes seleccionar los roles del usuario.', 'warning');
                                                    }
                                                }  
                                            } 
                                        });
                                        return false;
                                    }
                                }
                            }
                        }).on('shown.bs.modal', function () {
                            $('.bootbox-close-button').focus();

                            setTimeout(function () {
                                $('.bootbox-close-button').focusout();
                            }, 100);

                            // Inicialización
                            if(usuarios.idUsuario !== 0){
                                $('#cmbUsuario').prop('readonly', true);
                            }

                            usuarios.frmUsuario = $('#frmUsuario');
                            usuarios.txtUsuario = $('#txtUsuario');
                            usuarios.cmbUsuario = $('#cmbUsuario');
                            usuarios.txtNombre = $('#txtNombre');
                            usuarios.txtCorreo = $('#txtCorreo');

                            usuarios.tblRoles = $('#tblRoles');
                            usuarios.cmbRol = $('#cmbRol');
                            usuarios.frmRol = $('#frmRol');

                            // Funcionalidad
// usuarios.frmUsuario.formValidation({excluded: [':disabled', ':hidden',
// ':not(:visible)'], live: 'enabled', locale: 'es_ES'})
// .on('success.form.fv', function (e) {
// e.preventDefault();
// if (usuarios.txtCorreo.val().includes("@geq.net")) {
// general.notify('Direccion de correo invalida.', 'warning');
// usuarios.txtCorreo.val('');

// usuarios.frmUsuario.formValidation('resetField', 'txtCorreo');
// } else {
// if (usuarios.tblRoles.bootstrapTable('getOptions').totalRows > 0) {
// usuarios.guardarUsuario();
// usuarios.modalUsuario.modal('hide');
// } else {
// general.notify('Debes seleccionar los roles del usuario.', 'warning');
// }
// }
// });

                            usuarios.cargaRoles();

                            usuarios.cmbUsuario.select2({
                                theme: 'bootstrap4',
                                placeholder: 'Ingresa un usuario valido',
                                // language: 'es'," +
                                language: {
                                    errorLoading: function () {
                                        return "Ingrese un usuario correcto";
                                    },
                                    noResults: function() {
                                        return "No hay resultado";
                                    },
                                    searching: function() {
                                        return "Buscando..";
                                    }
                                },
                                dropdownParent: usuarios.modalUsuario, // solo
                                // modal
                                ajax: {
                                    url: general.base_url + '/administracion/usuarios/obtenerUsuarioLdap',
                                    type: 'GET',
                                    contentType: 'application/json; charset=utf-8',
                                    quietmillis : 250 ,
                                    delay: 25,
                                    data: function (params) {
                                        return {
                                            txtUsuario: params.term,
                                        };
                                    },
                                    processResults: function (data, params) {
                                        var datosNomina = [];

                                        if(data.datos !== null){
                                            datosNomina= [data.datos];
                                        }

                                        return {
                                            results: $.map(datosNomina, function (item) {
                                                return {
                                                    id: item.cuenta + '|' + item.nombre + '|' + item.apellido + '|' + item.correo+ '|' + item.secretaria+ '|' + item.direccion,
                                                    text: item.cuenta
                                                };
                                            })
                                        };
                                    },
                                    cache: true
                                },
                                escapeMarkup: function (markup) {
                                    return markup;
                                }
                            }).on('change.select2', function() {
                                // Revalidate the color field when an option is
                                // chosen
                                usuarios.fvUsuario.revalidateField('cmbUsuario');
                                usuarios.fvUsuario.revalidateField('txtNombre');
                                usuarios.fvUsuario.revalidateField('txtCorreo');
                            });

                            usuarios.fvUsuario = FormValidation.formValidation(document.getElementById('frmUsuario'),
                                    {
                                locale: 'es_ES',
                                localization: FormValidation.locales.es_ES,
                                plugins: {
                                    declarative: new FormValidation.plugins.Declarative({html5Input: true,}),
                                    excluded: new FormValidation.plugins.Excluded(),
                                    fieldStatus: new FormValidation.plugins.FieldStatus({
                                        onStatusChanged: function(areFieldsValid) {
                                            areFieldsValid ? $('.btn-dialog-success').prop('disabled', false) : $('.btn-dialog-success').prop('disabled', true);
                                        }
                                    }),
                                },
                                    },

                            );

                            usuarios.cmbUsuario.change(function () {
                                usuarios.llenarCamposUsuario($(this).val());
                            });

                            usuarios.tblRoles.bootstrapTable({
                                uniqueId: 'id'
                            });
                            usuarios.tblRoles.bootstrapTable('resetView');

                            usuarios.fvRol = FormValidation.formValidation(document.getElementById('frmRol'),
                                    {
                                locale: 'es_ES',
                                localization: FormValidation.locales.es_ES,
                                plugins: {
                                    declarative: new FormValidation.plugins.Declarative({html5Input: true,}),
                                    excluded: new FormValidation.plugins.Excluded(),
                                    submitButton: new FormValidation.plugins.SubmitButton(),
                                    fieldStatus: new FormValidation.plugins.FieldStatus({
                                        onStatusChanged: function(areFieldsValid) {
                                            (usuarios.tblRoles.bootstrapTable('getOptions').totalRows > 0) ? $('.btn-dialog-success').prop('disabled', false) : $('.btn-dialog-success').prop('disabled', true);
                                        }
                                    }),
                                },
                                    },
                            ) .on('core.form.valid', function() {
                                usuarios.agregarRol($.trim(usuarios.cmbRol.val()));
                            });
                            
                            

                            if (usuarios.idUsuario > 0) {
                                var optionUser = new Option(datos.usuario, datos.usuario);
                                optionUser.selected = true;
                                usuarios.cmbUsuario.append(optionUser);

                                usuarios.cargaTablaRoles();
                            } else {
                                general.unblock();
                            }
                        });
                    } catch (e) {
                        setTimeout(function () {
                            general.notify('Ocurrió un error al cargar la página de usuario: ' + e + '.', 'error');
                        }, 500);
                    }
                },
                error: function () {
                    general.unblock();
                    setTimeout(function () {
                        general.notify('Ocurrió un error en la petición al servidor al cargar la página de usuario.', 'error');
                    }, 500);
                }
            });
        },
        cargaRoles : function() {
            $.ajax({
                type : 'GET',
                url : general.base_url+ '/administracion/roles/obtenerRoles',
                contentType : 'application/json; charset=utf-8',
                success : function(resultado) {
                    try {
                        if (resultado.estatus === 'success') {
                            $.each(resultado.datos, function(i, item) {
                                usuarios.cmbRol.append('<option value="' + $.trim(item.id) + '|' + $.trim(item.rol) + '|' + $.trim(item.descripcion) + '" >' + $.trim(item.rol) + '</option>');
                            });
                        } else {
                            general.notify(resultado.mensaje, resultado.estatus);
                        }
                    } catch (e) {
                        setTimeout(function() {
                            general.notify('Ocurrió un error al cargar los Roles: ' + e + '.', 'error');
                        }, 500);
                    }
                },
                error : function() {
                    general.unblock();
                    setTimeout(function() {
                        general.notify('Ocurrió un error en la petición al servidor al cargar los Roles.', 'error');
                    }, 500);
                },
                complete : function() {
                    general.unblock();
                }
            });
        },
        llenarCamposUsuario : function(data) {
            usuarios.txtUsuario.val(data.split('|')[0]);
            usuarios.txtNombre.val(data.split('|')[1] + ' ' + data.split('|')[2]);
            usuarios.txtCorreo.val(data.split('|')[3]);

        },
        agregarRol: function (datos) {
            var datosRol = datos.split('|');

            if (usuarios.tblRoles.bootstrapTable('getRowByUniqueId', datosRol[0]) === null) {
                usuarios.tblRoles.bootstrapTable('insertRow', {
                    index: datosRol[0],
                    row: {
                        id: datosRol[0],
                        rol: datosRol[1],
                        descripcion: datosRol[2]
                    }
                });

                usuarios.cmbRol.val('').trigger('change');
                usuarios.fvRol.resetField(usuarios.cmbRol.attr('name'), true);
            } else {
                general.notify('El rol seleccionado ya se ha agregado previamente.', 'warning');
            }
        },
        quitarRol: function (idIn) {
            var id = $.base64.decode(idIn);
            var datos = usuarios.tblRoles.bootstrapTable('getRowByUniqueId', id);

            if (datos !== null) {
                usuarios.tblRoles.bootstrapTable('removeByUniqueId', id);
            } else {
                general.notify('El rol seleccionado no se ha agregado previamente.', 'warning');
            }
        },
        cargaTablaRoles: function () {
            $.ajax({
                type: 'GET',
                url: general.base_url + '/administracion/usuarios/obtenerRoles',
                data: {txtIdUsuario: usuarios.idUsuario},
                contentType: 'application/json; charset=utf-8',
                dataType : 'json',
                success: function (resultado) {
                    try {
                        if (resultado.estatus === 'success') {
                            var datos = resultado.datos;

                            usuarios.tblRoles.bootstrapTable({});
                            usuarios.tblRoles.bootstrapTable('load', datos);
                            usuarios.tblRoles.bootstrapTable('selectPage', 1);
                        } else {
                            usuarios.tblRoles.bootstrapTable({});
                            general.notify(resultado.mensaje, resultado.estatus);
                        }
                        usuarios.tblRoles.bootstrapTable('resetView');
                    } catch (e) {
                        setTimeout(function () {
                            general.notify('Ocurrió un error al cargar los roles: ' + e + '.', 'error');
                        }, 500);
                    }
                },
                error: function () {
                    general.unblock();
                    setTimeout(function () {
                        general.notify('Ocurrió un error en la petición al servidor al cargar los roles.', 'error');
                    }, 500);
                },
                complete: function () {
                    general.unblock();
                }
            });
        },
        guardarUsuario: function (resguardo) {
            var data = {
                    id: ($.trim(usuarios.idUsuario) !== '0') ? $.trim(usuarios.idUsuario) : null,
                            usuario: $.trim(usuarios.txtUsuario.val()),
                            nombre: $.trim(usuarios.txtNombre.val()),
                            correo: $.trim(usuarios.txtCorreo.val()),
                            roles: usuarios.tblRoles.bootstrapTable('getData')
            };

            $.ajax({
                type: 'POST',
                url: general.base_url + '/administracion/usuarios/usuario',
                data: JSON.stringify(data),
                contentType: 'application/json; charset=utf-8',
                dataType: 'json',
                beforeSend: function (xhr) {
                    general.block();
                    xhr.setRequestHeader('X-XSRF-TOKEN', Cookies.get('XSRF-TOKEN', {path: '/template'}));
                },
                success: function (resultado) {
                    try {
                        if (resultado.estatus === 'success') {
                            usuarios.tblUsuarios.bootstrapTable('refresh');
                        }
                        general.notify(resultado.mensaje, resultado.estatus);
                    } catch (e) {
                        setTimeout(function () {
                            general.notify('Ocurrió un error al guardar el usuario: ' + e + '.', 'error');
                        }, 500);
                    }
                },
                error: function () {
                    general.unblock();
                    setTimeout(function () {
                        general.notify('Ocurrió un error en la petición al servidor al guardar el usuario.', 'error');
                    }, 500);
                },
                complete: function () {
                    general.unblock();
                }
            });
        },
        borrarUsuario: function (idIn) {
            var id = (idIn !== undefined) ? $.base64.decode(idIn) : null;

            bootbox.confirm({
                message: "¿Deseas borrar el usuario?",
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
                            url: general.base_url + '/administracion/usuarios/usuario/' + id,
                            contentType: 'application/json; charset=utf-8',
                            beforeSend: function (xhr) {
                                general.block();
                                xhr.setRequestHeader('X-XSRF-TOKEN', general.obtenerCookie('XSRF-TOKEN'));
                            },
                            success: function (resultado) {
                                try {
                                    if (resultado.estatus === 'success') {
                                        usuarios.tblUsuarios.bootstrapTable('refresh');
                                    }
                                    general.notify(resultado.mensaje, resultado.estatus);
                                } catch (e) {
                                    setTimeout(function () {
                                        general.notify('Ocurrió un error al borrar el usuario: ' + e + '.', 'error');
                                    }, 500);
                                }
                            },
                            error: function () {
                                general.unblock();
                                setTimeout(function () {
                                    general.notify('Ocurrió un error en la petición al servidor al borrar el usuario.', 'error');
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
                '&nbsp;<a class="btn btn-warning btn-sm text-white" role="button" href="javascript:usuarios.cargaFrmUsuario(\'' + $.base64.encode(JSON.stringify(row)) + '\');" data-toggle="tooltip" data-placement="top" title="Editar Usuario"><i class="far fa-edit"></i></i></i></i></a>',
                '&nbsp;<a class="btn btn-danger btn-sm" role="button" href="javascript:usuarios.borrarUsuario(\'' + $.base64.encode(row.id) + '\');" data-toggle="tooltip" data-placement="top" title="Borrar Usuario"><i class="far fa-trash-alt"></i></i></a>'
                ].join('');
        },
        rolesFormatter: function (value, row, index) {
            return [

                '&nbsp;<a class="btn btn-danger btn-sm" role="button" href="javascript:usuarios.quitarRol(\'' + $.base64.encode(row.id) + '\');" data-toggle="tooltip" data-placement="top" title="Borrar Rol"><i class="far fa-trash-alt"></i></i></a>'
                ].join('');
        }
};

usuarios.init();
