package reminder.reminderbe.global.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.filter.OncePerRequestFilter;
import reminder.reminderbe.global.error.CustomException;
import reminder.reminderbe.global.error.ErrorResponse;

import java.io.IOException;

public class GlobalExceptionFilter extends OncePerRequestFilter {

    private final ObjectMapper objectMapper;

    public GlobalExceptionFilter(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {
        try {
            filterChain.doFilter(request, response);
        } catch (CustomException e) {
            var errorCode = e.getErrorCode();
            var errorResponse = new ErrorResponse(errorCode.getHttpStatus(), errorCode.getMessage());
            writeErrorResponse(response, errorCode.getHttpStatus(), errorResponse);
        }
    }

    private void writeErrorResponse(HttpServletResponse response, int status, ErrorResponse errorResponse) throws IOException {
        response.setStatus(status);
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        objectMapper.writeValue(response.getWriter(), errorResponse);
    }
}
