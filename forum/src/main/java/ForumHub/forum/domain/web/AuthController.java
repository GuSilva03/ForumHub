package ForumHub.forum.domain.web;

import ForumHub.forum.domain.infra.security.TokenService;
import ForumHub.forum.domain.usuario.Usuario;
import ForumHub.forum.domain.usuario.UsuarioDados;
import ForumHub.forum.domain.usuario.UsuarioRepository;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import jdk.jfr.Label;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/login")
public class AuthController {

    private final UsuarioRepository usuarioRepository;
    private TokenService tokenService;
    private PasswordEncoder passwordEncoder;

    @Value("${jwt.secret}")
    private String secret;

    @Autowired
    public AuthController(UsuarioRepository usuarioRepository, TokenService tokenService, PasswordEncoder passwordEncoder) {
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
        this.tokenService = tokenService;
    }

    @PostMapping
    public ResponseEntity<String> logar(@RequestBody UsuarioDados usuarioDados){
        var usuario = usuarioRepository.findByEmail(usuarioDados.email()).orElseThrow(() -> new RuntimeException("Usuário não encontrado."));
        if(!passwordEncoder.matches(usuarioDados.senha(), usuario.getSenha())){
            return ResponseEntity.status(403).body("Senha incorreta.");
        }
        String token = tokenService.gerarToken(usuario);
        return ResponseEntity.ok(token);
    }
}
