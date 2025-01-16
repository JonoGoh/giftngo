package dev.jonogoh.giftngo.service;

import java.time.LocalDateTime;
import java.time.ZoneId;
import static java.util.UUID.randomUUID;

import org.springframework.stereotype.Service;

import dev.jonogoh.giftngo.domain.RequestLog;
import dev.jonogoh.giftngo.repository.RequestLogRepository;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class RequestLoggerService {

  private final RequestLogRepository requestLogRepository;

  public RequestLog initializeLog(HttpServletRequest request) {
    RequestLog initialLog = new RequestLog();
    initialLog.setId(randomUUID());
    initialLog.setRequestUri(request.getRequestURI());
    initialLog.setRequestTimestamp(LocalDateTime.now());
    initialLog.setRequestIpAddress(request.getRemoteAddr());

    return initialLog;
  }

  public void finalizeLog(RequestLog requestLog) {
    long startTime = requestLog.getRequestTimestamp().atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
    requestLog.setTimeLapsed(System.currentTimeMillis() - startTime);
    requestLogRepository.save(requestLog);
  }
}
