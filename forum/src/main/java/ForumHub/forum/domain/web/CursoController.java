package ForumHub.forum.domain.web;


import ForumHub.forum.domain.usuario.curso.Curso;
import ForumHub.forum.domain.usuario.curso.CursoRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/cursos")
public class CursoController {
    private final CursoRepository cursoRepository;


    public CursoController(CursoRepository cursoRepository) {
        this.cursoRepository = cursoRepository;
    }
    @PostMapping
    public ResponseEntity<Curso> criar(@RequestBody Curso curso){
        cursoRepository.save(curso);
        return ResponseEntity.ok(curso);
    }
    @GetMapping
    public ResponseEntity<List<Curso>> listar(){
        return ResponseEntity.ok(cursoRepository.findAll());
    }
}
