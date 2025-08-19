package ForumHub.forum.domain.infra.security;

import ForumHub.forum.domain.usuario.Usuario;
import ForumHub.forum.domain.usuario.UsuarioRepository;
import ForumHub.forum.domain.usuario.curso.Curso;
import ForumHub.forum.domain.usuario.curso.CursoRepository;
import ForumHub.forum.domain.usuario.topico.Topico;
import ForumHub.forum.domain.usuario.topico.TopicoRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class TopicoService {
    private final TopicoRepository topicoRepository;
    private final UsuarioRepository usuarioRepository;
    private final CursoRepository cursoRepository;

    public TopicoService(TopicoRepository topicoRepository, UsuarioRepository usuarioRepository, CursoRepository cursoRepository){
        this.topicoRepository = topicoRepository;
        this.cursoRepository = cursoRepository;
        this.usuarioRepository = usuarioRepository;
    }
    public List<Topico> listarTodos(){
        return topicoRepository.findAll();
    }

    public Optional<Topico> buscarPorId(Long id){
       return topicoRepository.findById(id);
    }

    @Transactional
    public Topico criarTopico(String titulo, String mensagem, String nomeCurso, Long usuarioId){
        Usuario autor = usuarioRepository.findById(usuarioId).orElseThrow(() -> new RuntimeException(("Usuário não encontrado.")));

        Curso curso = cursoRepository.findByNome(nomeCurso).orElseThrow(() -> new RuntimeException("Curso não encontrado."));

        Topico topico = new Topico();
        topico.setTitulo(titulo);
        topico.setMensagem(mensagem);
        topico.setCurso(curso);
        topico.setAutor(autor);
        topico.setDataCriacao(LocalDateTime.now());

        return topicoRepository.save(topico);
    }

    @Transactional
    public Topico atualizarTopico(Long id, String novoTitulo, String novaMensagem, Long usuariId){
        Topico topico = topicoRepository.findById(id).orElseThrow(() -> new RuntimeException("Tópico não encontrado."));
     if (!topico.getAutor().getId().equals(usuariId)){
         throw new RuntimeException("Usuário não é o autor do tópico");
     }
     topico.setTitulo(novoTitulo);
     topico.setMensagem(novaMensagem);
     return topicoRepository.save(topico);
    }

    @Transactional
    public void deletarTopico(Long id, Long usuarioId){
        Topico topico = topicoRepository.findById(id).orElseThrow(() -> new RuntimeException("Tópico não encontrado."));
        if(!topico.getAutor().getId().equals(usuarioId)){
            throw new RuntimeException("Usuário não é criador do tópico.");
        }
        topicoRepository.delete(topico);
    }
}
