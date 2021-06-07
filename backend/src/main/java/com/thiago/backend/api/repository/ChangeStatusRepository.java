package com.thiago.backend.api.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.thiago.backend.api.entity.ChangeStatus;

public interface ChangeStatusRepository extends MongoRepository<ChangeStatus, String> {
	
	/*
	 * Quando clica em um ticket na lista, vemos os detalhes do mesmo. Desse modo,
	 * este método retorna uma lista com todas alterações que ocorreram num ticket.
	 * */
	Iterable<ChangeStatus> findByTicketIdOrderByDateChangeStatusDesc(String ticketId);
	
}
