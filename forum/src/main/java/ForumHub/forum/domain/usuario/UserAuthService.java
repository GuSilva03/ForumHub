package ForumHub.forum.domain.usuario;

import org.springframework.stereotype.Service;

@Service
public class UserAuthService {
    private final UsuarioRepository repository;
    public UserAuthService(UsuarioRepository repository){
        this.repository = repository;
    }

}
