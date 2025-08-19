package ForumHub.forum.domain.usuario;

public record UsuarioDados(String nome, String email, String senha){
    public record DadosDetalhamentoUsuario(Long id, String nome, String email){
        public DadosDetalhamentoUsuario(Usuario usuario){
            this(usuario.getId(), usuario.getNome(), usuario.getEmail());
        }
    }

}
