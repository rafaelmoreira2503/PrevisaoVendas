DELIMITER $$

use confiabilidade$$

DROP PROCEDURE IF EXISTS  `criaUsuarioOmc`$$

CREATE  PROCEDURE `criaUsuarioOmc`( nome varchar(60),IN email varchar(80),senha varchar (30),permissao varchar(20) ,fixo varchar(16), celular varchar (17))
begin

if  exists (Select COUNT(*) AS Total
 from Usuario where usuario.nome= nome 
having total>0) THEN


update usuario
set nome=nome,
email=email,
senha=senha,
tel_fixo=fixo,
tel_celular=celular
where usuario.nome = nome;





else

insert into Usuario
(nome,email,senha, permissao, tel_fixo,tel_celular)

values
(nome,email,senha,permissao,fixo,celular);



end if;

 END$$
DELIMITER ;

//====================FIM DA PROCEDURE==================================================//

IF NOT EXISTS ((SELECT * FROM information_schema.columns WHERE table_schema=DATABASE() AND table_name='customers' AND column_name='email_address')) THEN
     ALTER TABLE customers ADD email_address VARCHAR(256);
END IF;

select ifnull((select max(u.version) +1 from usuario u where u.codigo = u1.codigo),0) from usuario u1

Scripr do Jasper Report

select * FROM confiabilidade.item;

Select count(ti.codigo_item) as quantidade,ti.codigo_tecnico,ti.codigo_item,i.codigo as icodigox,u.codigo as ucodigox,encarregado.nome,a.descricao,e.nome_fantasia,i.data_item

from Empresa e join item i

on  i.codigo_empresa = e.codigo

join tecnico_item ti

on   ti.codigo_item = i.codigo 

join Usuario u

on  ti.codigo_tecnico = u.codigo

join usuario encarregado

on u.codigo_chefe = encarregado.codigo

join area a

on encarregado.codigo_area = a.codigo



group by ti.codigo_tecnico, i.data_item)x,


(
Select ti.codigo_tecnico,ti.codigo_item, i.codigo as icodigoy, u.codigo as ucodigoy, i.codigo_item as CodigoItem, i.descricao, u.nome,i.data_item

from tecnico_item ti  join item i

on ti.codigo_item = i.codigo

join Usuario u

on u.codigo = ti.codigo_tecnico


Script de Tem em uma tabela e nao em uma outra NxN

select x.cnt, x.codigo_tecnico, x.codigo_item, x.nome, i.descricao from
(
SELECT ti.cnt,ti.codigo_tecnico,ti.codigo_item,u.nome
FROM Usuario u
-- use LEFT JOIN if you may not have a match to get zero counts
JOIN (
    SELECT codigo_tecnico,codigo_item,  COUNT(*) cnt
    FROM tecnico_item 
    GROUP BY codigo_tecnico
) ti ON ti.codigo_tecnico = u.codigo) x, Item i, usuario u2
where x.codigo_item= i.codigo
and u2.codigo = x.codigo_tecnico



SCRIPT DE UPDATE DE AREA DO ITEM
OBS: NA CLAUSULA FROM NÃO PODE TER A TABELA A SER UPDATED:

update item it
set codigo_area
= ( 
select distinct(a.codigo)

from tecnico_item ti, usuario u,usuario chefe,area a

where u.codigo = codigo_tecnico

and chefe.codigo = u.codigo

and a.codigo = chefe.codigo_area

) 

where  it.codigo in 
( 
select distinct(ti.codigo_item)

from tecnico_item ti, usuario u ,usuario chefe,area a

where u.codigo = codigo_tecnico

and chefe.codigo = u.codigo

and a.codigo = chefe.codigo_area

) 


SCRIPT QUE O SITE DA ALGAWORKS ME DEU E RESOLVEU JOIN DE ITEM COM ITEMS DO USUARIO
pega todos os items e seus encaregados , tecnocos e area

select i.codigo, i.descricao,i.status, tecnico.codigo Codigo_Tecnico, tecnico.nome  tecnico ,chefe.nome, chefe.nome chefe, area.descricao
from item i
left join tecnico_item ti on (ti.codigo_item = i.codigo)
left join usuario tecnico on (tecnico.codigo = ti.codigo_tecnico)
left join usuario chefe on (chefe.codigo = tecnico.codigo_chefe)
left join Area area on(area.codigo= chefe.codigo_area)


SCRIPT DE JOIN PARA VERIFICAR A EXISTENCIA DE A EM B

SELECT distinct(ti.cnt),ti.codigo_tecnico,ti.codigo_item,u.nome
FROM Usuario u
-- use LEFT JOIN if you may not have a match to get zero counts
Left JOIN (
    SELECT codigo_tecnico,codigo_item,  COUNT(*) cnt
    FROM tecnico_item 
    GROUP BY codigo_tecnico
) ti ON ti.codigo_tecnico = u.codigo
and  u.codigo is null


SCRIPT DE ITNERANTE USADO EM NX N PARA CONTAR O TOTAL ELIMINANDO DUPLICADOS E NULOS


SELECT count(u.codigo)
FROM usuario u
 INNER JOIN tecnico_item ti 
 ON u.codigo = ti.codigo_tecnico
 GROUP BY ti.codigo_tecnico
 HAVING COUNT(ti.codigo_item) = (SELECT COUNT(item.codigo) FROM item)

 
 SCRIPT PARA TESTE JOIN ENTRE 2 TABELAS alter
 
select x.*,y.*
from(
SELECT ti.cnt,ti.codigo_tecnico,ti.codigo_item,u.nome
 FROM Usuario u
 -- use LEFT JOIN if you may not have a match to get zero counts
 JOIN (
 SELECT codigo_tecnico,codigo_item, COUNT(*) cnt
 FROM tecnico_item 
 GROUP BY codigo_tecnico
 ) ti ON ti.codigo_tecnico = u.codigo)x,

(SELECT ti.codigo_tecnico,ti.codigo_item,i.descricao,i.data_item
 FROM Item i -- use LEFT JOIN if you may not have a match to get zero counts
 JOIN (
 SELECT codigo_tecnico,codigo_item
 FROM tecnico_item 
 GROUP BY codigo_item
 ) ti ON ti.codigo_item = i.codigo)y
where x.codigo_tecnico=y.codigo_tecnico;
B

END$$
DELIMITER ;


CALL `confiabilidade`.`criaUsuarioOmc`('usuarioteste','ricardo@x.com.br','123456','OMC', '(21)3244-2536', '(21)99804-3537');



