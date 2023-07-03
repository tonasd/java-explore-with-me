package ru.practicum.ewm.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.ewm.model.Compilation;

public interface CompilationRepository extends JpaRepository<Compilation, Long> {
    Slice<Compilation> findAllByPinnedIs(Boolean pinned, Pageable page);
}
