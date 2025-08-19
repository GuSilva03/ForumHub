package ForumHub.forum.domain.usuario.topico.dto;

public record TopicoResponse(
        Long id,
        String titulo,
        String mensagem,
        String autorNome,
        String cursoNome,
        java.time.LocalDateTime dataCriacao) { }
