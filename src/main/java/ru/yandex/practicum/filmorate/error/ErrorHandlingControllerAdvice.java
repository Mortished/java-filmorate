package ru.yandex.practicum.filmorate.error;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.ArrayList;
import java.util.List;

import static ru.yandex.practicum.filmorate.utils.DefaultData.MESSAGE;

@RestControllerAdvice
@Slf4j
public class ErrorHandlingControllerAdvice {

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public List<ApiError> handleValidationExceptions(MethodArgumentNotValidException ex) {
        List<ApiError> errorList = new ArrayList<>();
        ex.getBindingResult().getAllErrors().forEach(error -> {
            String errorMessage = error.getDefaultMessage();
            errorList.add(new ApiError(errorMessage));
            log.warn(MESSAGE + errorMessage);
        });
        return errorList;
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(ValidationException.class)
    public ApiError handleValidationExceptions(ValidationException ex) {
        log.warn(MESSAGE + ex.getMessage());
        return new ApiError(ex.getMessage());
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(NotFoundException.class)
    public ApiError handleValidationExceptions(NotFoundException ex) {
        log.warn(MESSAGE + ex.getMessage());
        return new ApiError(ex.getMessage());
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(NotExistException.class)
    public ApiError handleValidationExceptions(NotExistException ex) {
        log.warn(MESSAGE + ex.getMessage());
        return new ApiError(ex.getMessage());
    }

}
