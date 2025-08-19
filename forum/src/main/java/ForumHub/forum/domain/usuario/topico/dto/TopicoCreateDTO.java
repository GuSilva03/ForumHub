package ForumHub.forum.domain.usuario.topico.dto;

import jakarta.validation.constraints.NotBlank;

public record TopicoCreateDTO(
        @NotBlank String titulo,
        @NotBlank String mensagem,
        @NotBlank String cursoNome
) { }
