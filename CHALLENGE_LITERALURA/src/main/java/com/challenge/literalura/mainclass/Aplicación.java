package com.challenge.literalura.mainclass;

import com.challenge.literalura.models.Autor;
import com.challenge.literalura.models.Libro2;
import com.challenge.literalura.models.ResultadosLibro;
import com.challenge.literalura.models.Libro;
import com.challenge.literalura.repository.AutorRepository;
import com.challenge.literalura.repository.LibroRepository;
import com.challenge.literalura.service.APIRequest;
import com.challenge.literalura.service.Conversion;

import java.util.*;
import java.util.stream.Collectors;


    public void menu(){
        String menu = """
                ******************************
                \tSELECCIONA UNA OPCIÓN DEL MENÚ
       
                1. Buscar  titulo
                2. Catálogo de lubros
                3. Listar autores registrados
                4. Listar autores vivos año
                5. Listar libros por idioma
                6. Salir
                
                ------------------------------
                7. Ver estadisticas
                8. Top 10 libros mas descargados
                9. Buscar autor por nombre
                ******************************
                Elige una opción:
                """;
        System.out.println(menu);
}

    public void Idioma(){
        String msjIdioma = """
                ---------------------
                Teclee su preferencia de idioma:
                
                  -en  (Inglés)
                  -es  (Español)
                  -fr  (Francés)
                  -de  (Alemán)
                  -it  (Italiano)
                  -pt  (Portugués)
                  -ja  (Japonés)
                --------------------
                """;
        System.out.println(msjIdioma);
    }


public class Aplicación {
    private Scanner keyBoard = new Scanner(System.in);
    private final String BASE_URL = "https://gutendex.com/books";
    private List<Libro> bookSearched = new ArrayList<>();
    private List<Autor> authorsSearched = new ArrayList<>();
    

    
    private LibroRepository libroRepository;
    private AutorRepository autorRepository;

    public Aplicación(LibroRepository libroRepository, AutorRepository autorRepository) {
        this.libroRepository = libroRepository;
        this.autorRepository = autorRepository;
    }


    public void showMenu() {
        int option = 0;
        do {
            
            
            option = getNumberFromUser();

            switch (option) {
                case 1:
                    searchABookByTitle();
                    break;
                case 2:
                    getAllBooks();
                    break;
                case 3:
                    getAllAuthors();
                    break;
                case 4:
                    getAuthorsAliveInYear();
                    break;
                case 5:
                    getBooksByLanguage();
                    break;
                case 6:
                    System.out.println("Aplicación finalizada");
                    break;
                default:
                    System.out.println("Seleccione una opción válida");
                    break;
            }
        } while (option != 6);

    }


    private String getStringFromUser(String message) {
        String data = "";
        while (true) {
            System.out.println(message);
            data = keyBoard.nextLine();
            if (!data.isEmpty()) {
                return data;
            }
        }

    }

    public String getWebData(String title) {
        APIRequest request = new APIRequest();
        var url = BASE_URL + "/?search=" + title.replace(" ", "+");
        return request.getData(url);
    }

    public ResultadosLibro jsonToDatosLibros(String data) {
        Conversion dataConversion = new Conversion();
        return dataConversion.convertData(data, ResultadosLibro.class);
    }

    public Libro2 getFirstBookWithAuthor(List<Libro2> libros) {
        return libros.stream()
                .filter(libro -> !libro.autor().isEmpty())
                .findFirst()
                .orElse(null);
    }

    public Libro searchOrSaveBook(Autor author, Libro2 libro) {
        Libro bookToSave = null;
        List <Libro> books = author.getLibros();

        Optional <Libro> bookFromAuthor = books.stream()
                .filter(libro1 -> libro1.getTitulo().equals(libro.titulo()))
                .findFirst();

        if (bookFromAuthor.isPresent()) {
            System.out.println("Su opción ya fue registrada en la base de datos");
            bookToSave = bookFromAuthor.get();
        } else {

            bookToSave = new Libro(libro.titulo(), author,
                    libro.idioma().get(0), libro.numeroDeDescargas());

            author.setLibros(bookToSave);
            libroRepository.save(bookToSave);

            System.out.println("Opción registrada exitosamente");
        }
        return bookToSave;
    }

    public Autor searchOrSaveAuthor(Libro2 libro) {
        Optional<Autor> autorBuscado = autorRepository.findByNombre(libro.autor().get(0).nombre());
        Autor authorToSave = null;


        if (!autorBuscado.isPresent()) {
            authorToSave = new Autor(libro.autor().get(0).nombre(),
                    libro.autor().get(0).nacimiento(), libro.autor().get(0).muerte());
            autorRepository.save(authorToSave);
            System.out.println("Opción registrada exitosamente+");
        } else {
            authorToSave = autorBuscado.get();
            System.out.println("Su opción ya fue registrada en la base de datos");
        }
        return authorToSave;

    }

   
    public void searchABookByTitle() {

        String message = "Nueva búsqueda: ";
        var title = getStringFromUser(message);

        String data = getWebData(title);
        ResultadosLibro libros = jsonToDatosLibros(data);

        if (!libros.libros().isEmpty()) {
            Libro2 libro = getFirstBookWithAuthor(libros.libros());

            Autor author = searchOrSaveAuthor(libro);
            Libro book = searchOrSaveBook(author, libro);
            System.out.println(author);
            System.out.println(book);

        } else {
            System.out.println("Error, no hay resultados para su búsqueda");
        }
    }

    private void getAllBooks() {
      
        bookSearched = libroRepository.findAll();
        if (bookSearched.isEmpty()) {
            System.out.println("Error, no hay resultados para su búsqueda");
        }
        bookSearched.stream()
                .sorted(Comparator.comparing(Libro::getTitulo))
                .forEach(libro -> {
                    System.out.println(libro.toString());
                });
    }

    private void getAllAuthors() {
        authorsSearched = autorRepository.findAll();
        if (bookSearched.isEmpty()) {
            System.out.println("Error, no hay resultados para su búsqueda");
        }
        authorsSearched.stream()
                .sorted(Comparator.comparing(Autor::getNombre))
                .forEach(autor -> {
                    System.out.println(autor.toString() /*+ autor.getLibros()*/);
                    //System.out.println("Libros: ");
                    System.out.println(autor.getLibros());
                });
    }

    private void getAuthorsAliveInYear() {
        System.out.println("Ingrese año: ");

        var year = getNumberFromUser();
        List<Autor> autoresVivos = autorRepository.getAliveAuthors(year);
        if (autoresVivos.isEmpty()) {
            System.out.println("Error, no hay resultados para su búsqueda");
        } else {
            autoresVivos.stream()
                    .forEach(autor -> {
                        System.out.println(autor.toString());
                    });
        }

    }

    private int getNumberFromUser() {
       
        throw new UnsupportedOperationException("Unimplemented method 'getNumberFromUser'");
    }


    private void getBooksByLanguage() {

        
        System.out.println(Idioma();

        String message= "Introduce el idioma: ";
        String language = getStringFromUser(message);

        List<Libro> librosPorIdioma = libroRepository.findBookByLanguage(language);
        if (librosPorIdioma.isEmpty()) {
            System.out.println("Error, no hay resultados para su búsqueda" );
        } else {
            librosPorIdioma.stream()
                    .forEach(libro -> {
                        System.out.println(libro.toString());
                    });
        })
    }





}
