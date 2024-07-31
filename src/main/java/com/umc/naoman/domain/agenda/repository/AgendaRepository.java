package com.umc.naoman.domain.agenda.repository;

import com.umc.naoman.domain.agenda.entity.Agenda;
import com.umc.naoman.domain.shareGroup.entity.ShareGroup;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AgendaRepository extends JpaRepository<Agenda,Long> {
}
