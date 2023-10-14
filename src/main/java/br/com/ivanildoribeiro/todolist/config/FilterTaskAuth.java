package br.com.ivanildoribeiro.todolist.config;

import at.favre.lib.crypto.bcrypt.BCrypt;
import br.com.ivanildoribeiro.todolist.users.model.UserModel;
import br.com.ivanildoribeiro.todolist.users.repository.UserRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Base64;

@Component
public class FilterTaskAuth extends OncePerRequestFilter {

    @Autowired
    private UserRepository repository;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        var servletPath = request.getServletPath();

        if (servletPath.startsWith("/tasks")) {
            //Buscar usuário e senha no authorization
            String authorization = request.getHeader("Authorization");
            System.out.println(authorization);
            //Retirar o "basic " do authorization
            String base64 = authorization.substring("Basic".length()).trim();
            System.out.println(base64);

            //Faz a decodificação do base64 e converte em um array de bytes
            byte[] base64Array = Base64.getDecoder().decode(base64);
            System.out.println(base64Array);
            //Transforma a decodificação do base64 em string
            String decodedBase64 = new String(base64Array);
            System.out.println(decodedBase64);
            //Faz um split no string decodificado
            String[] autorized = decodedBase64.split(":");
            String username = autorized[0];
            String password = autorized[1];

            if (!repository.existsByUsername(username)) {
                response.sendError(401);
                System.out.println("usuário não cadastrado");
            } else {
                UserModel user = repository.findByUsername(username);
                BCrypt.with(BCrypt.Version.VERSION_2Y).hashToString(12,user.getPassword().toCharArray());
                BCrypt.Result result = BCrypt.verifyer().verify(password.toCharArray(), user.getPassword());
                if (result.verified) {
                    request.setAttribute("idUser",user.getId());
                    filterChain.doFilter(request, response);
                } else {
                    response.sendError(401);
                }
            }

        } else {
            filterChain.doFilter(request, response);
        }
    }
}
