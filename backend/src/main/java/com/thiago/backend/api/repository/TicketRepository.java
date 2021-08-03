package com.thiago.backend.api.repository;

import com.thiago.backend.api.entity.Ticket;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface TicketRepository extends MongoRepository<Ticket, String> {

        /*
         * Possibilita que uma lista paginada de Tickets seja encontrada a partir do id
         * do usuário (porque cada usuário só pode ver os Tickets associados a si
         * mesmo). Este método ordena a lista retornada por data em ordem decrescente
         */
        Page<Ticket> findByUserIdOrderByDateDesc(Pageable pages, String id);

        Page<Ticket> findByTitleIgnoreCaseContainingAndStatusAndPriorityIgnoreCaseContainingOrderByDateDesc(
                        String title, String status, String priority, Pageable pages);

        /*
         * Semelhante ao anterior, neste caso, ele será útil porque cada usuário/cliente
         * logado só poderá ver/pesquisar os tickets associados ao nome dele. Desse modo
         * Este método força com que além dos filtros do método anterior, uma validação
         * pelo id do usuário será aplicada para garantir o supracitado.
         */
        Page<Ticket> findByTitleIgnoreCaseContainingAndStatusAndPriorityAndUserIdOrderByDateDesc(String title,
                        String status, String priority, Pageable pages);

        /*
         * Quando um técnico loga na aplicação, ele só poderá ver os tickets que estão
         * atribuídos a ele. Este método provê os dados para esta situação
         */
        Page<Ticket> findByTitleIgnoreCaseContainingAndStatusAndPriorityAndAssignedUserOrderByDateDesc(String title,
                        String status, String priority, Pageable pages);

        /*
         * Como durante a cração do ticket é gerado um número aleatório, este método
         * facilita a pesquisa pelo número do ticket
         */
        Page<Ticket> findByNumber(Integer number, Pageable pages);

}
