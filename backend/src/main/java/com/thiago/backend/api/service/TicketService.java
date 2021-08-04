package com.thiago.backend.api.service;

import com.thiago.backend.api.entity.ChangeStatus;
import com.thiago.backend.api.entity.Ticket;

import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

@Component
public interface TicketService {

    /**
     * Cria um novo registro de ticket na base de dados ou atualiza um registro
     * existente, caso o id esteja presente no parâmetro ticket
     *
     * @param ticket
     * @return
     */
    Ticket createOrUpdate(Ticket ticket);

    /**
     * Pode ser usado para obter os dados de um ticket.
     *
     * @param id
     * @return
     */
    Ticket findById(String id);

    /**
     * Exclui um ticket da base de dados a partir do id.
     *
     * @param id
     */
    void delete(String id);

    /**
     * Usado quando é feita uma pesquisa por algum ticket. Retorna uma paginação de
     * dados.
     *
     * @param page
     * @param count
     * @return
     */
    Page<Ticket> listTicket(int page, int count);

    /**
     * Para cada alteração de status em um ticket cria um registro em uma coleção do
     * mongoDB (data, autor, etc).
     *
     * @param changeStatus
     * @return
     */
    ChangeStatus createChangeStatus(ChangeStatus changeStatus);

    /**
     * Retorna uma lista das alterações feitas em um ticket (data, autor, etc) a
     * partir do id do ticket.
     *
     * @param ticketId
     * @return
     */
    Iterable<ChangeStatus> listChangeStatus(String ticketId);

    /**
     * Quando um cliente está pesquisando um ticket, os resultados que são mostrados
     * são somente os que estão no nome dele.
     *
     * @param page
     * @param count
     * @param userId
     * @return
     */
    Page<Ticket> findByCurrentUser(int page, int count, String userId);

    /**
     * Faz uma pesquisa de ticket por parâmetros filtrando tickets por title, status
     * e / ou priority).
     *
     * @param page
     * @param count
     * @param title
     * @param status
     * @param priority
     * @return
     */
    Page<Ticket> findByParameters(int page, int count, String title, String status, String priority);

    /**
     * Mais uma opção de pesquisa para o contexto de um usuário, o cliente não
     * precisa ter acesso na pesquisa a tickets que não foram criados por ele mesmo.
     *
     * @param page
     * @param count
     * @param title
     * @param status
     * @param priority
     * @param userId   // id do usuário que está pesquisando
     * @return
     */
    Page<Ticket> findByParametersAndCurrentUser(int page, int count, String title, String status, String priority,
            String userId);

    /**
     * Um caminho de busca mais rápido que permite que um ticket seja pesquisado
     * pelo número.
     *
     * @param page
     * @param count
     * @param number
     * @return
     */
    Page<Ticket> findByNumber(int page, int count, Integer number);

    /**
     * Pode ser usado para permitir mostrar um resumo de todos os tickets.
     *
     * @return
     */
    Iterable<Ticket> findAll();

    /**
     * Quando um técnico está logado no sistema, ele poderá listar somente os
     * tickets que estão atribuídos a ele.
     *
     * @param page
     * @param count
     * @param title
     * @param status
     * @param priority
     * @param assignedUser
     * @return
     */
    Page<Ticket> findByParameterAndAssignedUser(int page, int count, String title, String status, String priority,
            String assignedUser);
}
