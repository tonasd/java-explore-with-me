package ru.practicum.ewm.exception;

public class CompilationNotFoundException extends NotFoundException {
    public CompilationNotFoundException(long compId) {
        super("Compilation with id=" + compId + " was not found");
    }
}
