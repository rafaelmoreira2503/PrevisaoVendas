DELIMITER $$
CREATE DEFINER=`root`@`localhost` PROCEDURE `update_ticket_value2`()
BEGIN
DECLARE index_value INT;
DECLARE loop_variable INT;  


SET @KeyValue = 100;
SET @LastPersonID = 0;
SET @TicketNum = 0;

SET @PersonIDToHandle = 0;

SELECT @PersonIDToHandle = PersonID, @TicketNum = NumTickets
FROM tblPeople
WHERE PersonId > @LastPersonID
ORDER BY PersonId
LIMIT 0,1;

WHILE @PersonIDToHandle IS NOT NULL
DO
    SET loop_variable = 0;

    WHILE(loop_variable < @TicketNum) DO
        INSERT INTO tblTickets(TicketId, PersonId) VALUES(@KeyValue + loop_variable, @PersonIDToHandle);
        SET loop_variable = loop_variable + 1;
    END WHILE;  

    SET @LastPersonID = @PersonIDToHandle;
    SET @PersonIDToHandle = NULL;
    SET @KeyValue = @KeyValue + @TicketNum;

    SELECT @PersonIDToHandle := PersonID, @TicketNum := NumTickets
    FROM tblPeople
    WHERE PersonId > @LastPersonID
    ORDER BY PersonId
    LIMIT 0,1;
END WHILE;

    END$$
DELIMITER ;

select TotalAssociado,TotalCriticado,items.item_codigo_item,items.item_data_item,items.item_descricao,items.item_status,items.Empresa,items.usuario_nome, Associado.usuario_codigo  from (
SELECT
     count(item.`codigo`) AS TotalAssociado,
     item.`codigo_item` AS item_codigo_item,
     item.`data_item` AS item_data_item,
     item.`descricao` AS item_descricao,
     item.`status` AS item_status,
     item.codigo_empresa,
     usuario.`codigo` AS usuario_codigo,
     usuario.`nome` AS usuario_nome,
     usuario.`permissao` AS usuario_permissao,
     tecnico_item.`codigo_tecnico` AS tecnico_item_codigo_tecnico,
     tecnico_item.`codigo_item` AS tecnico_item_codigo_item
FROM
     `item` item INNER JOIN `tecnico_item` tecnico_item ON item.`codigo` = tecnico_item.`codigo_item`
     INNER JOIN `usuario` usuario ON tecnico_item.`codigo_tecnico` = usuario.`codigo`
     group by usuario.codigo,item.codigo_empresa
  ) as Associado,
 (
   SELECT
     count(item.`codigo`) AS TotalCriticado,
     item.`codigo_item` AS item_codigo_item,
     item.`data_item` AS item_data_item,
     item.`descricao` AS item_descricao,
     item.`status` AS item_status,
     usuario.`codigo` AS usuario_codigo,
     usuario.`nome` AS usuario_nome,
     usuario.`permissao` AS usuario_permissao,
     tecnico_item.`codigo_tecnico` AS tecnico_item_codigo_tecnico,
     tecnico_item.`codigo_item` AS tecnico_item_codigo_item
FROM
     `item` item inner JOIN `tecnico_item` tecnico_item ON item.`codigo` = tecnico_item.`codigo_item`
    inner JOIN `usuario` usuario ON tecnico_item.`codigo_tecnico` = usuario.`codigo`
      where item.status = 'CRITICADO'
       group by usuario.codigo,item.codigo_empresa
   
  
   )as Criticado,
   (
   SELECT
     item.`codigo` AS item_codigo,
     item.`codigo_item` AS item_codigo_item,
     item.`data_item` AS item_data_item,
     item.`descricao` AS item_descricao,
     item.`status` AS item_status,
     item.codigo_empresa as Empresa,
     usuario.`codigo` AS usuario_codigo,
     usuario.`nome` AS usuario_nome,
     usuario.`permissao` AS usuario_permissao,
     tecnico_item.`codigo_tecnico` AS tecnico_item_codigo_tecnico,
     tecnico_item.`codigo_item` AS tecnico_item_codigo_item
FROM
     `item` item JOIN `tecnico_item` tecnico_item ON item.`codigo` = tecnico_item.`codigo_item`
     JOIN `usuario` usuario ON tecnico_item.`codigo_tecnico` = usuario.`codigo`
   ) as items
  
   where Associado.usuario_codigo = items.usuario_codigo and
   Criticado.usuario_codigo = items.usuario_codigo and
   Associado.codigo_empresa = items.Empresa  and 
   items.Empresa = 1   and items.usuario_codigo = 
  
   
   order by items.item_descricao
    