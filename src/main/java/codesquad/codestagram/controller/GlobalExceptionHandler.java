package codesquad.codestagram.controller;

import codesquad.codestagram.exception.ArticleNotFoundException;
import codesquad.codestagram.exception.UserNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.NoSuchElementException;

@ControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    // Optional.get() 실패 등
    @ExceptionHandler(NoSuchElementException.class)
    public String handleNoSuchElementException(NoSuchElementException ex, Model model) {
        log.warn("[NoSuchElement] {}", ex.getMessage());
        model.addAttribute("errorMessage", "요청하신 정보를 찾을 수 없습니다.");
        return "error/no-found";
    }

    // User 를 찾을 수 없을 때
    @ExceptionHandler(UserNotFoundException.class)
    public String handleUserNotFound(UserNotFoundException ex, Model model) {
        log.warn("[UserNotFound] {}", ex.getMessage());
        model.addAttribute("errorMessage", ex.getMessage());
        return "error/user-not-found";
    }

    // Article 를 찾을 수 없을 때
    @ExceptionHandler(ArticleNotFoundException.class)
    public String handleArticleNotFound(ArticleNotFoundException ex, Model model) {
        log.warn("[ArticleNotFound] {}", ex.getMessage());
        model.addAttribute("errorMessage", ex.getMessage());
        return "error/article-not-found";
    }

    // 예상치 못한 null
    @ExceptionHandler(NullPointerException.class)
    public String handleNullPointer(NullPointerException ex, Model model) {
        log.error("[NullPointer] {}", ex.getMessage(), ex);
        model.addAttribute("errorMessage", "처리 중 문제가 발생했습니다.");
        return "error/null-pointer";
    }

    // 현재 상태에서 수행할 수 없는 요청
    @ExceptionHandler(IllegalStateException.class)
    public String handleIllegalState(IllegalStateException ex, Model model) {
        log.error("[IllegalState] {}", ex.getMessage(), ex);
        model.addAttribute("errorMessage", ex.getMessage());
        return "error/illegal-state";
    }

    // 기타 모든 예외
    @ExceptionHandler(Exception.class)
    public String handleException(Exception ex, Model model) {
        log.error("[Unhandled Exception] {}", ex.getMessage(), ex);
        model.addAttribute("errorMessage", "알 수 없는 오류가 발생했습니다.");
        return "error/general-error";
    }
}
