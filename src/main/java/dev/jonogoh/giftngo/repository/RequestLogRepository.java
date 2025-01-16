package dev.jonogoh.giftngo.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import dev.jonogoh.giftngo.domain.RequestLog;

public interface RequestLogRepository extends JpaRepository<RequestLog, UUID> {
}
