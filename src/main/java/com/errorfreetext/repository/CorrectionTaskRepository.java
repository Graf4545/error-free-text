package com.errorfreetext.repository;

import com.errorfreetext.domain.CorrectionTask;
import com.errorfreetext.domain.TaskStatus;
import java.util.List;
import java.util.UUID;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface CorrectionTaskRepository extends JpaRepository<CorrectionTask, UUID> {

    @Query("""
            SELECT t FROM CorrectionTask t
            WHERE t.status = :status
            ORDER BY t.createdAt ASC
            """)
    List<CorrectionTask> findByStatusOrderByCreatedAtAsc(@Param("status") TaskStatus status, Pageable pageable);
}
