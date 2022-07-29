package com.dardan.springboot.reactor.springbootreactor;

import com.dardan.springboot.reactor.springbootreactor.model.Comentarios;
import com.dardan.springboot.reactor.springbootreactor.model.Usuario;
import com.dardan.springboot.reactor.springbootreactor.model.UsuarioComentarios;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.List;

@SpringBootApplication
public class SpringBootReactorApplication implements CommandLineRunner {

    private static final Logger log = LoggerFactory.getLogger(SpringBootReactorApplication.class);
    public static void main(String[] args) {
        SpringApplication.run(SpringBootReactorApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        ejemploUsuarioComentariosZipWith();
    }

    public void ejemploUsuarioComentariosZipWith() {
        Mono<Usuario> usuarioMono = Mono.fromCallable(() -> new Usuario("John", "Doe"));

        Mono<Comentarios> comentariosUsuarioMono = Mono.fromCallable(() -> {
            Comentarios comentarios = new Comentarios();
            comentarios.addComentario("Hola pepe, qué tal!");
            comentarios.addComentario("Mañana voy a la playa!");
            comentarios.addComentario("Estoy tomando el curso de spring con reactor");
            return comentarios;
        });

        Mono<UsuarioComentarios> usuarioConComentarios = usuarioMono.zipWith(comentariosUsuarioMono,
                (usuario, comentariosUsuario) -> new UsuarioComentarios(usuario, comentariosUsuario));

        usuarioConComentarios.subscribe(uc -> log.info(uc.toString()));
    }

    public void ejemploUsuarioComentariosZipWithForma2() {
        Mono<Usuario> usuarioMono = Mono.fromCallable(() -> new Usuario("John", "Doe"));

        Mono<Comentarios> comentariosUsuarioMono = Mono.fromCallable(() -> {
            Comentarios comentarios = new Comentarios();
            comentarios.addComentario("Hola pepe, qué tal!");
            comentarios.addComentario("Mañana voy a la playa!");
            comentarios.addComentario("Estoy tomando el curso de spring con reactor");
            return comentarios;
        });

        Mono<UsuarioComentarios> usuarioConComentarios = usuarioMono.zipWith(comentariosUsuarioMono).map(tuple -> {
            Usuario u = tuple.getT1();
            Comentarios c = tuple.getT2();
            return new UsuarioComentarios(u, c);
        });

        usuarioConComentarios.subscribe(uc -> log.info(uc.toString()));
    }

    public void ejemploUsuarioComentariosFlatMap() {
        Mono<Usuario> usuarioMono = Mono.fromCallable(() -> new Usuario("John", "Doe"));

        Mono<Comentarios> comentariosUsuarioMono = Mono.fromCallable(() -> {
            Comentarios comentarios = new Comentarios();
            comentarios.addComentario("Hola pepe, qué tal!");
            comentarios.addComentario("Mañana voy a la playa!");
            comentarios.addComentario("Estoy tomando el curso de spring con reactor");
            return comentarios;
        });

        Mono<UsuarioComentarios> usuarioConComentarios = usuarioMono
                .flatMap(u -> comentariosUsuarioMono.map(c -> new UsuarioComentarios(u, c)));
        usuarioConComentarios.subscribe(uc -> log.info(uc.toString()));
    }

    public void ejemploCollectList() throws Exception {
        List<Usuario> usuariosList = new ArrayList<>();
        usuariosList.add(new Usuario("Darwin",  "Quispe"));
        usuariosList.add(new Usuario("Pedro", "Sanchez"));
        usuariosList.add(new Usuario("Daniel", "Soto "));
        usuariosList.add(new Usuario("Daniel", "Soto"));
        usuariosList.add(new Usuario("Diego", "Suarez"));
        usuariosList.add(new Usuario("Bruce", "Lee"));
        usuariosList.add(new Usuario("Bruce", "Willis"));

        Flux.fromIterable(usuariosList)
                .collectList()
                .subscribe(lista -> {
                    lista.forEach(item -> log.info(item.toString()));
                });
    }

    public void ejemploToString() throws Exception {
        List<Usuario> usuariosList = new ArrayList<>();
        usuariosList.add(new Usuario("Darwin",  "Quispe"));
        usuariosList.add(new Usuario("Pedro", "Sanchez"));
        usuariosList.add(new Usuario("Daniel", "Soto "));
        usuariosList.add(new Usuario("Daniel", "Soto"));
        usuariosList.add(new Usuario("Diego", "Suarez"));
        usuariosList.add(new Usuario("Bruce", "Lee"));
        usuariosList.add(new Usuario("Bruce", "Willis"));

        Flux.fromIterable(usuariosList)
                .map(usuario -> usuario.getNombre().toUpperCase().concat(" ").concat(usuario.getApellido().toUpperCase()))
                .flatMap(nombre -> {
                    if(nombre.contains("Bruce".toUpperCase())){
                        return Mono.just(nombre);
                    } else {
                        return Mono.empty();
                    }
                })
                .map(nombre -> {
                    return nombre.toLowerCase();
                }).subscribe(u -> log.info(u.toString()));
    }
    public void ejemploflatMap() throws Exception {
        List<String> usuariosList = new ArrayList<>();
        usuariosList.add("Darwin Quispe");
        usuariosList.add("Pedro Sanchez");
        usuariosList.add("Daniel Soto ");
        usuariosList.add("Daniel Soto");
        usuariosList.add("Diego Suarez");
        usuariosList.add("Bruce Lee");
        usuariosList.add("Bruce Willis");

        Flux.fromIterable(usuariosList)
                .map(nombre -> new Usuario(nombre.split(" ")[0].toUpperCase(), nombre.split(" ")[1].toUpperCase()))
                .flatMap(usuario -> {
                    if(usuario.getNombre().equalsIgnoreCase("Bruce")){
                        return Mono.just(usuario);
                    } else {
                        return Mono.empty();
                    }
                })
                .map(usuario -> {
                    String nombre = usuario.getNombre().toLowerCase();
                    usuario.setNombre(nombre);
                    return usuario;
                }).subscribe(u -> log.info(u.toString()));
    }

    public void ejemploIterable() throws Exception {
        List<String> usuariosList = new ArrayList<>();
        usuariosList.add("Darwin Quispe");
        usuariosList.add("Pedro Sanchez");
        usuariosList.add("Daniel Soto ");
        usuariosList.add("Daniel Soto");
        usuariosList.add("Diego Suarez");
        usuariosList.add("Bruce Lee");
        usuariosList.add("Bruce Willis");
        Flux<String> nombres = Flux.fromIterable(usuariosList);//Flux.just("Darwin Quispe", "Pedro Sanchez", "Daniel Soto ", "Diego Suarez", "Pepe Soto", "Bruce Lee", "Bruce Willis");
        Flux<Usuario> usuarios = nombres.map(nombre -> new Usuario(nombre.split(" ")[0].toUpperCase(), nombre.split(" ")[1].toUpperCase()))
                .filter(usuario -> usuario.getNombre().equalsIgnoreCase("Bruce"))
                .doOnNext(usuario -> {
                    if(usuario == null){
                        throw new RuntimeException("Nombres no pueden ser vacíos");
                    }
                    System.out.println(usuario.getNombre().concat(" ").concat(usuario.getApellido()));
                })
                .map(usuario -> {
                    String nombre = usuario.getNombre().toLowerCase();
                    usuario.setNombre(nombre);
                    return usuario;
                });

        usuarios.subscribe(e -> log.info(e.toString()),
                error -> log.error(error.getMessage()),
                new Runnable() {
                    @Override
                    public void run() {
                        log.info("Ha finalizado la ejecución del observable con éxito!");
                    }
                });
    }
}
