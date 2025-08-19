package ForumHub.forum.domain.web;

import ForumHub.forum.domain.usuario.UsuarioRepository;
import ForumHub.forum.domain.usuario.curso.CursoRepository;
import ForumHub.forum.domain.usuario.topico.Topico;
import ForumHub.forum.domain.usuario.topico.TopicoRepository;
import ForumHub.forum.domain.usuario.topico.dto.TopicoCreateDTO;
import ForumHub.forum.domain.usuario.topico.dto.TopicoResponse;
import ForumHub.forum.domain.usuario.topico.dto.TopicoUpdate;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ReportAsSingleViolation;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/topicos")
public class TopicoController {
    private final TopicoRepository topicoRepository;
    private final CursoRepository cursoRepository;
    private final UsuarioRepository usuarioRepository;

    public TopicoController(TopicoRepository topicoRepository, CursoRepository cursoRepository, UsuarioRepository usuarioRepository) {
        this.topicoRepository = topicoRepository;
        this.cursoRepository = cursoRepository;
        this.usuarioRepository = usuarioRepository;
    }

    @GetMapping
    public List<TopicoResponse> listar() {
        var topicos = topicoRepository.findAll();
        System.out.println("Total de tópicos encontrados: " + topicos.size());

        for (var t : topicos) {
            System.out.println("Topico ID: " + t.getId() + ", Titulo: " + t.getTitulo()
                    + ", Autor: " + (t.getAutor() != null ? t.getAutor().getNome() : "null")
                    + ", Curso: " + (t.getCurso() != null ? t.getCurso().getNome() : "null"));
        }

        return topicos.stream()
                .map(t -> new TopicoResponse(
                        t.getId(),
                        t.getTitulo(),
                        t.getMensagem(),
                        t.getAutor() != null ? t.getAutor().getNome() : "N/A",
                        t.getCurso() != null ? t.getCurso().getNome() : "N/A",
                        t.getDataCriacao()))
                .toList();
    }


    @GetMapping("/{id}")
    public ResponseEntity<TopicoResponse> detalhar(@PathVariable Long id) {
        return topicoRepository.findById(id).map(t -> ResponseEntity.ok(new TopicoResponse(
                t.getId(), t.getTitulo(), t.getMensagem(), t.getAutor().getNome(), t.getCurso().getNome(), t.getDataCriacao()
        ))).orElse(ResponseEntity.notFound().build());
    }
    @PostMapping
    public ResponseEntity<TopicoResponse> criar(@RequestBody @Valid TopicoCreateDTO dados, HttpServletRequest request) {
        String emailUsuario = (String) request.getAttribute("emailUsuario");
        var autor = usuarioRepository.findByEmail(emailUsuario)
                .orElseThrow(() -> new IllegalArgumentException("Autor não encontrado."));
        var curso = cursoRepository.findByNome(dados.cursoNome())
                .orElseThrow(() -> new IllegalArgumentException("Curso não encontrado."));
        var topico = new Topico();
        topico.setTitulo(dados.titulo());
        topico.setMensagem(dados.mensagem());
        topico.setCurso(curso);
        topico.setAutor(autor);
        topico.setDataCriacao(LocalDateTime.now());
        topicoRepository.save(topico);
        return ResponseEntity.ok(new TopicoResponse(
                topico.getId(),
                topico.getTitulo(),
                topico.getMensagem(),
                autor.getNome(),
                curso.getNome(),
                topico.getDataCriacao()
        ));
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id, HttpServletRequest request){
        String emailUsuario = (String)  request.getAttribute("emailUsuario");
        var topico = topicoRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Tópico não encontrado."));
    if(!topico.getAutor().getEmail().equals(emailUsuario)){
        return ResponseEntity.status(403).build();
    }
    topicoRepository.delete(topico);
    return ResponseEntity.noContent().build();
    }
    @PutMapping("/{id}")
    public ResponseEntity<TopicoResponse> atualizar(
            @PathVariable Long id,
            @RequestBody @Valid TopicoUpdate dados,
            HttpServletRequest request) {

        String emailUsuario = (String) request.getAttribute("emailUsuario");

        var topico = topicoRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Tópico não encontrado."));

        if (!topico.getAutor().getEmail().equals(emailUsuario)) {
            return ResponseEntity.status(403).build();
        }

        var curso = cursoRepository.findByNome(dados.cursoNome())
                .orElseThrow(() -> new IllegalArgumentException("Curso não encontrado."));

        topico.setTitulo(dados.titulo());
        topico.setMensagem(dados.mensagem());
        topico.setCurso(curso);

        topicoRepository.save(topico);

        return ResponseEntity.ok(new TopicoResponse(
                topico.getId(),
                topico.getTitulo(),
                topico.getMensagem(),
                topico.getAutor().getNome(),
                curso.getNome(),
                topico.getDataCriacao()
        ));
    }

}