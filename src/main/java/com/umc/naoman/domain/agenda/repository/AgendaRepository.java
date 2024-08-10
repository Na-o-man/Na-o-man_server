package com.umc.naoman.domain.agenda.repository;

import com.umc.naoman.domain.agenda.entity.Agenda;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AgendaRepository extends JpaRepository<Agenda,Long> {
    Page<Agenda> findByShareGroupId(Long shareGroupId, Pageable pageable);

}
