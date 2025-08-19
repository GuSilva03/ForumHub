package ForumHub.forum.domain.web;


import ForumHub.forum.domain.usuario.Usuario;
import ForumHub.forum.domain.usuario.UsuarioDados;
import ForumHub.forum.domain.usuario.UsuarioRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/usuarios")
public class UsuarioController {
    private final UsuarioRepository repository;
    private final PasswordEncoder passwordEncoder;

    public UsuarioController(UsuarioRepository repository, PasswordEncoder passwordEncoder) {
        this.repository = repository;
        this.passwordEncoder = passwordEncoder;
    }
    @PostMapping
    public ResponseEntity<UsuarioDados.DadosDetalhamentoUsuario> cadastrar(@RequestBody UsuarioDados dados){
        Usuario usuario = new Usuario();
        usuario.setNome(dados.nome());
        usuario.setEmail(dados.email());
        usuario.setSenha(passwordEncoder.encode(dados.senha()));


        repository.save(usuario);
        return ResponseEntity.ok(new UsuarioDados.DadosDetalhamentoUsuario(usuario));
    }
    @GetMapping
    public ResponseEntity<List<UsuarioDados.DadosDetalhamentoUsuario>> listResponseEntity(){
        var usuario = repository.findAll().stream().map(UsuarioDados.DadosDetalhamentoUsuario::new).toList();
        return ResponseEntity.ok(usuario);
    }
}
