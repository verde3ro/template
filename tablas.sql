--------------------------------------------------------
-- Archivo creado  - miércoles-octubre-30-2019   
--------------------------------------------------------
--------------------------------------------------------
--  DDL for Sequence SEQ_PARA
--------------------------------------------------------

   CREATE SEQUENCE  "SEQ_PARA"  MINVALUE 1 MAXVALUE 9999999999999999999999999999 INCREMENT BY 1 START WITH 7 NOCACHE  ORDER  NOCYCLE ;
--------------------------------------------------------
--  DDL for Sequence SEQ_RECU
--------------------------------------------------------

   CREATE SEQUENCE  "SEQ_RECU"  MINVALUE 1 MAXVALUE 9999999999999999999999999999 INCREMENT BY 1 START WITH 3 NOCACHE  ORDER  NOCYCLE ;
--------------------------------------------------------
--  DDL for Sequence SEQ_ROLE
--------------------------------------------------------

   CREATE SEQUENCE  "SEQ_ROLE"  MINVALUE 1 MAXVALUE 9999999999999999999999999999 INCREMENT BY 1 START WITH 3 NOCACHE  ORDER  NOCYCLE ;
--------------------------------------------------------
--  DDL for Sequence SEQ_USUA
--------------------------------------------------------

   CREATE SEQUENCE  "SEQ_USUA"  MINVALUE 1 MAXVALUE 9999999999999999999999999999 INCREMENT BY 1 START WITH 3 NOCACHE  ORDER  NOCYCLE ;
--------------------------------------------------------
--  DDL for Table TC_PARAMETROS
--------------------------------------------------------

  CREATE TABLE "TC_PARAMETROS" 
   (  "ID" NUMBER, 
  "NOMBRE" VARCHAR2(50) DEFAULT '', 
  "VALOR" VARCHAR2(4000) DEFAULT '', 
  "USUARIO_I" VARCHAR2(20) DEFAULT '', 
  "FECHA_I" DATE DEFAULT SYSDATE, 
  "USUARIO_U" VARCHAR2(20), 
  "FECHA_U" DATE, 
  "ESTATUS" VARCHAR2(5) DEFAULT 'AC'
   ) ;

   COMMENT ON COLUMN "TC_PARAMETROS"."ID" IS 'IDENTIFICADOR UNICO CONSECUTIVO';
   COMMENT ON COLUMN "TC_PARAMETROS"."NOMBRE" IS 'NOMBRE DEL PARAMETRO';
   COMMENT ON COLUMN "TC_PARAMETROS"."VALOR" IS 'VALOR CONTENIDO';
   COMMENT ON COLUMN "TC_PARAMETROS"."USUARIO_I" IS 'USUARIO QUE INGRESA EL REGISTRO';
   COMMENT ON COLUMN "TC_PARAMETROS"."FECHA_I" IS 'FECHA DE INSERCION REGISTRO';
   COMMENT ON COLUMN "TC_PARAMETROS"."USUARIO_U" IS 'USUARIO QUE MODIFICA EL REGISTRO';
   COMMENT ON COLUMN "TC_PARAMETROS"."FECHA_U" IS 'FECHA DE MODIFICACION DE REGISTRO';
   COMMENT ON COLUMN "TC_PARAMETROS"."ESTATUS" IS 'ESTATUS DEL REGISTRO(ACTIVO/INACTIVO) ';
   COMMENT ON TABLE "TC_PARAMETROS"  IS 'CATALOGO DE PARAMETROS';
--------------------------------------------------------
--  DDL for Table TC_RECURSOS
--------------------------------------------------------

  CREATE TABLE "TC_RECURSOS" 
   (  "ID" NUMBER DEFAULT NULL, 
  "RECURSO" VARCHAR2(250) DEFAULT '', 
  "DESCRIPCION" VARCHAR2(250) DEFAULT '', 
  "USUARIO_I" VARCHAR2(20) DEFAULT '', 
  "FECHA_I" DATE DEFAULT SYSDATE, 
  "USUARIO_U" VARCHAR2(20) DEFAULT NULL, 
  "FECHA_U" DATE, 
  "ESTATUS" VARCHAR2(5) DEFAULT 'AC',
  "SESION"       NUMBER DEFAULT 1
   ) ;

   COMMENT ON COLUMN "TC_RECURSOS"."ID" IS 'IDENTIFICADOR UNICO CONSECUTIVO';
   COMMENT ON COLUMN "TC_RECURSOS"."RECURSO" IS 'NOMBRE DEL RECURSO';
   COMMENT ON COLUMN "TC_RECURSOS"."DESCRIPCION" IS 'DESCRIPCION EXTENDIDA DEL REGISTRO ';
   COMMENT ON COLUMN "TC_RECURSOS"."USUARIO_I" IS 'USUARIO QUE INGRESA EL REGISTRO';
   COMMENT ON COLUMN "TC_RECURSOS"."FECHA_I" IS 'FECHA DE INSERCION REGISTRO';
   COMMENT ON COLUMN "TC_RECURSOS"."USUARIO_U" IS 'USUARIO QUE MODIFICA EL REGISTRO';
   COMMENT ON COLUMN "TC_RECURSOS"."FECHA_U" IS 'FECHA DE MODIFICACION DE REGISTRO';
   COMMENT ON COLUMN "TC_RECURSOS"."ESTATUS" IS 'ESTATUS DEL REGISTRO(ACTIVO/INACTIVO) ';
   COMMENT ON COLUMN "TC_RECURSOS"."SESION" IS 'IDENTIFICA SI EL RECURSO SE VALIDA CON SESIÓN DE USUARIO (1: SI, 0: NO)';
   COMMENT ON TABLE "TC_RECURSOS"  IS 'CATALOGO DE RECURSOS';
--------------------------------------------------------
--  DDL for Table RECURSOS_ROLES_D
--------------------------------------------------------

  CREATE TABLE "TD_RECURSOS_ROLES" 
   (  "ID_RECURSO" NUMBER DEFAULT 0, 
  "ID_ROL" NUMBER DEFAULT 0, 
  "USUARIO_I" VARCHAR2(20) DEFAULT '', 
  "FECHA_I" DATE DEFAULT SYSDATE, 
  "USUARIO_U" VARCHAR2(20), 
  "FECHA_U" DATE, 
  "ESTATUS" VARCHAR2(5) DEFAULT 'AC'
   ) ;

   COMMENT ON COLUMN "TD_RECURSOS_ROLES"."ID_RECURSO" IS 'IDENTIFICADOR FORANEO DEL RECURSO';
   COMMENT ON COLUMN "TD_RECURSOS_ROLES"."ID_ROL" IS 'IDENTIFICADOR FORANEO DEL ROL';
   COMMENT ON COLUMN "TD_RECURSOS_ROLES"."USUARIO_I" IS 'USUARIO QUE INGRESA EL REGISTRO';
   COMMENT ON COLUMN "TD_RECURSOS_ROLES"."FECHA_I" IS 'FECHA DE INSERCION REGISTRO';
   COMMENT ON COLUMN "TD_RECURSOS_ROLES"."USUARIO_U" IS 'USUARIO QUE MODIFICA EL REGISTRO';
   COMMENT ON COLUMN "TD_RECURSOS_ROLES"."FECHA_U" IS 'FECHA DE MODIFICACION DE REGISTRO';
   COMMENT ON COLUMN "TD_RECURSOS_ROLES"."ESTATUS" IS 'ESTATUS DEL REGISTRO(ACTIVO/INACTIVO) ';
   COMMENT ON TABLE "TD_RECURSOS_ROLES"  IS 'RECURSOS POR ROL';
--------------------------------------------------------
--  DDL for Table TC_ROLES
--------------------------------------------------------

  CREATE TABLE "TC_ROLES" 
   (  "ID" NUMBER DEFAULT NULL, 
  "ROL" VARCHAR2(20) DEFAULT '', 
  "DESCRIPCION" VARCHAR2(250) DEFAULT '', 
  "USUARIO_I" VARCHAR2(20) DEFAULT '', 
  "FECHA_I" DATE DEFAULT SYSDATE, 
  "USUARIO_U" VARCHAR2(20), 
  "FECHA_U" DATE, 
  "ESTATUS" VARCHAR2(5) DEFAULT 'AC'
   ) ;

   COMMENT ON COLUMN "TC_ROLES"."ID" IS 'IDENTIFICADOR UNICO CONSECUTIVO';
   COMMENT ON COLUMN "TC_ROLES"."ROL" IS 'NOMBRE DEL ROL';
   COMMENT ON COLUMN "TC_ROLES"."DESCRIPCION" IS 'IDENTIFICADOR FORANEO DEL ';
   COMMENT ON COLUMN "TC_ROLES"."USUARIO_I" IS 'USUARIO QUE INGRESA EL REGISTRO';
   COMMENT ON COLUMN "TC_ROLES"."FECHA_I" IS 'FECHA DE INSERCION REGISTRO';
   COMMENT ON COLUMN "TC_ROLES"."USUARIO_U" IS 'USUARIO QUE MODIFICA EL REGISTRO';
   COMMENT ON COLUMN "TC_ROLES"."FECHA_U" IS 'FECHA DE MODIFICACION DE REGISTRO';
   COMMENT ON COLUMN "TC_ROLES"."ESTATUS" IS 'ESTATUS DEL REGISTRO(ACTIVO/INACTIVO) ';
   COMMENT ON TABLE "TC_ROLES"  IS 'CATALOGO DE ROLES';
--------------------------------------------------------
--  DDL for Table TD_USUARIOS_ROLES
--------------------------------------------------------

  CREATE TABLE "TD_USUARIOS_ROLES" 
   (  "ID_USUARIO" NUMBER DEFAULT 0, 
  "ID_ROL" NUMBER DEFAULT 0, 
  "USUARIO_I" VARCHAR2(20) DEFAULT '', 
  "FECHA_I" DATE DEFAULT SYSDATE, 
  "USUARIO_U" VARCHAR2(20), 
  "FECHA_U" DATE, 
  "ESTATUS" VARCHAR2(5) DEFAULT 'AC'
   ) ;

   COMMENT ON COLUMN "TD_USUARIOS_ROLES"."ID_USUARIO" IS 'IDENTIFICADOR FORANEO DEL USUARIO';
   COMMENT ON COLUMN "TD_USUARIOS_ROLES"."ID_ROL" IS 'IDENTIFICADOR FORANEO DEL ROL';
   COMMENT ON COLUMN "TD_USUARIOS_ROLES"."USUARIO_I" IS 'USUARIO QUE INGRESA EL REGISTRO';
   COMMENT ON COLUMN "TD_USUARIOS_ROLES"."FECHA_I" IS 'FECHA DE INSERCION REGISTRO';
   COMMENT ON COLUMN "TD_USUARIOS_ROLES"."USUARIO_U" IS 'USUARIO QUE MODIFICA EL REGISTRO';
   COMMENT ON COLUMN "TD_USUARIOS_ROLES"."FECHA_U" IS 'FECHA DE MODIFICACION DE REGISTRO';
   COMMENT ON COLUMN "TD_USUARIOS_ROLES"."ESTATUS" IS 'ESTATUS DEL REGISTRO(ACTIVO/INACTIVO) ';
   COMMENT ON TABLE "TD_USUARIOS_ROLES"  IS 'DETALLE DE ROLES POR USUARIO';
--------------------------------------------------------
--  DDL for Table TC_USUARIOS
--------------------------------------------------------

  CREATE TABLE "TC_USUARIOS" 
   (  "ID" NUMBER DEFAULT NULL, 
  "USUARIO" VARCHAR2(20) DEFAULT '', 
  "PASSWORD" VARCHAR2(50) DEFAULT '', 
  "NOMBRE" VARCHAR2(250) DEFAULT '', 
  "CORREO" VARCHAR2(100) DEFAULT '', 
  "USUARIO_I" VARCHAR2(20) DEFAULT '', 
  "FECHA_I" DATE DEFAULT SYSDATE, 
  "USUARIO_U" VARCHAR2(20), 
  "FECHA_U" DATE, 
  "ESTATUS" VARCHAR2(5) DEFAULT 'AC'
   ) ;

   COMMENT ON COLUMN "TC_USUARIOS"."ID" IS 'IDENTIFICADOR UNICO CONSECUTIVO';
   COMMENT ON COLUMN "TC_USUARIOS"."USUARIO" IS 'USUARIO DE APLICATIVO';
   COMMENT ON COLUMN "TC_USUARIOS"."PASSWORD" IS 'PASSWORD';
   COMMENT ON COLUMN "TC_USUARIOS"."NOMBRE" IS 'NOMBRE DEL USUARIO';
   COMMENT ON COLUMN "TC_USUARIOS"."CORREO" IS 'CORREO ELECTRONICO';
   COMMENT ON COLUMN "TC_USUARIOS"."USUARIO_I" IS 'USUARIO QUE INGRESA EL REGISTRO';
   COMMENT ON COLUMN "TC_USUARIOS"."FECHA_I" IS 'FECHA DE INSERCION REGISTRO';
   COMMENT ON COLUMN "TC_USUARIOS"."USUARIO_U" IS 'USUARIO QUE MODIFICA EL REGISTRO';
   COMMENT ON COLUMN "TC_USUARIOS"."FECHA_U" IS 'FECHA DE MODIFICACION DE REGISTRO';
   COMMENT ON COLUMN "TC_USUARIOS"."ESTATUS" IS 'ESTATUS DEL REGISTRO(ACTIVO/INACTIVO)';
   COMMENT ON TABLE "TC_USUARIOS"  IS 'CATALOGO DE USUARIOS ';

REM INSERTING into TC_PARAMETROS
SET DEFINE OFF;
Insert into TC_PARAMETROS (ID,NOMBRE,VALOR,USUARIO_I,FECHA_I,USUARIO_U,FECHA_U,ESTATUS) values (1,'AVISO_PRIVACIDAD','<p style=\"justify\"><b>La Dirección de Tecnologías de la Información de la Secretaría de Planeación y Finanzas</b>, es la responsable del tratamiento de los datos personales que nos sean proporcionados <b>derivado de la implementación de soluciones tecnológicas para la agilización de procesos y servicios;</b> los cuales no son transferidos a ninguna otra entidad, institución o dependencia en términos del artículo 3 fracción XXVIII de la Ley de Protección de Datos Personales en Posesión de Sujetos Obligados del Estado de Querétaro.</p><p align=\"justify\">En caso de negativa para el tratamiento de sus datos personales para esta finalidad, podrá presentar su solicitud para el ejercicio de derechos ARCO a través de la Plataforma Nacional de Transparencia o ante la Unidad de Transparencia del Poder Ejecutivo del Estado de Querétaro.</p><p align=\"justify\">Si desea conocer nuestro aviso de privacidad integral, podrá consultarlo en <a href=\"http://bit.ly/2zqyiGf\" target=\"_blank\">http://bit.ly/2zqyiGf</a> o en esta <b>Dirección.</b></p><p align=\"right\"><b>Fecha de actualización: 11 de julio del 2018.</b></p>','rverde',SYSDATE,null,null,'AC');
Insert into TC_PARAMETROS (ID,NOMBRE,VALOR,USUARIO_I,FECHA_I,USUARIO_U,FECHA_U,ESTATUS) values (2,'LDAP_URL','ldap://10.16.115.1:389/','rverde',SYSDATE,null,null,'AC');
Insert into TC_PARAMETROS (ID,NOMBRE,VALOR,USUARIO_I,FECHA_I,USUARIO_U,FECHA_U,ESTATUS) values (3,'LDAP_BASE','DC=geq,DC=net','rverde',SYSDATE,null,null,'AC');
Insert into TC_PARAMETROS (ID,NOMBRE,VALOR,USUARIO_I,FECHA_I,USUARIO_U,FECHA_U,ESTATUS) values (4,'LDAP_USUARIO','sraldap','rverde',SYSDATE,null,null,'AC');
Insert into TC_PARAMETROS (ID,NOMBRE,VALOR,USUARIO_I,FECHA_I,USUARIO_U,FECHA_U,ESTATUS) values (5,'LDAP_CONTRASENA','oZRzjktSvlAsj9LeRc0RdQ==','rverde',SYSDATE,null,null,'AC');
Insert into TC_PARAMETROS (ID,NOMBRE,VALOR,USUARIO_I,FECHA_I,USUARIO_U,FECHA_U,ESTATUS) values (6,'RUTA_GEQWS','http://10.16.8.114:18080/GEQWS/api/','rverde',SYSDATE,null,null,'AC');
commit;
REM INSERTING into TC_RECURSOS
SET DEFINE OFF;
Insert into TC_RECURSOS (ID,RECURSO,DESCRIPCION,USUARIO_I,FECHA_I,USUARIO_U,FECHA_U,ESTATUS,SESION) values (1,'/inicio.html','Recurso de la página de inicio','rverde',SYSDATE,null,null,'AC', 1);
Insert into TC_RECURSOS (ID,RECURSO,DESCRIPCION,USUARIO_I,FECHA_I,USUARIO_U,FECHA_U,ESTATUS,SESION) values (2,'/administracion/**','Recurso de administración del sistema','rverde',SYSDATE,null,null,'AC', 1);
commit;
REM INSERTING into TC_ROLES
SET DEFINE OFF;
Insert into TC_ROLES (ID,ROL,DESCRIPCION,USUARIO_I,FECHA_I,USUARIO_U,FECHA_U,ESTATUS) values (1,'ROLE_ADMIN','Rol del Administrador del Sistema','rverde',SYSDATE,null,null,'AC');
Insert into TC_ROLES (ID,ROL,DESCRIPCION,USUARIO_I,FECHA_I,USUARIO_U,FECHA_U,ESTATUS) values (2,'ROLE_USER','Rol de Usuario General','rverde',SYSDATE,null,null,'AC');
commit;
REM INSERTING into TC_USUARIOS
SET DEFINE OFF;
Insert into TC_USUARIOS (ID,USUARIO,PASSWORD,NOMBRE,CORREO,USUARIO_I,FECHA_I,USUARIO_U,FECHA_U,ESTATUS) values (1,'rverde',null,'Raúl Alejandro Verde Martínez','rverde@queretaro.gob.mx','rverde',SYSDATE,null,null,'AC');
Insert into TC_USUARIOS (ID,USUARIO,PASSWORD,NOMBRE,CORREO,USUARIO_I,FECHA_I,USUARIO_U,FECHA_U,ESTATUS) values (2,'hsuarez',null,'Héctor Carmelo Suárez Daniel','hsuarez@queretaro.gob.mx','rverde',SYSDATE,null,null,'AC');
commit;
REM INSERTING into TD_RECURSOS_ROLES
SET DEFINE OFF;
Insert into TD_RECURSOS_ROLES (ID_RECURSO,ID_ROL,USUARIO_I,FECHA_I,USUARIO_U,FECHA_U,ESTATUS) values (1,1,'rverde',SYSDATE,null,null,'AC');
Insert into TD_RECURSOS_ROLES (ID_RECURSO,ID_ROL,USUARIO_I,FECHA_I,USUARIO_U,FECHA_U,ESTATUS) values (2,1,'rverde',SYSDATE,null,null,'AC');
Insert into TD_RECURSOS_ROLES (ID_RECURSO,ID_ROL,USUARIO_I,FECHA_I,USUARIO_U,FECHA_U,ESTATUS) values (1,2,'rverde',SYSDATE,null,null,'AC');
commit;
REM INSERTING into TD_USUARIOS_ROLES
SET DEFINE OFF;
Insert into TD_USUARIOS_ROLES (ID_USUARIO,ID_ROL,USUARIO_I,FECHA_I,USUARIO_U,FECHA_U,ESTATUS) values (2,2,'rverde',SYSDATE,null,null,'AC');
Insert into TD_USUARIOS_ROLES (ID_USUARIO,ID_ROL,USUARIO_I,FECHA_I,USUARIO_U,FECHA_U,ESTATUS) values (1,1,'rverde',SYSDATE,null,null,'AC');
Insert into TD_USUARIOS_ROLES (ID_USUARIO,ID_ROL,USUARIO_I,FECHA_I,USUARIO_U,FECHA_U,ESTATUS) values (1,2,'rverde',SYSDATE,null,null,'AC');
commit;

--------------------------------------------------------
--  DDL for Index UK_PARA_NOMBRE
--------------------------------------------------------

  CREATE UNIQUE INDEX "UK_PARA_NOMBRE" ON "TC_PARAMETROS" ("NOMBRE") 
  ;
--------------------------------------------------------
--  DDL for Index PK_PARA_ID
--------------------------------------------------------

  CREATE UNIQUE INDEX "PK_PARA_ID" ON "TC_PARAMETROS" ("ID") 
  ;
--------------------------------------------------------
--  DDL for Index UK_RECU_RECU
--------------------------------------------------------

  CREATE UNIQUE INDEX "UK_RECU_RECURSO" ON "TC_RECURSOS" ("RECURSO") 
  ;
--------------------------------------------------------
--  DDL for Index PK_RECU_ID
--------------------------------------------------------

  CREATE UNIQUE INDEX "PK_RECU_ID" ON "TC_RECURSOS" ("ID") 
  ;
--------------------------------------------------------
--  DDL for Index PK_RERO_IDRE_IDRO
--------------------------------------------------------

  CREATE UNIQUE INDEX "PK_RERO_IDRE_IDRO" ON "TD_RECURSOS_ROLES" ("ID_RECURSO", "ID_ROL") 
  ;
--------------------------------------------------------
--  DDL for Index PK_ROLE_ID
--------------------------------------------------------

  CREATE UNIQUE INDEX "PK_ROLE_ID" ON "TC_ROLES" ("ID") 
  ;
--------------------------------------------------------
--  DDL for Index UK_ROLE_ROL
--------------------------------------------------------

  CREATE UNIQUE INDEX "UK_ROLE_ROL" ON "TC_ROLES" ("ROL") 
  ;
--------------------------------------------------------
--  DDL for Index PK_USRO_IDUS_IDRO
--------------------------------------------------------

  CREATE UNIQUE INDEX "PK_USRO_IDUS_IDRO" ON "TD_USUARIOS_ROLES" ("ID_USUARIO", "ID_ROL") 
  ;
--------------------------------------------------------
--  DDL for Index PK_USUA_ID
--------------------------------------------------------

  CREATE UNIQUE INDEX "PK_USUA_ID" ON "TC_USUARIOS" ("ID") 
  ;
--------------------------------------------------------
--  DDL for Index UK_USUA_USUARIO
--------------------------------------------------------

  CREATE UNIQUE INDEX "UK_USUA_USUARIO" ON "TC_USUARIOS" ("USUARIO") 
  ;
--------------------------------------------------------
--  DDL for Trigger TGR_PARA
--------------------------------------------------------

  CREATE OR REPLACE TRIGGER "TGR_PARA" 
BEFORE INSERT
ON "TC_PARAMETROS"
REFERENCING NEW AS NEW OLD AS OLD
FOR EACH ROW
DECLARE
BEGIN
IF (:new.ID IS NULL)
THEN
:new.ID := "SEQ_PARA".nextval;
END IF;
END;


/
ALTER TRIGGER "TGR_PARA" ENABLE;
--------------------------------------------------------
--  DDL for Trigger TGR_RECU
--------------------------------------------------------

  CREATE OR REPLACE TRIGGER "TGR_RECU" 
BEFORE INSERT
ON "TC_RECURSOS"
REFERENCING NEW AS NEW OLD AS OLD
FOR EACH ROW
DECLARE
BEGIN
IF (:new.ID IS NULL)
THEN
:new.ID := "SEQ_RECU".nextval;
END IF;
END;


/
ALTER TRIGGER "TGR_RECU" ENABLE;
--------------------------------------------------------
--  DDL for Trigger TGR_ROLE
--------------------------------------------------------

  CREATE OR REPLACE TRIGGER "TGR_ROLE" 
BEFORE INSERT
ON "TC_ROLES"
REFERENCING NEW AS NEW OLD AS OLD
FOR EACH ROW
DECLARE
BEGIN
IF (:new.ID IS NULL)
THEN
:new.ID := "SEQ_ROLE".nextval;
END IF;
END;


/
ALTER TRIGGER "TGR_ROLE" ENABLE;
--------------------------------------------------------
--  DDL for Trigger USUARIOS_C_TGR
--------------------------------------------------------

  CREATE OR REPLACE TRIGGER "TGR_USUA" 
BEFORE INSERT
ON "TC_USUARIOS"
REFERENCING NEW AS NEW OLD AS OLD
FOR EACH ROW
DECLARE
BEGIN
IF (:new.ID IS NULL)
THEN
:new.ID := "SEQ_USUA".nextval;
END IF;
END;


/
ALTER TRIGGER "TGR_USUA" ENABLE;
--------------------------------------------------------
--  Constraints for Table TC_PARAMETROS
--------------------------------------------------------

  ALTER TABLE "TC_PARAMETROS" ADD CONSTRAINT "UK_PARA_NOMBRE" UNIQUE ("NOMBRE") ENABLE;
  ALTER TABLE "TC_PARAMETROS" ADD CONSTRAINT "PK_PARA_ID" PRIMARY KEY ("ID") ENABLE;
  ALTER TABLE "TC_PARAMETROS" MODIFY ("FECHA_I" NOT NULL ENABLE);
  ALTER TABLE "TC_PARAMETROS" MODIFY ("USUARIO_I" NOT NULL ENABLE);
  ALTER TABLE "TC_PARAMETROS" MODIFY ("ESTATUS" NOT NULL ENABLE);
  ALTER TABLE "TC_PARAMETROS" MODIFY ("VALOR" NOT NULL ENABLE);
  ALTER TABLE "TC_PARAMETROS" MODIFY ("NOMBRE" NOT NULL ENABLE);
  ALTER TABLE "TC_PARAMETROS" MODIFY ("ID" NOT NULL ENABLE);
--------------------------------------------------------
--  Constraints for Table TC_RECURSOS
--------------------------------------------------------

  ALTER TABLE "TC_RECURSOS" MODIFY ("ID" NOT NULL ENABLE);
  ALTER TABLE "TC_RECURSOS" ADD CONSTRAINT "PK_RECU_ID" PRIMARY KEY ("ID") ENABLE;
  ALTER TABLE "TC_RECURSOS" ADD CONSTRAINT "UK_RECU_RECU" UNIQUE ("RECURSO") ENABLE;
--------------------------------------------------------
--  Constraints for Table TD_RECURSOS_ROLES
--------------------------------------------------------

  ALTER TABLE "TD_RECURSOS_ROLES" ADD CONSTRAINT "PK_RERO_IDRE_IDRO" PRIMARY KEY ("ID_RECURSO", "ID_ROL") ENABLE;
  ALTER TABLE "TD_RECURSOS_ROLES" MODIFY ("ESTATUS" NOT NULL ENABLE);
  ALTER TABLE "TD_RECURSOS_ROLES" MODIFY ("FECHA_I" NOT NULL ENABLE);
  ALTER TABLE "TD_RECURSOS_ROLES" MODIFY ("USUARIO_I" NOT NULL ENABLE);
  ALTER TABLE "TD_RECURSOS_ROLES" MODIFY ("ID_ROL" NOT NULL ENABLE);
  ALTER TABLE "TD_RECURSOS_ROLES" MODIFY ("ID_RECURSO" NOT NULL ENABLE);
--------------------------------------------------------
--  Constraints for Table TC_ROLES
--------------------------------------------------------

  ALTER TABLE "TC_ROLES" ADD CONSTRAINT "UK_ROLE_ROL" UNIQUE ("ROL") ENABLE;
  ALTER TABLE "TC_ROLES" ADD CONSTRAINT "PK_ROLE_ID" PRIMARY KEY ("ID") ENABLE;
  ALTER TABLE "TC_ROLES" MODIFY ("ESTATUS" NOT NULL ENABLE);
  ALTER TABLE "TC_ROLES" MODIFY ("FECHA_I" NOT NULL ENABLE);
  ALTER TABLE "TC_ROLES" MODIFY ("USUARIO_I" NOT NULL ENABLE);
  ALTER TABLE "TC_ROLES" MODIFY ("DESCRIPCION" NOT NULL ENABLE);
  ALTER TABLE "TC_ROLES" MODIFY ("ROL" NOT NULL ENABLE);
  ALTER TABLE "TC_ROLES" MODIFY ("ID" NOT NULL ENABLE);
--------------------------------------------------------
--  Constraints for Table TD_USUARIOS_ROLES
--------------------------------------------------------

  ALTER TABLE "TD_USUARIOS_ROLES" ADD CONSTRAINT "PK_USRO_IDUS_IDRO" PRIMARY KEY ("ID_USUARIO", "ID_ROL") ENABLE;
  ALTER TABLE "TD_USUARIOS_ROLES" MODIFY ("ESTATUS" NOT NULL ENABLE);
  ALTER TABLE "TD_USUARIOS_ROLES" MODIFY ("FECHA_I" NOT NULL ENABLE);
  ALTER TABLE "TD_USUARIOS_ROLES" MODIFY ("USUARIO_I" NOT NULL ENABLE);
  ALTER TABLE "TD_USUARIOS_ROLES" MODIFY ("ID_ROL" NOT NULL ENABLE);
  ALTER TABLE "TD_USUARIOS_ROLES" MODIFY ("ID_USUARIO" NOT NULL ENABLE);
--------------------------------------------------------
--  Constraints for Table TC_USUARIOS
--------------------------------------------------------

  ALTER TABLE "TC_USUARIOS" ADD CONSTRAINT "UK_USUA_USUARIO" UNIQUE ("USUARIO") ENABLE;
  ALTER TABLE "TC_USUARIOS" ADD CONSTRAINT "PK_USUA_ID" PRIMARY KEY ("ID") ENABLE;
  ALTER TABLE "TC_USUARIOS" MODIFY ("FECHA_I" NOT NULL ENABLE);
  ALTER TABLE "TC_USUARIOS" MODIFY ("USUARIO_I" NOT NULL ENABLE);
  ALTER TABLE "TC_USUARIOS" MODIFY ("ESTATUS" NOT NULL ENABLE);
  ALTER TABLE "TC_USUARIOS" MODIFY ("NOMBRE" NOT NULL ENABLE);
  ALTER TABLE "TC_USUARIOS" MODIFY ("USUARIO" NOT NULL ENABLE);
  ALTER TABLE "TC_USUARIOS" MODIFY ("ID" NOT NULL ENABLE);
--------------------------------------------------------
--  Ref Constraints for Table RECURSOS_ROLES_D
--------------------------------------------------------

  ALTER TABLE "TD_RECURSOS_ROLES" ADD CONSTRAINT "FK_RERO_ID_RECURSO" FOREIGN KEY ("ID_RECURSO")
    REFERENCES "TC_RECURSOS" ("ID") ENABLE;
  ALTER TABLE "TD_RECURSOS_ROLES" ADD CONSTRAINT "FK_RERO_ID_ROL" FOREIGN KEY ("ID_ROL")
    REFERENCES "TC_ROLES" ("ID") ENABLE;
--------------------------------------------------------
--  Ref Constraints for Table USUARIO_ROLES_D
--------------------------------------------------------

  ALTER TABLE "TD_USUARIOS_ROLES" ADD CONSTRAINT "FK_USRO_ID_ROL" FOREIGN KEY ("ID_ROL")
    REFERENCES "TC_ROLES" ("ID") ENABLE;
  ALTER TABLE "TD_USUARIOS_ROLES" ADD CONSTRAINT "FK_USRO_ID_USUARIO" FOREIGN KEY ("ID_USUARIO")
    REFERENCES "TC_USUARIOS" ("ID") ENABLE;
