package ForumHub.forum.domain.usuario.topico.dto;

import jakarta.validation.constraints.NotBlank;

public record TopicoUpdate(@NotBlank String titulo, @NotBlank String mensagem, @NotBlank String cursoNome) {


}
